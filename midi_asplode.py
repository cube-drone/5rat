
# https://github.com/vishnubob/python-midi
import midi

import os
import copy

def filter_unimportant_tracks(track_list):
    """
    Remove any tracks that do not contain a NoteOnEvent, 
    as these tracks are just informational.
    """
    new_list = []
    for track in track_list:
        it_has_notes = False
        for event in track:
            if "NoteOnEvent" in str(event.__class__):
                it_has_notes = True
                break
        if it_has_notes:
            new_list.append(track)
    return new_list

def track_name(track):
    track_name = "Unknown Track"
    for event in track:
        if "TrackName" in str(event.__class__):
            track_name = event.text
            break
    return track_name

def print_tracks(track_list):
    """
    Print the name of each track, as well as the number of notes
    contained within the track. 
    """
    for track in track_list:
        print track_name(track), len(track)

def next(counter, div_number):
    counter = counter + 1
    if counter >= div_number:
        counter = 0
    return counter

def deal_tracks(sorted_track_list, div_number):
    """
    Given a track list sorted by number-of-notes, 
    divide the tracks into div_number lists, trying to give
    every separate track as many notes as possible. 
    """
    track_split = []
    for i in range(0, div_number):
        track_split.append([])
    current_track = 0

    for track in sorted_track_list:
        track_split[current_track].append(track)
        current_track = next(current_track, div_number) 
    
    return track_split

def save_tracks(original_filename, new_filename, track_list):
    """
    Save the tracks to disk as a .mid file.
    """
    pattern = midi.read_midifile(filename)
    for track in pattern:
        if track not in track_list:
            for event in track:
                try:
                    event.velocity = 1
                except AttributeError:
                    pass
    midi.write_midifile(new_filename, pattern)

def asplode(filename, div_number):
    pattern = midi.read_midifile(filename)
    
    tracks = filter_unimportant_tracks(pattern)
    #for track in tracks:
    #    track.make_ticks_abs()
    print len(tracks), "available tracks"

    tracks = sorted(tracks, key=len)
    tracks.reverse()

    print div_number, "MIDI files"
    track_split = deal_tracks(tracks, div_number)
    
    directory_name = filename[:-4]+"_split"
    try:
        print "creating",
        os.mkdir( directory_name )
    except OSError:
        print directory_name, "already exists"
    for counter, track_group in enumerate(track_split):
        group_filename = os.path.join( directory_name, str(counter)+".mid" ) 
        print group_filename, "------"
        print_tracks(track_group)
        save_tracks(filename, group_filename, track_group)
        


if __name__ == '__main__':
    import sys
    try:
        filename = sys.argv[1]
        div_number = int(sys.argv[2])
    except IndexError:
        print "Include a filename and a division-number, brigand"

    print filename
    asplode(filename, div_number)
