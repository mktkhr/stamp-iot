import type { Meta, StoryObj } from '@storybook/vue3';

import CommonButton from '@/components/common/commonButton/CommonButton.vue';
import DefaultFrame from './DefaultFrame.vue';

const meta = {
  title: 'Frame/DefaultFrame',
  component: DefaultFrame,
  render: (args) => ({
    components: { DefaultFrame, CommonButton },
    setup() {
      return { args };
    },
    template: `
    <DefaultFrame v-bind="args">
      <template v-slot:actionBar>
        <div style="height: 100%; width:100%; background-color: #00000020">
          <CommonButton button-title="アクションバースロット" />
        </div>
      </template>
      <template v-slot:content>
        <div style="height: 100%; width:100%; background-color: #00000080">
          <CommonButton button-title="コンテンツスロット" />
        </div>
      </template>
    </DefaultFrame>
    `,
  }),
  argTypes: {
    showActionBar: { control: 'boolean' },
    showBackButton: { control: 'boolean' },
  },
  tags: ['autodocs'],
  decorators: [],
} satisfies Meta<typeof DefaultFrame>;

export default meta;
type Story = StoryObj<typeof meta>;

export const HideActionBar: Story = {
  args: {
    showActionBar: false,
    showBackButton: false,
  },
};

export const ShowActionBar: Story = {
  args: {
    showActionBar: true,
    showBackButton: false,
  },
};
