

* We've been playing Dance of the Sugar Plum faeries non stop
  for 2 god-damn days. 

* Why is it called 5 Rat Harmony? 
  * Sauce has a lot of machines, but these machines are big, powerful 
    servers, made out of the cheapest parts money can buy. 
    You can think of these machines like "cattle".
    They can do a lot of work with them. Some people think it's fun
    to knock them down for sport.
  * But what about the far-away team in the frozen north? 
    We work on mobile devices. 
    They're small, they have minds of their own, and they die 
    really, _really_ easily. 
    They're rats.
 * But there are some things that rats can do that cattle can't. 
 * So we decided to build a choir, and get five devices to sing
   in unison. 
 

* what business value does this provide?
  * We've been plotting ways to torture people in the data center
  * It looks really cool
  * And, to be serious, I need some practice getting a lot of 
    devices to do things. 

*what did we do? 

* Jonah wrote the client, in Java, that runs on each of the devices. It 
  loads up a midi file and waits until it gets a message to trigger it. 
* Eric wrote the server, in Node, that does its best to send each device a 
  message as quickly as possible. We had a lot of clever ideas to try to get
  the best possible synchronization, but this solution had the benefits
  of 
  A: being very quick to code
  and
  B: working just well enough 
* I wrote a python thinger that takes a MIDI file, weights the instrumental
  tracks by how many notes they have, and tries to distribute tracks 
  as fairly as it can across phones. Then it loads the midi file on to
  each phone, turns the application on, and activates the sync server. 

