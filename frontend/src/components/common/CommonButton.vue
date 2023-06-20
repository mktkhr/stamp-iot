<script setup lang="ts">
interface Props {
  buttonTitle?: string;
  errorMessage?: string;
  height?: string;
  width?: string;
  backgroundColor?: string;
  hoverColor?: string;
}

withDefaults(defineProps<Props>(), {
  buttonTitle: '',
  errorMessage: '',
  height: '30px',
  width: '60px',
  backgroundColor: '#eeeeee',
  hoverColor: '#888888',
});

interface Emits {
  (e: 'clickButton'): void;
}

const emit = defineEmits<Emits>();

const clickButton = () => {
  emit('clickButton');
};
</script>

<template>
  <button
    class="button"
    @click="clickButton"
    :style="{ height: height, width: width, 'background-color': backgroundColor }"
  >
    <span class="button-title">{{ buttonTitle }}</span>
  </button>
</template>

<style lang="scss" scoped>
.button {
  border-radius: 10px;
  border: none;
  cursor: pointer;
}
.button:hover {
  background-color: #888;
}
.button:active {
  background-color: #888;
}
.button-title {
  font-size: 16px;
  letter-spacing: 2px;
  color: #333333;
}
</style>
