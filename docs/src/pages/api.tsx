import type {ReactNode} from 'react';
import {Redirect} from '@docusaurus/router';

export default function ApiRedirect(): ReactNode {
  return <Redirect to="/api/kotlin/index.html" />;
}
