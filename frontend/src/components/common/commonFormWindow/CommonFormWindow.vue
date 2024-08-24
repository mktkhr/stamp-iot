<script setup lang="ts"></script>

<template>
  <div class="wrapper-frame">
    <div class="container">
      <slot name="title"></slot>
    </div>
    <div class="container">
      <slot name="content"></slot>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.wrapper {
  &-frame {
    width: 100%;
    max-width: 800px;
    height: max-content;
    max-height: 800px;
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    margin: auto;
    padding: 32px 16px;
    border-radius: 24px;
    box-shadow: 0px 8px 24px -2px rgba(0, 0, 0, 0.6);
    background-color: white;
    display: flex;

    > .container {
      width: 50%;
      height: 100%;
      padding: 16px;
      display: flex;
      flex-direction: column;
      gap: 16px;
      overflow: auto;

      &:first-child {
        text-align: left;
      }
    }
  }
}

@media screen and (max-width: 900px) {
  .wrapper {
    &-frame {
      flex-direction: column;

      > .container {
        width: 100%;
        height: max-content;
      }
    }
  }
}
</style>
