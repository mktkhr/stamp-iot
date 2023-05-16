<script setup lang="ts">
import HeaderComponent from '@/components/HeaderComponent.vue';
import { ref } from 'vue';
import FormWindow from '@/components/common/FormWindow.vue';
import InformationInput from '@/components/common/InformationInput.vue';
import CommonButton from '@/components/common/CommonButton.vue';
import validation from '@/methods/validation';
import login from '@/methods/login';

const menuStateRef = ref<boolean>();
const changeState = (param: boolean) => {
  menuStateRef.value = param;
};

const mailAddressRef = ref<string>();
const passwordRef = ref<string>();
const mailAddressError = ref<string>('');
const passwordError = ref<string>('');

const getMailAddress = (value: string) => {
  mailAddressRef.value = value;
};

const getPassword = (value: string) => {
  passwordRef.value = value;
};

const validate = () => {
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

const clickButton = () => {
  mailAddressError.value = '';
  passwordError.value = '';

  if (validate()) {
    console.log('ログイン失敗');
    return;
  }
  login.post(mailAddressRef.value, passwordRef.value);
};
</script>

<template>
  <HeaderComponent :hamburgerState="false" :menuState="menuStateRef" @clickEvent="changeState" />
  <FormWindow title="ログイン">
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
    <template #button>
      <CommonButton button-title="ログイン" @click-button="clickButton" />
    </template>
    <template #link>
      <RouterLink to="/register" class="link">新規登録はこちら</RouterLink>
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
