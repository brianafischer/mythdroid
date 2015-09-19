# MDD #

MDD is designed to run on every frontend and backend and 'push' a range of
information that either cannot be accessed remotely by MythDroid or that
MythDroid would otherwise have to poll for. This information includes
detailed frontend location, details about currently playing programs and
music (including album art), progress information, etc.

In addition MDD can run arbitrary commands on frontends (see below) and allows
MythDroid to browse videos and schedule recordings.

Whilst MythDroid will work if MDD is not installed many features will not be
available and performance will suffer. We therefore strongly recommend taking
a few minutes to install and setup MDD.

## Configuration ##

No configuration is required but you might like to read
[the MDD configuration guide](MDDConf.md) anyway.

## Details ##

When running on a frontend MDD operates by sitting between mythfrontend and
mythlcdserver (during installation mythlcdserver is renamed to mythlcd and mdd
is installed as mythlcdserver). MDD intercepts information that is sent to the
LCD and makes it available to MythDroid. Note that you don't need an LCD for
this to work.

On a backend-only system mdd just runs as a normal daemon and is usually
started at system boot.
