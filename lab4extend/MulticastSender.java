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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Enumeration;

public class MulticastSender {

    private static final String GROUP_ADDRESS = "239.255.0.1";
    private static final int PORT = 5000;

    public static void main(String[] args) {

        String message;

        if (args.length > 0) {
            message = String.join(" ", args);
        } else {
            message = "Thong bao Multicast tu Sender";
        }

        try {

            InetAddress group =
                    InetAddress.getByName(GROUP_ADDRESS);

            /*
             * Tim NetworkInterface dang hoat dong
             * va co dia chi IPv4.
             */
            NetworkInterface networkInterface =
                    findNetworkInterface();

            if (networkInterface == null) {

                System.out.println(
                        "Khong tim thay NetworkInterface IPv4."
                );

                return;
            }

            System.out.println(
                    "===== MULTICAST SENDER ====="
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

            System.out.println(
                    "Message: " + message
            );

            /*
             * Tao socket de gui Multicast.
             */
            try (MulticastSocket socket =
                         new MulticastSocket()) {

                /*
                 * Chi dinh NetworkInterface
                 * cho socket.
                 */
                socket.setNetworkInterface(
                        networkInterface
                );

                byte[] data =
                        message.getBytes("UTF-8");

                DatagramPacket packet =
                        new DatagramPacket(
                                data,
                                data.length,
                                group,
                                PORT
                        );

                socket.send(packet);

                System.out.println(
                        "Da gui Multicast."
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Loi Sender: "
                    + e.getMessage()
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

            /*
             * Tim interface co IPv4.
             */
            var addresses =
                    ni.getInetAddresses();

            while (addresses.hasMoreElements()) {

                InetAddress address =
                        addresses.nextElement();

                if (address instanceof
                        java.net.Inet4Address) {

                    return ni;
                }
            }
        }

        return null;
    }
}