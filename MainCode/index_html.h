String generateHtml(const char *macAddress, const char *ssid, const char *version, const char *bootMode);

// 雛形HTML生成
// https://cpprefjp.github.io/lang/cpp11/raw_string_literals.html#:~:text=matched-,%E9%9B%9B%E5%BD%A2%E3%81%AEHTML%E3%82%92%E5%87%A6%E7%90%86%E3%81%99%E3%82%8B,-%23include%20%3Cfstream%3E
const char INDEX_HTML[] PROGMEM = R"=====(
    <!doctype html>
    <html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta charset="UTF-8">
        <title>EMS engineering</title>
    </head>
    <style type="text/css">
    body{
        height: 100vh;
        height: 100svh;
        width: 100vw;
        font-family: YuGothic,'Yu Gothic',YuGothic,'Yu Gothic',sans-serif;
    }
    body{
        margin: 0px;
    }
    div{
        box-sizing: border-box;
    }
    .header{
        height: 50px;
        width: 100%;
        background-color: #aaa;
        display: flex;
        align-items: center;
        padding: 0 8px;
        position: relative;
        box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.6);
        z-index: 100;
    }
    .logo{
        height: 35px;
        width: auto;
    }
    .reboot{
        position: absolute;
        right: 8px;
    }
    .hero{
        height: 80px;
        width: 100%;
        background-color: gray;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }
    .hero-span{
        font-size: 16px;
        font-weight: bold;
        letter-spacing: 1px;
        color: #eee;
    }
    .content{
        height: calc(100% - 80px - 50px);
        width: 100%;
        padding: 10px;
        background-color: #eee;
        display: flex;
        gap: 10px;
    }
    button{
        font-size: 12px;
        font-weight: bold;
        height: 30px;
        background-color: white;
        color: #666;
        border: 2px solid #888888;
        border-radius: 10px;
        box-shadow: 2px 2px 3px 1px #666;
    }
    button:active{
        color: white;
        background-color: #888;
    }
    span{
        color: #666;
    }
    .wrapper{
        height: 100%;
        width: 120px;
    }
    .title{
        height: 40px;
        width: 100%;
        display: flex;
        align-items: center;
        background-color: #888;
        justify-content: center;
        border-radius: 15px 15px 0 0;
    }
    .item{
        height: calc(100% - 40px);
        background-color: white;
        border-radius: 0 0 15px 15px;
        border: 2px solid #888;
        overflow-y: auto;
        padding: 8px;
        text-align: center;
    }
    .item-wrapper{
        display: grid;
        grid-template-rows: repeat(6, 40px);
        gap: 10px;
    }
    .item-button{
        height: 100%;
    }
    .title-span{
        font-size: 20px;
        font-weight: bold;
        color: white;
    }
    .status-wrapper{
        width: calc(100% - 120px);
    }
    #info #wifi #sdi #version{
        height: 100%;
        width: 100%;
        display: block;
    }
    .info-wrapper{
        height: 50px;
        display: flex;
        flex-direction: column;
        position: relative;
        max-width: 200px;
        margin: 0 auto;
    }
    .info-title{
        font-size: 12px;
        font-weight: bold;
        height: 20px;
        padding-left: 10px;
        text-align: left;
    }
    .info-span{
        height: 30px;
        width: 100%;
        text-align: center;
        overflow-x: auto;
    }
    .from-wrapper{
        display: flex;
        flex-direction: column;
        position: relative;
        max-width: 200px;
        margin: 0 auto;
    }
    .input{
        width: 80%;
        height: 25px;
        margin: 0 auto 10px;
        font-size: 16px;
        font-weight: bold;
    }
    </style>

    <body>
        <div class="header">
            <img class="logo"
                src="data:image/png;base64,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"
                alt="EMS Logo" onclick="onClickLogo()">
            <form class="reboot" method="post">
                <button name="reboot" value="reboot">再起動</button>
            </form>
        </div>

        <div class="hero">
            <span class="hero-span">ユニット設定ページへようこそ</span>
            <span class="hero-span">設定したい項目を選択して下さい</span>
        </div>
        <div class="content">
            <div class="wrapper">
                <div class="title">
                    <span class="title-span">項目</span>
                </div>
                <div class="item">
                    <div class="item-wrapper">
                        <button class="item-button" onclick="onClickButton('info')">機器情報</button>
                        <button class="item-button" onclick="onClickButton('wifi')">Wi-Fi</button>
                        <button class="item-button" onclick="onClickButton('sdi')">アドレス変更</button>
                        <button class="item-button" id="bootButton" onclick="onClickButton('boot')">起動モード</button>
                    </div>
                </div>
            </div>
            <div class="wrapper status-wrapper">
                <div class="title">
                    <span class="title-span">ステータス</span>
                </div>
                <div class="item">
                    <div id="info">
                        <div class="info-wrapper">
                            <span class="info-title">MACアドレス</span>
                            <span class="info-span">$macAddress$</span>
                        </div>
                        <div class="info-wrapper">
                            <span class="info-title">Wi-Fi</span>
                            <span class="info-span">$ssid$</span>
                        </div>
                        <div class="info-wrapper">
                            <span class="info-title">システムバージョン</span>
                            <span class="info-span">$version$</span>
                        </div>
                    </div>
                    <div id="wifi">
                        <div class="info-wrapper">
                            <span class="info-title">現在の設定値</span>
                            <span class="info-span">$ssid$</span>
                        </div>
                        <hr>
                        <form class="from-wrapper" action="post">
                            <span class="info-title">SSID</span>
                            <input class="input" name="ssid" type="text" list="ssid" placeholder="入力もしくは選択" autocomplete="on" required>
                            <datalist id="ssid" size="1">
                                <option value="例">例</option>
                            </datalist>
                            <span class="info-title">パスワード</span>
                            <input class="input" name="pass" type="text" placeholder="入力" autocomplete="on" required>
                            <button name="wifi" value="wifi">送信</button>
                        </form>
                    </div>
                    <div id="sdi">
                        <div class="info-wrapper">
                            <form class="from-wrapper" action="post">
                                <span class="info-title">変更元アドレス</span>
                                <input class="input" name="present_address" type="text" placeholder="例:1" pattern="^[0-9A-Za-z]$" maxlength="1" required>
                                <span class="info-title">変更先アドレス</span>
                                <input class="input" name="following_address" type="text" placeholder="例:3" pattern="^[0-9A-Za-z]$" maxlength="1" required>
                                <button name="address" value="address">送信</button>
                            </form>
                        </div>
                    </div>
                    <div id="boot">
                        <div class="info-wrapper">
                            <span class="info-title">現在の起動モード</span>
                            <span class="info-span">$bootMode$</span>
                        </div>
                        <hr>
                        <form class="from-wrapper" action="post">
                            <span class="info-title">ホスト</span>
                            <input class="input" name="host" type="text" autocomplete="on" required>
                            <button name="maintenance" value="メンテナンスモード">メンテナンスモード</button>
                        </form>
                        <hr>
                        <form class="from-wrapper" action="post">
                            <button name="normal" value="通常モード">通常モード</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <script>
            var info = document.getElementById("info");
            var wifi = document.getElementById("wifi");
            var sdi = document.getElementById("sdi");
            var boot = document.getElementById("boot");
            var bootButton = document.getElementById("bootButton");
            bootButton.style.display = "none"
            var count = 0;
            function onClickButton(value){
                info.style.display = "none";
                wifi.style.display = "none";
                sdi.style.display = "none";
                boot.style.display = "none";
                switch (value) {
                    case "info":
                        info.style.display = "block";
                        break;
                    case "wifi":
                        wifi.style.display = "block";
                        break;
                    case "sdi":
                        sdi.style.display = "block";
                        break;
                    case "boot":
                        boot.style.display = "block";
                        break;
                    default:
                        info.style.display = "block";
                        break;
                }
            }
            onClickButton();
            function onClickLogo(){
                count++;
                if(count >= 10){
                    bootButton.style.display = "block";
                }
            }
        </script>
    </body>
    </html>
)=====";