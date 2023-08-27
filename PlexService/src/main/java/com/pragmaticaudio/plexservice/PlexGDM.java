package com.pragmaticaudio.plexservice;

import com.pragmaticaudio.plexservice.controllers.entities.PlexPlayerInfo;
import com.pragmaticaudio.plexservice.controllers.entities.PlexServerInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketOptions;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Simple class to handle the 3 alternative ways of using "Good Day Mate" Plex discovery protocol
 * 1. Discovery the plex server on the network
 * 2. Broadcast our availalbilty as a PlexPlayer on the network
 * 3. Simple respond to a broadcast from a plex server with our details
 *
 * Note: These methods will be needed to be wrapped in a thread
 */
public class PlexGDM {

    private static final String multicast = "239.0.0.250";
    private static final String searchPayload = "M-SEARCH * HTTP/1.1\r\n";

    private static final int serversBroadcastPort = 32414; // Port for listening to servers
    private static final int playerAvailabilityPort = 32412; // Port for broadcasting client availability
    private static final int multicastPort = 32413;

    private static final int localBroadcastPort = 50114;
    public static final String HTTP_OK_RESPONSE = "HTTP/1.0 200 OK";

    protected List<PlexServerInfo> searchForPlexServers(List<PlexServerInfo> listServers) throws IOException {

        DatagramSocket socket = new DatagramSocket(0);
        socket.setBroadcast(true);
        DatagramPacket packet = new DatagramPacket(searchPayload.getBytes(), searchPayload.length(),
                InetAddress.getByName(multicast), serversBroadcastPort);

        socket.send(packet);

        byte[] buf = new byte[256];
        packet = new DatagramPacket(buf, buf.length);
        socket.setSoTimeout(2000);  // 2 seconds timeout ?? - might add thread and increase timeout
        boolean listening = true;
        while (listening) {
            try {
                socket.receive(packet);
                String packetData = new String(packet.getData());
                if (packetData.contains(HTTP_OK_RESPONSE)) {
                    // Broadcast Received Packet
                    PlexServerInfo plexServerInfo = parsePacketData(packet.getAddress(), packetData);

                    listServers.add(plexServerInfo);
                }
            } catch (SocketTimeoutException e) {
                socket.close();
                listening = false;  // Try the enxt
            }
        }

        return listServers;
    }

    // Parse the plex server details from the packet
    private PlexServerInfo parsePacketData(InetAddress address, String packetData) {
        // Strip the header from the packet data
        packetData = packetData.substring(HTTP_OK_RESPONSE.length());

        return new PlexServerInfo(address, packetData);
    }

    private MulticastSocket multicastSocket = null;

    protected void broadcastAvailability(PlexPlayerInfo playerInfo) throws IOException {

        String ipAddress = Utils.getIPAddress(true);

        if (multicastSocket == null) {
            multicastSocket = new MulticastSocket(playerAvailabilityPort);

            multicastSocket.setReuseAddress(true);

            multicastSocket.setTimeToLive(255);

            InetAddress group = InetAddress.getByName(multicast);
            multicastSocket.joinGroup(group);

            // Non-blocking
            multicastSocket.setSoTimeout(0); // Setting timeout to 0 makes the socket non-blocking
        }

        String broadcastAvailabilityPayload = playerInfo.generateGDMHelloPayload();
        DatagramPacket packet = new DatagramPacket(broadcastAvailabilityPayload.getBytes(), broadcastAvailabilityPayload.length(),
                InetAddress.getByName("255.255.255.255"), playerAvailabilityPort);

        multicastSocket.send(packet);

    }


    protected void declareAvailability(PlexPlayerInfo playerInfo) throws IOException {

        // 1. Broadcast a hello on the playerAvailabilityPort - to get 'peer' players
        MulticastSocket  helloSocket = new MulticastSocket(playerAvailabilityPort);
        helloSocket.setReuseAddress(true);
        helloSocket.joinGroup(InetAddress.getByName(multicast));
        helloSocket.setBroadcast(true);

        String helloPayload = playerInfo.generateGDMHelloPayload();
        DatagramPacket helloPacket = new DatagramPacket(helloPayload.getBytes(), helloPayload.length(),
                InetAddress.getByName(multicast), multicastPort);

        helloSocket.send(helloPacket);

        // 2. Listen for responses to this packet on the playerAvailabilityPort
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        boolean listening = true;
        while (listening) {
            try {
                helloSocket.receive(packet);

                // 3. If we get a response - let see if its a peer player on the network
                String packetData = new String(packet.getData(), 0, packet.getLength());

                // If its not us and it is a search payload - broadcast our availability back directly
                if (!isLocalhost(packet) && (searchPayload.contains(packetData))) {

                    replyWithOurInfoToPeer(playerInfo, packet, helloSocket);
                }
            } catch (SocketTimeoutException e) {
                // No more packets so tidy up and return
                listening = false;
            }
        }
        helloSocket.close();

    }

    private static void replyWithOurInfoToPeer(PlexPlayerInfo playerInfo, DatagramPacket packet, DatagramSocket replySocket) throws IOException {
        // Send our details back to the sender on their port !!
        String broadcastAvailabilityPayload = playerInfo.generateGDMResponse();

        // Packet should contain our details and send directly Peer
        // Reply to their port
        DatagramPacket respsonsePacket = new DatagramPacket(broadcastAvailabilityPayload.getBytes(),
                broadcastAvailabilityPayload.length(), packet.getAddress(), packet.getPort());

        replySocket.send(respsonsePacket);
    }

    private boolean isLocalhost(DatagramPacket packet) {
        String sourceAddress = packet.getAddress().getHostAddress();
        return sourceAddress.equals("127.0.0.1") || sourceAddress.equals("::1");
    }
}
