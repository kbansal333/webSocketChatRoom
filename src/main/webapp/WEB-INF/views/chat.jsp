<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <title>Chat Room</title>
    <meta charset="utf-8" name="viewport" content="width=device-width">
    <link rel="stylesheet" href="/webjars/mdui/dist/css/mdui.css">
    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/mdui/dist/js/mdui.js"></script>
</head>
<body class="mdui-theme-primary-indigo mdui-theme-accent-pink">

<div style="margin-left:20px;margin-right:20px">
    <div class="mdui-toolbar mdui-color-theme">
        <span class="mdui-typo-title">Chat Room</span>
        <div class="mdui-toolbar-spacer"></div>
        <a class="mdui-btn mdui-btn-icon" href="/">
            <i class="mdui-icon material-icons">exit_to_app</i>
        </a>
    </div>
</div>

<div style="margin-left:20px;margin-right:20px">
    <div class="container_text">
        <div class="mdui-row">
            <div class="mdui-col-xs-12 mdui-col-sm-6">
                <div class="mdui-col-xs-12 mdui-col-sm-10">
                    <div class="mdui-textfield-floating-label" style="margin-top:15px">
                        <i class="mdui-icon">Welcome: </i>
                        <i class="mdui-icon" id="username">${username}</i>
                    </div>
                </div>
                <div class="mdui-col-xs-12 mdui-col-sm-10">
                    <div class="mdui-textfield mdui-textfield-floating-label">
                        <i class="mdui-icon material-icons">textsms</i>
                        <label class="mdui-textfield-label">Send Text</label>
                        <input class="mdui-textfield-input" id="msg"/>
                    </div>
                    <div class="mdui-container" style="padding:20px 35px">
                        <!-- TODO: add a send button to send message -->
                        <button class="mdui-btn mdui-color-theme mdui-ripple"
                                onclick="clearMsg()">Clear
                        </button>
                    </div>
                </div>
            </div>

            <div class="mdui-col-xs-6 mdui-col-sm-5" style="padding:10px 0">
                <div class="mdui-chip">
                    <span class="mdui-chip-icon mdui-color-blue">
                        <i class="mdui-icon material-icons">&#xe420;</i></span>
                    <span class="mdui-chip-title">Content</span>
                </div>

                <div class="mdui-chip">
                    <span class="mdui-chip-icon mdui-color-blue">
                        <i class="mdui-icon material-icons">face</i></span>
                    <span class="mdui-chip-title">Online Users : <span class="chat-num">0</span></span>
                    <!-- TODO: check online number num -->
                </div>
                <!-- TODO: add a message container -->
                <div class="message-container"></div>
            </div>

        </div>
    </div>
</div>

<script>
    var username = '<c:out value="${username}"/>';
    /**
     * WebSocket Client
     *
     * 1、WebSocket client receive messages with callback. example：webSocket.onmessage
     * 2、WebSocket client send message to server. example：webSocket.send();
     */
    function getWebSocket() {
        /**
         * WebSocket client PS：URL shows WebSocket protocal, port number, and then end point.
         */
        var webSocket = new WebSocket('ws://localhost:8080/chat/'+username);
        /**
         * websocket open connection.
         */
        webSocket.onopen = function (event) {
            console.log('WebSocket open connection');
        };

        /**
         * Server send 1) broadcast message, 2) online users.
         */
        webSocket.onmessage = function (event) {
            console.log('WebSocket Receives: ' + event.data, 'color:green');
            //Receive Message from Server
            var message = JSON.parse(event.data) || {};
            var $messageContainer = $('.message-container');
            if(message.type == 1) {
                $messageContainer.append(
                    '<div class="mdui-card" style="margin: 10px 0;">' +
                    '<div class="mdui-card-primary">' +
                    '<div class="mdui-card-content message-content">' + message.sender + ": " + message.content + '</div>' +
                    '</div></div>');
            }
            $('.chat-num').text(message.onlineCount);
            var $cards = $messageContainer.children('.mdui-card:visible').toArray();
            if ($cards.length > 5) {
                $cards.forEach(function (item, index) {
                    index < $cards.length - 5 && $(item).slideUp('fast');
                });
            }
        };

        /**
         * Close connection
         */
        webSocket.onclose = function (event) {
            console.log('WebSocket close connection.');
        };

        /**
         * Exception
         */
        webSocket.onerror = function (event) {
            console.log('WebSocket exception.');
        };
        return webSocket;
    }

    var webSocket = getWebSocket();


    /**
     * Send messages to server use webSocket.
     */
    function sendMsgToServer() {
        var $message = $('#msg');
        if ($message.val()) {
            webSocket.send(JSON.stringify({username: $('#username').text(), content: $message.val()}));
            $message.val(null);
        }
    }

    /**
     * Clear screen
     */
    function clearMsg() {
        $(".message-container").empty();
    }

    /**
     * Enter to send message.
     */
    document.onkeydown = function (event) {
        var e = event || window.event || arguments.callee.caller.arguments[0];
        e.keyCode === 13 && sendMsgToServer();
    };

</script>

</body>
</html>
