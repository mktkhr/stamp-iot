import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import Login from '@/views/login/Login.vue';
import Register from '@/views/register/Register.vue';
import Home from '@/views/home/Home.vue';
import MeasuredData from '@/views/measuredData/MeasuredData.vue';
import axios from 'axios';
import { AccountStore } from '@/store/accountStore';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'login',
    component: Login,
  },
  {
    path: '/register',
    name: 'register',
    component: Register,
  },
  {
    path: '/home',
    name: 'home',
    component: Home,
  },
  {
    path: '/result/:microControllerUuid',
    name: 'result',
    props: true,
    component: MeasuredData,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

/**
 * 画面遷移前の共通処理
 * セッションの有効性チェック
 */
router.beforeEach(async (to, _from, next) => {
  //セッションの有効チェック
  const sessionStatus = await checkSession();

  if (!sessionStatus && to.name != 'login' && to.name != 'register') {
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
