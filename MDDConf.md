## MDD Configuration ##

MDD does not require any configuration. However, you can modify some settings
by editing /etc/mdd.conf.

### Configuration Parameters ###

|Parameter|Value|Default|Description|
|:--------|:----|:------|:----------|
|debug    |true or false|false  |Setting debug to true makes MDD write extra information in the log|
|logfile  |path |/tmp/mdd.log|Set the path of MDD's log file|
|user     |username|mdd    |Set the username that mdd will switch to if it's run as root|
|cmux     |true or false|true   |Enable or disable MDD's [CMux](CMux.md) module|
|cmux\_extra\_ports|Space separated list of port numbers|None   |A list of extra ports that [CMux](CMux.md) will permit connections to|
|command  |name => command|None   |A MDD command that will be available from MythDroid, multiple definitions are permitted|
|stream   |command|See [Streaming Guide](StreamingGuide.md)|See [Streaming Guide](StreamingGuide.md)|

### Syntax ###

Parameter definitions must be one per line.
Lines beginning with a '#' character are ignored.
Long lines can be split with the '\' character.

### Default mdd.conf ###

```
# Turn debug mode on or off
# debug = false

# Set log file location
# logfile = /tmp/mdd.log

# Set the user that mdd will chuid to
# user = mdd

# Enable or disable MDD's connection muxer
# cmux = true

# Add to CMux's list of ports it's allowed to open connections to
# cmux_extra_ports = 80 443

# MDD commands
# command = Sleep => dbus-send --system --dest=org.freedesktop.DeviceKit.Power\
#  /org/freedesktop/DeviceKit/Power org.freedesktop.DeviceKit.Power.Suspend

# Custom streaming command
# Variables:
#   %FILE% will be substituted with the filename
#   %VB% will be substituted with the video bitrate in kb/s
#   %AB% will be substituted with the audio bitrate in kb/s
#   %WIDTH% will be substituted with the desired video width in pixels
#   %HEIGHT% will be substituted with the desired video height in pixels
#   %THR% will be substituted with the number of cpus available
#   Mythdroid requires that the sdp be available at rtsp://host:5554/stream
#   Mythdroid expects, but doesn't require, a VLC rc interface on tcp port 16547
# stream = /usr/bin/vlc -vvv -I oldrc --rc-host 0.0.0.0:16547 --rc-fake-tty \
# --file-caching=2000 %FILE% --sout='#transcode{vcodec=h264\
# ,venc=x264{no-cabac,level=30,keyint=50,ref=4,bframes=0,bpyramid=none,\
# profile=baseline,no-weightb,weightp=0,no-8x8dct,trellis=0},vb=%VB%,\
# threads=%THR%,deinterlace,acodec=mp4a,samplerate=48000,ab=%AB%,channels=2,\
# audio-sync}:rtp{sdp=rtsp://0.0.0.0:5554/stream}' 2>&1
```