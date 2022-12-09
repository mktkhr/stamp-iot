<script setup lang="ts">
import Plus from 'vue-material-design-icons/Plus.vue';

interface Props {
  title?: string;
  isForMultiMedia?: boolean;
  useAccountInfo?: boolean;
}

withDefaults(defineProps<Props>(), {
  title: '',
  isForMultiMedia: true,
  useAccountInfo: false,
});

interface Emits {
  (e: 'clickButton'): void;
}

const emit = defineEmits<Emits>();
</script>

<template>
  <div
    class="content-box"
    v-bind:class="{
      col: isForMultiMedia,
      'col-one-half': isForMultiMedia,
      'col-one-fourth': isForMultiMedia,
    }"
  >
    <div class="title-box">
      <span>{{ title }}</span>
      <button v-if="useAccountInfo" style="height: 40px">
        <Plus id="plus-icon" style="" @click="emit('clickButton')" />
      </button>
    </div>
    <slot name="content"></slot>
  </div>
</template>

<style scoped>
button {
  padding: 0;
  float: right;
  border: 1px solid #aaa;
  margin: 1px 1px 0 0;
}
#plus-icon {
  width: 35px;
  margin-top: auto;
  margin-bottom: auto;
  padding: 0;
  color: #888;
}
.content-box {
  width: 100%;
  box-shadow: 0px 10px 20px -5px rgba(0, 0, 0, 0.6);
}
.title-box {
  width: 100%;
  text-align: left;
  height: auto;
  min-height: 40px;
  background-color: #aaa;
  display: table;
}
.title-box span {
  font-size: 18px;
  font-weight: 700;
  padding-left: 15px;
  display: table-cell;
  vertical-align: middle;
}
.col {
  width: calc(100% - 10px);
  margin: 5px;
}

@media (min-width: 600px) {
  .col-one-half {
    width: calc(50% - 10px);
  }
  .col {
    float: left;
  }
}

@media (min-width: 1000px) {
  .col-one-fourth {
    width: calc(25% - 10px);
  }
}
</style>
