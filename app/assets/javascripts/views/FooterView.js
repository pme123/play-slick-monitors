/*** EmptyView ***/

// define this module in Require.JS
define(function (require, exports, module) {

    // Import additional modules to be used in this view
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var Transform = require('famous/core/Transform');
    var StateModifier = require('famous/modifiers/StateModifier');

    // Constructor function for our EmptyView class
    function FooterView() {

        // Applies View's constructor function to EmptyView class
        View.apply(this, arguments);
        this.rootModifier = new StateModifier({
            align: [0.5, 0.0],
            origin: [0.5, 0.0]
        });

        // saving a reference to the new node
        this.mainNode = this.add(this.rootModifier);

        _createBackground.call(this);
        // _createPhoto.call(this);
    }

    // Establishes prototype chain for EmptyView class to inherit from View
    FooterView.prototype = Object.create(View.prototype);
    FooterView.prototype.constructor = FooterView;

    // Default options for EmptyView class
    FooterView.DEFAULT_OPTIONS = {};

    function _createBackground() {
        var background = new Surface({
            content: '</br>screenFOODnet:  Digital Signage Retail Services AG ::  Hirschengraben 43, 6003 Luzern :: Fon +41 (0)41 444 21 41 :: Fax +41 (0)41 444 21 43 :: Mail: info@screenfoodnet.com',

            properties: {}
        });
        background.addClass('footer');

        this.mainNode.add(background);

    }

    // Define your helper functions and prototype methods here

    module.exports = FooterView;
});
