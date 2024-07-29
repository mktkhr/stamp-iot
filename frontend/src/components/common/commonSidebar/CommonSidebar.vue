<script setup lang="ts">
import SidebarTile from './components/sidebarTile/SidebarTile.vue';
</script>

<template>
  <div class="wrapper-sidebar">
    <RouterLink to="/home">
      <SidebarTile :title="$t('Navigator.home')">
        <template #icon>
          <span class="ems-home common-icon-style"></span>
        </template>
      </SidebarTile>
    </RouterLink>
  </div>
</template>

<style lang="scss" scoped>
.wrapper {
  &-sidebar {
    height: 100%;
    width: 100%;
    padding: 8px;
    display: flex;
    flex-direction: column;
    transition: 0.4s ease;
    position: relative;
  }
}
.current {
  background-color: rgba(var(--ems-theme));
}
</style>
