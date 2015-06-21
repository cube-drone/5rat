var iosDevice = require('node-ios-device');
var file = process.argv[2];

iosDevice.devices(function (err, devices) {
  if (err) { console.log('could not get ios devices: ', err)};

  devices.forEach(function(device) {
    console.log('installing for device: ', device.udid);
    iosDevice.installApp(device.udid, file, function(err) {
      if (err) {
        console.log('failed to install ', file, 'on ', device.udid, '. ', err);
      };

      
    });
  });
});
