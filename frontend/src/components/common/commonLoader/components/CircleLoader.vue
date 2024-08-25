<script setup lang="ts">
interface Props {
  size?: string;
}

withDefaults(defineProps<Props>(), {
  size: '40px',
});
</script>

<template>
  <div class="half-circle-spinner">
    <div class="circle circle-1"></div>
    <div class="circle circle-2"></div>
  </div>
</template>

<style lang="scss" scoped>
$dot_size: v-bind(size);

.half-circle-spinner,
.half-circle-spinner * {
  box-sizing: border-box;
}

.half-circle-spinner {
  width: $dot_size;
  height: $dot_size;
  border-radius: 100%;
  position: relative;
}

.half-circle-spinner .circle {
  content: '';
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 100%;
  border: calc($dot_size / 10) solid transparent;
}

.half-circle-spinner .circle.circle-1 {
  border-top-color: var(--ems-theme);
  animation: half-circle-spinner-animation 1s infinite;
}

.half-circle-spinner .circle.circle-2 {
  border-bottom-color: var(--ems-theme);
  animation: half-circle-spinner-animation 1s infinite alternate;
}

@keyframes half-circle-spinner-animation {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
