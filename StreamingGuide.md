## Streaming ##

Streaming of recordings and video is performed via MDD on the backend, which
uses vlc to transcode and stream the media. For this to work, you'll need
[MDD](MDD.md) and vlc installed on your backend.

In MythDroid, stream a recording or video by selecting "Here" from a frontend
chooser (found by long pressing a recording or video or the 'play' button in a
recording or video detail page).

Transcoding the video requires a hefty cpu on the backend; a reasonable
dual-core is probably the minimum. You can configure the stream complexity in MythDroid's [settings](SettingsGuide.md). A lower complexity will require less cpu on the backend.

You should be able to stream any recording or video that vlc can demux and
decode (dvds ripped to iso won't work). If you have problems, check
/tmp/vlc.out for more information.

To stream over the Internet, you'll need to:

  * Forward port 16550/tcp (MDD's CMux) to your backend (either at your router or via SSH)
  * Forward port 5554/tcp to your backend (at your router - NOT via SSH)
  * Configure the public address of your backend in the manual backend configuration of MythDroid
  * If MythDroid is behind NAT, hope that it has a RTSP ALG
  * Hope that your carrier doesn't block RTSP or RTP streams

In addition, the public address of your backend (i.e. of your router) must be configured in the MythDroid [manual backend configuration](SettingsGuide.md).

If you know what you're doing, you can customise the streaming command in
[mdd.conf](MDDConf.md).