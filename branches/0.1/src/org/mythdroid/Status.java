/*
    MythDroid: Android MythTV Remote
    
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

package org.mythdroid;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.R.layout;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Status extends ListActivity {

    /** The status XML doc from the backend */
    public static Document        statusDoc   = null;
    
    final static private int      DIALOG_LOAD = 0;
    final private Context         ctx         = this;
    final private static String[] StatusItems =
    { "Recorders", "Scheduled", "Job Queue", "Backend Info" };

    final private Handler handler = new Handler();
            
    final private Runnable getStatusTask = new Runnable() {
        @Override
        public void run() {
            getStatus(ctx);
            handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        dismissDialog(DIALOG_LOAD);
                        setListAdapter(
                            new ArrayAdapter<String>(
                                ctx, layout.simple_list_item_1, StatusItems
                            )
                        );
                    }
                }
            );
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        showDialog(DIALOG_LOAD);
        MythDroid.wHandler.post(getStatusTask);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        statusDoc = null;
    }

    /** When a status menu entry is selected */
    @Override
    public void onListItemClick(ListView list, View item, int pos, long id) {
        
        final String action = (String)list.getItemAtPosition(pos);
        Class<?> activity = null;

        if (action.equals("Recorders"))       activity = StatusRecorders.class;
        else if (action.equals("Scheduled"))  activity = StatusScheduled.class;
        else if (action.equals("Job Queue"))  activity = StatusJobs.class;
        else if (action.equals("Backend Info")) activity = StatusBackend.class;

        startActivity(new Intent().setClass(this, activity));

    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
    }

    @Override
    public Dialog onCreateDialog(int id) {
        ProgressDialog d = new ProgressDialog(this);
        d.setIndeterminate(true);
        d.setMessage("Loading");
        return d;
    }

    /**
     * Get new statusDoc from the backend
     * @param ctx
     */
    public static void getStatus(Context ctx) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            URL url = new URL(MythDroid.beMgr.getStatusURL() + "/xml");
            statusDoc = dbf.newDocumentBuilder().parse(
                url.openConnection().getInputStream()
            );
        } catch (SAXException e) {
            Util.err(ctx, "Status XML parse error");
        } catch (Exception e) { Util.err(ctx, e); }

    }

}