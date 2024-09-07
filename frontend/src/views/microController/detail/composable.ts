import { SelectOptionType } from '@/components/common/commonSelect/composable';
import { i18n } from '@/main';
import { MicroControllerDetailPatchParam } from '@/methods/microController';
import validation from '@/methods/validation';
import { MicroControllerStore } from '@/store/microControllerStore';
import { computed, ref } from 'vue';

export const useMicroControllerDetail = (props: { microControllerUuid: string }) => {
  const isEditMode = ref(false);
  const intervalForEdit = ref('');
  const unitNameForEdit = ref('');
  const addressForEdit = ref('');
  const isShowCancelModal = ref(false);
  const sdiAddressErrorMessage = ref('');

  const microControllerStore = MicroControllerStore();
  const microControllerDetail = computed(() => microControllerStore.getDetail);

  /**
   * 詳細情報の取得
   */
  const fetchDetail = async () => {
    await microControllerStore.fetchMicroControllerDetail(props.microControllerUuid);
  };
  fetchDetail();

  /**
   * 測定間隔のリスト
   */
  const intervalList: SelectOptionType[] = [
    { key: '1', word: '1分' },
    { key: '5', word: '5分' },
    { key: '10', word: '10分' },
    { key: '15', word: '15分' },
    { key: '20', word: '20分' },
    { key: '30', word: '30分' },
    { key: '60', word: '60分' },
  ];

  /**
   * 編集ボタン押下時の処理
   */
  const onClickEdit = () => {
    intervalForEdit.value = microControllerDetail.value.interval.toString();
    unitNameForEdit.value = microControllerDetail.value.name ?? '';
    addressForEdit.value = microControllerDetail.value.sdi12Address;
    isEditMode.value = true;
  };

  /**
   * 編集キャンセルボタン押下時の処理
   */
  const onClickCancel = () => {
    if (checkDiff()) {
      isShowCancelModal.value = true;
      return;
    }
    sdiAddressErrorMessage.value = '';
    isEditMode.value = false;
  };

  /**
   * 編集保存ボタン押下時の処理
   */
  const onClickSave = () => {
    sdiAddressErrorMessage.value = '';

    // NOTE: 未入力の場合はバリデーションチェックを行わない
    if (addressForEdit.value && !validation.sdiAddressValidate(addressForEdit.value)) {
      sdiAddressErrorMessage.value = i18n.global.t('Validation.Error.invalidSdiAddress');
      return;
    }

    const param: MicroControllerDetailPatchParam = {
      microControllerUuid: props.microControllerUuid,
      name: unitNameForEdit.value,
      interval: intervalForEdit.value,
      sdi12Address: addressForEdit.value,
    };

    microControllerStore.updateMicroControllerDetail(param);
    isEditMode.value = false;
  };

  /**
   * 編集差分チェック処理
   * @returns 差分がある場合true,差分がない場合false
   */
  const checkDiff = () => {
    return (
      // 測定間隔
      microControllerDetail.value.interval.toString() !== intervalForEdit.value ||
      // 端末名称が設定済みだった場合
      (microControllerDetail.value.name &&
        microControllerDetail.value.name !== unitNameForEdit.value) ||
      // 端末名称が元から未設定の場合
      (!microControllerDetail.value.name && unitNameForEdit.value !== '') ||
      microControllerDetail.value.sdi12Address !== addressForEdit.value
    );
  };

  /**
   * キャンセルダイアログのはい押下時の処理
   */
  const onClickCancelAccept = () => {
    isEditMode.value = false;
    isShowCancelModal.value = false;
  };

  /**
   * キャンセルダイアログのいいえクリック時の処理
   */
  const onClickCancelDeny = () => {
    isShowCancelModal.value = false;
  };

  return {
    isEditMode,
    onClickEdit,
    onClickCancel,
    onClickSave,
    microControllerDetail,
    unitNameForEdit,
    intervalForEdit,
    intervalList,
    addressForEdit,
    sdiAddressErrorMessage,
    isShowCancelModal,
    onClickCancelDeny,
    onClickCancelAccept,
  };
};
