<script setup lang="ts">
interface Props {
  title?: string;
  isForMultiMedia?: boolean;
}

withDefaults(defineProps<Props>(), {
  title: '',
  isForMultiMedia: true,
});

interface Emits {
  (e: 'clickButton'): void;
}

const emit = defineEmits<Emits>();
</script>

<template>
  <div class="information-detail-frame-box-content">
    <div class="information-detail-frame-box-title">
      <span class="title">{{ title }}</span>
      <div class="wrapper-button">
        <slot name="button"> </slot>
      </div>
    </div>
    <slot name="content"></slot>
  </div>
</template>

<style lang="scss" scoped>
// wrapper-button > i にcssを当てるため，scopedをつけない
.information-detail-frame {
  &-box {
    &-content {
      width: auto;
      border-radius: 2px;
      box-shadow: 0px 10px 10px -5px rgba(0, 0, 0, 0.6);
    }
    &-title {
      width: 100%;
      text-align: left;
      height: auto;
      min-height: 40px;
      background-color: #aaa;
      border-radius: 2px 2px 0 0;
      display: table;
      position: relative;
      > .title {
        font-size: 18px;
        font-weight: 700;
        padding-left: 15px;
        display: table-cell;
        vertical-align: middle;
      }
      > .wrapper-button {
        height: 100%;
        width: 40px;
        position: absolute;
        right: 0;
        > i {
          height: 100%;
          width: 100%;
        }
      }
    }
  }
}
</style>
