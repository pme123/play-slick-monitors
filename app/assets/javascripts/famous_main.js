/*** main.js ***/

define(function (require, exports, module) {
    var Engine = require('famous/core/Engine');

    var AppView = require('views/AppView');

    var Config = require('playlists/article');

    var Surface = require('famous/core/Surface');

    var mainContext = Engine.createContext();
    mainContext.setPerspective(Config.perspective);
    initApp();

    function initApp() {
        // parses out reponse data and retrieves array of urls
        initConfig();

    }

    function initConfig() {
        var playlist = document.getElementById("playlist");
        var url = 'playlists/' + playlist.value;
    //    if (require.defined(url)) {
     //       Config = require(url);
    //    }
        require([url], function (config) {
            Config = config;
            addContent();
        });
    }

    function addContent() {
        // instantiates AppView with our url data
        this.appView = new AppView({
            config: Config
        });
        mainContext.add(this.appView);

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
                    AppView.prototype.showArticle(json);
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



