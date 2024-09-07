<script setup lang="ts">
import CommonButton from '@/components/common/commonButton/CommonButton.vue';
import CommonDisplayInformation from '@/components/common/commonDisplayInformation/CommonDisplayInformation.vue';
import CommonFormWindow from '@/components/common/commonFormWindow/CommonFormWindow.vue';
import CommonInput from '@/components/common/commonInput/CommonInput.vue';
import CommonSelect from '@/components/common/commonSelect/CommonSelect.vue';
import { convertLocalDate } from '@/utils/dayjsUtil';
import MicroControllerDetailEditCancelDialog from './components/MicroControllerDetailEditCancelDialog.vue';
import { useMicroControllerDetail } from './composable';

const props = defineProps<{
  microControllerUuid: string;
}>();

const {
  isEditMode,
  onClickEdit,
  onClickCancel,
  onClickSave,
  microControllerDetail,
  unitNameForEdit,
  intervalForEdit,
  intervalList,
  addressForEdit,
  sdiAddressErrorMessage,
  isShowCancelModal,
  onClickCancelDeny,
  onClickCancelAccept,
} = useMicroControllerDetail(props);
</script>
<template>
  <CommonFormWindow window-style="vertical">
    <template #title>
      <span class="title">{{ $t('MicroControllerDetail.title') }}</span>
    </template>
    <template #content>
      <div class="action-bar">
        <CommonButton
          v-if="!isEditMode"
          type="fill"
          :button-title="$t('Button.edit')"
          @clickButton="onClickEdit"
        />
        <CommonButton
          v-if="isEditMode"
          :button-title="$t('Button.cancel')"
          width="120px"
          @clickButton="onClickCancel"
        />
        <CommonButton
          v-if="isEditMode"
          type="fill"
          :button-title="$t('Button.save')"
          @clickButton="onClickSave"
        />
      </div>
      <div class="wrapper-content">
        <div class="wrapper-unit-info">
          <div class="wrapper-unit-photo">
            <span class="icon ems-photo"></span>
          </div>
          <div class="wrapper-unit-name">
            <div class="span-name-box">
              <span v-if="!isEditMode" class="name">{{
                `${
                  !microControllerDetail.name
                    ? $t('MicroControllerDetail.unnamedDevice')
                    : microControllerDetail.name
                }`
              }}</span>
              <CommonInput v-if="isEditMode" v-model="unitNameForEdit" type="text" />
              <span class="address">{{
                `${$t('MicroControllerDetail.macAddress')}: ${microControllerDetail.macAddress}`
              }}</span>
            </div>
          </div>
        </div>
        <CommonDisplayInformation
          :title="$t('MicroControllerDetail.interval')"
          :content="`${microControllerDetail.interval}分`"
          show-border-top
          :is-edit-mode="isEditMode"
        >
          <template #content>
            <CommonSelect
              v-if="isEditMode"
              class="input-space"
              v-model="intervalForEdit"
              :option-list="intervalList"
            />
          </template>
        </CommonDisplayInformation>
        <CommonDisplayInformation
          :title="$t('MicroControllerDetail.measurementAddress')"
          :content="
            !microControllerDetail.sdi12Address
              ? $t('MicroControllerDetail.unsetSdiAddress')
              : microControllerDetail.sdi12Address
          "
          :is-edit-mode="isEditMode"
        >
          <template #content>
            <CommonInput
              v-if="isEditMode"
              class="input-space"
              v-model="addressForEdit"
              type="text"
              :error-message="sdiAddressErrorMessage"
            />
          </template>
        </CommonDisplayInformation>
        <CommonDisplayInformation
          :title="$t('MicroControllerDetail.registrationDate')"
          :content="convertLocalDate(microControllerDetail.createdAt)"
        />
        <CommonDisplayInformation
          :title="$t('MicroControllerDetail.lastUpdatedDate')"
          :content="convertLocalDate(microControllerDetail.updatedAt)"
        />

        <MicroControllerDetailEditCancelDialog
          v-model="isShowCancelModal"
          @on-click-deny="onClickCancelDeny"
          @on-click-accept="onClickCancelAccept"
        />
      </div>
    </template>
  </CommonFormWindow>
</template>

<style lang="scss" scoped>
$unit_info_height: 100px;
$action_bar_height: 50px;

.wrapper {
  &-content {
    height: 100%;
    width: 100%;
    overflow-y: auto;
  }
  &-unit-info {
    height: #{$unit_info_height};
    width: 100%;
    display: flex;
  }
  &-unit-photo {
    height: 100%;
    width: #{$unit_info_height};
    display: flex;
    justify-content: center;
    align-items: center;
    > .icon {
      border: 2px solid #00000022;
      border-radius: 50%;
      height: 80px;
      width: 80px;
      font-size: 50px;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
  &-unit-name {
    height: 100%;
    width: calc(100% - #{$unit_info_height});
    position: relative;
    display: flex;
    align-items: center;
    padding-left: 20px;
  }
}
.span {
  &-name-box {
    display: flex;
    flex-direction: column;
    text-align: left;
    > .name {
      font-size: 18px;
      max-width: 200px;
    }
    > .address {
      font-size: 12px;
      color: #888888;
    }
  }
}
.action-bar {
  width: 100%;
  height: $action_bar_height;
  border-bottom: 1px solid #00000022;
  display: flex;
  position: sticky;
  justify-content: flex-end;
  gap: 16px;
  top: 0;
}
.title {
  font-size: 24px;
}
.input-space {
  padding: 0 16px;
  display: grid;
  align-items: center;
}
</style>
