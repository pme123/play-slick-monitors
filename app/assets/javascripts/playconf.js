
 document.addEventListener('DOMContentLoaded', function () {
    webSocket();
 });
        function webSocket()
{
  if ("WebSocket" in window)
  {
     console.log("WebSocket is supported by your Browser!");
     // Let us open a web socket
     var ws = new WebSocket(document.getElementById("ws-url").value);
     ws.onopen = function()
     {
        console.log("WebSocket open ...");
     };
     ws.onmessage = function (evt)
     {
         console.log("Message is received...");

        var json = JSON.parse(evt.data);
        if(json["messageType"]=="article")  {
        showArticle(json);                   }
        else {
        showClient(json)
        }
     };
     ws.onclose = function()
     {
        // websocket is closed.
        console.log("Connection is closed...");
     };
  }
  else
  {
     // The browser doesn't support WebSocket
     console.log("WebSocket NOT supported by your Browser!");
  }

}
  function showArticle(json) {
      var name =  document.getElementById("articleName");
      name.innerHTML =json.name;
      var descr = document.getElementById("articleDescr");
      descr.innerHTML = json.descr;
      var image = document.getElementById("articleImg")
      image.src = json.img
  }

    function showClient(json) {
       var uuid =  document.getElementById("clientUUID");
        uuid.innerHTML =json.uuid;
    }
