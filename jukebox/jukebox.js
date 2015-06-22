'use strict'

let SubProcess = require('teen_process').SubProcess
let fs = require('fs-promise')


let file = "midi_files/african0.mid"

this.proc = new SubProcess('./rat_parade', [file]);

// handle log output
this.proc.on('output', function (stdout, stderr) {
  if (stdout) {
    console.log(`[STDOUT] ${stdout.trim()}`);
  }
  if (stderr) {
    console.log(`[STDERR] ${stderr.trim()}`);
  }
});

let startDetector = function () {
  
}

this.proc.start(startDetector);
