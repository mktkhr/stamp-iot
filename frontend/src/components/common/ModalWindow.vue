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
    <v-container class="modal-title modal-container">
      <span>{{ title }}</span>
    </v-container>
    <v-container class="modal-container">
      <span>{{ description }}</span>
    </v-container>
    <slot name="content"></slot>
    <div class="button">
      <slot name="button"></slot>
    </div>
  </div>
</template>

<style scoped>
.modal-content {
  width: 100%;
  height: 100%;
  background-color: #eee;
  box-shadow: 0px 10px 20px -5px rgba(0, 0, 0, 0.6);
  border-radius: 3px;
}
.modal-title {
  font-size: 22px;
  background-color: #aaa;
}
.modal-container {
  margin-top: 20px;
  height: auto;
  max-width: 80%;
  display: table;
  background-color: #eee;
  padding: 0;
  text-align: center;
}
.button {
  text-align: center;
}
span {
  display: table-cell;
  vertical-align: middle;
}
</style>
