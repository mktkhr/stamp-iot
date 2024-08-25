import App from '@/App.vue';
import en from '@/locales/en';
import ja from '@/locales/ja';
import router from '@/router';
import '@/styles/style.scss';
import { createPinia } from 'pinia';
import { createApp } from 'vue';
import { createI18n } from 'vue-i18n';

const app = createApp(App);

const pinia = createPinia();
export const i18n = createI18n({
  legacy: false,
  locale: 'ja',
  fallbackLocale: 'en',
  allowComposition: true,
  messages: {
    ja: {
      ...ja,
    },
    en: {
      ...en,
    },
  },
  globalInjection: true,
});

app.use(pinia);
app.use(router);
app.use(i18n);
app.mount('#app');
