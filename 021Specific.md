## MythTV 0.21 Notes ##

For the program guide viewer to function correctly, you must apply
[this patch](http://mythdroid.googlecode.com/files/xml-guide.patch) to the
MythTV 0.21 fixes source code.

Once you have checked out a copy of release-0-21-fixes, apply the patch like so:

```
cd /path/to/release-0-21-fixes
patch -p1 < xml-guide.patch
```

and then follow the normal steps to configure, build and install MythTV.