package org.mythdroid.frontend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.mythdroid.mdd.MDDManager;
import org.mythdroid.util.DatabaseUtil;

import android.annotation.SuppressLint;
import android.content.Context;

/** Format and display messages on the MythTV OSD */
final public class OSDMessage {

    final private static String alert =
        "<mythnotify version=\"1\">\n" + //$NON-NLS-1$
        "  <container name=\"notify_alert_text\">\n" + //$NON-NLS-1$
        "    <textarea name=\"notify_text\">\n" + //$NON-NLS-1$
        "      <value>%alert_text%</value>\n" + //$NON-NLS-1$
        "    </textarea>\n" + //$NON-NLS-1$
        "  </container>\n" + //$NON-NLS-1$
        "</mythnotify>";  //$NON-NLS-1$

    final private static String scroll =
        "<mythnotify version=\"1\" displaytime=\"-1\">\n" + //$NON-NLS-1$
        "  <container name=\"news_scroller\">\n" + //$NON-NLS-1$
        "    <textarea name=\"text_scroll\">\n" + //$NON-NLS-1$
        "      <value>%scroll_text%</value>\n" + //$NON-NLS-1$
        "    </textarea>\n" + //$NON-NLS-1$
        "  </container>\n" + //$NON-NLS-1$
        "</mythnotify>"; //$NON-NLS-1$

    final private static String cid =
        "<mythnotify version=\"1\">\n" + //$NON-NLS-1$
        "  <container name=\"notify_cid_info\">\n" + //$NON-NLS-1$
        "    <textarea name=\"notify_cid_name\">\n" + //$NON-NLS-1$
        "      <value>NAME: %caller_name% </value>\n" + //$NON-NLS-1$
        "    </textarea>\n" + //$NON-NLS-1$
        "    <textarea name=\"notify_cid_num\">\n" + //$NON-NLS-1$
        "      <value>NUM : %caller_number%</value>\n" + //$NON-NLS-1$
        "    </textarea>\n" + //$NON-NLS-1$
        "  </container>\n" + //$NON-NLS-1$
        "</mythnotify>";  //$NON-NLS-1$

    /** SimpleDateFormat of EEE d MMM yy */
    @SuppressLint("SimpleDateFormat")
	final private static SimpleDateFormat dateFmt =
        new SimpleDateFormat("EEE d MMM yy"); //$NON-NLS-1$
    /** SimpleDateFormat of HH:mm */
    @SuppressLint("SimpleDateFormat")
	final private static SimpleDateFormat timeFmt =
        new SimpleDateFormat("HH:mm"); //$NON-NLS-1$

    static {
        timeFmt.setTimeZone(TimeZone.getDefault());
        dateFmt.setTimeZone(TimeZone.getDefault());
    }
    
    // Broadcast address (255.255.255.255)
    private static InetAddress address;
    static {
        try {
            address = InetAddress.getByAddress(new byte[] { -1, -1, -1, -1 });
        } catch (UnknownHostException e) {}
    }

    /**
     * Send an 'alert' message for display on OSD
     * @param message String containing message to display
     */
    public static void Alert(String message) throws IOException {
        String msg = alert.replace("%alert_text%", message); //$NON-NLS-1$
        send(msg);
    }

    /**
     * Send a 'scrolling' message for display on the OSD
     * @param message String containing the message
     * @param displaytime for display, in seconds
     */
    public static void Scroller(String message, int displaytime)
        throws IOException {
        String msg = scroll.replace("%scroll_text%", message); //$NON-NLS-1$
        if (displaytime != 1)
            msg = msg.replaceFirst("-1", String.valueOf(displaytime)); //$NON-NLS-1$
        send(msg);
    }

    /**
     * Send a 'callerid' message for display on the OSD
     * @param name String containing the name of the caller
     * @param number String containing the number of the caller
     */
    public static void Caller(String name, String number) throws IOException {
        String msg = cid.replace("%caller_name%", name); //$NON-NLS-1$
        msg = msg.replace("%caller_number%", number); //$NON-NLS-1$
        send(msg);
    }
    
    /**
     * Display a message via XOSD (via MDD)
     * @param ctx Context
     * @param msg Message to display
     */
    public static void XOSD(Context ctx, String msg) {
        for (String fe : DatabaseUtil.getFrontendNames(ctx))
            try {
                MDDManager.osdMsg(
                    DatabaseUtil.getFrontendAddr(ctx, fe), msg
                );
            } catch (IOException e) {}
    }

    private static void send(String message) throws IOException {
        byte[] buf = message.getBytes("utf8"); //$NON-NLS-1$
        DatagramPacket dgram =
            new DatagramPacket(buf, buf.length, address, 6948);
        DatagramSocket sock = new DatagramSocket();
        sock.setBroadcast(true);
        sock.send(dgram);
    }

}
