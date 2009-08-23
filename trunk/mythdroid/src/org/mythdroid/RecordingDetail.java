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

package org.mythdroid;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordingDetail extends MDActivity {

    final static private int DELETE_DIALOG = 0, STOP_DIALOG = 1;

    final private Context ctx = this;

    private boolean livetv = false;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.recording_detail);
        livetv = getIntent().getBooleanExtra(MythDroid.LIVETV, false);
        setViews();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setResult(Activity.RESULT_OK);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        setContentView(R.layout.recording_detail);
        setViews();
    }

    @Override
    public Dialog onCreateDialog(int id) {

        OnClickListener no = new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        switch (id) {

            case DELETE_DIALOG:
                return 
                    new AlertDialog.Builder(this)
                        .setTitle("Delete recording")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes",
                            new OnClickListener() {
                                @Override
                                public void onClick(
                                    DialogInterface dialog, int which) {
                                    try {
                                        MythDroid.beMgr.deleteRecording(
                                            MythDroid.curProg
                                        );
                                    } catch (IOException e) { 
                                        Util.err(ctx, e); 
                                    }
                                    setResult(Recordings.REFRESH_NEEDED);
                                    dialog.dismiss();
                                    finish();
                                }
                            }
                        )
                        .setNegativeButton("No", no)
                        .create();

            case STOP_DIALOG:
                return
                    new AlertDialog.Builder(this)
                        .setTitle("Stop recording")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes",
                            new OnClickListener() {
                                @Override
                                public void onClick(
                                    DialogInterface dialog, int which
                                ) {
                                    try {
                                        MythDroid.beMgr.stopRecording(
                                            MythDroid.curProg
                                        );
                                    } catch (IOException e) {
                                        Util.err(ctx, e);
                                    }
                                    setResult(Recordings.REFRESH_NEEDED);
                                    dialog.dismiss();
                                    finish();
                                }
                            }
                        )
                        .setNegativeButton("No", no)
                        .create();
             default:
                 return super.onCreateDialog(id);

        }
        
    }

    private void setViews() {

        final Program prog = MythDroid.curProg;
        ((ImageView)findViewById(R.id.rec_thumb))
            .setImageBitmap(prog.previewImage());
        ((TextView)findViewById(R.id.rec_title)).setText(prog.Title);
        ((TextView)findViewById(R.id.rec_subtitle)).setText(prog.SubTitle);
        ((TextView)findViewById(R.id.rec_channel)).setText(prog.Channel);
        ((TextView)findViewById(R.id.rec_start)).setText(prog.startString());
        ((TextView)findViewById(R.id.rec_category))
            .setText("Type: " + prog.Category);
        ((TextView)findViewById(R.id.rec_status))
            .setText("Status: " + prog.Status.msg());
        ((TextView)findViewById(R.id.rec_desc)).setText(prog.Description);

        if (livetv) return;
        
        switch (prog.Status) {

            case RECORDING:
                final Button stop = (Button) findViewById(R.id.rec_stop);
                stop.setVisibility(View.VISIBLE);
                stop.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(STOP_DIALOG);
                        }
                    }
                );
                        
            case RECORDED:
            case CURRENT:
                final Button del = (Button) findViewById(R.id.rec_del);
                final Button play = (Button) findViewById(R.id.rec_play);

                del.setVisibility(View.VISIBLE);
                del.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(DELETE_DIALOG);
                        }
                    }
                );

                play.setVisibility(View.VISIBLE);
                play.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(
                                new Intent().setClass(ctx, TVRemote.class)
                            );
                        }
                    }
                );
                play.setOnLongClickListener(
                    new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            nextActivity = TVRemote.class;
                            showDialog(FRONTEND_CHOOSER);
                            return true;
                        }
                    }
                );

        }
    }
}