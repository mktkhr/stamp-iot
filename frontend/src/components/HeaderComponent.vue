<script setup lang="ts">
import { computed, ref } from 'vue';
import router from '@/router';
import { AccountStore } from '@/store/accountStore';
import CommonButton from './common/CommonButton.vue';
import ModalWindow from '@/components/common/ModalWindow.vue';
import NotificationBar from './common/NotificationBar.vue';

import { StatusCode } from '@/constants/statusCode';
import { NotificationType } from '@/constants/notificationType';
import { i18n } from '@/main';

interface Props {
  showHamburgerMenu: boolean;
  menuState: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  hamburgerState: true,
  menuState: false,
});

const emit = defineEmits<{
  (e: 'clickEvent', menuState: boolean);
  (e: 'onClickLogout');
}>();

const accountStore = AccountStore();

const isShowModal = ref(false);
const isDeleteAccountChecked = ref(false);
const isDeleteAccountDataChecked = ref(false);
const errorMessage = ref('');
const showNotification = ref(false);

const showAcceptButton = computed(
  () => isDeleteAccountChecked.value && isDeleteAccountDataChecked.value
);

const showLogoutButton = computed(() => {
  if (router.currentRoute.value.name == 'login' || router.currentRoute.value.name == 'register') {
    return false;
  }
  return true;
});

const onClickMenuButton = (): void => {
  emit('clickEvent', !props.menuState);
};

const onClickLogout = async () => {
  if (confirm(i18n.global.t('Header.logoutConfirm'))) {
    changeButtonView();
    emit('onClickLogout');
  } else {
    return;
  }
};

const changeButtonView = () => {
  const element = document.getElementById('manage');
  if (!element) {
    return;
  }
  if (!element.style.display || element.style.display === 'none') {
    element.style.display = 'flex';
  } else {
    element.style.display = 'none';
  }
};

const onClickAccountDelete = () => {
  isShowModal.value = true;
};

const onClickDeny = () => {
  isShowModal.value = false;
};

const onClickAccept = async () => {
  await accountStore
    .delete()
    .then(() => {
      changeButtonView();
      isShowModal.value = false;
      isDeleteAccountChecked.value = false;
      isDeleteAccountDataChecked.value = false;

      router.replace('/login');
    })
    .catch((e) => {
      const statusCode = e.response.status.toString();
      if (statusCode === StatusCode.INTERNAL_SERVER_ERROR) {
        errorMessage.value = i18n.global.t('ApiError.internalServerError');
      } else {
        errorMessage.value = i18n.global.t('ApiError.unexpectedError');
      }
      changeButtonView();
      isShowModal.value = false;
      isDeleteAccountChecked.value = false;
      isDeleteAccountDataChecked.value = false;
      showNotification.value = true;
      setTimeout(() => (showNotification.value = false), 5000);
    });
};
</script>

<template>
  <header>
    <div class="header-left">
      <button
        class="hamburger"
        id="hamburger"
        v-bind:class="{ active: menuState }"
        v-on:click="onClickMenuButton"
        v-if="showHamburgerMenu"
      >
        <span></span>
        <span></span>
        <span></span>
      </button>
      <img src="@/assets/logo_white.png" alt="logo" />
    </div>
    <div class="header-right">
      <v-icon v-if="showLogoutButton" class="icon" @click="changeButtonView">
        ems-account_circle
      </v-icon>
    </div>
    <div class="account-manage" id="manage">
      <CommonButton
        class="account-button"
        :button-title="$t('Button.logout')"
        width="auto"
        @click="onClickLogout"
      />
      <CommonButton
        class="account-button"
        :button-title="$t('Button.deleteAccount')"
        width="auto"
        @click="onClickAccountDelete"
      />
    </div>
    <v-dialog v-model="isShowModal" max-width="600px">
      <ModalWindow
        :title="$t('Dialog.accountDeleteConfirm')"
        :description="$t('Dialog.accountDeleteConfirmDescription')"
      >
        <template #content>
          <div class="modal-content">
            <div class="wrapper-confirm">
              <input id="deleteAccount" type="checkbox" v-model="isDeleteAccountChecked" />
              <label for="deleteAccount">{{ $t('Dialog.accountDeleteConfirmDescription') }}</label>
            </div>
            <div class="wrapper-confirm">
              <input id="deleteAccountData" type="checkbox" v-model="isDeleteAccountDataChecked" />
              <label for="deleteAccountData">{{
                $t('Dialog.relatedDataDeleteConfirmDescription')
              }}</label>
            </div>
          </div>
        </template>
        <template #button>
          <CommonButton
            :button-title="$t('Button.back')"
            width="auto"
            @click-button="onClickDeny"
          />
          <CommonButton
            v-if="showAcceptButton"
            class="delete-button"
            :button-title="$t('Button.delete')"
            width="auto"
            @click-button="onClickAccept"
          />
        </template>
      </ModalWindow>
    </v-dialog>
    <div class="notification">
      <NotificationBar
        v-if="showNotification"
        :type="NotificationType.ERROR"
        :text="errorMessage"
      />
    </div>
  </header>
</template>
<style lang="scss" scoped>
header {
  width: 100%;
  height: 50px;
  background: #999999;
  display: flex;
  position: fixed;
  top: 0;
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.6);
  z-index: 999;
}
.hamburger {
  width: 100%;
  height: 100%;
  cursor: pointer;
  padding: 0;
  background-color: transparent;
  border-color: transparent;
}
.hamburger span {
  margin-left: 12px;
  width: 25px;
  height: 1px;
  background-color: #ffffff;
  position: relative;
  transition: ease 0.4s;
  display: block;
}

button {
  border-width: 0;
}

.hamburger span:nth-child(1) {
  top: 0;
}

.hamburger span:nth-child(2) {
  margin: 8px 12px;
}

.hamburger span:nth-child(3) {
  top: 0;
}
.hamburger.active span:nth-child(1) {
  top: 9px;
  transform: rotate(45deg);
}

.hamburger.active span:nth-child(2) {
  opacity: 0;
}

.hamburger.active span:nth-child(3) {
  top: -9px;
  transform: rotate(-45deg);
}
.header-left {
  height: 50px;
  display: flex;
  z-index: 999;
}
.header-left img {
  margin: 5px;
  height: 40px;
  width: auto;
}
.header-right {
  position: absolute;
  display: flex;
  right: 0;
  height: 50px;
  width: auto;
  margin-right: 10px;
  align-items: center;
  > .icon {
    font-size: 30px;
    color: white;
    cursor: pointer;
  }
}

.account-manage {
  position: absolute;
  right: 5px;
  top: 55px;
  padding: 10px;
  background-color: white;
  border-radius: 5px;
  display: none;
  box-shadow: 0 0 10px #0009;
  animation: fadeIn 0.7s cubic-bezier(0.33, 1, 0.68, 1) forwards;
}

.account-button + .account-button {
  margin-left: 10px;
}

.modal-content {
  padding: 10px;
  display: flex;
  flex-direction: column;
}

.wrapper-confirm {
  display: flex;
  align-items: center;
}

.delete-button {
  margin-left: 10px;
}
.notification {
  position: absolute;
  top: 50px;
  width: 100%;
}

@keyframes fadeIn {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}
</style>
