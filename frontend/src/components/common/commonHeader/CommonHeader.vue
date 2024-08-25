<script setup lang="ts">
import router from '@/router';
import { SidebarStore } from '@/store/sidebarStore';
import { computed, ref, watch } from 'vue';
import CommonMenu from '../commonMenu/CommonMenu.vue';
import AccountMenu from './components/accountMenu/AccountMenu.vue';

const sidebarStore = SidebarStore();
const isActive = computed(() => sidebarStore.isActive);

const onClickHamburger = () => {
  sidebarStore.toggleSidebarStatus();
};

// ハンバーガーメニューの非表示設定
const showHamburgerMenu = ref(false);
watch(router.currentRoute, () => {
  // ログイン画面と新規登録画面では非表示
  if (router.currentRoute.value.name === 'login' || router.currentRoute.value.name === 'register') {
    showHamburgerMenu.value = false;
  } else {
    showHamburgerMenu.value = true;
  }
});
</script>

<template>
  <div class="header">
    <div v-if="showHamburgerMenu" class="wrapper-hamburger">
      <div class="hamburger" :class="{ active: isActive }" @click.stop="onClickHamburger">
        <span></span>
        <span></span>
        <span></span>
      </div>
    </div>
    <div v-if="showHamburgerMenu" class="header-right">
      <CommonMenu>
        <template #activator>
          <span class="ems-account_circle common-effect-ripple icon"></span>
        </template>
        <AccountMenu />
      </CommonMenu>
    </div>
  </div>
</template>
<style lang="scss" scoped>
$header_height: 50px;
$hamburger_width: 66px;
$hamburger_size: 36px;
$hamburger_span_width: 24px;
$hamburger_span_height: 2px;

.header {
  width: 100%;
  height: $header_height;
  display: flex;
  background-color: var(--ems-theme);
  &-right {
    position: absolute;
    display: flex;
    right: 0;
    height: $header_height;
    width: auto;
    margin-right: 16px;
    align-items: center;
  }
}

.icon {
  font-size: 30px;
  color: white;
  cursor: pointer;
}

.wrapper {
  &-hamburger {
    width: $hamburger_width;
    height: $header_height;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.hamburger {
  height: $hamburger_size;
  width: $hamburger_size;
  cursor: pointer;
  background-color: white;
  display: block;
  border-radius: 4px;
  padding: 8px 0;

  > span {
    margin-left: calc(($hamburger_size - $hamburger_span_width) / 2);
    width: $hamburger_span_width;
    height: $hamburger_span_height;
    position: relative;
    transition: ease 0.4s;
    display: block;
    background-color: #666;
    border-radius: $hamburger_span_height;
  }

  > span:nth-child(1) {
    top: 0;
  }

  > span:nth-child(2) {
    margin: 7px calc(($hamburger_size - $hamburger_span_width) / 2);
  }

  > span:nth-child(3) {
    top: 0;
  }
  &.active {
    > span:nth-child(1) {
      top: 9px;
      transform: rotate(45deg);
    }
    > span:nth-child(2) {
      opacity: 0;
    }
    > span:nth-child(3) {
      top: -9px;
      transform: rotate(-45deg);
    }
  }
}
</style>
