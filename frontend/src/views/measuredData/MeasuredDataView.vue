<script setup lang="ts">
import InformationSelect from '@/components/common/InformationSelect.vue';
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
  onChangeSdi12Select,
  sdi12ChartDataSet,
  onChangeEnvironmentalSelect,
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
      <InformationSelect
        class="selector"
        :title="$t('MeasuredData.displayedItem')"
        :option-list="sdi12OptionList"
        @selectedValue="onChangeSdi12Select"
      />
    </div>
    <div class="wrapper-chart">
      <LineChart
        ref="lineRef"
        class="chart"
        :chartData="environmentalChartDataSet"
        :options="environmentalChartConfig"
      />
      <InformationSelect
        class="selector"
        :title="$t('MeasuredData.displayedItem')"
        :option-list="environmentalOptionList"
        @selectedValue="onChangeEnvironmentalSelect"
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
