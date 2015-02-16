/*** EmptyView ***/

// define this module in Require.JS
define(function (require, exports, module) {

    // Import additional modules to be used in this view
    var Modifier = require('famous/core/Modifier');
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var ContainerSurface = require('famous/surfaces/ContainerSurface');
    var RenderNode = require('famous/core/RenderNode');
    var Transform = require('famous/core/Transform');
    var StateModifier = require('famous/modifiers/StateModifier');
    var FlexScrollView = require('famous-flex/FlexScrollView');
    var scrollView;
    // Constructor function for our EmptyView class
    function PlayerClient2View() {
        // Applies View's constructor function to EmptyView class
        View.apply(this, arguments);
        this.rootModifier = new StateModifier({
            align: [1.0, 1.0],
            origin: [1.0, 1.0],
            opacity: 0.85,
            size: [200]
        });
        // saving a reference to the new node
        this.mainNode = this.add(this.rootModifier);
        //    this.mainNode.add( _createBackground());
        scrollView = new FlexScrollView({
            useContainer: true, // wraps scrollview inside a ContainerSurface
            flow: true,             // enable flow-mode (can only be enabled from the constructor)
            insertSpec: {           // render-spec used when inserting renderables
                opacity: 0,          // start opacity is 0, causing a fade-in effect,
                //size: [0, 0],     // uncommented to create a grow-effect
                transform: Transform.translate(-300, 0, 2) // uncomment for slide-in effect
            },
            layoutOptions: {
                margins: [10, 5, 0, 15], // margins in clockwise order: top, right, bottom, left
                spacing: 2,
                isHeaderCallback: function (renderNode) {
                    return renderNode.isHeader;
                }
            },
            //removeSpec: {...},    // render-spec used when removing renderables
            nodeSpring: {           // spring-options used when transitioning between states
                dampingRatio: 0.8,  // spring damping ratio
                period: 1000        // duration of the animation
            },
            paginated: true,
            //    paginationMode: FlexScrollView.PaginationMode.SCROLL,
            paginationEnergyThresshold: 0.01
        });
        this.mainNode.add(scrollView);
    }

    function _createHeader(locationUUID) {
        var surface = new Surface({content: locationUUID + " Clients:"});
        surface.addClass('clientPanel');
        surface.isHeader = true;
        var renderNode = new RenderNode(new StateModifier({
            size: [, 20],
            transform: Transform.infront
        }));
        renderNode.add(surface);
        return renderNode;
    }

    function _createBackground() {
        var background = new Surface({
            content: ''
        });
        background.addClass('locationPanel');
        return background;
    }

    // Establishes prototype chain for EmptyView class to inherit from View
    PlayerClient2View.prototype = Object.create(View.prototype);
    PlayerClient2View.prototype.constructor = PlayerClient2View;

    // Default options for EmptyView class
    PlayerClient2View.DEFAULT_OPTIONS = {};

    PlayerClient2View.prototype.locationMap = {};
    PlayerClient2View.prototype.addClient = function (json) {
        console.log("addClient: " + json.uuid);
        var clientMap = PlayerClient2View.prototype.locationMap[json.location];
        if (clientMap == undefined) {
            clientMap = {};
            PlayerClient2View.prototype.locationMap[json.location] = clientMap
        }
        clientMap[json.uuid] = json;
        console.log("addClient: " + PlayerClient2View.prototype.locationMap[json.location][json.uuid].uuid);
    };

    PlayerClient2View.prototype.showLocation = function (json) {
        var clientMap = PlayerClient2View.prototype.locationMap[json.id];

        console.log("clicked--" + json.uuid + "::" + PlayerClient2View.prototype.locationMap[json.id]);
        var sequence = [];
        for (clientUUID in clientMap) {
            sequence.push(clientUUID);
        }
        sequence.sort()
        scrollView.removeAll();
        scrollView.push(_createHeader(json.uuid));
        for (var i = 0; i < sequence.length; i++) {
            var client = clientMap[sequence[i]];
            var container = new ContainerSurface({
                size: [200, 50]
            });
            container.addClass('clientPanel');
            var surface = new Surface({
                content: client.uuid
            });
            surface.addClass('client' + client.connectionEvent);
            container.add(new Modifier({origin: [0, 0]})).add(surface);
            var subText = new Surface({
                content: "[Playlist: " + client.playlist + ", Order: " + client.order + "]"
            });
            subText.addClass('clientPanelSubtext');
            container.add(new Modifier({origin: [0, 0]})).add(subText);
            scrollView.push(container);
            console.log("location: " + json.uuid + " -client: " + clientUUID);
        }
        //  background.setContent(json.uuid)

    };
    // Define your helper functions and prototype methods here

    module.exports = PlayerClient2View;
});
