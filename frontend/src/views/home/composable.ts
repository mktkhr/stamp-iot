import { computed, ref } from 'vue';
import router from '@/router';

import validation from '@/methods/validation';
import { AccountStore } from '@/store/accountStore';
import { MicroControllerStore } from '@/store/microControllerStore';
import { NotificationType } from '@/constants/notificationType';
import { StatusCode } from '@/constants/statusCode';
import { i18n } from '@/main';

export const useHome = () => {
  /**
   * マイコン一覧取得リクエストとエラーハンドリング
   */
  const fetchMicroController = async () => {
    await microControllerStore.fetchMicroControllerList().catch((e) => {
      const statusCode = e.response.status.toString();
      if (statusCode === StatusCode.INTERNAL_SERVER_ERROR) {
        notificationMessage.value = i18n.global.t('ApiError.internalServerError');
      } else {
        notificationMessage.value = i18n.global.t('ApiError.unexpectedError');
      }
      notificationType.value = NotificationType.ERROR;
      showNotification.value = true;
      setTimeout(() => (showNotification.value = false), 3000);
    });
  };

  /**
   * マイコン登録リクエストとエラーハンドリング
   */
  const register = async () => {
    await microControllerStore
      .register(accountInfo.value.id.toString(), macAddressRef.value)
      .then(() => {
        notificationMessage.value = i18n.global.t('Home.successfullyRegistered');
        notificationType.value = NotificationType.SUCCESS;
        showNotification.value = true;
        fetchMicroController(); // 再取得
        setTimeout(() => {
          showNotification.value = false;
        }, 1500);
      })
      .catch((e) => {
        const statusCode = e.response.status.toString();
        if (statusCode === StatusCode.BAD_REQUEST) {
          notificationMessage.value = i18n.global.t('ApiError.invalidMacAddress');
        } else if (statusCode === StatusCode.UNAUTHORIZED) {
          notificationMessage.value = i18n.global.t('ApiError.macAddressInUse');
        } else if (statusCode === StatusCode.INTERNAL_SERVER_ERROR) {
          notificationMessage.value = i18n.global.t('ApiError.internalServerError');
        } else {
          notificationMessage.value = i18n.global.t('ApiError.unexpectedError');
        }
        notificationType.value = NotificationType.ERROR;
        showNotification.value = true;
        setTimeout(() => (showNotification.value = false), 3000);
      });
  };

  // Store
  const accountStore = AccountStore();
  const accountInfo = computed(() => accountStore.getAccountInfo);

  const microControllerStore = MicroControllerStore();
  fetchMicroController();
  const microControllerList = computed(() => microControllerStore.getMicroControllerList);

  const isShowModal = ref(false);
  const macAddressError = ref('');
  const macAddressRef = ref('');
  const showNotification = ref(false);
  const notificationMessage = ref('');
  const notificationType = ref(NotificationType.INFO);

  const onClickPlusButton = () => {
    isShowModal.value = true;
  };

  const onClickSubmit = () => {
    isShowModal.value = false;
  };

  const getMacAddress = (value: string) => {
    macAddressRef.value = value;
  };

  const onClickRegister = async () => {
    macAddressError.value = '';

    const macAddressValidateFlag = !validation.macAddressValidate(macAddressRef.value);

    if (macAddressValidateFlag) {
      macAddressError.value = i18n.global.t('Validation.Error.invalidMacAddress');
      return;
    }

    register();
  };

  // マイコンタイル押下時の処理
  const onClickTile = (uuid: string) => {
    router.push({ name: 'result', params: { microControllerUuid: uuid } });
  };

  // マイコンの設定アイコン押下時の処理
  const onClickSetting = (uuid: string) => {
    router.push({ name: 'microControllerDetail', params: { microControllerUuid: uuid } });
  };

  return {
    accountInfo,
    microControllerList,
    isShowModal,
    macAddressError,
    showNotification,
    notificationMessage,
    notificationType,
    onClickPlusButton,
    onClickSubmit,
    getMacAddress,
    onClickRegister,
    onClickTile,
    onClickSetting,
  };
};
