<script setup lang="ts">
import NotificationBar from '@/components/common/NotificationBar.vue';
import InformationSelect from '@/components/common/InformationSelect.vue';
import { LineChart } from 'vue-chart-3';
import { useMeasuredData } from './composable';

const {
  showNotification,
  notificationMessage,
  notificationType,
  sdi12ChartConfig,
  environmentalChartConfig,
  sdi12OptionList,
  environmentalOptionList,
  onChangeSdi12Select,
  sdi12ChartDataSet,
  onChangeEnvironmentalSelect,
  environmentalChartDataSet,
} = useMeasuredData();
</script>

<template>
  <NotificationBar :text="notificationMessage" :type="notificationType" v-if="showNotification" />
  <div class="main-content">
    <div class="graph-wrapper">
      <LineChart ref="lineRef" :chartData="sdi12ChartDataSet" :options="sdi12ChartConfig" />
      <InformationSelect
        title="表示項目"
        :option-list="sdi12OptionList"
        @selectedValue="onChangeSdi12Select"
      />
    </div>
    <div class="graph-wrapper">
      <LineChart
        ref="lineRef"
        :chartData="environmentalChartDataSet"
        :options="environmentalChartConfig"
      />
      <InformationSelect
        title="表示項目"
        :option-list="environmentalOptionList"
        @selectedValue="onChangeEnvironmentalSelect"
      />
    </div>
  </div>
</template>

<style scoped></style>
