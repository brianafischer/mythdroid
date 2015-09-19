## Preparation ##

You will need:

  * Eclipse IDE - http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/junor
  * Subclipse - http://subclipse.tigris.org/servlets/ProjectProcess?pageID=p4wYuA
  * Android ADT plugin - http://developer.android.com/sdk/eclipse-adt.html
  * Android SDK - http://developer.android.com/sdk/index.html

## Checkout ##

Once you've installed everything and configured the ADT plugin you can
checkout MythDroid. To do so, click on 'New' -> 'Project' ->
'Checkout projects from SVN'. Create a new repository location and enter the
URL http://mythdroid.googlecode.com/svn. Then select the appropriate folder
('trunk' unless you want an older branch) and click finish.

MythDroid will build automatically if all goes well.

## Install ##

To install MythDroid, connect your device and click the 'Run' button in
Eclipse. If you get errors about application signatures or keys you will need
to uninstall any other versions of MythDroid first.