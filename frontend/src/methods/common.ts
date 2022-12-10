import dayjs from 'dayjs';

export default {
  convertLocalDateTime(localDateTime: Date) {
    return dayjs(localDateTime).format('YYYY/MM/DD HH:mm:ss');
  },
};
