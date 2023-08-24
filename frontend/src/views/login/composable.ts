import { ref } from 'vue';
import router from '@/router';

import validation from '@/methods/validation';
import { AccountStore } from '@/store/accountStore';
import { StatusCode } from '@/constants/statusCode';
import { NotificationType } from '@/constants/notificationType';
import { i18n } from '@/main';

export const useLogin = () => {
  const mailAddressRef = ref('');
  const passwordRef = ref('');
  const mailAddressError = ref('');
  const passwordError = ref('');
  const showNotification = ref(false);
  const notificationMessage = ref('');
  const notificationType = ref(NotificationType.INFO);

  const accountStore = AccountStore();

  const getMailAddress = (value: string) => {
    mailAddressRef.value = value;
  };

  const getPassword = (value: string) => {
    passwordRef.value = value;
  };

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
      notificationMessage.value = i18n.global.t('Validation.Error.invalid');
      notificationType.value = NotificationType.ERROR;
      showNotification.value = true;
      setTimeout(() => (showNotification.value = false), 3000);
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
        notificationType.value = NotificationType.ERROR;
        showNotification.value = true;
        setTimeout(() => (showNotification.value = false), 3000);
      });
  };
  return {
    mailAddressError,
    passwordError,
    showNotification,
    notificationMessage,
    notificationType,
    getMailAddress,
    getPassword,
    onClickLoginButton,
  };
};
