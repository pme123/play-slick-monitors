/**
 * this is the default config
 */
define(function (require, exports, module) {

    var PlaylistConfig = require("views/PlaylistConfig");

    ArticleConf = function(){

    }

    ArticleConf.prototype = new PlaylistConfig();
    ArticleConf.prototype.constructor = ArticleConf;

    module.exports = ArticleConf;
});
