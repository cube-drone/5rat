'use strict'

let path = require('path');

function TwitterStream () {

  var self = this;
  this.init = function () {
    client.stream('statuses/filter', {track: 'appiumjukebox'}, function (stream) {
      stream.on('data', function(tweet) {
        console.log(tweet);
        var url = tweet.text.match(urlRegEx)[0];
        var fileName = path.resolve('./midi_files', `${tweet.user.screen_name}___${songNo}.mid`);
        var ws = fs.createWriteStream(fileName);
        if (url) {
          var httpGet = http.get(url, function(res) {
            res.pipe(ws);
            res.on('end', function() {
              console.log(fileName + ' saved!');
              songNo += 1;
              self.emit('new song', fileName);
            });
          });
        }
      }).on('error', function(error) {
        throw error;
      });
    });
  };


  var auth = require('./twitter_auth.json');

  var Twitter = require('twitter');
  var http = require('follow-redirects').http;
  var fs = require('fs');
  var songNo = 0;

  var client = new Twitter({
    consumer_key: auth.TWITTER_CONSUMER_KEY,
    consumer_secret: auth.TWITTER_CONSUMER_SECRET,
    access_token_key: auth.TWITTER_ACCESS_TOKEN_KEY,
    access_token_secret: auth.TWITTER_ACCESS_TOKEN_SECRET
  });

  var urlRegEx = /((([A-Za-z]{3,9}:(?:\/\/)?)(?:[\-;:&=\+\$,\w]+@)?[A-Za-z0-9\.\-]+|(?:www\.|[\-;:&=\+\$,\w]+@)[A-Za-z0-9\.\-]+)((?:\/[\+~%\/\.\w\-]*)?\??(?:[\-\+=&;%@\.\w]*)#?(?:[\.\!\/\\\w]*))?)/g;

}

var util = require('util');
var EventEmitter = require('events').EventEmitter;
util.inherits(TwitterStream, EventEmitter);

module.exports = TwitterStream;
