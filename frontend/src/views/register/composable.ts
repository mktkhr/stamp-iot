import { StatusCode } from '@/constants/statusCode';
import { i18n } from '@/main';
import validation from '@/methods/validation';
import router from '@/router';
import { AccountStore } from '@/store/accountStore';
import { AlertStore } from '@/store/alertStore';
import { generateRandomString } from '@/utils/stringUtil';
import { ref } from 'vue';

export const useRegister = () => {
  const mailAddressRef = ref('');
  const passwordRef = ref('');
  const passwordConfirmRef = ref('');
  const mailAddressError = ref('');
  const passwordError = ref('');
  const passwordConfirmError = ref('');
  const notificationMessage = ref('');

  const accountStore = AccountStore();
  const alertStore = AlertStore();

  /**
   * 登録時のバリデーション
   * @returns true: エラー有, false: エラー無し
   */
  const validate = () => {
    mailAddressError.value = '';
    passwordError.value = '';
    passwordConfirmError.value = '';

    const mailAddressValidateFlag = !validation.mailAddressValidate(mailAddressRef.value);
    const passwordValidateFlag = !validation.passwordValidate(passwordRef.value);
    const passwordConfirmValidateFlag = !validation.passwordValidate(passwordConfirmRef.value);

    if (mailAddressValidateFlag) {
      mailAddressError.value = i18n.global.t('Validation.Error.mailAddress');
    }
    if (passwordValidateFlag) {
      passwordError.value = i18n.global.t('Validation.Error.password');
    }
    if (passwordConfirmValidateFlag) {
      passwordConfirmError.value = i18n.global.t('Validation.Error.password');
    }
    if (passwordRef.value != passwordConfirmRef.value) {
      passwordConfirmError.value = i18n.global.t('Validation.Error.passwordNotMatch');
    }

    return mailAddressValidateFlag || passwordValidateFlag || passwordConfirmValidateFlag;
  };

  /**
   * 登録ボタン押下時の処理
   */
  const onClickRegister = async () => {
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
      .register(mailAddressRef.value, passwordRef.value)
      .then(() => {
        notificationMessage.value = i18n.global.t('Register.successfullyRegistered');

        router.replace('/login');

        alertStore.addAlert({
          id: '',
          type: 'info',
          content: 'アカウントの登録に成句しました。再度ログインしてください。',
          timeInSec: 5,
        });
      })
      .catch((e) => {
        const statusCode = e.response.status.toString();
        if (statusCode === StatusCode.BAD_REQUEST) {
          notificationMessage.value = i18n.global.t('ApiError.badRequest');
        } else if (statusCode === StatusCode.FORBIDDEN) {
          notificationMessage.value = i18n.global.t('ApiError.emailInUse');
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
    passwordConfirmRef,
    mailAddressError,
    passwordError,
    passwordConfirmError,
    onClickRegister,
  };
};
