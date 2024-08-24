<script setup lang="ts">
import { OVERLAY_CONTENT_Z_IDNEX, OVERLAY_Z_INDEX } from '@/config/zindex';
import { computed } from 'vue';
import { useOverlay } from './composable';

interface Props {
  teleportDestination?: string;
  backgroudColor?: string;
  customBackgroundClickEvent?: (() => void) | null;
  persistent?: boolean;
  hidePersistentAnimation?: boolean;
  maxWidth?: string;
  maxHeight?: string;
}

const modelValue = defineModel({ required: true, type: Boolean });

const props = withDefaults(defineProps<Props>(), {
  teleportDestination: '#app',
  backgroudColor: '#00000032',
  customBackgroundClickEvent: null,
  persistent: false,
  hidePersistentAnimation: false,
  maxWidth: '600px',
  maxHeight: '80%',
});

const emit = defineEmits<{
  (e: 'onClickBackground'): void;
  (e: 'update:modelValue', value: boolean): boolean;
}>();

const contentMaxHeight = computed(() => {
  if (isNaN(Number(props.maxHeight))) {
    return props.maxHeight;
  }
  return `${props.maxHeight}px`;
});

const contentMaxWidth = computed(() => {
  if (isNaN(Number(props.maxWidth))) {
    return props.maxWidth;
  }
  return `${props.maxWidth}px`;
});

const {
  backgroudColor,
  doTeleport,
  persistAnimationFlag,
  hideInProgress,
  TRANSITION_DELAY_IN_SEC,
  onClickBackground,
} = useOverlay(props, emit, modelValue);
</script>
<template>
  <Teleport v-if="doTeleport" :to="teleportDestination">
    <div
      class="background"
      :class="{ 'background-hide': hideInProgress }"
      :style="{ 'background-color': backgroudColor }"
    >
      <div
        class="wrapper-fullscreen"
        :class="{ persist: persistAnimationFlag }"
        @click.stop="onClickBackground"
      >
        <div
          class="wrapper-content"
          :class="{ 'wrapper-content-hide': hideInProgress }"
          :style="{ 'max-height': contentMaxHeight, 'max-width': contentMaxWidth }"
        >
          <slot></slot>
        </div>
      </div>
    </div>
  </Teleport>
</template>
<style lang="scss" scoped>
$overlay_z_index: v-bind(OVERLAY_Z_INDEX);
$overlay_content_z_index: v-bind(OVERLAY_CONTENT_Z_IDNEX);
$animation_span: 0.1s;

.background {
  margin: auto;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: $overlay_z_index;
  animation: fadeIn v-bind(TRANSITION_DELAY_IN_SEC) ease;
  &-hide {
    animation: fadeOut v-bind(TRANSITION_DELAY_IN_SEC) ease;
  }
}

.wrapper {
  &-fullscreen {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  &-content {
    width: 80%;
    display: flex;
    position: relative;
    justify-content: center;
    z-index: $overlay_content_z_index;
    animation: zoomIn v-bind(TRANSITION_DELAY_IN_SEC) ease;
    &-hide {
      animation: zoomOut v-bind(TRANSITION_DELAY_IN_SEC) ease;
    }
  }
  &-persist {
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

@keyframes faleIn {
  0% {
    background-color: #00000000;
  }
  100% {
    background-color: v-bind(backgroudColor);
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
@keyframes fadeOut {
  0% {
    background-color: v-bind(backgroudColor);
  }
  100% {
    background-color: #00000000;
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
.persist {
  animation: persistent-movement $animation_span infinite;
}
@keyframes persistent-movement {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(0.98);
  }
}
</style>
