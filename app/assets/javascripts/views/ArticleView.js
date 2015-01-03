/*** ArticleView ***/

// define this module in Require.JS
define(function (require, exports, module) {

    // Import additional modules to be used in this view
    var Engine = require('famous/core/Engine');
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var ImageSurface = require('famous/surfaces/ImageSurface');
    var ContainerSurface = require('famous/surfaces/ContainerSurface');
    var Transform = require('famous/core/Transform');
    var StateModifier = require('famous/modifiers/StateModifier');
    var Flipper = require('famous/views/Flipper');
    var ListLayout = require('famous-flex/layouts/ListLayout');

    // Constructor function for our FlipperView class
    function ArticleView(article) {

        // Applies View's constructor function to FlipperView class
        View.apply(this, arguments);
        this.rootModifier = new StateModifier({
            align: [0.5, 0.5],
            origin: [0.5, 0.5]
        });

        // saving a reference to the new node
        this.mainNode = this.add(this.rootModifier);
        _create.call(this, article);
    }


    function _create(article) {
        var toggle = false;
        var flipper = new Flipper({});
        var front = new ImageSurface({
            content: article.img,
            properties: {
                backgroundColor: 'gray'
            }
        });
        flipper.setFront(front);
        front.on('mouseover', _doToggle.bind(this));
        front.on('click', _showEdit.bind(this));
        var back = new Surface({
            content: article.name,
            properties: {
                backgroundColor: 'gray'
            }
        });
        flipper.setBack(back);
        back.on('mouseover', _doToggle.bind(this));
        back.on('click', _showEdit.bind(this));
        this.mainNode.add(flipper);



        function _doToggle() {
            var angle = toggle ? 0 : Math.PI;
            flipper.setAngle(angle, {
                curve: 'easeOutBounce',
                duration: 800
            });
            toggle = !toggle;
        };
        function _showEdit() {
            console.log("clicked--");
            this._eventOutput.emit('hello');

        };

    }


    // Establishes prototype chain for FlipperView class to inherit from View
    ArticleView.prototype = Object.create(View.prototype);
    ArticleView.prototype.constructor = ArticleView;


    // Default options for FlipperView class
    ArticleView.DEFAULT_OPTIONS = {};

    // Define your helper functions and prototype methods here

    module.exports = ArticleView;
});
