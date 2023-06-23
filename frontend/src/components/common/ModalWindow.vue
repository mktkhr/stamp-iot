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
    <div class="modal-container">
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
  margin: 20px auto;
  height: auto;
  width: 80%;
  display: table;
  background-color: #eee;
  padding: 0;
  text-align: center;
}
.button {
  width: 100%;
  display: flex;
  gap: 20px;
  justify-content: center;
  position: absolute;
  bottom: 30px;
}
span {
  display: table-cell;
  vertical-align: middle;
  white-space: pre-wrap;
}
</style>
