import { Meta, StoryObj } from '@storybook/vue3';
import CommonButton from '../commonButton/CommonButton.vue';
import CommonFormWindow from './CommonFormWindow.vue';

const meta: Meta<typeof CommonFormWindow> = {
  title: 'Common/CommonFormWindow',
  component: CommonFormWindow,
  render: (args) => ({
    components: { CommonFormWindow, CommonButton },
    setup() {
      return { args };
    },
    template: `
      <CommonFormWindow v-bind="args">
        <template v-slot:title>
          <span>タイトルスロット</span>
        </template>
        <template v-slot:content>
          <CommonButton button-title="コンテンツスロット" />
        </template>
      </CommonFormWindow
    `,
  }),
  // Props
  argTypes: {
    windowStyle: { options: ['auto', 'vertical', 'horizontal'] },
  },
  args: {
    title: 'タイトル',
    content: 'コンテンツ',
    windowStyle: 'auto',
  },
  parameters: {},
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {},
};
