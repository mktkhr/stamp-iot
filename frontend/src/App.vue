<script setup lang="ts">
import { computed, ref } from 'vue';

import { HEADER_Z_INDEX, SIDEBAR_Z_INDEX } from '@/config/zindex';
import { LOADER_TYPE } from './components/common/commonLoader/composable';

import { SidebarStore } from './store/sidebarStore';
import { SpinnerStore } from './store/spinnerStore';

import CommonLoader from '@/components/common/commonLoader/CommonLoader.vue';
import CommonAlertGroup from './components/common/commonAlertGroup/CommonAlertGroup.vue';
import CommonHeader from './components/common/commonHeader/CommonHeader.vue';
import CommonOverlay from './components/common/commonOverlay/CommonOverlay.vue';
import CommonSidebar from './components/common/commonSidebar/CommonSidebar.vue';

const sidebarStore = SidebarStore();
const spinnerStore = SpinnerStore();

// 画面サイズを計算する
const svh = ref('0px');
const calcInnerHeight = () => {
  svh.value = `${window.innerHeight}px`;
};
window.addEventListener('resize', () => calcInnerHeight); // resize時に再計算する
calcInnerHeight();

// サイドバーのサイズを計算する
const sidebarRef = ref<HTMLDivElement>();
const sideBarWidth = computed(() => {
  if (!sidebarRef.value) return '0px';
  return `${sidebarRef.value.clientWidth}px`;
});

// サイドバーの表示状態管理
const isSidebarActive = computed(() => sidebarStore.isActive);

// スピナー表示状態管理
const showSpinner = computed(() => spinnerStore.getStatus);
</script>

<template>
  <div class="wrapper-root">
    <div class="wrapper-header">
      <CommonHeader />
    </div>
    <div class="wrapper-content">
      <div class="wrapper-alert-container">
        <CommonAlertGroup />
      </div>
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
  &-alert-container {
    width: 80%;
    position: absolute;
    margin: 0 auto;
    top: 8px;
    left: 0;
    right: 0;
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
