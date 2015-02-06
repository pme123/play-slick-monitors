document.addEventListener('DOMContentLoaded', function () {
    webSocket();
});


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
                updateClient(json)
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

var clients = {};

function updateClient(json) {
    var uuid = document.getElementById("clientUUID");
    clients[json.uuid] = json
    console.log("Clients: " + clients)
    uuid.innerHTML = clientsHTML();
}

function clientsHTML() {
    var table = "<table><th>UUID</th><th>Playlist</th><th>Order</th><th>Active</th>"
    for (var key in clients) {
        table = table + "<tr><td>" + clients[key].uuid + "</td>" +
        "<td>" + clients[key].playlist + "</td>" +
        "<td>" + clients[key].order + "</td>" +
        "<td>" + clients[key].connectionEvent + "</td></tr>"
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

