<script setup lang="ts">
import router from '@/router';
import { computed, ref } from 'vue';

import CommonLoader from '@/components/common/commonLoader/CommonLoader.vue';
import { LOADER_TYPE } from './components/common/commonLoader/composable';
import { NotificationType } from './constants/notificationType';
import { StatusCode } from './constants/statusCode';
import { i18n } from './main';
import { AccountStore } from './store/accountStore';
import { SidebarStore } from './store/sidebarStore';
import { SpinnerStore } from './store/spinnerStore';

import { HEADER_Z_INDEX, SIDEBAR_Z_INDEX } from '@/config/zindex';
import CommonHeader from './components/common/commonHeader/CommonHeader.vue';
import CommonOverlay from './components/common/commonOverlay/CommonOverlay.vue';
import CommonSidebar from './components/common/commonSidebar/CommonSidebar.vue';

const sidebarStore = SidebarStore();

const svh = ref('0px');
const calcInnerHeight = () => {
  svh.value = `${window.innerHeight}px`;
};
window.addEventListener('resize', () => calcInnerHeight);
calcInnerHeight();

const sidebarRef = ref<HTMLDivElement>();
const sideBarWidth = computed(() => {
  if (!sidebarRef.value) return '0px';
  return `${sidebarRef.value.clientWidth}px`;
});

const isSidebarActive = computed(() => sidebarStore.isActive);

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
        notificationMessage.value = i18n.global.t('ApiError.internalServerError');
      } else {
        notificationMessage.value = i18n.global.t('ApiError.unexpectedError');
      }
      notificationType.value = NotificationType.ERROR;
      showNotification.value = true;
      setTimeout(() => (showNotification.value = false), 3000);
    });
};
</script>

<template>
  <div class="wrapper-root">
    <div class="wrapper-header">
      <CommonHeader />
    </div>
    <div class="wrapper-content">
      <div ref="sidebarRef" class="sidebar-container" :class="{ active: isSidebarActive }">
        <CommonSidebar />
      </div>
      <RouterView />
    </div>

    <CommonOverlay v-model="showSpinner" persistent>
      <CommonLoader :pattern="LOADER_TYPE.CIRCLE" />
    </CommonOverlay>

    <div id="teleport-container"></div>
  </div>
</template>

<style lang="scss" scoped>
$header_height: 50px;
$header_z_index: v-bind(HEADER_Z_INDEX);
$sidebar_width: v-bind(sideBarWidth);
$sidebar_z_index: v-bind(SIDEBAR_Z_INDEX);
$sidebar_top_bottom_margin: 32px;
$sidebar_max_width: 300px;
$common_left_margin: 16px;

.wrapper {
  &-root {
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
  }
  &-header {
    height: $header_height;
    width: 100%;
    box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.6);
    z-index: $header_z_index;
  }
  &-content {
    height: calc(100% - $header_height);
    width: 100%;
    position: relative;
  }
}

.sidebar-container {
  position: absolute;
  top: calc($sidebar_top_bottom_margin);
  left: calc($sidebar_max_width * -1);
  transition: 0.4s ease;
  max-height: calc(100% - $sidebar_top_bottom_margin * 2);
  width: auto;
  max-width: $sidebar_max_width;
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.6);
  z-index: $sidebar_z_index;

  &.active {
    left: $common_left_margin;
  }
}
</style>

<style lang="scss">
#app {
  height: 100vh;
  height: v-bind(svh);
  height: 100svh;
  width: 100vw;
  font-family: 'Roboto', 'Noto Sans JP', sans-serif;
  -moz-osx-font-smoothing: grayscale;
  color: var(--v-font-normal);
  overflow: hidden;
}
</style>
