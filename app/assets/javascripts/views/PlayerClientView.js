/*** EmptyView ***/

// define this module in Require.JS
define(function (require, exports, module) {

    // Import additional modules to be used in this view
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var Transform = require('famous/core/Transform');
    var StateModifier = require('famous/modifiers/StateModifier');

    // Constructor function for our EmptyView class
    function PlayerClientView() {

        // Applies View's constructor function to EmptyView class
        View.apply(this, arguments);
        this.rootModifier = new StateModifier({
            align: [1.0, 1.0],
            origin: [1.0, 1.0],
            opacity: 0.5,
            size: [200,]
        });

        // saving a reference to the new node
        this.mainNode = this.add(this.rootModifier);

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

    var locationMap = {};
    PlayerClientView.prototype.addClient = function (json) {
        console.log("addClient: " + json);
        var clientMap = locationMap[json.location];
        if (clientMap == undefined) {
            clientMap = {};
            clientMap[json.uuid] = json;
            locationMap[json.location] = clientMap
        }
        background.setContent(json)
    };

    // Define your helper functions and prototype methods here

    module.exports = PlayerClientView;
});
