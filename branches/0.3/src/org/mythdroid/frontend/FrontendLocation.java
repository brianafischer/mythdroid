/*
    MythDroid: Android MythTV Remote
    Copyright (C) 2009-2010 foobum@gmail.com
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package org.mythdroid.frontend;

import java.io.IOException;
import java.util.HashMap;

import org.mythdroid.activities.MythDroid;

import android.util.Log;

/** Describes a location in the frontend */
public class FrontendLocation {

    private static HashMap<String, String> locations = null;
        
    public String  location  = null, niceLocation = null;
    public int     position, end;
    public float   rate;
    public String  filename;
    public boolean 
        video = false, livetv = false, music = false, musiceditor = false;

    /**
     * Constructor
     * @param loc - String describing location
     */
    public FrontendLocation(FrontendManager feMgr, String loc) {

        location = loc;
        
        if (MythDroid.debug) Log.d("FrontendLocation", "loc: " + loc);
        
        if (locations == null && !populateLocations(feMgr)) 
            return;

        loc = loc.toLowerCase();

        if (loc.startsWith("playback "))
            parsePlaybackLoc(loc);
        else if (loc.startsWith("error"))
            location = niceLocation = "Error";
        else
            niceLocation = locations.get(loc);

        if (niceLocation == null)
            niceLocation = "Unknown";
        else if (loc.equals("playmusic")) 
            music = true;
        else if (loc.equals("musicplaylists"))
            musiceditor = true;
        
        if (MythDroid.debug) 
            Log.d(
                "FrontendLocation", 
                "loc: " + loc + 
                " location: " + location + 
                " niceLocation: " + niceLocation
            );

    }

    private Boolean populateLocations(FrontendManager feMgr) {

        if (feMgr == null || !feMgr.isConnected())
            return false;

        try {
            locations = feMgr.getLocs();
        } catch (IOException e) { return false; }
        
        return true;
        
    }

    private void parsePlaybackLoc(String loc) {
        String[] tok = loc.split(" ");
        niceLocation = tok[0] + " " + tok[1];
        location = niceLocation;
        position = timeToInt(tok[2]);
        
        if (tok[1].equals("recorded")) { //$NON-NLS-1$
            end = timeToInt(tok[4]);
            rate = Float.parseFloat(tok[5].substring(0, tok[5].lastIndexOf('x')));
            filename = tok[9];
        }
        else if (tok[1].equals("video")) { //$NON-NLS-1$
            end = -1;
            rate = Float.parseFloat(tok[3].substring(0, tok[3].lastIndexOf('x')));
            filename = "Video"; //$NON-NLS-1$
        }
        else {
            end = -1;
            rate = -1;
            filename = "Unknown"; //$NON-NLS-1$
        }
        
        video = true;
        if (tok[1].equals("livetv")) livetv = true;
        
        if (MythDroid.debug) 
            Log.d(
                "FrontendLocation", 
                "position: " + position + 
                " end: " + end + 
                " rate: " + rate + 
                " filename: " + filename + 
                " livetv: " + livetv
            );
        
    }

    private int timeToInt(String time) {
        String tm[] = time.split(":");
        return Integer.parseInt(tm[0]) * 3600 + 
                 Integer.parseInt(tm[1]) * 60 +
                   Integer.parseInt(tm[2]);
    }

}