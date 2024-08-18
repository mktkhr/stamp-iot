import dayjs from 'dayjs';
import customParseFormat from 'dayjs/plugin/customParseFormat';

dayjs.extend(customParseFormat); // isValidのplugin

// NOTE: 全てのエラーでエラーコードを分けること
const DAYJS_ERROR_001 = 'DAYJS_ERROR-001';


export const convertLocalDateTime = (localDateTime: Date) => {
  if (dayjs(localDateTime).isValid()) {
    return dayjs(localDateTime).format('YYYY/MM/DD HH:mm:ss');
  }

  console.error(`${DAYJS_ERROR_001}: Invalid date format error`);
  return '';
};
