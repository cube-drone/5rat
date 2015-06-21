var net = require("net");
var exec = require('child_process').exec;
var ADB = require('appium-adb');
var adb = new ADB();


var startSock = function(port, len) {
  var socket = net.connect(port, function () {
  	sockets.push(socket);
  	socket._port = port;
    socket.setEncoding('utf8');
    promised++;
    console.log("Connected on " + port);
    if (promised == len) {
    	for(var i = 0; i < sockets.length; i++) {
    		//console.log("Sending " + template);
			  sockets[i].write(template);
		  }
    }
    else{
        console.log("this happened");
        console.log(promised);
        console.log(len);
        console.log(sockets.length);
    }
  });

  socket.on('error', function (err) {
    console.dir(err);
  });

  socket.on('close', function () {
    console.log("Closing " + socket._port);
  });

  socket.on('data', function(data) {
    console.dir(data);
  });

}

function sleep(time, callback) {
  var stop = new Date().getTime();
  while(new Date().getTime() < stop + time) {
    ;
  }
  callback();
}

////////////////

var template = '{"action":"playSong", "cmd":"action"}';

var promised = 0;
var startPort = 4724;
var nextPort = startPort;
var args = "forward tcp:%p% tcp:" + nextPort;
var ports = [];
var sockets = [];

adb.getConnectedDevices(function(err, devices) {
	if (err) {
		console.log(err);
		process.exit(1);
	}
  	var i = 0;
	devices.forEach(function(device) {
		ports[i] = nextPort;
	    var arg = args.replace("%p%", ports[i]);
	    arg = "adb -s " + device.udid + " " + arg;
	    console.log("Running " + arg);
	    var tempPort = ports[i];

	    exec(arg, function(err, stdout, stderr){
	      if (err) {	 console.log ('error for device:', device.udid, err); }
		startSock(tempPort, devices.length);
	    });
	    i++;
	    nextPort++
	});
});
