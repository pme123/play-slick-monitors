
showArticle = (json) ->
  $("#articleName").html json.name
  $("#articleDescr").html json.descr


connectWebSocket = () ->
console.log("connect To Web Socket")
  websocket = new WebSocket $("#ws-url").val()
  websocket.onmessage = (evt) ->
     json = JSON.parse(evt.data)
     showArticle json if json.messageType is "JsonArticle"
  websocket.onopen = ->
    console.log("Connection is open")
  websocket.onerror = (evt) ->
    alert "error " + evt    

$(document).ready ->
   if(typeof(WebSocket) == "function")
      connectWebSocket()