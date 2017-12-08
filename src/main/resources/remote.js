var page = require('webpage').create();
var system = require('system');
var args = system.args;
var port;

if (args.length !== 2) {
    console.log("Usage phantomjs remote.js portNumber")
    phantom.exit();
} else {
    port = args[1];
}

socket = new WebSocket("ws://localhost:" + port + "/events/");
    socket.onmessage = function(event) {
        socket.send(command(JSON.parse(event.data)));
    }
    this.socket.onclose = function() {
      return phantom.exit();
    };



function command(message) {
    console.log(message.command);
    console.log(message.args);
    switch(message.command) {
        case "go":
            page.open(message.args.pop(), function(status) {

            });
            return "OK";
        case "source":
            return page.content;
        case "render":
            page.render("page.png");
            return "OK";
     }

 }
