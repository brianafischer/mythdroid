## Configuration ##

If you have wireless enabled and a MythTV backend is on the local network
MythDroid will hopefully locate it automatically and you will be presented
with the main menu.

If you're running MythTV 0.25 or above MythDroid should locate your frontends automatically. Otherwise, or if your frontends are not switched on, you'll have to configure them. To do this, press the menu button and select 'Settings'. Then select 'Frontends Config' and add your frontends (a MAC address is not required unless you want to be able to Wake On LAN the frontend).

See the [settings guide](SettingsGuide.md) for more information on MythDroid's
settings.

## Main Menu ##

The menu button brings up a menu that lets you configure MythDroid, start
the appropriate remote (depending on frontend state), set the
_current frontend_, wake up a frontend (via Wake On LAN) or run a
['MDD command'](MDD.md) on a frontend.

The _current frontend_ is the frontend on which actions will take place when
certain menu items or buttons are short pressed. Examples include Watch TV,
playing a recording or selecting the remote entry in the Main Menu pop-up
menu. In most circumstances, long pressing such items will pop up a chooser
allowing you to select a frontend on which to perform the activity. Doing this
will automatically set the _current frontend_.

The default _current frontend_ can be specified in MythDroid's frontend
[settings](SettingsGuide.md).

## Recordings ##

The recordings activity lists the recordings available on the backend. Short
pressing a recording takes you to the Program Detail activity, where you'll be
shown more information about the program and offered the chance to play, delete
or edit the recording schedule of the recording. Long pressing a recording
allows you to select a frontend on which to play the recording. Pressing menu
will allow you to filter the recordings by program title, reset the filter or
refresh the recordings list.

## Videos ##

If you're running MythTV 0.24 or below you'll need MDD installed on your backend for the Videos activity to work. Short pressing a video will take you to the Video Detail activity, which shows you more information about the video and
offers the opportunity to play it or open the video's TVDB entry in the
browser. Short pressing the play button will play the video on the
_current frontend_. Long pressing the play button will allow you to choose
which frontend should play the video.

## Program Guide ##

Pressing on a program will bring up additional details about the program and allow you to add or edit a recording schedule. Short pressing a channel will play that channel in LiveTV on the _current frontend_. Long pressing a channel will display the frontend chooser. The menu allows you to change the guide start date and time.