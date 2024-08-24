<script setup lang="ts">
import CommonButton from '@/components/common/commonButton/CommonButton.vue';
import CommonOverlay from '@/components/common/commonOverlay/CommonOverlay.vue';
import { AccountStore } from '@/store/accountStore';
import { ref } from 'vue';
import AccountDeleteDialog from '../accountDeleteDialog/AccountDeleteDialog.vue';

const showDeleteDialog = ref(false);

const accountStore = AccountStore();

/**
 * アカウント削除ボタン押下時の処理
 */
const onClickAccountDeleteButton = () => {
  showDeleteDialog.value = true;
};

/**
 * アカウント削除ダイアログのキャンセルボタン押下時の処理
 */
const onClickAccountDeleteCancel = () => {
  showDeleteDialog.value = false;
};

/**
 * アカウント削除ダイアログの削除ボタン押下時の処理
 */
const onClickAccountDeleteAccept = async () => {
  await accountStore.delete();
  showDeleteDialog.value = false;
};

/**
 * ログアウトボタン押下時の処理
 */
const onClickLogout = async () => {
  await accountStore.logout();
};
</script>
<template>
  <div class="wrapper-menu">
    <CommonButton
      :button-title="$t('Button.logout')"
      width="175px"
      icon="ems-exit"
      justify-content="flex-start"
      @click-button="onClickLogout"
    />
    <CommonButton
      :button-title="$t('Button.deleteAccount')"
      width="175px"
      icon="ems-user-minus"
      type="fill"
      justify-content="flex-start"
      color="var(--ems-alert-theme)"
      title-color="white"
      font-weight="bold"
      @click-button="onClickAccountDeleteButton"
    >
      <template #icon>
        <span class="ems-user-minus common-icon-style icon-alert"></span>
      </template>
    </CommonButton>
  </div>
  <CommonOverlay v-model="showDeleteDialog" persistent>
    <AccountDeleteDialog
      @on-click-back="onClickAccountDeleteCancel"
      @on-click-delete-accept="onClickAccountDeleteAccept"
    />
  </CommonOverlay>
</template>
<style lang="scss" scoped>
.wrapper {
  &-menu {
    border-radius: 4px;
    background-color: #eee;
    box-shadow: 0px 10px 20px -5px rgba(0, 0, 0, 0.6);
    padding: 8px;
    display: grid;
    gap: 8px;
  }
}

.icon {
  &-alert {
    color: white;
    font-size: 20px;
  }
}
</style>
