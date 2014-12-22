/**
 * this is the default config
 */
define(function (require, exports, module) {

    var PlaylistConfig = require("views/PlaylistConfig");
    var Transform = require('famous/core/Transform');
    var Easing = require('famous/transitions/Easing');

    ArticleConf = function(){

    }

    ArticleConf.prototype = new PlaylistConfig();
    ArticleConf.prototype.constructor = ArticleConf;

    // override props
    ArticleConf.prototype.angle = 0.0;
    ArticleConf.prototype.lightboxOpts = {
        inOpacity: 1,
        outOpacity: 1,

        inOrigin: [0.5, 0.5],
        outOrigin: [0.5, 0.5],
        showOrigin: [0.5, 0.5],
        inTransform: Transform.translate(-window.innerWidth, 0),
        outTransform: Transform.translate(window.innerWidth, 0),
        inTransition: {duration: 1000, curve: Easing.easeIn},
        outTransition: {duration: 1000, curve: Easing.easeOut}
    };
    ArticleConf.prototype.textOpacity = 1;
    ArticleConf.prototype.lightboxFadeIn = {duration: 1000, curve: 'easeIn'};
    ArticleConf.prototype.lightboxShakeRotate = {duration: 1000, curve: 'easeOut'};
    ArticleConf.prototype.lightboxBounceRotate = {method: 'spring', period: 0, dampingRatio: 0.15};


    module.exports = ArticleConf;
});
