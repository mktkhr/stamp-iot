<script setup lang="ts">
import { onMounted, ref } from 'vue';

interface Props {
  windowStyle: 'auto' | 'vertical' | 'horizontal';
}

const props = withDefaults(defineProps<Props>(), { windowStyle: 'auto' });

// 固定値
const MAX_CONTENT = 'max-content';
const _100_WIDTH = '100%';
const _50_WIDTH = '50%';
const FLEX_ROW = 'row';
const FLEX_COLUMN = 'column';

const flexDirection = ref(FLEX_COLUMN);
const containerWidth = ref(_100_WIDTH);
const containerHeight = ref(MAX_CONTENT);

onMounted(() => {
  /**
   * 画面サイズでstyleを再計算する
   * propsでwindowのすたいるを固定できるように
   */
  const calculateStyle = () => {
    switch (props.windowStyle) {
      case 'auto':
        {
          const windowWidth = window.innerWidth;
          if (windowWidth < 900) {
            flexDirection.value = FLEX_COLUMN;
            containerWidth.value = _100_WIDTH;
            containerHeight.value = MAX_CONTENT;
            break;
          }
          flexDirection.value = FLEX_ROW;
          containerWidth.value = _50_WIDTH;
          containerHeight.value = MAX_CONTENT;
        }
        break;
      case 'horizontal':
        {
          flexDirection.value = FLEX_ROW;
          containerWidth.value = _50_WIDTH;
          containerHeight.value = MAX_CONTENT;
        }
        break;
      case 'vertical':
        {
          flexDirection.value = FLEX_COLUMN;
          containerWidth.value = _100_WIDTH;
          containerHeight.value = MAX_CONTENT;
        }
        break;
    }
  };

  calculateStyle();
  window.addEventListener('resize', () => calculateStyle());
});
</script>

<template>
  <div class="wrapper-frame">
    <div class="container">
      <slot name="title"></slot>
    </div>
    <div class="container">
      <slot name="content"></slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.wrapper {
  &-frame {
    width: 100%;
    max-width: 800px;
    height: max-content;
    max-height: 800px;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    margin: auto;
    padding: 32px 16px;
    border-radius: 24px;
    box-shadow: 0px 8px 24px -2px rgba(0, 0, 0, 0.6);
    background-color: white;
    flex-direction: v-bind(flexDirection);
    display: flex;

    > .container {
      width: v-bind(containerWidth);
      height: v-bind(containerHeight);
      padding: 16px;
      display: flex;
      flex-direction: column;
      gap: 16px;
      overflow: auto;

      &:first-child {
        text-align: left;
      }
    }
  }
}
</style>
