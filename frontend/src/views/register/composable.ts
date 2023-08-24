import { ref } from 'vue';
import { i18n } from '@/main';
import validation from '@/methods/validation';
import { AccountStore } from '@/store/accountStore';
import { NotificationType } from '@/constants/notificationType';
import router from '@/router';
import { StatusCode } from '@/constants/statusCode';

export const useRegister = () => {
  const mailAddressRef = ref('');
  const passwordRef = ref('');
  const passwordConfirmRef = ref('');
  const mailAddressError = ref('');
  const passwordError = ref('');
  const passwordConfirmError = ref('');
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

  const getPasswordConfirm = (value: string) => {
    passwordConfirmRef.value = value;
  };

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
      return false;
    }

    return mailAddressValidateFlag || passwordValidateFlag || passwordConfirmValidateFlag;
  };

  const onClickRegister = async () => {
    if (validate()) {
      notificationMessage.value = i18n.global.t('Validation.Error.invalid');
      notificationType.value = NotificationType.ERROR;
      showNotification.value = true;
      setTimeout(() => (showNotification.value = false), 3000);
      return;
    }
    await accountStore
      .register(mailAddressRef.value, passwordRef.value)
      .then(() => {
        notificationMessage.value = i18n.global.t('Register.successfullyRegistered');
        notificationType.value = NotificationType.SUCCESS;
        showNotification.value = true;
        setTimeout(() => {
          showNotification.value = false;
          router.replace('/login');
        }, 1500);
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
        notificationType.value = NotificationType.ERROR;
        showNotification.value = true;
        setTimeout(() => (showNotification.value = false), 3000);
      });
  };

  return {
    mailAddressError,
    passwordError,
    passwordConfirmError,
    showNotification,
    notificationMessage,
    notificationType,
    getMailAddress,
    getPassword,
    getPasswordConfirm,
    onClickRegister,
  };
};
