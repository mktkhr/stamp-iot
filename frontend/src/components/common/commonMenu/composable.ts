import { generateRandomString } from '@/utils/stringUtil';
import { onMounted, ref, Ref, watch } from 'vue';

export const useMenu = (
  props: {
    teleportDestination?: string;
    customBackgroundClickEvent?: (() => void) | null;
    persistent?: boolean;
  },
  anchorRef: Ref<HTMLDivElement | null>,
  contentRef: Ref<HTMLDivElement | null>
) => {
  // 初期値
  const showContent = ref(false); // contentの表示状態管理フラグ
  const hideInProgress = ref(false); // contentの描画が削除されようとしている状態のフラグ
  const contentTransformOrigin = ref('top left'); // contentのアニメーションのtransform-origin

  // 固定値
  const TRANSITION_DELAY_IN_SEC = 0.2; // contentのズームイン・アウトのtransitionにかける時間(s)
  const CONTENT_MARGIN = 8; // anchorとcontentとのmargin(px)

  // 画面内に複数のmenuが存在した場合に
  const menuContentId = generateRandomString();

  /**
   * menuのcontent以外をクリックした際に発火する処理
   */
  const onClickBackground = () => {
    if (!props.persistent) {
      hideProcess();
      return;
    }
  };

  /**
   * contentの表示フラグをwatchしてcontentの表示状態を制御する処理
   */
  watch(showContent, (newVal, oldVal) => {
    if (newVal && !oldVal) {
      updateContentPosition();
      return;
    }
  });

  /**
   * activatorによってい表示されるcontentの位置を算出する
   */
  const updateContentPosition = () => {
    if (!anchorRef.value) {
      return;
    }

    // contentの親要素の位置を取得
    const anchorClientRect = anchorRef.value.getBoundingClientRect();

    if (!contentRef.value) {
      return;
    }
    // 表示したいcontentの高さと幅を取得
    const contentWidth = contentRef.value.clientWidth;
    const contentHight = contentRef.value.clientHeight;

    // 画面の高さと幅を取得
    const windowHeight = window.innerHeight;
    const windowWidth = window.innerWidth;

    // anchor下にcontentを描画した際に画面外に溢れるか否か
    const isOverflowY = anchorClientRect.bottom + contentHight + CONTENT_MARGIN > windowHeight;
    const contentYPosition =
      // 溢れる場合はanchorの上側にcontentを表示する
      isOverflowY
        ? anchorClientRect.top - contentHight - CONTENT_MARGIN
        : anchorClientRect.bottom + CONTENT_MARGIN;

    // anchorの左端にcontentを描画して画面外に溢れるか否か
    const isOverflowX = anchorClientRect.left + contentWidth > windowWidth;
    const contentXPosition =
      // 溢れる場合はcontentの右端をanchorの右端に合わせてcontentを表示する
      isOverflowX ? anchorClientRect.right - contentWidth : anchorClientRect.left;

    // transform-originの書き換え
    if (isOverflowY) {
      contentTransformOrigin.value = isOverflowX ? 'bottom right' : 'bottom left';
    }
    if (isOverflowX) {
      contentTransformOrigin.value = isOverflowY ? 'bottom right' : 'top right';
    }

    contentRef.value.style.top = `${contentYPosition}px`;
    contentRef.value.style.left = `${contentXPosition}px`;
  };

  /**
   * activatorクリック時の処理
   */
  const onClickActivator = () => {
    showContent.value = true;
  };

  /**
   * contentをズームアウトさせてから描画を削除する処理
   * setTimeoutでcontentのズームアウト終了まで待機する
   */
  const hideProcess = () => {
    hideInProgress.value = true;

    // NOTE: transition終了より少し早めに実行しないとチラつく場合がある
    setTimeout(() => {
      showContent.value = false;
      hideInProgress.value = false;
    }, TRANSITION_DELAY_IN_SEC * 1000 - 50);
  };

  onMounted(() => {
    // document全体にclickイベントを付与し，content以外がクリックされたことを検知してcontentの表示を削除する
    document.addEventListener('click', (event) => {
      if (!showContent.value) return; // content非描画時はreturn

      if (event) {
        // content以外がクリックされた場合にcontentの描画を削除する
        // TODO: 型アサーションを避ける方法を検討する
        if (!(event.target as Element).closest('#' + menuContentId)) {
          onClickBackground();
        }
      }
    });

    // 画面リサイズ時にcontentの位置を再計算する
    window.addEventListener('resize', () => updateContentPosition());
  });

  return {
    showContent,
    hideInProgress,
    contentTransformOrigin,
    TRANSITION_DELAY_IN_SEC: TRANSITION_DELAY_IN_SEC + 's',
    menuContentId,
    onClickActivator,
  };
};
