document.addEventListener('DOMContentLoaded', function () {
    webSocket();
});
function webSocket() {
    if ("WebSocket" in window) {
        console.log("WebSocket is supported by your Browser!");
        // Let us open a web socket
        var ws = new WebSocket(document.getElementById("echo-ws-url").value);
        ws.onopen = function () {
            console.log("WebSocket open ...");
            ws.send("hello from client")
            console.log("message sent ...");

        };
        ws.onmessage = function (evt) {
            console.log("Message is received..." + evt.data);
        };
        ws.onclose = function () {
            // websocket is closed.
            console.log("Connection is closed...");
        };
    }
    else {
        // The browser doesn't support WebSocket
        console.log("WebSocket NOT supported by your Browser!");
    }

}