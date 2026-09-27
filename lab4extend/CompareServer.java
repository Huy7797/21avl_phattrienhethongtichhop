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

public class CompareServer {

    private static final int TCP_PORT = 8000;
    private static final int UDP_PORT = 8001;

    public static void main(String[] args) {

        System.out.println("===== TCP / UDP TEST SERVER =====");

        Thread tcpThread = new Thread(() -> runTCPServer());
        Thread udpThread = new Thread(() -> runUDPServer());

        tcpThread.start();
        udpThread.start();
    }

    // =====================================================
    // TCP SERVER
    // =====================================================

    public static void runTCPServer() {

        try (ServerSocket serverSocket =
                     new ServerSocket(TCP_PORT)) {

            System.out.println(
                    "TCP Server dang lang nghe port "
                    + TCP_PORT
            );

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println(
                        "TCP Client ket noi: "
                        + socket.getInetAddress()
                );

                Thread clientThread = new Thread(() -> {

                    try {

                        InputStream in =
                                socket.getInputStream();

                        OutputStream out =
                                socket.getOutputStream();

                        byte[] buffer = new byte[64];

                        int bytesRead;

                        while ((bytesRead = in.read(buffer)) != -1) {

                            out.write(buffer, 0, bytesRead);
                            out.flush();
                        }

                    } catch (IOException e) {

                        System.out.println(
                                "TCP Client ngat ket noi."
                        );

                    } finally {

                        try {
                            socket.close();
                        } catch (IOException e) {
                        }
                    }

                });

                clientThread.start();
            }

        } catch (IOException e) {

            System.out.println(
                    "TCP Server loi: "
                    + e.getMessage()
            );
        }
    }

    // =====================================================
    // UDP SERVER
    // =====================================================

    public static void runUDPServer() {

        try (DatagramSocket socket =
                     new DatagramSocket(UDP_PORT)) {

            System.out.println(
                    "UDP Server dang lang nghe port "
                    + UDP_PORT
            );

            byte[] buffer = new byte[64];

            while (true) {

                DatagramPacket request =
                        new DatagramPacket(
                                buffer,
                                buffer.length
                        );

                socket.receive(request);

                DatagramPacket response =
                        new DatagramPacket(
                                request.getData(),
                                request.getLength(),
                                request.getAddress(),
                                request.getPort()
                        );

                socket.send(response);
            }

        } catch (IOException e) {

            System.out.println(
                    "UDP Server loi: "
                    + e.getMessage()
            );
        }
    }
}