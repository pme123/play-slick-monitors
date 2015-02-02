document.addEventListener('DOMContentLoaded', function () {
    webSocket();
});
var clients = [];

function webSocket() {
    if ("WebSocket" in window) {
        console.log("WebSocket is supported by your Browser!");
        // Let us open a web socket
        var ws = new WebSocket(document.getElementById("ws-url").value);
        ws.onopen = function () {
            console.log("WebSocket open ...");
        };
        ws.onmessage = function (evt) {
            console.log("Message is received...");

            var json = JSON.parse(evt.data);
            if (json["messageType"] == "client") {
                if (json["connectionEvent"] == "ADDED") {
                    addClient(json);
                } else {
                    removeClient(json)
                }
            }
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

function addClient(json) {
    var uuid = document.getElementById("clientUUID");
    clients.push(json.uuid);
    uuid.innerHTML = clientsHTML();

}
function removeClient(json) {
    var uuid = document.getElementById("clientUUID");
    var index = clients.indexOf("uuid");
    clients.splice(index);

    uuid.innerHTML = clientsHTML();

}

function clientsHTML() {
    var table = "<table><th>UUID</th>"
    for (i = 0; i < clients.length; i++) {
        table = table + "<tr><td>" + clients[i] + "</td></tr>"
    }
    return table + "</table>";
}

function activateArticle(name) {
    var ajaxCallBack = {
        success: onSuccess,
        error: onError
    }

    jsRoutes.controllers.Application.activateArticle(name).ajax(ajaxCallBack);
    var onSuccess = function (data) {
        alert(data);
    }

    var onError = function (error) {
        alert(error);
    }
};

