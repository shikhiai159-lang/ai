import type { CapacitorConfig } from '@capacitor/cli';
const config: CapacitorConfig = {
  appId: 'com.autokaaj.aistudio',
  appName: 'AI Video Studio Pro',
  webDir: 'www',
  bundledWebRuntime: false,
  android: { allowMixedContent: true },
  server: { androidScheme: 'http', cleartext: true }
};
export default config;
