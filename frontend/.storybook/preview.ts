import { Preview, setup } from '@storybook/vue3';
import { createPinia } from 'pinia';
import { ScreenshotOptions, withScreenshot } from 'storycap';
import { App } from 'vue';
import '../src/assets/icomoon/style.css';
import { i18n } from '../src/main';
import '../src/styles/style.scss';

const pinia = createPinia();

setup((app: App) => {
  app.use(pinia);
  app.use(i18n);
});

const options: ScreenshotOptions = {
  fullPage: false,
};

const preview: Preview = {
  parameters: {
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },
    disableSaveFromUI: true,
  },
};

export const decorators = [withScreenshot(options)];

export default preview;
