<script setup lang="ts">
import DefaultFrame from '@/components/DefaultFrame.vue';
import DisplayInformation from '@/components/common/DisplayInformation.vue';
import GridFrame from '@/components/common/GridFrame.vue';
import InformationDetailFrame from '@/components/common/InformationDetailFrame.vue';
import InformationInput from '@/components/common/InformationInput.vue';
import ModalWindow from '@/components/common/ModalWindow.vue';
import CommonButton from '@/components/common/commonButton/CommonButton.vue';

import { convertLocalDateTime } from '@/utils/dayjsUtil';
import { useHome } from './composable';

const {
  accountInfo,
  microControllerList,
  isShowModal,
  macAddressError,
  onClickPlusButton,
  onClickSubmit,
  getMacAddress,
  onClickRegister,
  onClickTile,
  onClickSetting,
} = useHome();
</script>

<template>
  <DefaultFrame :show-action-bar="false">
    <template #content>
      <v-dialog v-model="isShowModal" width="80%" max-width="500px">
        <ModalWindow
          @click-button="onClickSubmit"
          :title="$t('Home.registerNewDevice')"
          :description="$t('Home.enterMacAddress')"
        >
          <template #content>
            <div class="wrapper-input">
              <InformationInput
                class="input-content"
                text
                @input-value="getMacAddress"
                :error-message="macAddressError"
              />
            </div>
          </template>
          <template #button>
            <CommonButton :button-title="$t('Button.register')" @click-button="onClickRegister" />
          </template>
        </ModalWindow>
      </v-dialog>

      <div class="wrapper-main-content">
        <div class="wrapper-account-info" v-if="accountInfo">
          <InformationDetailFrame
            class="account-info"
            :title="$t('Home.accountInformation')"
            @clickButton="onClickPlusButton"
          >
            <template #button>
              <span class="ems-add_circle icon" @click="onClickPlusButton"></span>
            </template>
            <template #content>
              <DisplayInformation
                :title="$t('Home.accountName')"
                :content="accountInfo.name ?? $t('Home.unnamed')"
              />
              <DisplayInformation
                :title="$t('Home.registrationDate')"
                :content="convertLocalDateTime(accountInfo.createdAt)"
              />
              <DisplayInformation
                :title="$t('Home.lastUpdatedDate')"
                :content="convertLocalDateTime(accountInfo.updatedAt)"
              />
            </template>
          </InformationDetailFrame>
        </div>
        <div class="wrapper-micro-controller">
          <template v-if="microControllerList">
            <GridFrame>
              <template #content>
                <div
                  class="wrapper-info"
                  v-for="microController in microControllerList"
                  :key="microController.uuid"
                >
                  <InformationDetailFrame
                    class="micro-controller-tile"
                    :title="microController.name ?? $t('Home.unnamedDevice')"
                    @click="onClickTile(microController.uuid)"
                  >
                    <template #button>
                      <span
                        class="ems-settings icon"
                        @click.stop="onClickSetting(microController.uuid)"
                      ></span>
                    </template>
                    <template #content>
                      <DisplayInformation
                        :title="$t('Home.macAddress')"
                        :content="microController.macAddress"
                      />
                      <DisplayInformation
                        :title="$t('Home.interval')"
                        :content="microController.interval.toString()"
                      />
                      <DisplayInformation
                        :title="$t('Home.registrationDateTime')"
                        :content="convertLocalDateTime(microController.createdAt)"
                      />
                      <DisplayInformation
                        :title="$t('Home.lastUpdatedDateTime')"
                        :content="convertLocalDateTime(microController.updatedAt)"
                      />
                    </template>
                  </InformationDetailFrame>
                </div>
              </template>
            </GridFrame>
          </template>
        </div>
      </div>
    </template>
  </DefaultFrame>
</template>

<style lang="scss" scoped>
$account_info_height: 170px;
.wrapper {
  &-main-content {
    height: 100%;
    width: 100%;
    overflow-y: auto;
  }
  &-account-info {
    padding: 10px;
    width: 100%;
  }
  &-micro-controller {
    height: calc(100% - #{$account_info_height});
    width: 100%;
  }
  &-info {
    padding: 10px;
  }
  &-input {
    width: 100%;
    display: flex;
    justify-content: center;
    padding: 8px 16px;
    > .input-content {
      width: 80%;
    }
  }
}
img {
  margin-top: 30px;
  height: 50px;
  width: auto;
}
.micro-controller-tile {
  cursor: pointer;
}
.account-info {
  max-width: 400px;
}
.icon {
  height: 32px;
  width: 32px;
  font-size: 28px;
  color: var(--ems-theme);
  cursor: pointer;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.2s ease;

  &:hover {
    background-color: #00000020;
  }
}
</style>
