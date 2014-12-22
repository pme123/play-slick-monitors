/**
 * this is the default config
 */
define(function (require, exports, module) {

    var PlaylistConfig = require("views/PlaylistConfig");

    IntroConf = function () {

    }

    IntroConf.prototype = new PlaylistConfig();
    IntroConf.prototype.constructor = IntroConf;

    // override props
    IntroConf.prototype.photoFontSize = parseInt(PlaylistConfig.prototype.slideWidth / 15) + "pt";

    module.exports = IntroConf;
});
