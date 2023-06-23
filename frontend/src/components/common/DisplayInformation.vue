<script setup lang="ts">
interface Props {
  title?: string;
  content?: string;
  height?: string;
  titleWidth?: string;
  titleBackGroundColor?: string;
  contentBackGroundColor?: string;
  showBorderTop?: boolean;
  showBorderBottom?: boolean;
  isEditMode?: boolean;
}

const props = withDefaults(defineProps<Props>(), {
  title: '',
  content: '',
  height: '36px',
  titleWidth: '40%',
  titleBackGroundColor: '#eeeeee',
  contentBackGroundColor: 'white',
  showBorderTop: false,
  showBorderBottom: true,
  isEditMode: false,
});
</script>

<template>
  <div
    class="information-row"
    :class="{ 'border-top': showBorderTop, 'border-bottom': showBorderBottom }"
  >
    <div class="information-title" :style="{ width: titleWidth }">
      <span v-if="title" class="span-text span-title">{{ props.title }}</span>
      <slot name="title"></slot>
    </div>
    <div class="information-content" :style="{ width: `calc(100% - ${titleWidth})` }">
      <span v-if="content && !isEditMode" class="span-text span-content">{{ props.content }}</span>
      <slot name="content"></slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.information {
  &-row {
    display: flex;
    min-height: 36px;
  }
  &-title {
    height: auto;
    display: table;
    border-right: 1px solid #33333380;
  }
  &-content {
    display: table;
  }
}
.span {
  &-text {
    display: table-cell;
    vertical-align: middle;
    color: #333333;
  }
  &-title {
    font-size: 14px;
  }
  &-content {
    font-weight: bold;
  }
}
.border {
  &-top {
    border-top: 1px solid #33333380;
  }
  &-bottom {
    border-bottom: 1px solid #33333380;
  }
}
</style>
