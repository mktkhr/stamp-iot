import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import LoginView from '@/views/LoginView.vue';
import RegisterView from '@/views/RegisterView.vue';
import HomeView from '@/views/HomeView.vue';
import axios from 'axios';
import { AccountStore } from '@/store/accountStore';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'login',
    component: LoginView,
  },
  {
    path: '/register',
    name: 'register',
    component: RegisterView,
  },
  {
    path: '/home',
    name: 'home',
    component: HomeView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

/**
 * 画面遷移前の共通処理
 * セッションの有効性チェック
 */
router.beforeEach(async (to, _from, next) => {
  //セッションの有効チェック
  const sessionStatus = await checkSession();

  if (!sessionStatus && to.name != 'login') {
    //セッションが無効かつログイン画面以外に遷移する場合
    next({ name: 'login' });
  } else if (sessionStatus && (to.name === 'login' || to.path === '/')) {
    //セッションが有効かつログインか"/"に遷移する場合
    next({ name: 'home' });
  } else {
    next();
  }
});

/**
 * 画面遷移後の共通処理
 * アカウント情報をStoreに保存する
 */
router.afterEach(async (to) => {
  const accountStore = AccountStore();
  if (to.name != 'login' || to.path != '/') {
    await accountStore.fetchAccountInfo();
  }
});

/**
 * Cookieの有無を確認
 *
 * @return セッションが有効(true) or 無効(false)
 */
const checkSession = async (): Promise<boolean> => {
  let sessionStatus = false;
  await axios
    .post('/api/ems/session')
    .then((response) => {
      if (response.status === 200) {
        sessionStatus = true;
      } else {
        sessionStatus = false;
      }
    })
    .catch(() => {
      sessionStatus = false;
    });
  return sessionStatus;
};

export default router;
