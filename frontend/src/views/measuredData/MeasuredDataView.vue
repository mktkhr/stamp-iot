<script setup lang="ts">
import CommonSelect from '@/components/common/commonSelect/CommonSelect.vue';
import { LineChart } from 'vue-chart-3';
import { useMeasuredData } from './composable';

const props = defineProps<{
  microControllerUuid: string;
}>();

const {
  sdi12ChartConfig,
  environmentalChartConfig,
  sdi12OptionList,
  environmentalOptionList,
  selectedSdi12Option,
  sdi12ChartDataSet,
  selectedEnvironmentalOption,
  environmentalChartDataSet,
} = useMeasuredData(props.microControllerUuid);
</script>

<template>
  <div class="wrapper-chart-container">
    <div class="wrapper-chart">
      <LineChart
        ref="lineRef"
        class="chart"
        :chartData="sdi12ChartDataSet"
        :options="sdi12ChartConfig"
      />
      <CommonSelect
        class="selector"
        v-model="selectedSdi12Option"
        :placeholder="$t('MeasuredData.displayedItem')"
        :option-list="sdi12OptionList"
      />
    </div>
    <div class="wrapper-chart">
      <LineChart
        ref="lineRef"
        class="chart"
        :chartData="environmentalChartDataSet"
        :options="environmentalChartConfig"
      />
      <CommonSelect
        class="selector"
        v-model="selectedEnvironmentalOption"
        :placeholder="$t('MeasuredData.displayedItem')"
        :option-list="environmentalOptionList"
      />
    </div>
  </div>
</template>

<style lang="scss" scoped>
.wrapper {
  &-chart {
    padding: 16px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border-radius: 16px;
    background-color: white;
    box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.6);

    &-container {
      height: 100%;
      width: 100%;
      overflow-y: auto;
      display: flex;
      flex-direction: column;
      padding: 16px;
      gap: 16px;
      background-color: rgba(var(--ems-theme-rgb), 0.2);
    }
  }
}
.selector {
  width: 80%;
  max-width: 500px;
}
.chart {
  width: 100%;
}
</style>
