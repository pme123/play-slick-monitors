/*** EmptyView ***/

// define this module in Require.JS
define(function (require, exports, module) {

    // Import additional modules to be used in this view
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var Transform = require('famous/core/Transform');
    var StateModifier = require('famous/modifiers/StateModifier');
    var ImageSurface = require('famous/surfaces/ImageSurface');

    var Modifier = require('famous/core/Modifier');
    var Transform = require('famous/core/Transform');
    var Transitionable = require('famous/transitions/Transitionable');
    var Easing = require('famous/transitions/Easing');
    var NavigationBar = require('famous/widgets/NavigationBar');

    var transitionable = new Transitionable(0);

    // Constructor function for our EmptyView class
    function HeaderView() {

        // Applies View's constructor function to EmptyView class
        View.apply(this, arguments);
        this.rootModifier = new Modifier({
            align: [0.5, 0.5],
            origin: [0.5, 0.5]
        });

        // saving a reference to the new node
        this.mainNode = this.add(this.rootModifier);

        _createBackground.call(this);
        _createTitle.call(this);
        _createPhoto.call(this);

        transitionable.set(1, {
            duration: 5000, curve: Easing.outBack
        });
    }

    // Establishes prototype chain for EmptyView class to inherit from View
    HeaderView.prototype = Object.create(View.prototype);
    HeaderView.prototype.constructor = HeaderView;

    // Default options for EmptyView class
    HeaderView.DEFAULT_OPTIONS = DEFAULT_OPTIONS = {
        logoSize: [230, 56]
    };

    function _createBackground() {
        var background = new Surface({
            // undefined size will inherit size from parent modifier
            properties: {
                backgroundColor: '#FFF'
            }
        });

        this.mainNode.add(background);

    }


    function _createTitle() {

        var title = new Surface({
            content: 'famo.us Intro App'
        });

        title.addClass('title')

        this.titleModifier = new Modifier({
            size: [window.innerWidth, undefined],
            origin: [1, 0.5],
            align: [1, 0.5],
            transform: function () {
                var scale = transitionable.get();
                return Transform.translate(-(window.innerWidth - 200) * scale, 0, 0);
            },
            opacity: function () {
                return transitionable.get();
            }

        });

        this.mainNode.add(this.titleModifier).add(title);
    }

    function _createPhoto() {

        var photo = new ImageSurface({
            size: this.options.logoSize,
            content: 'assets/images/famous/logo-sf.png',
            properties: {
                zIndex: 2,
                pointerEvents: 'none'
            }
        });

        this.photoModifier = new Modifier({
            origin: [0, 0.5],
            align: [0, 0.5],
            transform: function () {
                // cache the value of transitionable.get()
                // to optimize for performance
                var scale = transitionable.get();
                return Transform.translate((window.innerWidth - HeaderView.DEFAULT_OPTIONS.logoSize[0] - 30) * scale, 0, 0.1); //scale(scale, scale, 1);
            },
            opacity: function () {
                return transitionable.get();
            }

        });

        this.mainNode.add(this.photoModifier).add(photo);
    }

    // Define your helper functions and prototype methods here
    module.exports = HeaderView;
});
