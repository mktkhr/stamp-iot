import router from '@/router';
import axios from 'axios';

export default {
  async post() {
    await axios
      .post('/api/ems/account/logout')
      .then(() => {
        router.replace('/login');
      })
      .catch((error) => {
        if (error.response.status == '500') {
          alert('エラーが発生しました。時間をおいて再度お試しください。');
        } else {
          alert('予期せぬエラーが発生しました。時間をおいて再度お試しください。');
        }
      });
  },
};
