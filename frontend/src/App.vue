<script setup lang="ts">
import { watch, ref, computed } from 'vue';
import router from '@/router';

import HeaderComponent from '@/components/HeaderComponent.vue';
import NavigatorComponent from '@/components/NavigatorComponent.vue';
import NotificationBar from '@/components/common/NotificationBar.vue';

import { SpinnerStore } from './store/spinnerStore';
import { AccountStore } from './store/accountStore';
import { StatusCode } from './constants/statusCode';
import { NotificationType } from './constants/notificationType';

// スピナー表示状態管理
const spinnerStore = SpinnerStore();
const showSpinner = computed(() => spinnerStore.getStatus);

const accountStore = AccountStore();

const showNotification = ref(false);
const notificationMessage = ref('');
const notificationType = ref(NotificationType.INFO);

const menuStateRef = ref(false);
const changeState = (param: boolean) => {
  menuStateRef.value = param;
};

const onClickLogout = async () => {
  await accountStore
    .logout()
    .then(() => {
      router.replace('/login');
    })
    .catch((e) => {
      const statusCode = e.response.status.toString();
      if (statusCode === StatusCode.INTERNAL_SERVER_ERROR) {
        notificationMessage.value = 'エラーが発生しました。時間をおいて再度お試しください。';
      } else {
        notificationMessage.value =
          '予期せぬエラーが発生しました。時間をおいて再度お試しください。';
      }
      notificationType.value = NotificationType.ERROR;
      showNotification.value = true;
      setTimeout(() => (showNotification.value = false), 3000);
    });
};

// ハンバーガーメニューの非表示設定
const showHamburgerMenu = ref(false);
watch(router.currentRoute, () => {
  menuStateRef.value = false; // 画面遷移時にメニューを隠す
  if (router.currentRoute.value.name === 'login' || router.currentRoute.value.name === 'register') {
    showHamburgerMenu.value = false;
  } else {
    showHamburgerMenu.value = true;
  }
});
</script>

<template>
  <HeaderComponent
    :showHamburgerMenu="showHamburgerMenu"
    :menuState="menuStateRef"
    @clickEvent="changeState"
    @onClickLogout="onClickLogout"
  />
  <NavigatorComponent :menuState="menuStateRef" v-if="showHamburgerMenu" />
  <div v-if="showSpinner" class="spinner">
    <v-progress-circular color="blue" indeterminate />
  </div>
  <div class="main-view">
    <NotificationBar :text="notificationMessage" :type="notificationType" v-if="showNotification" />
    <router-view />
  </div>
</template>

<style lang="scss" scoped>
$header-height: 50px;
.main-view {
  height: calc(100vh - $header-height);
  width: 100vw;
  margin-top: $header-height;
  position: relative;
}
.spinner {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100vh;
  z-index: 9999;
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(#000, 0.5);
}
</style>
