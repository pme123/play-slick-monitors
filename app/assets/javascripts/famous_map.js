/*
 * Copyright (c) 2014 Gloey Apps
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * @author: Hein Rutjes (IjzerenHein)
 * @license MIT
 * @copyright Gloey Apps, 2014
 */

/*jslint browser:true, nomen:true, vars:true, plusplus:true*/
/*global define, google, L*/

define(function (require) {
    var Engine = require('famous/core/Engine');
    var MonitorMapView = require('views/MonitorMapView');
    var mainContext = Engine.createContext();
    initApp();

    function initApp() {
        // parses out reponse data and retrieves array of urls
        _addContent.call(this);
        _addWebSocket.call(this);
    }

    function _addContent() {
        // instantiates ServerView with our url data
        this.monitorMapView = new MonitorMapView({});
        mainContext.add(this.monitorMapView);
        console.log("_addContent: " + this.monitorMapView);

    }

    function _addWebSocket() {
        console.log("_addWebSocket: " + this.monitorMapView);
        if ("WebSocket" in window) {
            console.log("WebSocket is supported by your Browser!");
            // Let us open a web socket
            this.ws = new WebSocket(document.getElementById("ws-url").value);
            this.ws.onopen = function () {
                console.log("WebSocket open ...");
            };
            this.ws.onmessage = function (evt) {

                var json = JSON.parse(evt.data);
                console.log("Message is received..." + json["messageType"]);
                if (json["messageType"] == "location") {
                    MonitorMapView.prototype.showLocation(json);
                } else if (json["messageType"] == "client") {
                    MonitorMapView.prototype.addClient(json);
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
