/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab4pthtth;

/**
 *
 * @author User
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.StandardProtocolFamily;
import java.net.InetSocketAddress;
import java.util.Enumeration;

public class MulticastReceiver {

    private static final String GROUP_ADDRESS =
            "239.255.0.1";

    private static final int PORT = 5000;

    public static void main(String[] args) {

        try {

            InetAddress group =
                    InetAddress.getByName(
                            GROUP_ADDRESS
                    );

            NetworkInterface networkInterface =
                    findNetworkInterface();

            if (networkInterface == null) {

                System.out.println(
                        "Khong tim thay NetworkInterface IPv4."
                );

                return;
            }

            System.out.println(
                    "===== MULTICAST RECEIVER ====="
            );

            System.out.println(
                    "Group: " + GROUP_ADDRESS
            );

            System.out.println(
                    "Port: " + PORT
            );

            System.out.println(
                    "NetworkInterface: "
                    + networkInterface.getName()
            );

            /*
             * Tao MulticastSocket.
             */
            try (MulticastSocket socket =
                         new MulticastSocket(PORT)) {

                /*
                 * Chi dinh NetworkInterface.
                 */
                socket.setNetworkInterface(
                        networkInterface
                );

                /*
                 * Join group voi NetworkInterface
                 * cu the.
                 */
                socket.joinGroup(
                        new InetSocketAddress(
                                group,
                                PORT
                        ),
                        networkInterface
                );

                System.out.println(
                        "Da tham gia group."
                );

                System.out.println(
                        "Dang cho thong bao..."
                );

                byte[] buffer =
                        new byte[1024];

                DatagramPacket packet =
                        new DatagramPacket(
                                buffer,
                                buffer.length
                        );

                /*
                 * Cho nhan message.
                 */
                socket.receive(packet);

                String message =
                        new String(
                                packet.getData(),
                                packet.getOffset(),
                                packet.getLength(),
                                "UTF-8"
                        );

                System.out.println();
                System.out.println(
                        "Nhan duoc: " + message
                );

                System.out.println(
                        "Tu: "
                        + packet.getAddress()
                        + ":"
                        + packet.getPort()
                );

                /*
                 * ROI GROUP TRUOC KHI DONG SOCKET
                 */
                socket.leaveGroup(
                        new InetSocketAddress(
                                group,
                                PORT
                        ),
                        networkInterface
                );

                System.out.println(
                        "Da roi group."
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Loi Receiver: "
                    + e.getMessage()
            );

            System.out.println();
            System.out.println(
                    "Co the Multicast dang bi chan "
                    + "boi Wi-Fi, VPN hoac Firewall."
            );
        }
    }

    // =====================================================
    // TIM NETWORK INTERFACE
    // =====================================================

    private static NetworkInterface
    findNetworkInterface()
            throws SocketException {

        Enumeration<NetworkInterface> interfaces =
                NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {

            NetworkInterface ni =
                    interfaces.nextElement();

            if (!ni.isUp()
                    || ni.isLoopback()
                    || ni.isVirtual()) {

                continue;
            }

            Enumeration<InetAddress> addresses =
                    ni.getInetAddresses();

            while (addresses.hasMoreElements()) {

                InetAddress address =
                        addresses.nextElement();

                if (address instanceof Inet4Address) {

                    return ni;
                }
            }
        }

        return null;
    }
}