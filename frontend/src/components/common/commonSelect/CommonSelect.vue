<script setup lang="ts">
import { SelectOptionType } from './composable';

interface Props {
  placeholder?: string;
  optionList: SelectOptionType[];
  errorMessage?: string;
  useDummyOption?: boolean;
}

withDefaults(defineProps<Props>(), {
  placeholder: '',
  errorMessage: '',
  type: 'text',
  useDummyOption: false,
});

const modelValue = defineModel<string>({ required: true });
</script>

<template>
  <div class="wrapper-select-container">
    <div class="wrapper-select">
      <select v-model="modelValue">
        <option v-if="useDummyOption" value="" hidden disabled selected></option>
        <option
          v-for="option in optionList"
          :key="option.key"
          :value="option.key"
          :selected="option.key === modelValue"
        >
          {{ option.word }}
        </option>
      </select>
      <label v-if="placeholder" :class="{ inputted: modelValue }">{{ placeholder }}</label>
    </div>
    <div class="error-wrap" v-if="errorMessage">
      <span class="ems-error"></span>
      <p class="error-message">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<style lang="scss" scoped>
$select_height: 32px;
$select_padding: 4px; // CommonInputとpaddingを合わせている
$lable_height: 24px;

.wrapper {
  &-select {
    position: relative;
    height: $select_height;

    > select {
      height: 100%;
      width: 100%;
      padding: 0 $select_padding;
      text-align: left;
      letter-spacing: 1px;
      border-radius: 4px;
      box-sizing: border-box;
      outline: none;

      & ~ label {
        position: absolute;
        height: 24px;
        text-align: left;
        transition: 0.3s;
        letter-spacing: 1px;
        pointer-events: none;
      }

      &:not(:focus) {
        border: 1px solid #000000aa;

        // selectの値が空でfocus状態でない場合
        & ~ label {
          font-size: 16px;
          top: calc(($select_height - $lable_height) / 2);
          left: 8px;
          color: #444;
        }

        // selectに値があってfocus状態でない場合
        ~ .inputted {
          font-size: 12px;
          top: -18px;
          left: 0;
        }
      }

      &:focus {
        border: 2px solid var(--ems-theme);

        & ~ label {
          font-size: 12px;
          top: -18px;
          left: 0;
          transition: 0.3s;
          color: var(--ems-theme);
        }
      }
    }

    &-container {
      width: 100%;
      height: calc($select_height + $select_padding * 2);
      padding: $select_padding 0;
    }
  }
}

.error {
  &-wrap {
    height: $select_padding;
    display: flex;
    text-align: left;
    align-items: center;

    > span {
      font-size: $select_padding;
      color: var(--ems-error-theme);
    }
  }
  &-message {
    color: var(--ems-error-theme);
    font-size: 10px;
  }
}
</style>
