<script setup lang="ts">
import CircleLoader from './components/CircleLoader.vue';
import FlowerLoader from './components/FlowerLoader.vue';
import ThreeDotsLoader from './components/ThreeDotsLoader.vue';
import { LOADER_TYPE, LoaderType, useCommonLoader } from './composable';

interface Props {
  pattern: LoaderType;
}

defineProps<Props>();

useCommonLoader();
</script>

<template>
  <CircleLoader v-if="pattern.id === LOADER_TYPE.CIRCLE.id" />
  <FlowerLoader v-else-if="pattern.id === LOADER_TYPE.FLOWER.id" />
  <ThreeDotsLoader v-else />
</template>

<style lang="scss" scoped></style>
