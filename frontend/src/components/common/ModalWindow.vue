<script setup lang="ts">
interface Props {
  title?: string;
  description?: string;
}
withDefaults(defineProps<Props>(), {
  title: '',
  description: '',
});
</script>

<template>
  <div class="modal-content">
    <div class="modal-title modal-container">
      <span>{{ title }}</span>
    </div>
    <div class="modal-container" v-if="description">
      <span>{{ description }}</span>
    </div>
    <slot name="content"></slot>
    <div class="button">
      <slot name="button"></slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.modal-content {
  width: 100%;
  height: 100%;
  background-color: #eee;
  box-shadow: 0px 10px 20px -5px rgba(0, 0, 0, 0.6);
  border-radius: 3px;
}
.modal-title {
  font-size: 22px;
}
.modal-container {
  width: 100%;
  display: flex;
  background-color: #eee;
  justify-content: center;
  align-items: center;
  padding: 8px 16px;
}
.button {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 8px 16px;
}
</style>
