/*** EmptyView ***/

// define this module in Require.JS
define(function (require, exports, module) {

    // Import additional modules to be used in this view
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var Transform = require('famous/core/Transform');
    var StateModifier = require('famous/modifiers/StateModifier');
    var ImageSurface = require('famous/surfaces/ImageSurface');

    var SlideData = require('data/SlideData');
    var Transitionable = require('famous/transitions/Transitionable');
    var SpringTransition = require('famous/transitions/SpringTransition');

    var Config = require('data/Config');

    Transitionable.registerMethod('spring', SpringTransition);

    // Constructor function for our EmptyView class
    function SlideView() {
        // Applies View's constructor function to EmptyView class
        View.apply(this, arguments);
        this.rootModifier = new StateModifier({
            align: [0.5, 0.0],
            origin: [0.5, 0.0],
            size: this.options.size
        });

        // saving a reference to the new node
        this.mainNode = this.add(this.rootModifier);

        _createBackground.call(this);
        _createFilm.call(this);
        _createPhoto.call(this);
        _createText.call(this);
    }

    // Establishes prototype chain for EmptyView class to inherit from View
    SlideView.prototype = Object.create(View.prototype);
    SlideView.prototype.constructor = SlideView;

    // Default options for EmptyView class
    SlideView.DEFAULT_OPTIONS = {
        size: [Config.slideWidth, Config.slideHeight],
        filmBorder: Config.filmBorder,
        photoBorder: Config.photoBorder,
        photoUrl: undefined,
        photoTitle: undefined,
        angle: Config.angle
    };

    function _createBackground() {
        var background = new Surface({
            // undefined size will inherit size from parent modifier
            properties: {}
        });
        background.addClass('photo-bg');
        this.mainNode.add(background);

        background.on('click', function () {
            // the event output handler is used to broadcast outwards
            this._eventOutput.emit('click');
        }.bind(this));
    }

    function _createFilm() {
        this.options.filmSize = this.options.size[0] - 2 * this.options.filmBorder;

        var film = new Surface({
            size: [this.options.filmSize, this.options.filmSize]
        });
        film.addClass("film");

        var filmModifier = new StateModifier({
            origin: [0.5, 0],
            align: [0.5, 0],
            transform: Transform.translate(0, this.options.filmBorder, 0.05)
        });

        this.mainNode.add(filmModifier).add(film);
    }

    function _createPhoto() {
        var size = this.options.filmSize - 2 * this.options.photoBorder;

        var photo = new ImageSurface({
            size: [size, size],
            content: this.options.photoUrl,
        });
        photo.addClass('photo');
        this.photoModifier = new StateModifier({
            origin: [0.5, 0],
            align: [0.5, 0],
            transform: Transform.translate(0, this.options.filmBorder + this.options.photoBorder, 0.1),
            opacity: 0.01
        });

        this.mainNode.add(this.photoModifier).add(photo);
    }

    function _createText() {
        var size = 2 * this.options.photoBorder;

        var title = new Surface({
            size: [this.options.filmSize - 2 * this.options.photoBorder, size],
            content: this.options.photoTitle,
            properties: {
                fontSize: Config.photoFontSize
            }
        });
        title.addClass('photo-text');

        this.titleModifier = new StateModifier({
            origin: [0.5, 0],
            align: [0.5, 0],
            transform: Transform.translate(0, this.options.filmSize + 1.5 * this.options.filmBorder, 0.1),
            opacity: 0.01
        });

        this.mainNode.add(this.titleModifier).add(title);
    }


    SlideView.prototype.fadeIn = function () {
        this.photoModifier.setOpacity(1, {duration: 1500, curve: 'easeIn'});
        this.titleModifier.setOpacity(1, {duration: 1500, curve: 'easeIn'});
        this.shake();
    };

    SlideView.prototype.shake = function () {
        this.rootModifier.halt();

        // rotates the slide view back along the top edge
        this.rootModifier.setTransform(
            Transform.rotateX(this.options.angle),
            {duration: 200, curve: 'easeOut'}
        );

        // returns the slide back to 0 degress but using a spring transition
        this.rootModifier.setTransform(
            Transform.identity,
            {method: 'spring', period: 600, dampingRatio: 0.15}
        );
    };


    // Define your helper functions and prototype methods here

    module.exports = SlideView;
});
