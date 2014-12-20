/*** AppView ***/

define(function (require, exports, module) {
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var StateModifier = require('famous/modifiers/StateModifier');
    var ContainerSurface = require('famous/surfaces/ContainerSurface');
    var SlideshowView = require('views/SlideshowView');

    var Config = require('data/Config');

    function AppView() {
        View.apply(this, arguments);

        _createSlideshow.call(this);
    }

    AppView.prototype = Object.create(View.prototype);
    AppView.prototype.constructor = AppView;

    AppView.DEFAULT_OPTIONS = {
        // it's a good idea to add a property in the default options
        // even when it's undefined
        data: undefined,
        slideWidth: Config.slideWidth,
        slideHeight: Config.slideHeight,
        slidePosition: Config.slidePosition
    };

    function _createSlideshow() {
        this.slideshowView = new SlideshowView({
            size: [this.options.slideWidth, this.options.slideHeight],
            data: this.options.data
        });

        var slideshowModifier = new StateModifier({
            origin: [0.5, 0],
            align: [0.5, 0],
            transform: [0, this.options.slidePosition, 0]
        });
        var slideshowContainer = new ContainerSurface({
            properties: {
                overflow: 'hidden'
            }
        });

        this.add(slideshowModifier).add(slideshowContainer);
        slideshowContainer.add(this.slideshowView);
        slideshowContainer.context.setPerspective(Config.perspective);
    }

    AppView.prototype.showArticle = function (json) {
        SlideshowView.prototype.showSlide(json);
    };

    module.exports = AppView;
});
