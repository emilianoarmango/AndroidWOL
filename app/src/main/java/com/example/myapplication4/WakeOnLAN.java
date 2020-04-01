package com.example.myapplication4;

import android.util.Log;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.net.InetAddress;

public class WakeOnLAN {

    public static final int DEFAULT_PORT = 3131;

    private static final String TAG = WakeOnLAN.class.getSimpleName();
    private static final String MAC_REGEX = "([0-9a-fA-F]{2}[-:]){5}[0-9a-fA-F]{2}";

    public static int sendPacket(String ipStr, String macStr) {
        return sendPacket(ipStr, macStr, DEFAULT_PORT);
    }

    public static int sendPacket(String ipStr, String macStr, int port) throws IllegalArgumentException {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Port must be in the range [0, 65535]. Magic packet is usually used on port 7 or 9");
        }

        byte[] macBytes = getMacBytes(macStr);
        byte[] bytes = new byte[6 + 16 * macBytes.length];

        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) 0xff;
            }
            for (int i = 6; i < bytes.length; i += macBytes.length) {
                System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
            }
            DatagramSocket socket = new DatagramSocket(port);

            InetAddress address = InetAddress.getByName(ipStr);
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
            socket.send(packet);
            socket.close();

            Log.d(TAG, "Wake-on-LAN packet sent.");

            return 0;
        } catch (Exception e) {
            Log.e(TAG, "Failed to send Wake-on-LAN packet:" + e);
            return -1;
        }
    }

    private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {

        if (!macStr.matches(MAC_REGEX)) {
            throw new IllegalArgumentException("Invalid MAC address");
        }

        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(:|\\-)");

        try {
            for (int i = 0; i < 6; i++) {
                bytes[i] = (byte) Integer.parseInt(hex[i], 16);
            }
        } catch (NumberFormatException e) {
            // Should not happen, the regex forbids it, but it doesn't compile otherwise.
            throw new IllegalArgumentException("Invalid hex digit in MAC address.");
        }
        return bytes;
    }
}