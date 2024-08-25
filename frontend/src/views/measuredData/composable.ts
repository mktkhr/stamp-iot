import { SelectOptionType } from '@/components/common/commonSelect/composable';
import { StatusCode } from '@/constants/statusCode';
import { i18n } from '@/main';
import {
  convertEnvironmentalKeyWordToScale,
  convertEnvironmentalKeyWordToTitle,
  convertSdi12KeyWordToScale,
  convertSdi12KeyWordToTitle,
} from '@/methods/measuredData';
import { AlertStore } from '@/store/alertStore';
import { MeasuredDataStore } from '@/store/measuredDataStore';
import { MicroControllerStore } from '@/store/microControllerStore';
import { generateRandowmString } from '@/utils/stringUtil';
import { Chart, ChartData, ChartOptions, registerables } from 'chart.js';
import { computed, ref } from 'vue';

export const useMeasuredData = (microControllerUuid: string) => {
  const alertStore = AlertStore();

  const fetchMeasuredData = async (microControllerUuid: string) => {
    await measuredDataStore.fetchMeasuredData(microControllerUuid).catch((e) => {
      const statusCode = e.response.status.toString();
      if (statusCode === StatusCode.INTERNAL_SERVER_ERROR) {
        notificationMessage.value = i18n.global.t('ApiError.internalServerError');
      } else {
        notificationMessage.value = i18n.global.t('ApiError.unexpectedError');
      }

      alertStore.addAlert({
        id: generateRandowmString(),
        type: 'alert',
        content: notificationMessage.value,
        timeInSec: 5,
      });
    });
  };

  // Store
  const measuredDataStore = MeasuredDataStore();
  fetchMeasuredData(microControllerUuid);
  const microControllerStore = MicroControllerStore();
  microControllerStore.fetchMicroControllerList();

  // 初期値
  const notificationMessage = ref('');

  const {
    sdi12ChartConfig,
    environmentalChartConfig,
    sdi12OptionList,
    environmentalOptionList,
    selectedSdi12Option,
    sdi12ChartDataSet,
    selectedEnvironmentalOption,
    environmentalChartDataSet,
  } = useChart();

  return {
    sdi12ChartConfig,
    environmentalChartConfig,
    sdi12OptionList,
    environmentalOptionList,
    selectedSdi12Option,
    sdi12ChartDataSet,
    selectedEnvironmentalOption,
    environmentalChartDataSet,
  };
};

export const useChart = () => {
  // 初期値
  const selectedSdi12Option = ref('');
  const selectedEnvironmentalOption = ref('');

  // Store
  const measuredDataStore = MeasuredDataStore();

  // Chart関連
  Chart.register(...registerables);

  // SDI-12グラフの設定
  const sdi12ChartConfig = computed<ChartOptions<'line'>>(() => ({
    responsive: true,
    scales: {
      x: {
        type: 'linear',
        display: true,
        position: 'bottom',
        title: {
          display: true,
          align: 'center',
          text: i18n.global.t('MeasuredData.dayOfYear'),
          font: {
            size: 14,
          },
        },
      },
      y: {
        type: 'linear',
        display: true,
        position: 'bottom',
        title: {
          display: true,
          align: 'center',
          text: convertSdi12KeyWordToScale(selectedSdi12Option.value),
          font: {
            size: 14,
          },
        },
      },
    },
    plugins: {
      legend: {
        display:
          measuredDataStore.getMeasuredDataList &&
          measuredDataStore.getMeasuredDataList.sdi12Data &&
          measuredDataStore.getMeasuredDataList.sdi12Data.length > 0 &&
          selectedSdi12Option.value !== '',
      },
      title: {
        text: convertSdi12KeyWordToTitle(selectedSdi12Option.value),
        display: true,
        font: {
          size: 16,
        },
      },
    },
  }));

  // 環境データグラフの設定
  const environmentalChartConfig = computed<ChartOptions<'line'>>(() => ({
    responsive: true,
    scales: {
      x: {
        type: 'linear',
        display: true,
        position: 'bottom',
        title: {
          display: true,
          align: 'center',
          text: i18n.global.t('MeasuredData.dayOfYear'),
          font: {
            size: 14,
          },
        },
      },
      y: {
        type: 'linear',
        display: true,
        position: 'bottom',
        title: {
          display: true,
          align: 'center',
          text: convertEnvironmentalKeyWordToScale(selectedEnvironmentalOption.value),
          font: {
            size: 14,
          },
        },
      },
    },
    plugins: {
      legend: {
        display:
          measuredDataStore.getMeasuredDataList &&
          measuredDataStore.getMeasuredDataList.environmentalData &&
          measuredDataStore.getMeasuredDataList.environmentalData.length > 0 &&
          selectedEnvironmentalOption.value !== '',
      },
      title: {
        text: convertEnvironmentalKeyWordToTitle(selectedEnvironmentalOption.value),
        display: true,
        font: {
          size: 16,
        },
      },
    },
  }));

  // SDI-12グラフ表示切替用選択肢
  const sdi12OptionList: SelectOptionType[] = [
    { word: i18n.global.t('MeasuredData.volumetricWaterContent'), key: 'vwc' },
    { word: i18n.global.t('MeasuredData.bulkRelativePermittivity'), key: 'brp' },
    { word: i18n.global.t('MeasuredData.soilTemperature'), key: 'soilTemp' },
    { word: i18n.global.t('MeasuredData.bulkElectricConductivity'), key: 'sbec' },
    { word: i18n.global.t('MeasuredData.soilPoreWaterElectricConductivity'), key: 'spwec' },
    { word: i18n.global.t('MeasuredData.gravitationalAccelerationX'), key: 'gax' },
    { word: i18n.global.t('MeasuredData.gravitationalAccelerationY'), key: 'gay' },
    { word: i18n.global.t('MeasuredData.gravitationalAccelerationZ'), key: 'gaz' },
  ];

  // 環境データグラフ表示切替用選択肢
  const environmentalOptionList: SelectOptionType[] = [
    { word: i18n.global.t('MeasuredData.airPressure'), key: 'airPress' },
    { word: i18n.global.t('MeasuredData.temperature'), key: 'temp' },
    { word: i18n.global.t('MeasuredData.relativeHumidity'), key: 'humi' },
    { word: i18n.global.t('MeasuredData.co2Concentration'), key: 'co2Concent' },
    { word: i18n.global.t('MeasuredData.totalVolatileOrganicCompounds'), key: 'tvoc' },
    { word: i18n.global.t('MeasuredData.analogValue'), key: 'analogValue' },
  ];

  // SDI-12グラフ用データ
  const sdi12ChartData = computed(() =>
    measuredDataStore.getSdi12DataList(selectedSdi12Option.value)
  );
  const sdi12ChartDataSet = computed<ChartData<'line'>>(() => ({
    datasets: sdi12ChartData.value,
  }));

  // 環境データグラフ用データ
  const environmentalChartData = computed(() =>
    measuredDataStore.getEnvironmentDataList(selectedEnvironmentalOption.value)
  );
  const environmentalChartDataSet = computed<ChartData<'line'>>(() => ({
    datasets: environmentalChartData.value,
  }));

  return {
    sdi12ChartConfig,
    environmentalChartConfig,
    sdi12OptionList,
    environmentalOptionList,
    selectedSdi12Option,
    sdi12ChartDataSet,
    selectedEnvironmentalOption,
    environmentalChartDataSet,
  };
};
