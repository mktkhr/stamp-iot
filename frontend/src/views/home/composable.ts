import { computed, ref } from 'vue';
import router from '@/router';

import validation from '@/methods/validation';
import { AccountStore } from '@/store/accountStore';
import { MicroControllerStore } from '@/store/microControllerStore';
import { NotificationType } from '@/constants/notificationType';
import { StatusCode } from '@/constants/statusCode';

export const useHome = () => {
  /**
   * マイコン一覧取得リクエストとエラーハンドリング
   */
  const fetchMicroController = async () => {
    await microControllerStore.fetchMicroControllerList().catch((e) => {
      const statusCode = e.response.status.toString();
      if (statusCode === StatusCode.INTERNAL_SERVER_ERROR) {
        notificationMessage.value = 'エラーが発生しました。時間をおいて再度お試しください。';
      } else {
        notificationMessage.value =
          '予期せぬエラーが発生しました。時間をおいて再度お試しください。';
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
        notificationMessage.value = '登録に成功しました。';
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
          notificationMessage.value = '登録に失敗しました。他のMACアドレスで再度お試しください。';
        } else if (statusCode === StatusCode.UNAUTHORIZED) {
          notificationMessage.value =
            'この端末は既に登録されています。別の端末を登録してください。';
        } else if (statusCode === StatusCode.INTERNAL_SERVER_ERROR) {
          notificationMessage.value = 'エラーが発生しました。時間をおいて再度お試しください。';
        } else {
          notificationMessage.value =
            '予期せぬエラーが発生しました。時間をおいて再度お試しください。';
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
      macAddressError.value = 'MACアドレスが正しく入力されていません。';
      return;
    }

    register();
  };

  const onClickTile = (uuid: string) => {
    router.push({ name: 'result', params: { microControllerUuid: uuid } });
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
  };
};
