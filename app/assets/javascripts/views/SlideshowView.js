/*** EmptyView ***/

// define this module in Require.JS
define(function (require, exports, module) {

    // Import additional modules to be used in this view
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var Transform = require('famous/core/Transform');
    var StateModifier = require('famous/modifiers/StateModifier');
    var SlideView = require('views/SlideView');
    var Lightbox = require('famous/views/Lightbox');
    var Easing = require('famous/transitions/Easing');
    var Timer = require('famous/utilities/Timer');
    var Config = require('data/Config');


    function SlideshowView() {
        View.apply(this, arguments);

        this.rootModifier = new StateModifier({
            size: this.options.size,
            origin: [0.5, 0],
            align: [0.5, 0]
        });

        this.mainNode = this.add(this.rootModifier);
        _createLightbox.call(this);
    }

    // Establishes prototype chain for EmptyView class to inherit from View
    SlideshowView.prototype = Object.create(View.prototype);
    SlideshowView.prototype.constructor = SlideshowView;

    // Default options for EmptyView class
    SlideshowView.DEFAULT_OPTIONS = {
        size: [450, 500]
    };

    function _createLightbox() {
        SlideshowView.prototype.lightbox = new Lightbox(Config.lightboxOpts);
        this.mainNode.add(SlideshowView.prototype.lightbox);
    }


    SlideshowView.prototype.showSlide = function (json) {

        var slide = new SlideView({
            size: SlideshowView.DEFAULT_OPTIONS.size,
            photoUrl: json.img,
            photoTitle: json.name
        });

        SlideshowView.prototype.lightbox.show(slide, function () {
            slide.fadeIn();
        }.bind(this));
    };

    // Define your helper functions and prototype methods here
    module.exports = SlideshowView;
});
