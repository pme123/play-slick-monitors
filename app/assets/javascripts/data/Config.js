/**
 * Created by pascal.mengelt on 18.12.2014.
 */
define(function (require, exports, module) {


    var Transform = require('famous/core/Transform');
    var Easing = require('famous/transitions/Easing');

    var slideWidth = 0.7 * window.innerHeight;
    var slideHeight = slideWidth + 40;
    var slidePosition = 0;

    module.exports =
    {
        perspective: 1000,
        slideWidth: slideWidth,
        slideHeight: slideHeight,
        slidePosition: slidePosition,
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
