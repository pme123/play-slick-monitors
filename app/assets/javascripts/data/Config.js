/**
 * Created by pascal.mengelt on 18.12.2014.
 */
define(function (require, exports, module) {


    var Transform = require('famous/core/Transform');
    var Easing = require('famous/transitions/Easing');

    var slideWidth = parseInt(0.6 * window.innerHeight);
    var filmBorder = parseInt(slideWidth/20);
    var slideHeight =slideWidth + 3*filmBorder;

    module.exports =
    {
        photoFontSize: parseInt(slideWidth/30)+"pt",
        perspective: 1000,
        slideWidth: slideWidth,
        slideHeight: slideWidth + 3*filmBorder,
        slidePosition: 10,
        filmBorder: filmBorder,
        photoBorder: parseInt(filmBorder/6),
        angle: 0.3,
        lightboxOpts: {
            inOpacity: 1,
            outOpacity: 1,
            inOrigin: [0.5, 0],
            outOrigin: [0.5, 1],
            showOrigin: [0.5, 0.5],
            inTransform: Transform.thenMove(Transform.rotateZ(0.7), [0, -slideHeight - 40, 0]),
            outTransform: Transform.thenMove(Transform.rotateZ(0.7), [0, window.innerHeight, 0])
            // inTransition: {duration: 650, curve: 'easeOut'},
            //outTransition: {duration: 500, curve: Easing.inCubic}
        }
    };
});
