/*** PlaylistConfig ***/
define(function (require, exports, module) {


    // Constructor function for our PlaylistConfig class
    function PlaylistConfig() {

    }

    var Transform = require('famous/core/Transform');
    var Easing = require('famous/transitions/Easing');

    var slideWidth = parseInt(0.6 * window.innerHeight);
    var filmBorder = parseInt(slideWidth / 20);
    var slideHeight = slideWidth + 3 * filmBorder;

    PlaylistConfig.prototype.slideWidth = slideWidth;
    PlaylistConfig.prototype.photoFontSizeFactor = function(){30};
    PlaylistConfig.prototype.photoFontSize = parseInt(slideWidth / this.photoFontSizeFactor) + "pt";
    PlaylistConfig.prototype.perspective = 1000;
    PlaylistConfig.prototype.slideHeight = slideWidth + 3 * filmBorder;
    PlaylistConfig.prototype.slidePosition = 10;
    PlaylistConfig.prototype.filmBorder = filmBorder;
    PlaylistConfig.prototype.photoBorder = parseInt(filmBorder / 6);
    PlaylistConfig.prototype.angle = 0.3;
    PlaylistConfig.prototype.lightboxOpts = {
        inOpacity: 1,
        outOpacity: 1,
        inOrigin: [0.5, 0],
        outOrigin: [0.5, 1],
        showOrigin: [0.5, 0.5],
        inTransform: Transform.thenMove(Transform.rotateZ(0.7), [0, -slideHeight - 40, 0]),
        outTransform: Transform.thenMove(Transform.rotateZ(0.7), [0, window.innerHeight, 0])
        // inTransition= {duration: 650, curve: 'easeOut'},
        //outTransition: {duration: 500, curve: Easing.inCubic}
    };
    PlaylistConfig.prototype.lightboxFadeIn = {duration: 1500, curve: 'easeIn'};
    PlaylistConfig.prototype.lightboxShakeRotate = {duration: 200, curve: 'easeOut'};
    PlaylistConfig.prototype.lightboxBounceRotate = {method: 'spring', period: 600, dampingRatio: 0.15};

    module.exports = PlaylistConfig;
});
