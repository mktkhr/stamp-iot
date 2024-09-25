import { Preview, setup } from '@storybook/vue3';
import { createPinia } from 'pinia';
import { withScreenshot } from 'storycap';
import { App } from 'vue';
import '../src/assets/icomoon/style.css';
import { i18n } from '../src/main';
import '../src/styles/style.scss';

const pinia = createPinia();

setup((app: App) => {
  app.use(pinia);
  app.use(i18n);
});

const preview: Preview = {
  parameters: {
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },
    screenshot: {
      fullPage: false,
      delay: 0,
      viewports: {
        desktop: { width: 1920, height: 1080 },
        tablet: { width: 768, height: 1024 },
        mobile: { width: 360, height: 800, isMobile: true, hasTouch: true },
      },
    },
    disableSaveFromUI: true,
  },
};

export const decorators = [withScreenshot];

export default preview;
