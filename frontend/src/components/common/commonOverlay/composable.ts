import { Ref, ref, toRefs, watch } from 'vue';

export const useOverlay = (
  props: {
    teleportDestination?: string;
    backgroudColor?: string;
    customBackgroundClickEvent: () => void;
    persistent: boolean;
    hidePersistentAnimation?: boolean;
    maxWidth?: string;
    maxHeight?: string;
  },
  emit: {
    (e: 'onClickBackground'): void;
    (e: 'update:modelValue', value: boolean): boolean;
  },
  modelValue: Ref<boolean>
) => {
  const { backgroudColor } = toRefs(props);

  const doTeleport = ref(false); // Teleport の表示管理フラグ
  const persistAnimationFlag = ref(false); // プルプルアニメーション表示フラグ
  const hideInProgress = ref(false); // overlay を隠す過程フラグ(animation用)
  const TRANSITION_DELAY_IN_SEC = 0.2; // overlay の表示を消す際の待機時間
  const ANIMATION_TIME_IN_SEC = 0.2; // アニメーションの描画時間

  /**
   * props から showOverlay が変更された場合の処理s
   */
  watch(modelValue, (newVal, oldVal) => {
    if (!newVal && oldVal) {
      hideProcess();
      return;
    }
    if (newVal && !oldVal) {
      doTeleport.value = newVal;
      return;
    }
  });

  /**
   * overlay の表示を隠す処理
   */
  const hideProcess = () => {
    hideInProgress.value = true;

    // NOTE: transition終了より少し早めに実行しないとチラつく場合がある
    setTimeout(() => {
      doTeleport.value = false;
      modelValue.value = false;
      hideInProgress.value = false;
    }, TRANSITION_DELAY_IN_SEC * 1000 - 10);
  };

  /**
   * 背景クリック時の処理
   */
  const onClickBackground = () => {
    emit('onClickBackground');

    if (props.customBackgroundClickEvent) {
      props.customBackgroundClickEvent();
    }

    if (!props.persistent) {
      hideProcess();
      return;
    }

    // persistent かつ animation を描画する場合
    if (!props.hidePersistentAnimation) {
      persistEvent();
      return;
    }
  };

  // persist の場合のプルプルアニメーション有効化処理
  const persistEvent = () => {
    persistAnimationFlag.value = true;
    setTimeout(() => (persistAnimationFlag.value = false), ANIMATION_TIME_IN_SEC * 1000);
  };

  return {
    backgroudColor,
    doTeleport,
    persistAnimationFlag,
    hideInProgress,
    TRANSITION_DELAY_IN_SEC: TRANSITION_DELAY_IN_SEC + 's', // css 用に s を付与
    onClickBackground,
  };
};
