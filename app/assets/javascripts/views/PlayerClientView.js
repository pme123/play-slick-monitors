/*** EmptyView ***/

// define this module in Require.JS
define(function (require, exports, module) {

    // Import additional modules to be used in this view
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var Transform = require('famous/core/Transform');
    var StateModifier = require('famous/modifiers/StateModifier');
    var GridLayout = require('famous/views/GridLayout');
    var cols = 1;
    var sequence = [];
    // Constructor function for our EmptyView class
    function PlayerClientView() {

        // Applies View's constructor function to EmptyView class
        View.apply(this, arguments);
        this.rootModifier = new StateModifier({
            align: [1.0, 1.0],
            origin: [1.0, 1.0],
            opacity: 0.8,
            size: [200]
        });


        // saving a reference to the new node
        this.mainNode = this.add(this.rootModifier);
        var layout = new GridLayout({
            dimensions: [cols, 10],
            gutterSize: [4, 4]
        });
        layout.sequenceFrom(sequence);
        this.mainNode.add(layout);

        sequence.push(new Surface({
            content: "hello"
        }));

        _createBackground.call(this);
        // _createPhoto.call(this);
    }

    // Establishes prototype chain for EmptyView class to inherit from View
    PlayerClientView.prototype = Object.create(View.prototype);
    PlayerClientView.prototype.constructor = PlayerClientView;

    // Default options for EmptyView class
    PlayerClientView.DEFAULT_OPTIONS = {};

    var background;

    function _createBackground() {
        background = new Surface({
            content: 'player',

            properties: {}
        });
        background.addClass('clientPanel');

        this.mainNode.add(background);

    }

    PlayerClientView.prototype.locationMap = {};
    PlayerClientView.prototype.addClient = function (json) {
        console.log("addClient: " + json.uuid);
        var clientMap = PlayerClientView.prototype.locationMap[json.location];
        if (clientMap == undefined) {
            clientMap = {};
            PlayerClientView.prototype.locationMap[json.location] = clientMap
        }
        clientMap[json.uuid] = json;
        console.log("addClient: " + PlayerClientView.prototype.locationMap[json.location][json.uuid].uuid);
    };

    PlayerClientView.prototype.showLocation = function (json) {
        var clientMap = PlayerClientView.prototype.locationMap[json.id];
        console.log("clicked--" + json.uuid + "::" + PlayerClientView.prototype.locationMap[json.id]);
        sequence.length = 0;
        var index = 0;
        for (clientUUID in clientMap) {
            var client = clientMap[clientUUID];
            sequence.push(new Surface({
                content: client.uuid
            }));
            console.log("location: " + json.uuid + " -client: " + clientUUID);
        }
        //  background.setContent(json.uuid)

    };
    // Define your helper functions and prototype methods here

    module.exports = PlayerClientView;
});
