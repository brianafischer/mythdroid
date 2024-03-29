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

package org.mythdroid.remote;

import java.lang.ref.WeakReference;

import org.mythdroid.util.LogUtil;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

/**
 * A service that listens for accelerometer sensor events and wakes
 * the device if the device is moved
 */
public class WakeService extends Service implements SensorEventListener {

    /** Message value to send to start monitoring the sensor */
    final public static int MSG_START = 1;
    /** Message value to send to stop monitoring the sensor */
    final public static int MSG_STOP  = 2;
    
    final private static String tag   = "MythDroid";  //$NON-NLS-1$
    final private static int wakeTime = 10000; // ms
    
    final private static IntentFilter filter =
        new IntentFilter(Intent.ACTION_SCREEN_OFF);
    static {
        filter.addAction(Intent.ACTION_SCREEN_ON);
    }
     
    final private Messenger messenger = new Messenger(new MessageHandler(this));
    
    private SensorManager   sensorMgr = null;
    private Sensor          sensor = null;
    private PowerManager    pm = null;
    private KeyguardManager km = null;
    private KeyguardLock    kl = null;
    private WakeLock        partialLock = null;
    private float           last0 = 0, last1 = 0, last2 = 0;
    private boolean         isStarted = false;
    private long            startTime = 0;
    
    /**
     * Re-register the sensor event listener when the screen is turned off
     * to work around a bug in Android
     */
    private BroadcastReceiver screenStateReceiver = new BroadcastReceiver() {

        @SuppressLint("Wakelock")
		@Override
        public void onReceive(Context context, Intent intent) {

            sensorMgr.unregisterListener(WakeService.this);

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                if (partialLock.isHeld())
                    partialLock.release();
                return;
            }
           
            sensorMgr.registerListener(
                WakeService.this, sensor, SensorManager.SENSOR_DELAY_NORMAL
            );

            partialLock.acquire();

        }

    };
    
    private static class MessageHandler extends Handler {
        
        private final WeakReference<WakeService> target;
        
        MessageHandler(WakeService ws) {
            target = new WeakReference<WakeService>(ws);
        }
        
        @SuppressLint("Wakelock")
		@Override
        public synchronized void handleMessage(Message msg) {
            
            WakeService ws = target.get();
            
            switch (msg.what) {
             
                case MSG_START:
                    if (ws.isStarted) return;
                    LogUtil.debug("Start monitoring"); //$NON-NLS-1$
                    ws.registerReceiver(ws.screenStateReceiver, filter);
                    ws.sensorMgr.registerListener(
                        ws, ws.sensor,
                        SensorManager.SENSOR_DELAY_NORMAL
                    );
                    ws.partialLock.acquire();
                    ws.startTime = System.currentTimeMillis();
                    ws.isStarted = true;
                    break;
                
                case MSG_STOP:
                    if (!ws.isStarted) return;
                    LogUtil.debug("Stop monitoring"); //$NON-NLS-1$
                    if (ws.partialLock.isHeld())
                        ws.partialLock.release();
                    ws.unregisterReceiver(ws.screenStateReceiver);
                    ws.sensorMgr.unregisterListener(ws);
                    ws.isStarted = false;
                    break;
                    
                default:
                    
            }
        }
    }

    @Override
    public void onCreate() {

        super.onCreate();
        
        sensorMgr = (SensorManager)getApplicationContext()
                        .getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        kl = km.newKeyguardLock(tag);
        try {
            kl.disableKeyguard();
        } catch (SecurityException e) {
            LogUtil.error("Not allowed to disable keyguard"); //$NON-NLS-1$
        }
        
        pm = (PowerManager)getSystemService(POWER_SERVICE);
        partialLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
        
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isStarted) {
            unregisterReceiver(screenStateReceiver);
            sensorMgr.unregisterListener(WakeService.this);
            if (partialLock.isHeld())
                partialLock.release();
        }
        try {
            kl.reenableKeyguard();
        } catch (SecurityException e) {
            LogUtil.error("Not allowed to reenable keyguard"); //$NON-NLS-1$
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (!isStarted || event.values[2] == 0) return;

        if (last0 != 0 || last1 != 0 || last2 != 0)
            if (
                Math.abs(event.values[0] - last0) > 2 ||
                Math.abs(event.values[1] - last1) > 2 ||
                Math.abs(event.values[2] - last2) > 2
            )
                wakeUp();

        last0 = event.values[0];
        last1 = event.values[1];
        last2 = event.values[2];

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void wakeUp() {

        // Ignore wake ups that are so soon after going to sleep
        if (System.currentTimeMillis() < startTime + 500)
            return;
        
        LogUtil.debug("Waking up"); //$NON-NLS-1$
        pm.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
            PowerManager.ACQUIRE_CAUSES_WAKEUP,
            tag
        ).acquire(wakeTime);

    }

}
