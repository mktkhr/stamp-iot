<script setup lang="ts">
import Plus from 'vue-material-design-icons/Plus.vue';

interface Props {
  title?: string;
  isForMultiMedia?: boolean;
  gap?: string;
}

withDefaults(defineProps<Props>(), {
  title: '',
  isForMultiMedia: true,
  gap: '0px',
});
</script>

<template>
  <div
    class="wrapper-grid"
    :class="{
      'grid-one-half': isForMultiMedia,
      'grid-one-fourth': isForMultiMedia,
    }"
    :style="{ gap: `${gap}` }"
  >
    <slot name="content"></slot>
  </div>
</template>

<style lang="scss" scoped>
.wrapper-grid {
  height: auto;
  width: 100%;
  overflow-y: scroll;
  display: grid;
  grid-template-columns: 1fr;
}

@media (min-width: 800px) {
  .grid-one-half {
    grid-template-columns: 1fr 1fr;
  }
}

@media (min-width: 1200px) {
  .grid-one-fourth {
    grid-template-columns: 1fr 1fr 1fr;
  }
}
</style>
