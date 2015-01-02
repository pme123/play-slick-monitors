/*** main.js ***/

define(function (require, exports, module) {
    var Engine = require('famous/core/Engine');

    var ServerView = require('views/ServerView');

    var Surface = require('famous/core/Surface');

    var mainContext = Engine.createContext();
    initApp();

    function initApp() {
        // parses out reponse data and retrieves array of urls
        addContent();
    }



    function setArticle(articles) {
        console.log("articlesllll\n: " + articles.value);

    }

    function addContent() {
        // instantiates ServerView with our url data
        this.serverView = new ServerView({});
        mainContext.add(this.serverView);

    }

    webSocket();
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
                if (json["messageType"] == "article") {
                    ServerView.prototype.showArticle(json);
                }
                else {
                    ServerView.prototype.showClient(json);
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
});



