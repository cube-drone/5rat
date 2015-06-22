'use strict'

let exec = require('teen_process').exec
let midiplayer = require('midiplayer')
let midifile = require('midifile')
let fs = require('fs-promise')


let file = "../midi_files/SMWOverworld.mid"

let player = new midiplayer()
//exec('../rat_parade')

fs.readFile(file).then(function (buf) {
  let file = new midifile(buf)

  player.load(file);

  player.play(function () {
    console.log('finished playing song yo');
  })
});
