<script setup lang="ts">
interface Props {
  showActionBar?: boolean;
  showBackButton?: boolean;
}
const props = withDefaults(defineProps<Props>(), {
  showActionBar: false,
  showBackButton: false,
});
</script>
<template>
  <div class="wrapper">
    <div class="wrapper-action-bar" v-show="showActionBar">
      <slot name="actionBar"></slot>
    </div>
    <div
      :class="{
        'wrapper-main-content': showActionBar,
        'wrapper-main-content-no-action-bar': !showActionBar,
      }"
    >
      <slot name="content"></slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
$action_bar_height: 50px;
.wrapper {
  height: 100%;
  width: 100%;
  &-action-bar {
    height: #{$action_bar_height};
    width: 100%;
  }
  &-main-content {
    height: calc(100% - #{$action_bar_height});
    width: 100%;
    &-no-action-bar {
      height: 100%;
      width: 100%;
    }
  }
}
</style>
