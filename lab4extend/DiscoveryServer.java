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

import java.io.*;
import java.net.*;

public class DiscoveryServer {

    private static final int UDP_PORT = 10001;

    private static final String SERVICE_NAME = "ChatService";

    private static final int TCP_PORT = 11000;

    private static final String VERSION = "1.0";

    public static void main(String[] args) {

        System.out.println("===== UDP DISCOVERY SERVER =====");
        System.out.println("UDP Port: " + UDP_PORT);
        System.out.println("TCP Service Port: " + TCP_PORT);

        try (DatagramSocket socket =
                     new DatagramSocket(UDP_PORT)) {

            byte[] buffer = new byte[1024];

            while (true) {

                DatagramPacket request =
                        new DatagramPacket(
                                buffer,
                                buffer.length
                        );

                socket.receive(request);

                String message =
                        new String(
                                request.getData(),
                                request.getOffset(),
                                request.getLength(),
                                "UTF-8"
                        ).trim();

                System.out.println(
                        "Nhan tu "
                        + request.getAddress()
                        + ":"
                        + request.getPort()
                        + " -> "
                        + message
                );

                // Chỉ xử lý DISCOVER_SERVICE
                if (message.equals(
                        "DISCOVER_SERVICE")) {

                    String response =
                            "SERVICE "
                            + SERVICE_NAME
                            + " "
                            + TCP_PORT
                            + " "
                            + VERSION;

                    byte[] data =
                            response.getBytes("UTF-8");

                    DatagramPacket responsePacket =
                            new DatagramPacket(
                                    data,
                                    data.length,
                                    request.getAddress(),
                                    request.getPort()
                            );

                    socket.send(responsePacket);

                    System.out.println(
                            "Tra loi: " + response
                    );
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Loi Discovery Server: "
                    + e.getMessage()
            );
        }
    }
}