/*** main.js ***/

define(function (require, exports, module) {
    var Engine = require('famous/core/Engine');

    var AppView = require('views/AppView');

    var MapView = require('famous-map/MapView');

    var mapView = new MapView({
        type: MapView.MapType.LEAFLET,
        mapOptions: {
            zoom: 3,
            center: {lat: 51.4484855, lng: 5.451478}
        }
    });
    this.add(mapView);

// Wait for the map to load and initialize
    mapView.on('load', function () {

        // Add tile-layer (you can also get your own at mapbox.com)
        L.tileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a>'
        }).addTo(mapView.getMap());
    }.bind(this));


});



