<script setup lang="ts">
interface Props {
  placeholder?: string;
  errorMessage?: string;
  type?: 'text' | 'email' | 'password';
}

withDefaults(defineProps<Props>(), {
  placeholder: '',
  errorMessage: '',
  type: 'text',
});

const modelValue = defineModel<string>({ required: true, default: '' });
</script>

<template>
  <div class="wrapper-input-container">
    <div class="wrapper-input">
      <input :type="type" placeholder="" v-model="modelValue" />
      <label v-if="placeholder" :class="{ inputted: modelValue }">{{ placeholder }}</label>
    </div>
    <div class="error-wrap" v-if="errorMessage">
      <span class="ems-error"></span>
      <p class="error-message">{{ errorMessage }}</p>
    </div>
  </div>
</template>

<style lang="scss" scoped>
$input_height: 32px;
$input_padding: 16px;
$lable_height: 24px;

.wrapper {
  &-input {
    position: relative;
    height: $input_height;

    > input {
      height: 100%;
      width: 100%;
      padding: 0 $input_padding;
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

        // inputの値が空でfocus状態でない場合
        & ~ label {
          font-size: 16px;
          top: calc(($input_height - $lable_height) / 2);
          left: $input_padding;
          color: #444;
        }

        // inputに値があってfocus状態でない場合
        ~ .inputted {
          font-size: 12px;
          top: calc(($input_padding + 2px) * -1);
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
      height: calc($input_height + $input_padding * 2);
      padding: $input_padding 0;
    }
  }
}

.error {
  &-wrap {
    height: $input_padding;
    display: flex;
    text-align: left;
    align-items: center;

    > span {
      font-size: $input_padding;
      color: var(--ems-error-theme);
    }
  }
  &-message {
    color: var(--ems-error-theme);
    font-size: 10px;
  }
}
</style>
