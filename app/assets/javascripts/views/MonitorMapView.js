/*** ServerView ***/

// define this module in Require.JS
define(function (require, exports, module) {
    var pinIconPrefix = "assets/images/pins/ball_";
    var pinIconPostfix = ".png";
    var latitude = 46.9;
    var longitude = 8.3072438;
    var zoom = 8;
    var center = {lat: latitude, lng: longitude};

    // import dependencies
    var Engine = require('famous/core/Engine');
    var Modifier = require('famous/core/Modifier');
    var Surface = require('famous/core/Surface');
    var RenderNode = require('famous/core/RenderNode');
    var ImageSurface = require('famous/surfaces/ImageSurface');
    var Transform = require('famous/core/Transform');
    var StateModifier = require('famous/modifiers/StateModifier');
    var Easing = require('famous/transitions/Easing');
    var Timer = require('famous/utilities/Timer');

    var MapView = require('famous-map/MapView');
    var MapModifier = require('famous-map/MapModifier');
    var MapStateModifier = require('famous-map/MapStateModifier');
    var MapUtility = require('famous-map/MapUtility');
    var MapPositionTransitionable = require('famous-map/MapPositionTransitionable');
    var MapTransition = require('famous-map/MapTransition');

    var ClientPlayerView = ('views/ClientPlayerView');
    var PlayerClientView = require('views/PlayerClientView');

    var mainContext = Engine.createContext();
    var mapView;
    var playerClientView;

    // Constructor function for our ServerView class
    function MonitorMapView() {
        // Determine map-type
        var mapType = MapView.MapType.GOOGLEMAPS;
        // Create map-view
        mapView = new MapView({
            type: mapType,
            mapOptions: {
                zoom: zoom,
                center: center,
                disableDefaultUI: false,
                disableDoubleClickZoom: true,
                mapTypeId: google.maps.MapTypeId.TERRAIN,
                minZoom: 3
            }
        });
        mainContext.add(mapView);
        // PlayerClientView

        playerClientView = new PlayerClientView();
        mainContext.add(playerClientView);
    }


    MonitorMapView.prototype.showLocation = function (json) {
        console.log("showLocation: " + json);
        // Wait a little bit and let the map load some tiles, before doing the photo animation
        Timer.setTimeout(function () {

            // Create (almost) transparent markers.
            // The markers are created with correct dimensions but transparent,
            // so that they are correctly displayed on Android devices.

            var marker = _createPhotoMarker(json);
            marker.modifier.setOpacity(0.01);
            mainContext.add(marker.renderable);
            // Wait for the rendering to occur and then show the marker with an expand animation.
            // If we don't then we trigger the eager optimisation bug on Android.
            var interPhotoDelay = 50;
            var photoZoomDuration = 500;
            Timer.after(function () {
                marker.modifier.setTransform(Transform.scale(0, 0, 1));
                marker.modifier.setOpacity(1);
                marker.modifier.setTransform(
                    Transform.scale(0.01, 0.01, 1),
                    {duration: interPhotoDelay}
                );
                marker.modifier.setTransform(
                    Transform.scale(1, 1, 1),
                    {duration: photoZoomDuration, curve: Easing.outBack}
                );
            }, 5);

            // Wait for all animations to complete before loading the images.
            // Loading the images is done is the same thread by the browser and therefore greatly affects
            // javascript execution and rending.
            Timer.setTimeout(function () {
                marker.content.photo.setContent(pinIconPrefix + json.locationStatus + pinIconPostfix);
            }.bind(this), interPhotoDelay + photoZoomDuration);
        }, 200);
    };

    var markerMap = {};

    /**
     * Creates a photo-marker
     */
    function _createPhotoMarker(location) {
        var marker = markerMap[location.uuid];
        if (marker == undefined) {
            var randomAngle = (Math.PI / 180) * (15 - (Math.random() * 30));
            marker = {
                mapModifier: new MapModifier({
                    mapView: mapView,
                    position: [location.latitude, location.longitude]
                }),
                modifier: new StateModifier({
                    align: [0, 0],
                    origin: [0.5, 1]
                }),
                content: {
                    modifier: new Modifier({
                        size: [72, 72],
                        transform: Transform.rotateZ(randomAngle)
                    }),
                    photoModifier: new Modifier({
                        origin: [0.5, 1],
                        align: [0.5, 1],
                        transform: Transform.scale(0.86, 0.78, 1)
                    }),
                    photo: new ImageSurface({
                        classes: ['arrow-yellow']
                    })
                }
            };
            marker.renderable = new RenderNode(marker.mapModifier);
            var renderable = marker.renderable.add(marker.modifier).add(marker.content.modifier);
            var addedModifier = renderable.add(marker.content.photoModifier);
            addedModifier.add(marker.content.photo);
            markerMap[location.uuid] = marker;
            marker.content.photo.on('click', _showClients.bind(this));

            function _showClients() {
                console.log("clicked--" + location);
                mainContext.add(new Surface({
                    content: location.uuid

                }))
            }

        }
        return marker;
    }

    MonitorMapView.prototype.addClient = function (json) {
        console.log("addClient: " + json);
        playerClientView.addClient(json);
    };

    // Default options for ServerView class
    MonitorMapView.DEFAULT_OPTIONS = {};


    module.exports = MonitorMapView;
});
