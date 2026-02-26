// Inject "Back to Docs" link and logo into Dokka header
(function() {
    // Use absolute path for images
    const IMAGES_PATH = '/api/images/';

    function injectElements() {
        if (document.querySelector('.back-to-docs')) return; // Already injected

        const header = document.querySelector('.navigation');
        if (!header) return;

        // Inject back link
        const backLink = document.createElement('a');
        backLink.href = '/';
        backLink.className = 'back-to-docs';
        backLink.textContent = '← Docs';
        backLink.title = 'Back to Documentation';
        header.insertBefore(backLink, header.firstChild);

        // Inject logo before library name
        const libraryName = header.querySelector('.library-name--link');
        if (libraryName) {
            const logo = document.createElement('img');
            logo.src = IMAGES_PATH + 'logo.png';
            logo.alt = 'VoxaTrace';
            logo.className = 'library-logo';
            libraryName.insertBefore(logo, libraryName.firstChild);
        }
    }

    // Handle both cases: DOM already loaded or not yet
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', injectElements);
    } else {
        injectElements();
    }
})();
