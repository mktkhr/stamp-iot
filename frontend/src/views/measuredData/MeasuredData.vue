<script setup lang="ts">
import NotificationBar from '@/components/common/NotificationBar.vue';
import InformationSelect from '@/components/common/InformationSelect.vue';
import { LineChart } from 'vue-chart-3';
import { useMeasuredData } from './composable';

const props = defineProps<{
  microControllerUuid: string;
}>();

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
} = useMeasuredData(props.microControllerUuid);
</script>

<template>
  <NotificationBar :text="notificationMessage" :type="notificationType" v-if="showNotification" />
  <div class="wrapper-content">
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
  &-content {
    height: 100%;
    width: 100%;
    overflow-y: auto;
  }
  &-chart {
    padding: 16px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
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
