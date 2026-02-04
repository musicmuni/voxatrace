/**
 * Remark plugin to substitute {{version}} placeholders with the actual SDK version.
 * Reads version from sdk-meta.json (single source of truth).
 */
const fs = require('fs');
const path = require('path');
const {visit} = require('unist-util-visit');

function remarkVersion() {
  // Read version from sdk-meta.json
  const metaPath = path.resolve(__dirname, '../../../sdk-meta.json');
  let version = 'UNKNOWN';

  try {
    const meta = JSON.parse(fs.readFileSync(metaPath, 'utf8'));
    version = meta.version || 'UNKNOWN';
  } catch (e) {
    console.warn('Could not read sdk-meta.json for version:', e.message);
  }

  return (tree) => {
    visit(tree, (node) => {
      if (node.type === 'text' && node.value) {
        node.value = node.value.replace(/\{\{version\}\}/g, version);
      }
      if (node.type === 'code' && node.value) {
        node.value = node.value.replace(/\{\{version\}\}/g, version);
      }
      if (node.type === 'inlineCode' && node.value) {
        node.value = node.value.replace(/\{\{version\}\}/g, version);
      }
    });
  };
}

module.exports = remarkVersion;
