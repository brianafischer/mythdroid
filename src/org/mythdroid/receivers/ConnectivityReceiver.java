package org.mythdroid.receivers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.mythdroid.util.ConnMgr;
import org.mythdroid.util.LogUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;

/**
 * A BroadcastReceiver for connectivity events
 */
public class ConnectivityReceiver extends BroadcastReceiver {

    static private boolean connected = false;
    static private int netType = -1;

    /**
     * Register a new ConnectivityReceiver to monitor and act upon
     * connectivity changes
     * @param ctx Context
     */
    public ConnectivityReceiver(Context ctx) {

        final ConnectivityManager cm =
            (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null) {
            connected = (info.getState() == NetworkInfo.State.CONNECTED);
            netType = info.getType();
        }

        final IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        ctx.registerReceiver(this, filter);

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION))
            return;

        final NetworkInfo info = (NetworkInfo)intent.getExtras().get(
            ConnectivityManager.EXTRA_NETWORK_INFO
        );

        int type = info.getType();
        State state = info.getState();

        LogUtil.debug(
            "wasConnected " + connected + " type " + info.getTypeName() + //$NON-NLS-1$ //$NON-NLS-2$
            " state " + state //$NON-NLS-1$
        );

        // Ignore irrelevant connectivity changes
        if (
            // We already knew we were connected via this type
            (connected && state == State.CONNECTED && netType == type)    ||
            // We don't care about disconnections if we're not using that type
            (connected && state == State.DISCONNECTED && type != netType)
        )
            return;

        switch (state) {
            case DISCONNECTED:
                LogUtil.debug("Disconnected"); //$NON-NLS-1$
                try {
                    ConnMgr.disconnectAll();
                } catch (IOException e) {}
                connected = false;
                break;
            case CONNECTED:
                boolean typeChanged = netType != type;
                netType = type;
                connected = true;
                if (!typeChanged) {
                    LogUtil.debug("Reconnected"); //$NON-NLS-1$
                    // We've regained connectivity - attempt to reconnect
                    ConnMgr.reconnectAll();
                    break;
                }
                LogUtil.debug("Connected with change of type"); //$NON-NLS-1$
                // Change of connectivity type, disconnect
                try {
                    ConnMgr.disconnectAll();
                } catch (IOException e) {}
        }

    }

    /**
     * Unregister the broadcast receiver
     * @param ctx Context to unregister from
     */
    public void dispose(Context ctx) {
        ctx.unregisterReceiver(this);
    }

    /**
     * Get the current network type (WiFi or mobile)
     * @return ConnectivityManager.TYPE_WIFI or ConnectivityManager.TYPE_MOBILE
     */
    public static int networkType() {
        return netType;
    }

    /**
     * If a WiFi connection is being established wait for it to complete
     * @param timeout maximum wait time in milliseconds
     */
    public static void waitForWifi(Context ctx, int timeout) {

        if (ctx == null) return;
        
        final ConnectivityManager cm =
            (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        final WifiManager wm =
            (WifiManager)
                ctx.getSystemService(Context.WIFI_SERVICE);

        NetworkInfo winfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (winfo == null) return;
        
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return;
        
        connected = (info.getState() == NetworkInfo.State.CONNECTED);
        netType = info.getType();
        
        int wifiState = wm.getWifiState();

        if (
            wifiState == WifiManager.WIFI_STATE_ENABLED ||
            wifiState == WifiManager.WIFI_STATE_ENABLING
        ) {

            final Thread thisThread = Thread.currentThread();
            final Timer timer = new Timer();
            
            timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        thisThread.interrupt();
                    }
                }, timeout
            );

            while (
                wifiState == WifiManager.WIFI_STATE_ENABLING     ||
                winfo.getState() == NetworkInfo.State.CONNECTING
            ) {

                LogUtil.debug("Waiting for WiFi link"); //$NON-NLS-1$

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    timer.cancel();
                    return; 
                }

                winfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                wifiState = wm.getWifiState();

            }

            while (
                netType != ConnectivityManager.TYPE_WIFI || connected == false
            ) {

                LogUtil.debug("Waiting for WiFi connection"); //$NON-NLS-1$

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    timer.cancel();
                    return; 
                }

            }
            
            timer.cancel();

        }

    }

}
