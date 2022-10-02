<template>
    <nav class="navigator-side" id="navigator" v-bind:class="{ active: menuState }">
        <div class="nav-top">
            <img class="logo-menu" src="@/assets/logo_white.png" alt="logo">
        </div>
        <ul class="navigator">
            <form method="post">
                <li class="navigator-list">
                    <router-link to=""></router-link>
                    <button>
                        <Information style="margin-top: auto; margin-bottom: auto;"/>
                        <div>登録機器</div>
                    </button>
                </li>
                <li class="navigator-list">
                    <button>
                        <DataBase style="margin-top: auto; margin-bottom: auto;"/>
                        <div>データ</div>
                    </button>
                </li>
                <li class="navigator-list">
                    <button>
                        <WiFi style="margin-top: auto; margin-bottom: auto;"/>
                        <div>Wi-Fi</div>
                    </button>
                </li>
                <li class="navigator-list">
                    <button>
                        <Timer style="margin-top: auto; margin-bottom: auto;"/>
                        <div>測定間隔</div>
                    </button>
                </li>
                <li class="navigator-list">
                    <button>
                        <TuneIcon style="margin-top: auto; margin-bottom: auto;"/>
                        <div>SDI-12<br>アドレス変更</div>
                    </button>
                </li>
                <li class="navigator-list">
                    <button>
                        <Update style="margin-top: auto; margin-bottom: auto;"/>
                        <div>アップデート</div>
                    </button>
                </li>
                <li class="navigator-list">
                    <button>
                        <AccountSettings style="margin-top: auto; margin-bottom: auto;"/>
                        <div>設定</div>
                    </button>
                </li>
            </form>
        </ul>
    </nav>
</template>

<script lang="ts">
import TuneIcon from 'vue-material-design-icons/Tune.vue'
import Update from 'vue-material-design-icons/Update.vue'
import Information from 'vue-material-design-icons/InformationOutline.vue'
import DataBase from 'vue-material-design-icons/Database.vue'
import WiFi from 'vue-material-design-icons/Wifi.vue'
import Timer from 'vue-material-design-icons/TimerOutline.vue'
import AccountSettings from 'vue-material-design-icons/AccountSettings.vue'

export default {
    components: {
        TuneIcon,
        Update,
        Information,
        DataBase,
        WiFi,
        Timer,
        AccountSettings,
    },
    props: {
        menuState: {
            type: Boolean,
            default: false,
        }
    },
}
    
</script>

<style scoped>
nav {
    padding: 0;/*navのデフォルト値(30px)をリセット*/
}
.navigator-side {
    position: absolute;
    left: 0;
    top: 0;
    width: 50%;
    max-width: 250px;
    height: 100vh;
    transform: translateX(-100%);
    background-color: #eeeeee;
    transition: ease 0.5s;
    opacity: 98%;
    overflow: hidden;
    border-right: #444444 1px solid;
}
.navigator-side.active {
    transform: translateX(0);  
}
.navigator {
    position: absolute;
    top: 16%;
    right :0;
    width: 100%;
    padding: 0;
}
.navigator-list {
    border-top: #333333 1px solid;
    border-bottom: #333333 1px solid;
    height: 7vh;
    margin: 10px;
    display: flex;
    flex-direction: column;
    cursor: pointer;
}
.navigator-list:hover {
    background: #dddddd;
}
.navigator-list button {
    height: 100%;
	background: none;
	border: none;
	font-size: 2vh;
	color:#111111;
	cursor: pointer;
    display: flex;
    justify-content: center;
}
.navigator-list div {
    margin: auto 0 auto 10px;
}
.navigator-list span {
    height: 24px; /*アイコンの縦中央寄せに必要*/
}
.nav-top {
    height: 16vh;
    overflow: hidden;
    border-bottom-left-radius: 1000px 300px;
    border-bottom-right-radius: 1000px 300px;
    margin-right: -50px;
    margin-left: -50px;
    padding-right: 50px;
    padding-left: 50px;
    background: #444444;
}
.logo-menu {
    width: 10vh;
    height: auto;
    margin-top: 4vh;
    margin-left: auto;
    margin-right: auto;
}
</style>