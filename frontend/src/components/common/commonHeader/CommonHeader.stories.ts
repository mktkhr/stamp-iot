import { SidebarStore } from '@/store/sidebarStore';
import { Meta, StoryObj } from '@storybook/vue3';
import { vueRouter } from 'storybook-vue3-router';
import CommonButton from '../commonButton/CommonButton.vue';
import CommonCheckBox from '../commonCheckBox/CommonCheckBox.vue';
import CommonMenu from '../commonMenu/CommonMenu.vue';
import CommonHeader from './CommonHeader.vue';
import AccountDeleteDialog from './components/accountDeleteDialog/AccountDeleteDialog.vue';
import AccountMenu from './components/accountMenu/AccountMenu.vue';

const meta: Meta<typeof CommonHeader> = {
  title: 'Common/CommonHeader',
  component: CommonHeader,
  render: (args) => ({
    components: {
      CommonHeader,
      CommonMenu,
      AccountMenu,
      AccountDeleteDialog,
      CommonButton,
      CommonCheckBox,
    },
    setup() {
      const sidebarStore = SidebarStore();
      sidebarStore.$reset();
      return { args };
    },
    template: `<CommonHeader v-bind="args" />`,
  }),
  tags: ['autodocs'],
  decorators: [vueRouter()],
};

export default meta;
type Story = StoryObj<typeof meta>;

// TODO: ログインしてない画面の表示とログイン後の画面のStoryに分岐させる
export const Default: Story = {};
