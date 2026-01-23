import type {ReactNode} from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import Heading from '@theme/Heading';

import styles from './index.module.css';

function HomepageHeader() {
  const {siteConfig} = useDocusaurusContext();
  return (
    <header className={clsx('hero hero--primary', styles.heroBanner)}>
      <div className="container">
        <Heading as="h1" className="hero__title">
          {siteConfig.title}
        </Heading>
        <p className="hero__subtitle">{siteConfig.tagline}</p>
        <div className={styles.buttons}>
          <Link
            className="button button--secondary button--lg"
            to="/docs/intro">
            Get Started
          </Link>
        </div>
      </div>
    </header>
  );
}

function FeatureCard({title, description}: {title: string; description: string}) {
  return (
    <div className="col col--6">
      <div className="card margin-bottom--lg padding--lg">
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

export default function Home(): ReactNode {
  const {siteConfig} = useDocusaurusContext();
  return (
    <Layout
      title="Home"
      description="VozOS - Audio SDK for Mobile Apps">
      <HomepageHeader />
      <main className="container margin-vert--xl">
        <div className="row">
          <FeatureCard
            title="Sonix"
            description="Audio playback with pitch shifting, recording, multi-track mixing, metronome, and MIDI synthesis."
          />
          <FeatureCard
            title="Calibra"
            description="Real-time pitch detection, voice activity detection, vocal range analysis, and singing evaluation."
          />
        </div>
        <div className="row">
          <FeatureCard
            title="Cross-Platform"
            description="Built with Kotlin Multiplatform. One codebase for Android and iOS."
          />
          <FeatureCard
            title="Easy Integration"
            description="Simple APIs designed for mobile apps. Get started in minutes."
          />
        </div>
      </main>
    </Layout>
  );
}
