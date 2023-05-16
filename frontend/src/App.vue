<script setup lang="ts">
import { computed, ref } from 'vue';
import router from '@/router';

import HeaderComponent from '@/components/HeaderComponent.vue';
import NavigatorComponent from '@/components/NavigatorComponent.vue';

const menuStateRef = ref(false);
const changeState = (param: boolean) => {
  menuStateRef.value = param;
};

// ハンバーガーメニューの非表示設定
const showHamburgerMenu = computed(
  () => !(router.currentRoute.value.name == 'login' || router.currentRoute.value.name == 'register')
);
</script>

<template>
  <HeaderComponent
    :showHamburgerMenu="showHamburgerMenu"
    :menuState="menuStateRef"
    @clickEvent="changeState"
  />
  <NavigatorComponent :menuState="menuStateRef" />
  <div class="main-view">
    <router-view />
  </div>
</template>

<style lang="scss" scoped>
$header-height: 50px;
.main-view {
  height: calc(100vh - $header-height);
  width: 100vw;
  margin-top: $header-height;
}
</style>
