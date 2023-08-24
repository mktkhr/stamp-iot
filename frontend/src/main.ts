import { createApp } from 'vue';
import App from '@/App.vue';
import router from '@/router';
import { createPinia } from 'pinia';
import { createVuetify } from 'vuetify';
import '@/style/common-style.css';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import { createI18n } from 'vue-i18n';
import ja from '@/locales/ja';
import en from '@/locales/en';

const app = createApp(App);

const pinia = createPinia();
const vuetify = createVuetify({ components, directives });
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
app.use(vuetify);
app.use(router);
app.use(i18n);
app.mount('#app');
