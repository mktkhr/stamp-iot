<script setup lang="ts">
const props = defineProps<{
  title?: string;
}>();
</script>

<template>
  <div class="form-frame">
    <slot name="icon"></slot>
    <div class="title">
      <span v-if="title">{{ props.title }}</span>
    </div>
    <slot name="mailAddress"></slot>
    <slot name="password"></slot>
    <slot name="passwordConfirm"></slot>
    <slot name="button"></slot>
    <slot name="link"></slot>
  </div>
</template>

<style lang="scss" scoped>
.form-frame {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  width: 80%;
  max-width: 600px;
  height: 60%;
  max-height: 800px;
  margin: auto;
  border: 1px solid #888888;
  border-radius: 5px;
  box-shadow: 0px 10px 20px -5px rgba(0, 0, 0, 0.6);
}
.title {
  margin-bottom: 30px;
}
.title span {
  font-size: 25px;
}

@media screen and (max-height: 900px) {
  .form-frame {
    height: 80%;
  }
  .title span {
    font-size: 20px;
  }
}
</style>
