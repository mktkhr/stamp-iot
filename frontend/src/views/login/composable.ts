import { ref } from 'vue';
import router from '@/router';

import validation from '@/methods/validation';
import { AccountStore } from '@/store/accountStore';
import { StatusCode } from '@/constants/statuCode';
import { NotificationType } from '@/constants/notificationType';

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
      mailAddressError.value = 'メールアドレスが正しく入力されていません。';
    }
    if (passwordValidateFlag) {
      passwordError.value = 'パスワードを入力して下さい。';
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
      notificationMessage.value = '入力内容に誤りがあります。';
      notificationType.value = NotificationType.ERROR;
      showNotification.value = true;
      setTimeout(() => (showNotification.value = false), 3000);
      return;
    }
    await accountStore
      .login(mailAddressRef.value, passwordRef.value)
      .then(() => {
        notificationMessage.value = 'ログインに成功しました。';
        notificationType.value = NotificationType.SUCCESS;
        showNotification.value = true;
        setTimeout(() => {
          showNotification.value = false;
          router.replace('/home');
        }, 1500);
      })
      .catch((e) => {
        const statusCode = e.response.status.toString();
        if (statusCode === StatusCode.BAD_REQUEST || statusCode === StatusCode.UNAUTHORIZED) {
          notificationMessage.value = 'アカウント情報が間違っています。';
        } else if (statusCode === StatusCode.INTERNAL_SERVER_ERROR) {
          notificationMessage.value = 'エラーが発生しました。時間をおいて再度お試しください。';
        } else {
          notificationMessage.value =
            '予期せぬエラーが発生しました。時間をおいて再度お試しください。';
        }
        notificationMessage.value = '入力内容に誤りがあります。';
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
