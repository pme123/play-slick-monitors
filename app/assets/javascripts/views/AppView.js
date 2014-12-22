/*** AppView ***/

define(function (require, exports, module) {
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var StateModifier = require('famous/modifiers/StateModifier');
    var ContainerSurface = require('famous/surfaces/ContainerSurface');
    var SlideshowView = require('views/SlideshowView');

    var Config;

    function AppView() {
        View.apply(this, arguments);
        Config = this.options.config;
        _createSlideshow.call(this);
    }

    AppView.prototype = Object.create(View.prototype);
    AppView.prototype.constructor = AppView;

    AppView.DEFAULT_OPTIONS = {
        config: undefined
    };

    function _createSlideshow() {
        this.slideshowView = new SlideshowView({
        config: Config
        });

        var slideshowModifier = new StateModifier({
            origin: [0.5, 0],
            align: [0.5, 0],
            transform: [0, Config.slidePosition, 0]
        });
        var slideshowContainer = new ContainerSurface({
            config: Config,
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
