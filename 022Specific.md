## MythTV 0.22 Notes ##

MythTV 0.22 has some bugs in the network control feature. Apply
[this patch](http://mythdroid.googlecode.com/files/myth-netcontrol-2.patch) so
that MythDroid is able to initiate recording playback properly.

Once you have checked out a copy of release-0-22-fixes, apply the patch like so:

```
cd /path/to/release-0-22-fixes
patch -p1 < myth-netcontrol-2.patch
```

and then follow the normal steps to configure, build and install MythTV.