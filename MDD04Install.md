## MDD 0.4.x Installation ##

The steps required to install MDD 0.4.4 or below:

Checkout or download
[mdd](http://code.google.com/p/mythdroid/downloads/detail?name=mdd-0.4.4.tgz) to
each frontend (or combined backend/frontend) and then (as root):

```
tar zxf mdd-0.4.4.tgz
cd mdd
perl mdd.pl
```

**Note:** You will need to reinstall MDD each time you upgrade MythTV. See the
details below for the reason.

On a box that serves only as a backend:

```
tar zxf mdd-0.4.4.tgz
cd mdd
perl mdd.pl --backend
```