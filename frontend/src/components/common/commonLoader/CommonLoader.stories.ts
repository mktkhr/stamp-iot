import { Meta, StoryObj } from '@storybook/vue3';
import CommonLoader from './CommonLoader.vue';
import { LOADER_TYPE } from './composable';

const meta: Meta<typeof CommonLoader> = {
  title: 'Common/CommonLoader',
  component: CommonLoader,
  render: (args) => ({
    components: {
      CommonLoader,
    },
    setup() {
      return { args };
    },
    template: `<CommonLoader v-bind="args" />`,
  }),
  argTypes: {
    pattern: {
      options: Object.keys(LOADER_TYPE),
      mapping: LOADER_TYPE,
    },
  },
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: { pattern: LOADER_TYPE.FLOWER },
};

export const ThreeDots: Story = {
  args: { pattern: LOADER_TYPE.THREE_DOTS },
};

export const Circle: Story = {
  args: { pattern: LOADER_TYPE.CIRCLE },
};
