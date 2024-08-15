<script setup lang="ts">
import { ALERT_Z_INDEX } from '@/config/zindex';
import { AlertContent, AlertStore } from '@/store/alertStore';
import { nextTick, onMounted, ref } from 'vue';

interface Props {
  alert: AlertContent;
}

const props = defineProps<Props>();

const alertStore = AlertStore();

// transition後の状態で表示されているかフラグ
const showAlert = ref(false);

// プログレスバーの要素
const progressRef = ref<HTMLDivElement | null>(null);

// 固定値
const TRANSITION_DELAY_IN_SEC = '0.2s'; // transitionにかける時間
const TIME_IN_SEC = props.alert.timeInSec ? props.alert.timeInSec + 's' : '0s';
const ALERT_RGB = 'rgba(var(--ems-alert-rgb), 0.2)';
const WARNING_RGB = 'rgba(var(--ems-warning-rgb), 0.2)';
const SUCCESS_RGB = 'rgba(var(--ems-success-rgb), 0.2)';
const INFO_RGB = 'rgba(var(--ems-theme-rgb), 0.2)';
const ALERT_ICON = 'ems-cancel';
const WARNING_ICON = 'ems-warning';
const SUCCESS_ICON = 'ems-check';
const INFO_ICON = 'ems-info';
const ALERT_COLOR = 'var(--ems-alert-theme)';
const WARNING_COLOR = 'var(--ems-warning-theme)';
const SUCCESS_COLOR = 'var(--ems-success-theme)';
const INFO_COLOR = 'var(--ems-theme)';

// アラートアイコンの背景色
const alertColor = ref<
  typeof ALERT_COLOR | typeof WARNING_COLOR | typeof SUCCESS_COLOR | typeof INFO_COLOR
>(ALERT_COLOR);

// アラートコンテンツの背景色
const alertBackgroundColor = ref<
  typeof ALERT_RGB | typeof WARNING_RGB | typeof SUCCESS_RGB | typeof INFO_RGB
>(ALERT_RGB);

// アラートアイコン
const alertIcon = ref<
  typeof ALERT_ICON | typeof WARNING_ICON | typeof SUCCESS_ICON | typeof INFO_ICON
>(ALERT_ICON);

/**
 * アラートタイプに応じた色の算出
 */
const calculateColor = () => {
  switch (props.alert.type) {
    case 'alert':
      alertColor.value = ALERT_COLOR;
      alertBackgroundColor.value = ALERT_RGB;
      alertIcon.value = ALERT_ICON;
      break;
    case 'warning':
      alertColor.value = WARNING_COLOR;
      alertBackgroundColor.value = WARNING_RGB;
      alertIcon.value = WARNING_ICON;
      break;
    case 'success':
      alertColor.value = SUCCESS_COLOR;
      alertBackgroundColor.value = SUCCESS_RGB;
      alertIcon.value = SUCCESS_ICON;
      break;
    case 'info':
      alertColor.value = INFO_COLOR;
      alertBackgroundColor.value = INFO_RGB;
      alertIcon.value = INFO_ICON;
      break;
  }
};
calculateColor();

/**
 * キーを指定してアラートを削除
 */
const onClickAlert = () => {
  alertStore.deleteAlert(props.alert.id);
};

onMounted(() => {
  if (!progressRef.value) return;

  // 一度描画を待ってからclassを付与する
  nextTick(() => {
    if (!progressRef.value) return;
    progressRef.value.classList.add('progress-bar-zero');

    // NOTE: 時間をおいてからtrueにしないとtransitionが効かないため，少し待機する
    setTimeout(() => {
      showAlert.value = true;
    }, 10);
  });
});
</script>

<template>
  <div class="wrapper-alert" :class="{ 'wrapper-alert-show': showAlert }" @click="onClickAlert">
    <div class="wrapper-icon">
      <span class="alert-icon" :class="alertIcon"></span>
    </div>
    <div class="wrapper-content">
      <slot>
        <span class="alert-content"> {{ alert.content }}</span>
      </slot>
      <div ref="progressRef" class="progress-bar" />
    </div>
  </div>
</template>

<style lang="scss" scoped>
$alert_height: 40px;
$icon_wrapper_width: 40px;
$alert_border_radius: 9999px;
$alert_color: v-bind(alertColor);
$alert_background_color: v-bind(alertBackgroundColor);

.wrapper {
  &-alert {
    width: 100%;
    height: $alert_height;
    display: flex;
    position: relative;
    align-items: center;
    border: 1px solid $alert_color;
    border-radius: $alert_border_radius;
    z-index: v-bind(ALERT_Z_INDEX);
    background-color: white;
    cursor: pointer;
    transform: translateX(100%); // スライドイン前の位置
    transition: all v-bind(TRANSITION_DELAY_IN_SEC) ease;
    &-show {
      transform: translateX(0); // スライドイン先の位置
    }
  }
  &-icon {
    width: $icon_wrapper_width;
    height: $alert_height;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: $alert_color;
    border-radius: $alert_border_radius 0 0 $alert_border_radius;
  }
  &-content {
    width: calc(100% - $icon_wrapper_width);
    height: $alert_height;
    display: flex;
    position: relative;
    padding: 0 16px;
    align-items: center;
    justify-content: flex-start;
    border-radius: 0 $alert_border_radius $alert_border_radius 0;
    overflow: hidden;
    background-color: $alert_background_color;
  }
}

.alert {
  &-icon {
    color: white;
    font-size: 24px;
  }
  &-content {
    color: #444;
    font-size: 14px;
  }
}

.progress-bar {
  position: absolute;
  left: 0;
  bottom: 0;
  height: 2px;
  width: calc(100% - $alert_height / 2); // アラートのborder-radiusの半径分を引く
  border-radius: 0 4px 4px 0;
  background-color: $alert_color;
  transition: v-bind(TIME_IN_SEC) linear;
  &-zero {
    width: 0px;
  }
}
</style>
