import { Meta, StoryObj } from '@storybook/vue3';
import CommonCheckBox from './CommonCheckBox.vue';

const meta: Meta<typeof CommonCheckBox> = {
  title: 'Common/CommonCheckBox',
  component: CommonCheckBox,
  render: (args) => ({
    components: { CommonCheckBox },
    setup() {
      return { args };
    },
    template: `<CommonCheckBox v-bind="args" />`,
  }),
  // Props
  argTypes: {
    title: { control: 'text' },
  },
  args: {},
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    title: 'デフォルト',
  },
};
