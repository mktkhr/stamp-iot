<script setup lang="ts">
interface Props {
  buttonTitle?: string;
  height?: string;
  width?: string;
  backgroundColor?: string;
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

withDefaults(defineProps<Props>(), {
  buttonTitle: '',
  height: '30px',
  width: '60px',
  backgroundColor: '#00000022',
  titleColor: '#333',
  fontSize: '12px',
  fontWeight: 'normal',
  justifyContent: 'center',
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
  <button class="button common-effect-ripple" @click="clickButton">
    <slot v-if="icon" name="icon" :class="icon">
      <span :class="icon" class="common-icon-style icon-small"></span>
    </slot>
    <span class="button-title">{{ buttonTitle }}</span>
  </button>
</template>

<style lang="scss" scoped>
$button_height: v-bind(height);
$button_width: v-bind(width);
$background_color: v-bind(backgroundColor);
$justify_content: v-bind(justifyContent);
$font_size: v-bind(fontSize);
$font_weight: v-bind(fontWeight);
$text_color: v-bind(titleColor);

.button {
  height: $button_height;
  width: $button_width;
  background-color: $background_color;
  border-radius: 5px;
  border: none;
  display: flex;
  align-items: center;
  justify-content: $justify_content;
  padding: 8px 16px;
  gap: 8px;
  pointer-events: all;
  cursor: pointer;
  &-title {
    font-size: $font_size;
    font-weight: $font_weight;
    letter-spacing: 2px;
    color: $text_color;
  }
  &:hover {
    filter: brightness(1.2);
  }
  &:active {
    filter: brightness(1.2);
  }
}

.icon-small {
  font-size: 20px;
}
</style>
