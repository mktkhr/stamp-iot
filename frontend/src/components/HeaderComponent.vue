<script setup lang="ts">
interface Props {
  hamburgerState: boolean;
  menuState: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  hamburgerState: true,
  menuState: false,
});

const emit = defineEmits<{
  (e: 'clickEvent', menuState: boolean);
}>();

const onClickMenuButton = (): void => {
  emit('clickEvent', !props.menuState);
};
</script>

<template>
  <header>
    <div class="header-left" v-if="hamburgerState">
      <button
        class="hamburger"
        id="hamburger"
        v-bind:class="{ active: menuState }"
        v-on:click="onClickMenuButton"
      >
        <span></span>
        <span></span>
        <span></span>
      </button>
    </div>
    <div class="header-right">
      <img src="@/assets/logo_white.png" alt="logo" />
    </div>
  </header>
</template>

<style scoped>
header {
  width: 100%;
  height: 50px;
  background: #999999;
  display: flex;
  position: fixed;
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.6);
  z-index: 999;
}
.hamburger {
  width: 100%;
  height: 100%;
  cursor: pointer;
  padding: 0;
  background-color: transparent;
  border-color: transparent;
}
.hamburger span {
  margin-left: 12px;
  width: 25px;
  height: 1px;
  background-color: #ffffff;
  position: relative;
  transition: ease 0.4s;
  display: block;
}

button {
  border-width: 0;
}

.hamburger span:nth-child(1) {
  top: 0;
}

.hamburger span:nth-child(2) {
  margin: 8px 12px;
}

.hamburger span:nth-child(3) {
  top: 0;
}
.hamburger.active span:nth-child(1) {
  top: 9px;
  transform: rotate(45deg);
}

.hamburger.active span:nth-child(2) {
  opacity: 0;
}

.hamburger.active span:nth-child(3) {
  top: -9px;
  transform: rotate(-45deg);
}
.header-left {
  height: 50px;
  width: 50px;
  cursor: pointer;
  z-index: 999;
}
.header-right {
  position: absolute;
  right: 0;
  height: 50px;
  width: 70px;
}
.header-right img {
  margin-top: 5px;
  height: 40px;
  width: auto;
}
</style>
