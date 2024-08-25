<script setup lang="ts">
interface Props {
  size?: string;
}

withDefaults(defineProps<Props>(), {
  size: '40px',
});
</script>

<template>
  <div class="flower-spinner">
    <div class="dots-container">
      <div class="bigger-dot">
        <div class="smaller-dot"></div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
$flower_spinner_size: v-bind(size);
$bigger_spinner_size: calc($flower_spinner_size * 0.4);
$bigger_spinner_side_size: calc($flower_spinner_size * 0.3);
$smaller_spinner_size: calc($flower_spinner_size * 0.2);
$smaller_spinner_side_size: calc($flower_spinner_size * 0.15);

.flower-spinner,
.flower-spinner * {
  box-sizing: border-box;
}

.flower-spinner {
  height: $flower_spinner_size;
  width: $flower_spinner_size;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}

.flower-spinner .dots-container {
  height: calc($flower_spinner_size / 7);
  width: calc($flower_spinner_size / 7);
}

.flower-spinner .smaller-dot {
  background: var(--ems-theme);
  height: 100%;
  width: 100%;
  border-radius: 50%;
  animation: flower-spinner-smaller-dot-animation 2.5s 0s infinite both;
}

.flower-spinner .bigger-dot {
  background: var(--ems-theme);
  height: 100%;
  width: 100%;
  padding: 10%;
  border-radius: 50%;
  animation: flower-spinner-bigger-dot-animation 2.5s 0s infinite both;
}

@keyframes flower-spinner-bigger-dot-animation {
  0%,
  100% {
    box-shadow: rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px;
  }

  50% {
    transform: rotate(180deg);
  }

  25%,
  75% {
    box-shadow: rgb(var(--ems-theme-rgb)) $bigger_spinner_size 0px 0px,
      rgb(var(--ems-theme-rgb)) calc(-1 * $bigger_spinner_size) 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px $bigger_spinner_size 0px,
      rgb(var(--ems-theme-rgb)) 0px calc(-1 * $bigger_spinner_size) 0px,
      rgb(var(--ems-theme-rgb)) $bigger_spinner_side_size calc(-1 * $bigger_spinner_side_size) 0px,
      rgb(var(--ems-theme-rgb)) $bigger_spinner_side_size $bigger_spinner_side_size 0px,
      rgb(var(--ems-theme-rgb)) calc(-1 * $bigger_spinner_side_size)
        calc(-1 * $bigger_spinner_side_size) 0px,
      rgb(var(--ems-theme-rgb)) calc(-1 * $bigger_spinner_side_size) $bigger_spinner_side_size 0px;
  }

  100% {
    transform: rotate(360deg);
    box-shadow: rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px;
  }
}

@keyframes flower-spinner-smaller-dot-animation {
  0%,
  100% {
    box-shadow: rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px;
  }

  25%,
  75% {
    box-shadow: rgb(var(--ems-theme-rgb)) $smaller_spinner_size 0px 0px,
      rgb(var(--ems-theme-rgb)) calc(-1 * $smaller_spinner_size) 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px $smaller_spinner_size 0px,
      rgb(var(--ems-theme-rgb)) 0px calc(-1 * $smaller_spinner_size) 0px,
      rgb(var(--ems-theme-rgb)) $smaller_spinner_side_size calc(-1 * $smaller_spinner_side_size) 0px,
      rgb(var(--ems-theme-rgb)) $smaller_spinner_side_size $smaller_spinner_side_size 0px,
      rgb(var(--ems-theme-rgb)) calc(-1 * $smaller_spinner_side_size)
        calc(-1 * $smaller_spinner_side_size) 0px,
      rgb(var(--ems-theme-rgb)) calc(-1 * $smaller_spinner_side_size) $smaller_spinner_side_size 0px;
  }

  100% {
    box-shadow: rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px,
      rgb(var(--ems-theme-rgb)) 0px 0px 0px, rgb(var(--ems-theme-rgb)) 0px 0px 0px;
  }
}
</style>
