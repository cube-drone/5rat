from subprocess import Popen, PIPE, call

class Device(object):
    def __init__(self, device_id):
        self.device_id = device_id

    def push_file(self, destination, location):
        print "pushing file from", destination, "to location", location
        call(['adb', '-s', self.device_id, 'push', destination, location])

class Devices(list):
    def __init__(self):
        p = Popen(['adb', 'devices', '-l'], stdout=PIPE)
        output, err = p.communicate()
        lines = output.split("\n")[1:]
        for line in lines:
            if len(line) >= 1:
                self.append(Device(line.split(" ")[0]))

if __name__ == '__main__':
    devices = Devices()
    for device in devices:
        print "Device ID:", device.device_id
