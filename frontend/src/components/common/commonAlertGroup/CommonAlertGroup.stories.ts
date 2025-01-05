import type { Meta, StoryObj } from '@storybook/vue3';

import { AlertContent, AlertStore } from '@/store/alertStore';
import { generateRandomString } from '@/utils/stringUtil';
import CommonAlertGroup from './CommonAlertGroup.vue';

const meta = {
  title: 'Common/CommonAlertGroup',
  component: CommonAlertGroup,
  render: (args) => ({
    components: { CommonAlertGroup },
    setup() {
      const alertStore = AlertStore();
      alertStore.resetAlert();
      alertStore.addAlertAll([ALERT, WARNING, SUCCESS, INFO]);
      return { args };
    },
    template: `<CommonAlertGroup />`,
  }),
  args: {},
} satisfies Meta<typeof CommonAlertGroup>;

export default meta;
type Story = StoryObj<typeof meta>;
export const Default: Story = {};

const ALERT: AlertContent = {
  id: generateRandomString(),
  type: 'alert',
  content: 'Alert',
  timeInSec: 3,
};

const WARNING: AlertContent = {
  id: generateRandomString(),
  type: 'warning',
  content: 'Warning',
  timeInSec: 4,
};

const SUCCESS: AlertContent = {
  id: generateRandomString(),
  type: 'success',
  content: 'Success',
  timeInSec: 5,
};

const INFO: AlertContent = {
  id: generateRandomString(),
  type: 'info',
  content: 'Info',
  timeInSec: 6,
};
