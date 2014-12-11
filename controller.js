var net = require("net");
var exec = require('child_process').exec;
var ADB = require('appium-adb');
var adb = new ADB();

var startSock = function(port) {
  var socket = net.connect(port, function () {
    socket.setEncoding('utf8');
    console.log("Connected on " + port);
    socket.write(template);
  });

  socket.on('error', function (err) {
    console.dir(err);
  });

  socket.on('close', function () {
    console.dir("Closing " + port);
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

var args = "forward tcp:%p% tcp:4724";
var startPort = 4724;
var ports = [startPort++, startPort++, startPort++, startPort++, startPort++];

adb.getConnectedDevices(function(err, devices) {
	if (err) {
		console.log(err);
		process.exit(1);
	}
  var i = 0;
	devices.forEach(function(device) {
    var arg = args.replace("%p%", ports[i]);
    arg = "adb -s " + device.udid + " " + arg;
    exec(arg, function(err, stdout, stderr){
      if (err) {	 console.log ('error for device:', device.udid, err); }
      console.log(device.udid, ':', stdout, stderr || '');
    });
    i++;
	});
});

sleep(5000, function(){
  ports.forEach(function(p){
    startSock(p);
  });
});
