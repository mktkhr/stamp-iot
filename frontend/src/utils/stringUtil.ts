/**
 * ランダムな文字列を生成する
 * @param length 生成する文字列の長さ
 * @returns 生成した文字列
 */
export const generateRandowmString = (length: number = 16) => {
  const sourceCharacters: string = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  const randomValues = new Uint32Array(length);
  crypto.getRandomValues(randomValues);

  const generatedString = Array.from(
    randomValues,
    (value) => sourceCharacters[value % sourceCharacters.length]
  ).join('');

  return generatedString;
};
