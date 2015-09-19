## MDD Installation ##

MDD makes use of the MythTV perl bindings so they must be installed. If you
install MythTV from source, ensure that the --with-bindings=perl option was
passed to configure. If you install MythTV from packages, ensure that the
perl bindings package is installed if available.

For MDD to function, you must ensure that the following settings in the LCD
page under Setup -> Appearance are enabled on each frontend:

  * Enable LCD device
  * Display time
  * Display menus
  * Display music artist and title (items = Artist - Title)
  * Display channel information

MDD will check these settings during installation and let you know if you need
to take action.

To install MDD 0.5.0 or greater, checkout or download the MDD tarball and then:

```
tar zxf mdd.tgz
cd mdd
perl Build.PL
./Build test
./Build install
```

See [the MDD 0.4 install guide](MDD04Install.md) if you're installing MDD
0.4.4 or below.