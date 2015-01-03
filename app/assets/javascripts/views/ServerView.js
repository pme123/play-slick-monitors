/*** ServerView ***/

// define this module in Require.JS
define(function (require, exports, module) {

    // Import additional modules to be used in this view
    var Engine = require('famous/core/Engine');
    var View = require('famous/core/View');
    var Surface = require('famous/core/Surface');
    var ImageSurface = require('famous/surfaces/ImageSurface');
    var Transform = require('famous/core/Transform');
    var Modifier = require('famous/core/Modifier');
    var StateModifier = require('famous/modifiers/StateModifier');

    var HeaderFooterLayout = require("famous/views/HeaderFooterLayout");
    var GridLayout = require("famous/views/GridLayout");
    var FlexScrollView = require('famous-flex/FlexScrollView');
    var CollectionLayout = require('famous-flex/layouts/CollectionLayout');
    var ListLayout = require('famous-flex/layouts/ListLayout');

    var ArticleView = require('views/ArticleView');

    var mainContext = Engine.createContext();

    // Constructor function for our ServerView class
    function ServerView() {

        // Applies View's constructor function to ServerView class
        View.apply(this, arguments);

        this.rootModifier = new StateModifier({
            align: [0.5, 0.5],
            origin: [0.5, 0.5]
        });

        // saving a reference to the new node
        this.mainNode = this.add(this.rootModifier);

        _createLayout.call(this);
        _addHeader.call(this);
        _addContent.call(this);
        _addFooter.call(this);

    }

    function _createLayout() {
        layout = new HeaderFooterLayout({
            headerSize: 100,
            footerSize: 50
        });

        mainContext.add(layout);
    }

    function _addHeader() {
        layout.header.add(new Surface({
            content: "Header",
            properties: {
                backgroundColor: 'gray',
                lineHeight: "100px",
                textAlign: "center"
            }
        }));
    }

    function _addContent() {
        _createScrollView.call(this);
        layout.content.set(ServerView.prototype.scrollView);
    }

    function _addFooter() {
        layout.footer.add(new Surface({
            content: "Footer",
            properties: {
                backgroundColor: 'gray',
                lineHeight: "50px",
                textAlign: "center"
            }
        }));
    }

    // Establishes prototype chain for ServerView class to inherit from View
    ServerView.prototype = Object.create(View.prototype);
    ServerView.prototype.constructor = ServerView;


    function _createScrollView() {
        ServerView.prototype.scrollView = new FlexScrollView({
            layout: CollectionLayout,
            layoutOptions: {
                itemSize: [100, 100],
                margins: [10, 10, 10, 10],
                spacing: [10, 10]
            },
            opacity: 0.5,
            useContainer: true,
            //  autoPipeEvents: true,
            paginated: true,
            flow: true,             // enable flow-mode (can only be enabled from the constructor)
            insertSpec: {           // render-spec used when inserting renderables
                opacity: 0,          // start opacity is 0, causing a fade-in effect,
                size: [0, 0]     // uncommented to create a grow-effect
                //       transform: Transform.translate(-300, 0, 0) // uncomment for slide-in effect
            },
            //removeSpec: {...},    // render-spec used when removing renderables
            nodeSpring: {           // spring-options used when transitioning between states
                dampingRatio: 0.8,  // spring damping ratio
                period: 1000        // duration of the animation
            }
        });
    }

    ServerView.prototype.showClient = function (json) {
        console.log("showClient: " + json);
    };
    ServerView.prototype.showArticle = function (json) {
        var articleView = new ArticleView(json);
        articleView.on('hello', function() {
            console.log("event yeee: " + json);

            var editModifier = new Modifier({
                origin: [0, 0.5],
                align: [0, 0.5],
                size: [400, 400],
                transform: function () {
                    return Transform.translate(0, 0, 4);
                },
                opacity: function () {
                    return 0.5;
                }

            });
            var editSurface = new Surface({
                content: json.name
            });
            layout.content.set(editModifier).add(editSurface);
        });

        ServerView.prototype.scrollView.push(articleView);
    };

    // Default options for ServerView class
    ServerView.DEFAULT_OPTIONS = {};

    // Define your helper functions and prototype methods here

    module.exports = ServerView;
});
