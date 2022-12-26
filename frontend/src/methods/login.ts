import router from '@/router';
import axios from 'axios';

export default {
  async post(mailAddress: string, password: string) {
    await axios
      .post('/api/ems/account/login', {
        email: mailAddress,
        password: password,
      })
      .then(() => {
        router.replace('/home');
      })
      .catch((error) => {
        if (error.response.status == '401') {
          alert('アカウント情報が間違っています。');
        } else if (error.response.status == '500') {
          alert('エラーが発生しました。時間をおいて再度お試しください。');
        } else if (error.response.status == '400') {
          alert('アカウント情報が間違っています。');
        } else {
          alert('予期せぬエラーが発生しました。時間をおいて再度お試しください。');
        }
      });
  },
};
