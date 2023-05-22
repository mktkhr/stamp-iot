import { ref } from 'vue';
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
      mailAddressError.value = 'メールアドレスが正しく入力されていません。';
    }
    if (passwordValidateFlag) {
      passwordError.value = '1文字以上の大文字を含む,8~24文字のパスワードを入力して下さい。';
    }
    if (passwordConfirmValidateFlag) {
      passwordConfirmError.value = '1文字以上の大文字を含む,8~24文字のパスワードを入力して下さい。';
    }
    if (passwordRef.value != passwordConfirmRef.value) {
      passwordConfirmError.value = 'パスワードが一致していません。';
      return false;
    }

    return mailAddressValidateFlag || passwordValidateFlag || passwordConfirmValidateFlag;
  };

  const onClickRegister = async () => {
    if (validate()) {
      notificationMessage.value = '入力内容に誤りがあります。';
      notificationType.value = NotificationType.ERROR;
      showNotification.value = true;
      setTimeout(() => (showNotification.value = false), 3000);
      return;
    }
    await accountStore
      .register(mailAddressRef.value, passwordRef.value)
      .then(() => {
        notificationMessage.value = '登録に成功しました。ログイン画面に遷移します。';
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
          notificationMessage.value = 'エラーが発生しました。再度入力してください。';
        } else if (statusCode === StatusCode.FORBIDDEN) {
          notificationMessage.value = 'このメールアドレスは既に使用されています。';
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
