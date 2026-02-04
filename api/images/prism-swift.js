// Prism Swift syntax highlighting for Dokka
// Waits for Prism.js to load before registering Swift language

(function() {
    function registerSwift() {
        if (typeof Prism === 'undefined' || !Prism.languages) {
            // Prism not loaded yet, retry
            setTimeout(registerSwift, 50);
            return;
        }

        // Already registered
        if (Prism.languages.swift) {
            return;
        }

        Prism.languages.swift = {
            'comment': {
                pattern: /(^|[^\\:])(?:\/\/.*|\/\*(?:[^/*]|\/(?!\*)|\*(?!\/)|\/\*(?:[^*]|\*(?!\/))*\*\/)*\*\/)/,
                lookbehind: true,
                greedy: true
            },
            'string-literal': [
                {
                    // Multi-line strings
                    pattern: /"""(?:\\(?:\\)?.|(?!""")[^\\])*"""/s,
                    greedy: true,
                    inside: {
                        'interpolation': {
                            pattern: /\\([a-z_]\w*|\((?:[^()]|\([^()]*\))*\))/i,
                            inside: {
                                'content': {
                                    pattern: /^\\\([\s\S]*\)$/,
                                    lookbehind: false,
                                    inside: null
                                }
                            }
                        },
                        'string': /[\s\S]+/
                    }
                },
                {
                    // Single-line strings
                    pattern: /"(?:\\.|[^"\\])*"/,
                    greedy: true,
                    inside: {
                        'interpolation': {
                            pattern: /\\([a-z_]\w*|\((?:[^()]|\([^()]*\))*\))/i,
                            inside: {
                                'content': {
                                    pattern: /^\\\([\s\S]*\)$/,
                                    lookbehind: false,
                                    inside: null
                                }
                            }
                        },
                        'string': /[\s\S]+/
                    }
                }
            ],
            'directive': {
                pattern: /#(?:available|colorLiteral|column|dsohandle|(?:else|elseif|endif|error|if|line|sourceLocation|warning)(?:\b|$)|file(?:ID|Literal|Path)?|fileLiteral|function|imageLiteral|selector|keyPath)(?!\w)/,
                alias: 'property'
            },
            'keyword': /\b(?:Any|Protocol|Self|Type|actor|as|assignment|associatedtype|associativity|async|await|break|case|catch|class|continue|convenience|default|defer|deinit|didSet|do|dynamic|else|enum|extension|fallthrough|false|fileprivate|final|for|func|get|guard|higherThan|if|import|in|indirect|infix|init|inout|internal|is|isolated|lazy|left|let|lowerThan|mutating|nil|none|nonisolated|nonmutating|open|operator|optional|override|postfix|precedencegroup|prefix|private|protocol|public|repeat|required|rethrows|return|right|safe|self|set|some|static|struct|subscript|super|switch|throw|throws|true|try|typealias|unowned|unsafe|var|weak|where|while|willSet)\b/,
            'boolean': /\b(?:false|true)\b/,
            'nil': {
                pattern: /\bnil\b/,
                alias: 'constant'
            },
            'number': /\b(?:[\d_]+(?:\.[\de_]+)?|0x[a-f0-9_]+(?:\.[a-f0-9p_]+)?|0b[01_]+|0o[0-7_]+)\b/i,
            'function': /\b[a-z_]\w*(?=\s*\()/i,
            'operator': /[-+*/%=!<>&|^~?]+|\.[.!?]+/,
            'punctuation': /[()[\]{},;:@\\]/
        };

        // Set up string interpolation recursion
        Prism.languages.swift['string-literal'].forEach(function(rule) {
            if (rule.inside && rule.inside.interpolation && rule.inside.interpolation.inside && rule.inside.interpolation.inside.content) {
                rule.inside.interpolation.inside.content.inside = Prism.languages.swift;
            }
        });

        // Re-highlight all code blocks now that Swift is registered
        Prism.highlightAll();
    }

    // Start registration (will retry if Prism not loaded yet)
    registerSwift();
})();
