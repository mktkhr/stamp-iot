import { Meta, StoryObj } from '@storybook/vue3';
import { onMounted, ref } from 'vue';
import CommonButton from '../commonButton/CommonButton.vue';
import CommonDialog from '../commonDialog/CommonDialog.vue';
import CommonMenu from './CommonMenu.vue';

const meta: Meta<typeof CommonMenu> = {
  title: 'Common/CommonMenu',
  component: CommonMenu,
  render: (args) => ({
    components: {
      CommonMenu,
      CommonButton,
      CommonDialog,
    },
    setup() {
      // レンダリングを待つ必要があるので，Storybook用にフラグを持たせる
      const isMounted = ref(false);
      onMounted(() => {
        isMounted.value = true;
      });
      return { args, isMounted };
    },
    template: `
      <CommonMenu v-if="isMounted" v-bind="args">
        <CommonDialog
          title="コンテンツタイトル"
          description="コンテンツ内容"
        >
          <CommonButton button-title="コンテンツスロット" />
        </CommonDialog>
        <template v-slot:activator>
          <CommonButton button-title="アクティベータースロット" />
        </template>
      </CommonMenu>
    `,
  }),
  decorators: [],
  argTypes: {
    teleportDestination: { control: 'text' },
    persistent: { control: 'boolean' },
  },
  tags: ['autodocs'],
};

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: { teleportDestination: '#app', persistent: false },
};

export const Persistent: Story = {
  args: { teleportDestination: '#app', persistent: true },
};
