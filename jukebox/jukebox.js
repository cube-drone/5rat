'use strict'

let SubProcess = require('teen_process').SubProcess
let fs = require('fs-promise')
let Midifile = require('midifile')
let path = require('path')
let TwitterStream = require('./twitter.js')
let B = require('bluebird')


let now_playing = B.resolve(true)

let file = "midi_files/african0.mid"

function toArrayBuffer(buffer) {
    var ab = new ArrayBuffer(buffer.length);
    var view = new Uint8Array(ab);
    for (var i = 0; i < buffer.length; ++i) {
        view[i] = buffer[i];
    }
    return ab;
}

function checkMidiFile (path) {
  return new Promise(function (resolve, reject) {
    fs.readFile(file).then(function (d) {
      let buf = toArrayBuffer(d);
      try {
        new Midifile(buf, true);
        resolve(path);
      } catch (e) {
        console.log(`file ${file} was not valid!`)
        reject();
      }
    })
  })
}

function playSong (path) {
  return new Promise(function (resolve, reject) {
    let proc = new SubProcess('./rat_parade', [path]);

    // handle log output
    proc.on('output', function (stdout, stderr) {
      if (stdout) {
        console.log(`[STDOUT] ${stdout.trim()}`);
      }
      if (stderr) {
        console.log(`[STDERR] ${stderr.trim()}`);
      }
    });

    proc.on('exit', function () {
      resolve(true);
    });

    let startDetector = function () {

    }

    proc.start(startDetector);
  })

}

let twitterEvents = new TwitterStream();

twitterEvents.on('new song', function (file) {
  now_playing = now_playing.finally(function () {
    console.log('we got a new song')
    return checkMidiFile(file).then(playSong, console.log)
  })
})

twitterEvents.init()
