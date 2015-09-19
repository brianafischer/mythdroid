## MDD CMux ##

The MDD Connection Muxer module allows MythDroid to make connections to the
various (MDD and backend related) services it needs through a single TCP
connection (on port 16550/tcp). This is useful if you are using MythDroid
remotely since you only need forward a single port to your backend.

MythDroid will automatically use CMux if it's available and if it thinks
you are connecting remotely.

### Automatic, secure, remote connectivity ###

Since MDD 0.6, MDD will automatically authenticate MythDroid and encrypt all communications (except streamed video) between MythDroid and your backend. This means it is no longer necessary to use SSH to achieve secure connectivity to your MythTV server. Key setup is automatic - if you use MythDroid on your LAN (and have MDD installed), it is automatically given a key to facilitate secure connectivity when you are away. For additional details, see [here](CMuxCrypt.md).

### Configuration ###

By default CMux will only permit connections to TCP ports 6543, 6544, 16546
and 16547 for mythbackend, MDD and VLC's rc interface.

CMux can be configured in [mdd.conf](MDDConf.md), where it is possible to disable
CMux and add to the list of permitted ports.