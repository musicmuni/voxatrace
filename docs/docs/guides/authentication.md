---
sidebar_position: 5
---

# Authentication

A complete guide to authenticating with the VoxaTrace SDK.

## What You'll Learn

- Choose the right authentication method for your app
- Set up API key authentication for server-side use
- Configure proxy-based authentication for production mobile apps
- Use platform attestation for mobile apps without a backend
- Implement a proxy server for secure key management

## Overview

VoxaTrace must be initialized with valid credentials before any Sonix or Calibra APIs can be used. Calling any factory method before initialization throws `VoxaTraceNotInitializedException`.

There are three initialization methods:

| Method | `VT.initialize(proxyEndpoint:)` | `VT.initializeWithAttestation(apiKey:)` | `VT.initializeForServer(apiKey:)` |
|---|---|---|---|
| **Best for** | Production mobile apps with a backend | Mobile apps without a backend | Server-side, desktop, CLI, testing |
| **API key location** | Your server only | Embedded in app | Environment variable or config |
| **Security** | Highest — key never leaves your server | High — protected by Play Integrity / App Attest | Depends on environment |
| **Requires backend** | Yes | No | No |
| **Async** | No (synchronous) | Yes (callback) | No (synchronous) |

## API Key Format

VoxaTrace API keys follow the format:

- `sk_live_...` — Production keys (metered usage)
- `sk_test_...` — Test keys (free, rate-limited)

Get your key from the [VoxaTrace Dashboard](https://dashboard.musicmuni.com).

## Method 1: Server Auth (API Key)

Use `VT.initializeForServer` when running outside mobile apps — server-side processing, desktop applications, Python bindings, CLI tools, or automated tests.

### Kotlin

```kotlin
try {
    VT.initializeForServer(
        apiKey = "sk_live_abc123...",
        debugLogging = BuildConfig.DEBUG,
        preload = setOf(AIModels.Pitch.REALTIME)
    )
} catch (e: VoxaTraceKilledException) {
    println("License error: ${e.message}")
}
```

### Swift

```swift
do {
    try VT.initializeForServer(apiKey: "sk_live_abc123...")
} catch {
    print("License error: \(error)")
}
```

### Python

```python
import voxatrace as vt

vt.initialize_for_server("sk_live_abc123...")
```

### How It Works

1. SDK sends the API key to `https://api.musicmuni.com/v1/voxatrace/devices/register`
2. Server validates the key and returns a device token
3. SDK uses the device token for all subsequent requests
4. Token refreshes automatically (30-day expiration, 7-day refresh window)

### When to Use

- Server-side audio processing pipelines
- Desktop or CLI applications
- Automated testing and CI/CD
- Development and prototyping

:::caution
Do not embed `sk_live_` keys directly in mobile app code shipped to users. Use **Proxy** or **App Attestation** instead.
:::

## Method 2: Proxy Authentication (Recommended for Mobile)

Use `VT.initialize(proxyEndpoint:)` for production mobile apps. Your API key stays on your server, and the SDK receives a short-lived device token.

### Kotlin (Android)

```kotlin
// In Application.onCreate()
try {
    VT.initialize(
        proxyEndpoint = "https://your-server.com/voxatrace/register",
        context = this,
        debugLogging = BuildConfig.DEBUG,
        preload = setOf(AIModels.Pitch.REALTIME)
    )
} catch (e: VoxaTraceKilledException) {
    // License invalid — disable VoxaTrace features
    showError("License error: ${e.message}")
}
```

### Swift (iOS)

```swift
// In AppDelegate or @main App init
do {
    try VT.initialize(proxyEndpoint: "https://your-server.com/voxatrace/register")
} catch {
    print("License error: \(error)")
}
```

### How It Works

```
┌─────────┐        ┌─────────────┐        ┌───────────────────┐
│   SDK   │──(1)──▶│ Your Server │──(2)──▶│ VoxaTrace API     │
│ (device)│◀──(4)──│   (proxy)   │◀──(3)──│ api.musicmuni.com │
└─────────┘        └─────────────┘        └───────────────────┘
```

1. SDK sends `{ "device_id": "sha256_hash" }` to your proxy endpoint
2. Your server authenticates the user (your auth system), then forwards the request to VoxaTrace with your `sk_live_` key
3. VoxaTrace returns `{ "device_token": "dt_xxx", "expires_at": 1735689600 }`
4. Your server forwards the response back to the SDK

### Implementing the Proxy Server

Your proxy needs a single endpoint that forwards device registration requests.

#### Node.js / Express

```javascript
app.post('/voxatrace/register', authenticateUser, async (req, res) => {
  const response = await fetch(
    'https://api.musicmuni.com/v1/voxatrace/devices/register',
    {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${process.env.VOXATRACE_API_KEY}`,
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ device_id: req.body.device_id }),
    }
  );
  const data = await response.json();
  res.json(data);
});
```

#### Python / FastAPI

```python
@app.post("/voxatrace/register")
async def register_device(request: Request, user=Depends(authenticate_user)):
    body = await request.json()
    response = requests.post(
        "https://api.musicmuni.com/v1/voxatrace/devices/register",
        headers={
            "Authorization": f"Bearer {os.environ['VOXATRACE_API_KEY']}",
            "Content-Type": "application/json",
        },
        json={"device_id": body["device_id"]},
    )
    return response.json()
```

#### Ruby / Rails

```ruby
class VoxatraceController < ApplicationController
  before_action :authenticate_user!

  def register
    response = HTTP.auth("Bearer #{ENV['VOXATRACE_API_KEY']}")
                   .post("https://api.musicmuni.com/v1/voxatrace/devices/register",
                         json: { device_id: params[:device_id] })
    render json: response.parse
  end
end
```

### Token Lifecycle

The SDK manages token lifecycle automatically:

| Event | Timing | SDK Behavior |
|-------|--------|--------------|
| Token issued | Day 0 | Stored securely (EncryptedSharedPreferences / Keychain) |
| Refresh | Day 23+ (< 7 days remaining) | SDK calls proxy to get a new token |
| Expiration | Day 30 | Token invalid, SDK requests new one |
| Grace period | Day 30–37 | SDK continues to work while refreshing |

No action is required from app developers — refresh is fully transparent.

### When to Use

- Production mobile apps distributed to users
- Any app where you have a backend server
- When you need to control or revoke access per-user

## Method 3: App Attestation

Use `VT.initializeWithAttestation(apiKey:)` for mobile apps that don't have a backend server. The SDK uses platform-level attestation to prove the app is genuine before activating.

### Kotlin (Android)

```kotlin
// In Application.onCreate()
VT.initializeWithAttestation(
    apiKey = "sk_live_abc123...",
    context = this,
    debugLogging = BuildConfig.DEBUG,
    preload = setOf(AIModels.Pitch.REALTIME)
) { success, error ->
    if (success) {
        // SDK ready
    } else {
        showError("License error: $error")
    }
}
```

### Swift (iOS)

```swift
VT.initializeWithAttestation(apiKey: "sk_live_abc123...") { success, error in
    if success {
        // SDK ready
    } else {
        print("License error: \(error ?? "unknown")")
    }
}
```

### How It Works

1. SDK generates a hardware-backed attestation using the platform API
2. Attestation is sent to VoxaTrace along with the API key
3. VoxaTrace verifies the attestation and issues a device token
4. Token is cached securely — attestation is only repeated when the token expires

### Platform Requirements

#### Android — Play Integrity API
- App must be distributed via Google Play Store
- Device must have Google Play Services
- Requires the Play Integrity dependency:

```kotlin
dependencies {
    implementation("com.google.android.play:integrity:1.3.0")
}
```

#### iOS — App Attest
- iOS 14.0 or later
- Physical device (not Simulator)
- App Attest entitlement must be enabled in your provisioning profile

:::note
Attestation is **async** because it requires a network round-trip to the platform's attestation service and then to VoxaTrace. The callback fires on the main thread.
:::

### When to Use

- Mobile apps without a backend server
- Indie apps or prototypes going to production
- When proxy setup is not feasible

## Choosing the Right Method

```
Do you have a backend server?
├── Yes → Use Proxy (Method 2)
└── No
    ├── Mobile app? → Use Attestation (Method 3)
    └── Server/Desktop/CLI? → Use Server Auth (Method 1)
```

## Error Handling

All initialization methods can fail if credentials are invalid or revoked.

### Synchronous Methods (Server Auth, Proxy)

```kotlin
try {
    VT.initialize(proxyEndpoint = "https://your-server.com/voxatrace/register", context = this)
} catch (e: VoxaTraceKilledException) {
    // API key revoked, license expired, or network error
    Log.e("VoxaTrace", "Init failed: ${e.message}")
}
```

### Async Method (Attestation)

```kotlin
VT.initializeWithAttestation(apiKey = "sk_live_...", context = this) { success, error ->
    if (!success) {
        // Attestation failed, device not supported, or key invalid
        Log.e("VoxaTrace", "Init failed: $error")
    }
}
```

### Common Errors

| Error | Cause | Fix |
|-------|-------|-----|
| `VoxaTraceKilledException` | Invalid or revoked API key | Check your key on the dashboard |
| "Platform attestation not available" | Device doesn't support Play Integrity / App Attest | Fall back to proxy, or test on a physical device |
| Network timeout | Can't reach VoxaTrace API or proxy | Check network connectivity and endpoint URL |
| `VoxaTraceNotInitializedException` | Using SDK before calling `VT.initialize` | Move initialization to `Application.onCreate()` or app launch |

## Security Best Practices

1. **Never ship `sk_live_` keys in mobile app source code** unless using attestation. Use proxy or attestation instead.
2. **Rotate keys** periodically via the dashboard. Existing device tokens continue to work until they expire.
3. **Authenticate your proxy endpoint** with your own auth system (session tokens, JWTs, etc.) to prevent unauthorized device registration.
4. **Use `sk_test_` keys** during development — they're free and rate-limited, so there's no risk if accidentally leaked.
5. **Store keys in environment variables** for server-side deployments, not in source code.

## Next Steps

- [Installation](../getting-started/installation) - Set up VoxaTrace
- [Android Quickstart](../getting-started/android-quickstart) - Build your first Android app
- [iOS Quickstart](../getting-started/ios-quickstart) - Build your first iOS app
