/*** EmptyView ***/

// define this module in Require.JS
define(function (require, exports, module) {

    // Import additional modules to be used in this view
    var Engine = require('famous/core/Engine');
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var Transform = require('famous/core/Transform');
    var StateModifier = require('famous/modifiers/StateModifier');
    var ImageSurface = require('famous/surfaces/ImageSurface');

    var Transitionable = require('famous/transitions/Transitionable');
    var SpringTransition = require('famous/transitions/SpringTransition');
    var Config = require("playlists/intro");
    var GridLayout = require("famous/views/GridLayout");

    Transitionable.registerMethod('spring', SpringTransition);
    var transitionable = new Transitionable(0);
    var mainContext = Engine.createContext();
    // Constructor function for our EmptyView class
    function ArticleEditView() {
        // Applies View's constructor function to EmptyView class
        View.apply(this, arguments);

        this.rootModifier = new StateModifier({
            align: [0.5, 0.5],
            origin: [0.5, 0.5],
            size: this.options.size
        });

        // saving a reference to the new node
        this.mainNode = this.add(this.rootModifier);

        _createLayout.call(this);
        _createContent.call(this);
    }

    function _createLayout() {

        this.layout = new GridLayout({
            dimensions: [2, 4]
        });
        layout = this.layout;

        this.mainNode.add(layout);
    }

    function _createContent() {
        this.layout.sequenceFrom([
            new Surface({
                content: "hello"
            })
        ]);
    }

    // Establishes prototype chain for EmptyView class to inherit from View
    ArticleEditView.prototype = Object.create(View.prototype);
    ArticleEditView.prototype.constructor = ArticleEditView;

    // Default options for EmptyView class
    ArticleEditView.DEFAULT_OPTIONS = {
        size: [Config.slideWidth, Config.slideHeight],
        article: undefined
    };



    // Define your helper functions and prototype methods here

    module.exports = ArticleEditView;
});
