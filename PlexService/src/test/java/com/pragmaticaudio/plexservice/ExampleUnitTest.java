package com.pragmaticaudio.plexservice;

import org.junit.Ignore;
import org.junit.Test;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@Ignore
public class ExampleUnitTest {
    @Test
    public void listen_udp_packets() {
        boolean running = true;
        int port = 32412;
        byte buffer[] = new byte[1024];

        try {
            DatagramSocket broadcastSocket = new DatagramSocket(port, InetAddress.getByName( "0.0.0.0"));
            broadcastSocket.setBroadcast(true);
            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                broadcastSocket.receive(packet);

                if (receivedFromEmulator(packet)) {
                    DatagramPacket packetToSend = new DatagramPacket(packet.getData(), packet.getLength(), InetAddress.getByName("0.0.0.0"), port);
                    broadcastSocket.send(packetToSend);
                } else if (isLocalhost(packet)) {
                    // Ignore
                } else {
                    // send to emulator
                    DatagramPacket packetToSend = new DatagramPacket(packet.getData(), packet.getLength(), InetAddress.getByName("10.0.2.2"), port);
                    broadcastSocket.send(packetToSend);
                }

                System.out.println("Received from " + packet.getAddress().getHostAddress());


                String received = new String(packet.getData(), 0, packet.getLength());

                // Handle the received message
                System.out.println("Received on port " + port + ": " + received);

                Thread.sleep(1000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean receivedFromEmulator(DatagramPacket packet) {
        return packet.getAddress().getHostAddress().equals("10.0.2.2");
    }

    private boolean isLocalhost(DatagramPacket packet) {
        String sourceAddress = packet.getAddress().getHostAddress();
        return sourceAddress.equals("127.0.0.1") || sourceAddress.equals("::1");
    }
}