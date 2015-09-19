## MythDroid Settings ##

### Manual Backend Config ###

Most users won't need to use this setting since MythDroid should
automatically locate and connect to MythTV backends that are on
the same LAN.

If your backend is on a different network or you are connecting via a
SSH tunnel you will need to set the 'Backend Address' to the hostname
or IP address of the backend.

If you are connecting via a SSH tunnel to a backend behind NAT and wish
to stream content to MythDroid you will also need to set the
'Backend Public Address' to the public hostname or IP address of your router.
See [the streaming guide](StreamingGuide.md) for additional information.

### Frontends Config ###

This is where you configure the frontends that you want MythDroid to control.
To add a frontend, click 'Add frontend', give it a name and set the IP address
or hostname. If you want to be able to wake the frontend remotely using
Wake-On-LAN, you will also need to set the MAC address of the frontend.

You can also choose a default frontend, which is the frontend that actions
will occur on unless otherwise specified. See [the user guide](UserGuide.md) for
additional information.

### OSD Settings ###

MythDroid can display incoming calls and SMS on the MythTV OSD. You can
disable these features here. You can also specify that MythDroid should
use the alternative XOSD-based method of displaying information (via MDD).
The XOSD-based method is useful if you are using a version of MythTV that
does not have support for OSD messages (e.g. 0.24) or if you would like
OSD messages to appear at all times (even when mythfrontend is not running).

### Wake on movement ###

By default, MythDroid will monitor the accelerometer and automatically wake
and unlock your device if it is moved whilst in a remote control activity.
You can disable this feature by unchecking this option.

### Default Remote Styles ###

The TV and Nav remotes have two basic styles: button and gesture. You can
select the default mode for each here. See [the remotes guide](RemoteGuide.md)
for additional information.

### Streaming Settings ###

In this menu you can change the complexity of stream that [MDD](MDD.md) generates via vlc. Higher complexities mean higher quality video (for a given bitrate) at the expense of increased CPU load on the backend. Higher complexities enable additional H.264 features and support for these features varies by device. Set it to "Main - high" and lower it if necessary.

You can also choose to use an external video player app for playback. You'll be prompted to select one once a stream has been started. Note that seeking will not be supported by external video player apps.

### Error Reporting ###

By default MythDroid will offer to send the developers crash report
information if MythDroid crashes. You can disable this feature here.

### Update Notifications ###

MythDroid will let you know if new versions of MythDroid or [MDD](MDD.md) are available and download them for you unless you disable this feature here. MythDroid checks the version of [MDD](MDD.md) on your backend when it starts and will check each frontend as you connect to them.