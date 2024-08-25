<script setup lang="ts">
import { ref } from 'vue';

interface Props {
  buttonTitle?: string;
  height?: string;
  width?: string;
  type?: 'fill' | 'text' | 'float' | 'outlined';
  color?: string;
  titleColor?: string;
  icon?: string; // icomoonのアイコンを指定する
  fontSize?: string;
  fontWeight?: 'normal' | 'bold';
  justifyContent?:
    | 'center'
    | 'flex-start'
    | 'flex-end'
    | 'space-between'
    | 'sprace-around'
    | 'space-evenly';
}

const props = withDefaults(defineProps<Props>(), {
  buttonTitle: '',
  height: 'max-content',
  width: 'max-content',
  type: 'float',
  color: 'var(--ems-theme)',
  fontSize: '14px',
  fontWeight: 'normal',
  justifyContent: 'center',
});

interface Emits {
  (e: 'clickButton'): void;
}

const emit = defineEmits<Emits>();

// 初期値
const border = ref('');
const backgroundColor = ref('');
const textColor = ref('');
const shadow = ref('');

/**
 * ボタンのデザインを算出する
 */
const calcalateDesign = () => {
  switch (props.type) {
    case 'fill':
      border.value = 'none';
      backgroundColor.value = props.color;
      textColor.value = props.titleColor ?? 'white';
      shadow.value = 'none';
      break;
    case 'float':
      border.value = 'none';
      backgroundColor.value = 'white';
      textColor.value = props.titleColor ?? props.color;
      shadow.value = '0 2px 8px #0006';
      break;
    case 'text':
      border.value = 'none';
      backgroundColor.value = 'white';
      textColor.value = props.titleColor ?? props.color;
      shadow.value = 'none';
      break;
    case 'outlined':
      border.value = `1px solid ${props.color}`;
      backgroundColor.value = 'white';
      textColor.value = props.titleColor ?? props.color;
      shadow.value = 'none';
      break;
  }
};
calcalateDesign();
</script>

<template>
  <button class="button common-effect-ripple" @click="emit('clickButton')">
    <slot v-if="icon" name="icon" :class="icon">
      <span :class="icon" class="common-icon-style icon-small"></span>
    </slot>
    <span class="button-title">{{ buttonTitle }}</span>
  </button>
</template>

<style lang="scss" scoped>
$button_height: v-bind(height);
$button_width: v-bind(width);
$button_border: v-bind(border);
$button_shodow: v-bind(shadow);
$background_color: v-bind(backgroundColor);
$justify_content: v-bind(justifyContent);
$font_size: v-bind(fontSize);
$font_weight: v-bind(fontWeight);
$text_color: v-bind(textColor);

.button {
  height: $button_height;
  width: $button_width;
  background-color: $background_color;
  border-radius: 5px;
  border: $button_border;
  display: flex;
  align-items: center;
  justify-content: $justify_content;
  padding: 8px 16px;
  gap: 8px;
  pointer-events: all;
  transition: all 0.2s ease;
  box-shadow: $button_shodow;
  cursor: pointer;

  &-title {
    font-size: $font_size;
    font-weight: $font_weight;
    letter-spacing: 2px;
    color: $text_color;
  }

  &:hover {
    filter: brightness(0.9);
  }

  &:active {
    filter: brightness(0.9);
  }
}

.icon-small {
  font-size: 20px;
}
</style>
