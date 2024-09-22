import { Preview, setup } from '@storybook/vue3';
import { createPinia } from 'pinia';
import { App } from 'vue';
import '../src/assets/icomoon/style.css';
import '../src/styles/style.scss';

const pinia = createPinia();

setup((app: App) => {
  app.use(pinia);
});

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

export default preview;
