import type { Meta, StoryObj } from '@storybook/vue3';

import { AlertContent } from '@/store/alertStore';
import CommonAlert from './CommonAlert.vue';

const meta = {
  title: 'Common/CommonAlert',
  component: CommonAlert,
  render: (args) => ({
    components: { CommonAlert },
    setup() {
      return { args };
    },
    template: `<CommonAlert v-bind="args" />`,
  }),
  args: {},
  tags: ['autodocs'],
} satisfies Meta<typeof CommonAlert>;

export default meta;
type Story = StoryObj<typeof meta>;

const alert: AlertContent = {
  id: '1',
  type: 'alert',
  content: 'Alertです。',
  timeInSec: null,
};
const warning: AlertContent = {
  id: '1',
  type: 'warning',
  content: 'Warningです。',
  timeInSec: null,
};
const success: AlertContent = {
  id: '1',
  type: 'success',
  content: 'Successです。',
  timeInSec: null,
};
const info: AlertContent = {
  id: '1',
  type: 'info',
  content: 'Infoです。',
  timeInSec: null,
};

export const Alert: Story = {
  args: {
    alert: alert,
  },
};

export const Warning: Story = {
  args: {
    alert: warning,
  },
};

export const Success: Story = {
  args: {
    alert: success,
  },
};

export const Info: Story = {
  args: {
    alert: info,
  },
};
