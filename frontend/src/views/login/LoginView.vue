<script setup lang="ts">
import CommonButton from '@/components/common/commonButton/CommonButton.vue';
import CommonFormWindow from '@/components/common/commonFormWindow/CommonFormWindow.vue';
import CommonInput from '@/components/common/commonInput/CommonInput.vue';
import { useLogin } from './composable';

const { mailAddressRef, passwordRef, mailAddressError, passwordError, onClickLoginButton } =
  useLogin();
</script>

<template>
  <CommonFormWindow>
    <template #title>
      <img class="position-img" src="@/assets/logo_blue.png" alt="logo" />
      <span class="title">{{ $t('Login.title') }}</span>
    </template>
    <template #content>
      <CommonInput
        class="position-input"
        v-model="mailAddressRef"
        type="email"
        :placeholder="$t('Login.mailAddress')"
        :error-message="mailAddressError"
      />
      <CommonInput
        class="position-input"
        v-model="passwordRef"
        type="password"
        :placeholder="$t('Login.password')"
        :error-message="passwordError"
      />
      <div class="wrapper-button-container">
        <RouterLink to="/register">
          <CommonButton :button-title="$t('Login.toRegister')" type="float" />
        </RouterLink>
        <CommonButton
          :button-title="$t('Button.login')"
          type="fill"
          @click-button="onClickLoginButton"
        />
      </div>
    </template>
  </CommonFormWindow>
</template>

<style lang="scss" scoped>
.wrapper {
  &-button-container {
    width: 100%;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: space-evenly;
  }
  &-link {
    width: 100%;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
}

.title {
  font-size: 24px;
}

.position {
  &-img {
    margin: 0;
  }
}

img {
  height: 50px;
  width: max-content;
}

@media screen and (max-width: 900px) {
  .title {
    margin: 0 auto;
  }
  .position {
    &-img {
      margin: 0 auto;
    }
    &-input {
      width: 80%;
      margin: 0 auto;
    }
  }
}
</style>
