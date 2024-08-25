<script setup lang="ts">
interface Props {
  title?: string;
  description?: string;
  useButton?: boolean;
}
withDefaults(defineProps<Props>(), {
  title: '',
  description: '',
  useButton: true,
});
</script>
<template>
  <div class="wrapper-content">
    <div v-if="title" class="wrapper-section">
      <span class="span-title">{{ title }}</span>
    </div>
    <div v-if="description" class="wrapper-section">
      <span class="span-description">{{ description }}</span>
    </div>
    <slot></slot>
    <div v-if="useButton" class="wrapper-button">
      <slot name="button"></slot>
    </div>
  </div>
</template>
<style lang="scss" scoped>
.wrapper {
  &-content {
    border-radius: 8px;
    padding: 16px;
    pointer-events: all;
    display: flex;
    flex-direction: column;
    gap: 16px;
    background-color: white;
    pointer-events: none;
  }
  &-section {
    width: 100%;
    text-align: left;
    pointer-events: none;
  }
  &-button {
    width: 100%;
    gap: 16px;
    display: flex;
    justify-content: flex-end;
  }
}
.span {
  &-title {
    font-size: 20px;
    font-weight: bold;
  }
  &-description {
    font-size: 14px;
  }
}
</style>
