import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';
const remarkVersion = require('./plugins/remark-version');

const config: Config = {
  title: 'VoxaTrace',
  tagline: 'Audio SDK for Mobile Apps',
  favicon: 'img/favicon.png',

  future: {
    v4: true,
  },

  // GitHub Pages URL
  url: 'https://musicmuni.github.io',
  baseUrl: '/voxatrace/',
  organizationName: 'musicmuni',
  projectName: 'voxatrace',
  trailingSlash: false,

  onBrokenLinks: 'warn',

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      {
        docs: {
          sidebarPath: './sidebars.ts',
          routeBasePath: '/', // Docs at root, no /docs prefix
          remarkPlugins: [remarkVersion],
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  themes: [
    [
      '@easyops-cn/docusaurus-search-local',
      {
        hashed: true,
        language: ['en'],
        highlightSearchTermsOnTargetPage: true,
        explicitSearchResultPath: true,
      },
    ],
  ],

  themeConfig: {
    image: 'img/voxatrace-social-card.jpg',
    colorMode: {
      defaultMode: 'dark',
      disableSwitch: true,
      respectPrefersColorScheme: false,
    },
    navbar: {
      title: 'VoxaTrace',
      logo: {
        alt: 'VoxaTrace Logo',
        src: 'img/logo.png',
      },
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'docsSidebar',
          position: 'left',
          label: 'Docs',
        },
        {
          href: 'pathname:///voxatrace/api/index.html',
          position: 'left',
          label: 'API Reference',
          target: '_self',
        },
        {
          href: 'https://github.com/musicmuni/voxatrace',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Docs',
          items: [
            {
              label: 'Getting Started',
              to: '/',
            },
            {
              label: 'API Reference',
              href: 'pathname:///voxatrace/api/index.html',
            },
          ],
        },
        {
          title: 'Community',
          items: [
            {
              label: 'GitHub',
              href: 'https://github.com/musicmuni/voxatrace',
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} MusicMuni.`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
      additionalLanguages: ['kotlin', 'swift', 'groovy'],
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
