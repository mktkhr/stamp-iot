<script setup lang="ts">
import HeaderComponent from '@/components/HeaderComponent.vue';
import { ref } from '@vue/reactivity';
import FormWindow from '@/components/common/FormWindow.vue';
import InformationInput from '@/components/common/InformationInput.vue';
import CommonButton from '@/components/common/CommonButton.vue';
import validation from '@/methods/validation';
import register from '@/methods/register';

const menuStateRef = ref<boolean>();
const changeState = (param: boolean) => {
  menuStateRef.value = param;
};

const mailAddressRef = ref<string>();
const passwordRef = ref<string>();
const passwordConfirmRef = ref<string>();
const mailAddressError = ref<string>('');
const passwordError = ref<string>('');
const passwordConfirmError = ref<string>('');

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

const clickButton = () => {
  if (validate()) {
    console.log("失敗");
    return;
  }
  register.post(mailAddressRef.value, passwordRef.value);
};
</script>

<template>
  <HeaderComponent :hamburgerState="false" :menuState="menuStateRef" @clickEvent="changeState" />
  <FormWindow title="新規登録">
    <template #icon>
      <img src="@/assets/logo_blue.png" alt="logo" />
    </template>
    <template #mailAddress>
      <InformationInput
        mail-address
        :error-message="mailAddressError"
        @input-value="getMailAddress"
      />
    </template>
    <template #password>
      <InformationInput password :error-message="passwordError" @input-value="getPassword" />
    </template>
    <template #passwordConfirm>
      <InformationInput
        passwordConfirm
        :error-message="passwordConfirmError"
        @input-value="getPasswordConfirm"
      />
    </template>
    <template #button>
      <CommonButton button-title="登録" @click-button="clickButton" />
    </template>
    <template #link>
      <RouterLink to="/login" class="link">ログインはこちら</RouterLink>
    </template>
  </FormWindow>
</template>

<style scoped>
img {
  margin-top: 20px;
  height: 50px;
  width: auto;
}
.link {
  background: linear-gradient(transparent 50%, #2e6dba80 50%);
}
@media screen and (max-height: 600px) {
  img {
    margin-top: 5px;
    height: 30px;
  }
}
</style>
