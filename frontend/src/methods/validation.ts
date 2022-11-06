export default {
    mailAddressValidate(value: string): boolean{
        //先頭に.を含まない @ 1文字目に-禁止，2文字目移行は-許可，トップレベルドメインは2文字以上
        const mailAddressRegExp = /^[a-zA-Z0-9_+-]+(.[a-zA-Z0-9_+-]+)*@([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]*\.)+[a-zA-Z]{2,}$/;
        return mailAddressRegExp.test(value);
    },

    passwordValidate(value: string): boolean{
        //1文字以上の大文字を含み，8~24文字
        const passwordRegExp = /^(?=.*[A-Z])[a-zA-Z0-9.?/-]{8,24}$/;
        return passwordRegExp.test(value);
    }

}