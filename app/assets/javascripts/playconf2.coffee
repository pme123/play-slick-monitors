



connectWebSocket = () ->
  websocket = new WebSocket $("#ws-url").val()
  websocket.onmessage = (evt) ->
     json = JSON.parse(evt.data)
     showArticle json if json.messageType is "article"
     showClient json if json.messageType is "client"
  websocket.onopen = ->
    console.log("Connection is open")
  websocket.onerror = (evt) ->
    alert "error " + evt    

showArticle = (json) ->
  $("#articleName").html json.name
  $("#articleDescr").html json.descr
  $("#articleImg").attr("src", json.img)

showClient = (json) ->
    $("#clientUUID").html json.uuid


$(document).ready ->
   if(typeof(WebSocket) == "function")
      connectWebSocket()