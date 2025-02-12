import router from '@/router';
import { ref } from 'vue';

import { StatusCode } from '@/constants/statusCode';
import { i18n } from '@/main';
import validation from '@/methods/validation';
import { AccountStore } from '@/store/accountStore';
import { AlertStore } from '@/store/alertStore';
import { generateRandomString } from '@/utils/stringUtil';

export const useLogin = () => {
  const mailAddressRef = ref('');
  const passwordRef = ref('');
  const mailAddressError = ref('');
  const passwordError = ref('');
  const showNotification = ref(false);
  const notificationMessage = ref('');

  const accountStore = AccountStore();
  const alertStore = AlertStore();

  /**
   * ログイン入力情報のバリデーション
   */
  const validate = (): boolean => {
    mailAddressError.value = '';
    passwordError.value = '';
    const mailAddressValidateFlag = !validation.mailAddressValidate(mailAddressRef.value);
    const passwordValidateFlag = passwordRef.value == ''; // セキュリティ確保のため，パスワードは空チェックのみとする

    if (mailAddressValidateFlag) {
      mailAddressError.value = i18n.global.t('Validation.Error.mailAddress');
    }
    if (passwordValidateFlag) {
      passwordError.value = i18n.global.t('Validation.Error.passwordNotEntered');
    }

    return mailAddressValidateFlag || passwordValidateFlag;
  };

  /**
   * ログインボタン押下時の処理
   */
  const onClickLoginButton = async (): Promise<void> => {
    mailAddressError.value = '';
    passwordError.value = '';

    if (validate()) {
      alertStore.addAlert(
        {
          id: generateRandomString(),
          type: 'warning',
          content: i18n.global.t('Validation.Error.invalid'),
          timeInSec: 5,
        },
        true
      );
      return;
    }
    await accountStore
      .login(mailAddressRef.value, passwordRef.value)
      .then(() => {
        router.replace('/home');
      })
      .catch((e) => {
        const statusCode = e.response.status.toString();
        if (statusCode === StatusCode.BAD_REQUEST || statusCode === StatusCode.UNAUTHORIZED) {
          notificationMessage.value = i18n.global.t('ApiError.inValidAccountInformation');
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
  return {
    mailAddressRef,
    passwordRef,
    mailAddressError,
    passwordError,
    showNotification,
    notificationMessage,
    onClickLoginButton,
  };
};
