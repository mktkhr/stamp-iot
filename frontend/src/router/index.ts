import { AccountStore } from '@/store/accountStore';
import HomeView from '@/views/home/HomeView.vue';
import LoginView from '@/views/login/LoginView.vue';
import MeasuredDataView from '@/views/measuredData/MeasuredDataView.vue';
import MicroControllerDetailView from '@/views/microController/detail/MicroControllerDetailView.vue';
import RegisterView from '@/views/register/RegisterView.vue';
import axios from 'axios';
import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';

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
  {
    path: '/microController/:microControllerUuid/result',
    name: 'result',
    props: true,
    component: MeasuredDataView,
  },
  {
    path: '/microController/:microControllerUuid/detail',
    name: 'microControllerDetail',
    props: true,
    component: MicroControllerDetailView,
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
router.beforeEach(async (to, from, next) => {
  const accountStore = AccountStore();
  //セッションの有効チェック
  const sessionStatus = await checkSession();

  if (!sessionStatus && to.name != 'login' && to.name != 'register') {
    //セッションが無効かつログイン・登録画面以外に遷移する場合
    next({ name: 'login' });
  } else if (!sessionStatus && (to.name === 'login' || to.name === 'register')) {
    //ログインか登録画面への遷移の場合，アカウント情報を取得せずに遷移
    next();
  } else if (sessionStatus && (to.name === 'login' || to.path === '/')) {
    //セッションが有効かつログインか"/"に遷移する場合
    await accountStore.fetchAccountInfo();
    next({ name: 'home' });
  } else {
    await accountStore.fetchAccountInfo();
    next();
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
      if (response.data.toString() === 'success') {
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
