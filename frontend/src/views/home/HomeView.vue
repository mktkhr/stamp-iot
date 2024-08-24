<script setup lang="ts">
import DefaultFrame from '@/components/DefaultFrame.vue';
import DisplayInformation from '@/components/common/DisplayInformation.vue';
import GridFrame from '@/components/common/GridFrame.vue';
import InformationDetailFrame from '@/components/common/InformationDetailFrame.vue';
import CommonButton from '@/components/common/commonButton/CommonButton.vue';
import CommonFormWindow from '@/components/common/commonFormWindow/CommonFormWindow.vue';
import CommonInput from '@/components/common/commonInput/CommonInput.vue';
import CommonOverlay from '@/components/common/commonOverlay/CommonOverlay.vue';
import { convertLocalDateTime } from '@/utils/dayjsUtil';
import { useHome } from './composable';

const {
  accountInfo,
  microControllerList,
  isShowModal,
  macAddressRef,
  macAddressError,
  onClickPlusButton,
  onClickRegister,
  onClickTile,
  onClickSetting,
} = useHome();
</script>

<template>
  <DefaultFrame :show-action-bar="false">
    <template #content>
      <CommonOverlay v-model="isShowModal" persistent>
        <CommonFormWindow>
          <template #title>
            <span class="title">{{ $t('Home.registerNewDevice') }}</span>
          </template>
          <template #content>
            <span>{{ $t('Home.enterMacAddress') }}</span>
            <CommonInput
              class="position-input"
              v-model="macAddressRef"
              type="text"
              :placeholder="$t('Home.macAddress')"
              :error-message="macAddressError"
            />
            <div class="wrapper-button">
              <CommonButton
                :button-title="$t('Button.cancel')"
                @click-button="() => (isShowModal = false)"
              />
              <CommonButton
                :button-title="$t('Button.register')"
                @click-button="onClickRegister"
                type="fill"
              />
            </div>
          </template>
        </CommonFormWindow>
      </CommonOverlay>

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
  &-button {
    display: flex;
    justify-content: space-evenly;
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
.title {
  font-size: 24px;
}
</style>
