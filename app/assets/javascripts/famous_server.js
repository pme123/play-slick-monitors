/*** main.js ***/

define(function (require, exports, module) {
    var Engine = require('famous/core/Engine');

    var ServerView = require('views/ServerView');

    var Surface = require('famous/core/Surface');

    var mainContext = Engine.createContext();
    initApp();

    function initApp() {
        // parses out reponse data and retrieves array of urls
        _addContent.call(this);
        _addWebSocket.call(this);
    }

    function _addContent() {
        // instantiates ServerView with our url data
        this.serverView = new ServerView({});
        mainContext.add(this.serverView);
        console.log("_addContent: " + this.serverView);

    }


    function _addWebSocket() {
        console.log("_addWebSocket: " + this.serverView);
        if ("WebSocket" in window) {
            console.log("WebSocket is supported by your Browser!");
            // Let us open a web socket
            this.ws = new WebSocket(document.getElementById("ws-url").value);
            this.ws.onopen = function () {
                console.log("WebSocket open ...");
            };
            this.ws.onmessage = function (evt) {
                console.log("Message is received..." + this.serverView);

                var json = JSON.parse(evt.data);
                if (json["messageType"] == "article") {
                    ServerView.prototype.showArticle(json);
                }
                else {
                    ServerView.prototype.showClient(json);
                }
            };
            this.ws.onclose = function () {
                // websocket is closed.
                console.log("Connection is closed...");
            };
        }
        else {
            // The browser doesn't support WebSocket
            console.log("WebSocket NOT supported by your Browser!");
        }

    }
});



