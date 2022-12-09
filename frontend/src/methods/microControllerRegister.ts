import axios from 'axios';

export default {
  async post(useId: string, macAddress: string) {
    await axios
      .post('/api/ems/micro-controller', {
        userId: useId,
        macAddress: macAddress,
      })
      .then(() => {
        if (confirm('登録に成功しました。')) {
          location.reload();
        }
      })
      .catch((error) => {
        if (error.response.status == '401') {
          alert('この端末は既に登録されています。別の端末を登録してください。');
        } else if (error.response.status == '500') {
          alert('エラーが発生しました。時間をおいて再度お試しください。');
        } else if (error.response.status == '400') {
          alert('エラーが発生しました。再度入力してください。');
        } else {
          alert('予期せぬエラーが発生しました。時間をおいて再度お試しください。');
        }
      });
  },
};
