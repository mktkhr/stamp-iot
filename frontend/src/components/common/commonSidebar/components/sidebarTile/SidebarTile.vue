<script setup lang="ts">
defineProps<{
  title: string;
}>();
</script>
<template>
  <div class="wrapper-item common-effect-ripple">
    <div class="icon centering">
      <slot name="icon"></slot>
    </div>
    <div class="title centering">
      <span>{{ title }}</span>
    </div>
    <div class="arrow"></div>
  </div>
</template>
<style lang="scss" scoped>
$wrapper_height: 40px;
$wrapper_width: 150px;
$icon_width: 50px;
$title_width: 100px;
$arrow_width: 50px;
$wrapper_radius: 4px;
$title_font_size: 16px;

.wrapper {
  &-item {
    width: $wrapper_width;
    height: $wrapper_height;
    display: flex;
    border-radius: $wrapper_radius;
    overflow: hidden;
    cursor: pointer;
    & + .wrapper-item {
      // 隣接セレクタ
      margin-top: 8px;
    }
  }
}
.icon {
  height: 100%;
  width: $icon_width;
}
.title {
  height: 100%;
  width: $title_width;
  > span {
    font-size: $title_font_size;
    font-weight: bold;
    color: #777;
  }
}
.arrow {
  height: 100%;
  width: $arrow_width;
}
.centering {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
