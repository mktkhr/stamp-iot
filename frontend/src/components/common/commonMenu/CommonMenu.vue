<script setup lang="ts">
import { MENU_CONTENT_Z_INDEX, MENU_Z_INDEX } from '@/config/zindex';
import { ref } from 'vue';
import { useMenu } from './composable';

interface Props {
  teleportDestination?: string;
  customBackgroundClickEvent?: (() => void) | null;
  persistent?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  teleportDestination: '#app',
  customBackgroundClickEvent: null,
  persistent: false,
});

const anchorRef = ref<HTMLDivElement | null>(null);
const contentRef = ref<HTMLDivElement | null>(null);

const {
  showContent,
  hideInProgress,
  contentTransformOrigin,
  TRANSITION_DELAY_IN_SEC,
  menuContentId,
  onClickActivator,
} = useMenu(props, anchorRef, contentRef);
</script>
<template>
  <!-- menuのアンカーになる要素 -->
  <div ref="anchorRef" class="anchor-activator" @click.stop="onClickActivator">
    <slot name="activator"></slot>
  </div>

  <!-- メニュー表示用要素 -->
  <Teleport :to="teleportDestination">
    <div :style="{ visibility: showContent ? 'visible' : 'hidden' }" class="anchor-background">
      <div
        ref="contentRef"
        class="wrapper-content"
        :id="menuContentId"
        :style="{ 'transform-origin': contentTransformOrigin }"
        :class="{ 'wrapper-content-visible': showContent, 'wrapper-content-hide': hideInProgress }"
      >
        <slot></slot>
      </div>
    </div>
  </Teleport>
</template>
<style lang="scss" scoped>
$menu_z_index: v-bind(MENU_Z_INDEX);
$menu_content_z_index: v-bind(MENU_CONTENT_Z_INDEX);
$animation_span: 0.1s;

.anchor {
  &-activator {
    // 子要素のサイズに合わせる
    height: auto;
    width: auto;
  }
  &-background {
    position: absolute;
    // anchorとして機能させるだけなので全画面である必要がなく，bottomとrightは指定しない
    top: 0;
    left: 0;
    z-index: $menu_z_index;
    pointer-events: none;
  }
}

.wrapper {
  &-fullscreen {
    height: 100%;
    width: 100%;
    position: relative;
    pointer-events: none;
  }
  &-content {
    height: auto;
    width: auto;
    position: absolute;
    z-index: $menu_content_z_index;
    pointer-events: all;
    transform: scale(0); // 非visible時
    &-visible {
      transform: scale(1); // 非visible時の値を上書き
      animation: zoomIn v-bind(TRANSITION_DELAY_IN_SEC) ease;
    }
    &-hide {
      animation: zoomOut v-bind(TRANSITION_DELAY_IN_SEC) ease;
    }
  }
}
@keyframes zoomIn {
  0% {
    transform: scale(0);
  }
  100% {
    transform: scale(1);
  }
}
@keyframes zoomOut {
  0% {
    transform: scale(1);
  }
  100% {
    transform: scale(0);
  }
}
</style>
