import { Preview, setup } from '@storybook/vue3';
import { http, HttpResponse } from 'msw';
import { initialize, mswDecorator, mswLoader } from 'msw-storybook-addon';
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

// Mock API
initialize();

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
  decorators: [
    // 共通のdecoratorを指定
    () => ({
      template:
        '<div id="app" style="position: absolute; inset: 0; margin: auto; padding: 16px;"><story/></div>',
    }),
  ],
  loaders: [mswLoader],
};

export const decorators = [withScreenshot(options)];

export default preview;
