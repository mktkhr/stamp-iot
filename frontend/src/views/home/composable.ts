import router from '@/router';
import { computed, ref } from 'vue';

import { NotificationType } from '@/constants/notificationType';
import { StatusCode } from '@/constants/statusCode';
import { i18n } from '@/main';
import { AccountStore } from '@/store/accountStore';
import { AlertStore } from '@/store/alertStore';
import { MicroControllerStore } from '@/store/microControllerStore';
import { generateRandomString } from '@/utils/stringUtil';

export const useHome = () => {
  const alertStore = AlertStore();

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

      alertStore.addAlert({
        id: generateRandomString(),
        type: 'alert',
        content: notificationMessage.value,
        timeInSec: 5,
      });
    });
  };

  /**
   * マイコン登録リクエストとエラーハンドリング
   */
  const onClickRegister = async () => {
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

        alertStore.addAlert({
          id: generateRandomString(),
          type: 'alert',
          content: notificationMessage.value,
          timeInSec: 5,
        });
      });
  };

  // Store
  const accountStore = AccountStore();
  const accountInfo = computed(() => accountStore.getAccountInfo);

  const microControllerStore = MicroControllerStore();
  fetchMicroController();
  const microControllerList = computed(() => microControllerStore.getMicroControllerList);

  const isShowModal = ref(false);
  const macAddressRef = ref('');
  const showNotification = ref(false);
  const notificationMessage = ref('');
  const notificationType = ref(NotificationType.INFO);

  const onClickPlusButton = () => {
    isShowModal.value = true;
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
    macAddressRef,
    onClickPlusButton,
    onClickRegister,
    onClickTile,
    onClickSetting,
  };
};
