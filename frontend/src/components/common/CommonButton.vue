<script setup lang="ts">
interface Props {
  buttonTitle?: string;
  errorMessage?: string;
  height?: string;
  width?: string;
  backgroundColor?: string;
}

withDefaults(defineProps<Props>(), {
  buttonTitle: '',
  errorMessage: '',
  height: '30px',
  width: '60px',
  backgroundColor: '#00000022',
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
    :style="{ height: height, width: width, background: backgroundColor }"
  >
    <span class="button-title">{{ buttonTitle }}</span>
  </button>
</template>

<style lang="scss" scoped>
.button {
  border-radius: 5px;
  border: none;
  cursor: pointer;
}
.button:hover {
  filter: brightness(1.2);
}
.button:active {
  filter: brightness(1.2);
}
.button-title {
  font-size: 16px;
  letter-spacing: 2px;
  color: #333333;
}
</style>
