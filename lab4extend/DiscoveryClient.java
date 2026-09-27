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
import java.util.*;

public class DiscoveryClient {

    private static final int UDP_PORT = 10001;

    private static final int TIMEOUT = 1000;

    private static final int DISCOVERY_TIME = 3000;

    public static void main(String[] args) {

        System.out.println(
                "===== UDP DISCOVERY CLIENT ====="
        );

        System.out.println(
                "Discovery message: DISCOVER_SERVICE"
        );

        System.out.println(
                "UDP Port: " + UDP_PORT
        );

        System.out.println(
                "Timeout moi lan: "
                + TIMEOUT + " ms"
        );

        System.out.println(
                "Thoi gian discovery: "
                + DISCOVERY_TIME + " ms"
        );

        try {

            InetAddress broadcastAddress =
                    InetAddress.getByName(
                            "255.255.255.255"
                    );

            /*
             * UDP socket
             */
            try (DatagramSocket socket =
                         new DatagramSocket()) {

                /*
                 * Cho phep broadcast.
                 */
                socket.setBroadcast(true);

                /*
                 * Timeout 1 giay.
                 */
                socket.setSoTimeout(TIMEOUT);

                // =========================================
                // GUI DISCOVER_SERVICE
                // =========================================

                String request =
                        "DISCOVER_SERVICE";

                byte[] data =
                        request.getBytes("UTF-8");

                DatagramPacket packet =
                        new DatagramPacket(
                                data,
                                data.length,
                                broadcastAddress,
                                UDP_PORT
                        );

                socket.send(packet);

                System.out.println();
                System.out.println(
                        "Da gui DISCOVER_SERVICE."
                );

                // =========================================
                // NHAN RESPONSE
                // =========================================

                long start =
                        System.currentTimeMillis();

                /*
                 * HashSet dùng để loại response trùng.
                 *
                 * Key:
                 * IP + TCP port + service + version
                 */
                Set<String> discovered =
                        new LinkedHashSet<>();

                List<ServerInfo> servers =
                        new ArrayList<>();

                while (
                        System.currentTimeMillis()
                        - start
                        < DISCOVERY_TIME
                ) {

                    byte[] buffer =
                            new byte[1024];

                    DatagramPacket response =
                            new DatagramPacket(
                                    buffer,
                                    buffer.length
                            );

                    try {

                        socket.receive(response);

                    } catch (SocketTimeoutException e) {

                        // Timeout thì tiếp tục chờ
                        continue;
                    }

                    String message =
                            new String(
                                    response.getData(),
                                    response.getOffset(),
                                    response.getLength(),
                                    "UTF-8"
                            ).trim();

                    System.out.println(
                            "Nhan response tu "
                            + response.getAddress()
                            + ":"
                            + response.getPort()
                            + " -> "
                            + message
                    );

                    // =====================================
                    // PHAN TICH SERVICE RESPONSE
                    // =====================================

                    String[] parts =
                            message.split("\\s+");

                    /*
                     * Dạng:
                     *
                     * SERVICE ChatService 11000 1.0
                     */
                    if (parts.length != 4) {

                        continue;
                    }

                    if (!parts[0].equals(
                            "SERVICE")) {

                        continue;
                    }

                    String serviceName =
                            parts[1];

                    int tcpPort;

                    try {

                        tcpPort =
                                Integer.parseInt(
                                        parts[2]
                                );

                    } catch (NumberFormatException e) {

                        continue;
                    }

                    String version =
                            parts[3];

                    /*
                     * QUAN TRONG:
                     *
                     * Lấy IP từ địa chỉ nguồn
                     * của UDP response.
                     */
                    String sourceIP =
                            response.getAddress()
                                    .getHostAddress();

                    /*
                     * Tạo key để loại trùng.
                     */
                    String key =
                            sourceIP
                            + ":"
                            + tcpPort
                            + ":"
                            + serviceName
                            + ":"
                            + version;

                    if (discovered.add(key)) {

                        ServerInfo server =
                                new ServerInfo(
                                        sourceIP,
                                        serviceName,
                                        tcpPort,
                                        version
                                );

                        servers.add(server);
                    }
                }

                // =========================================
                // HIEN THI CAC SERVER
                // =========================================

                System.out.println();
                System.out.println(
                        "===== DISCOVERY RESULT ====="
                );

                if (servers.isEmpty()) {

                    System.out.println(
                            "Khong tim thay server."
                    );

                    return;
                }

                for (int i = 0;
                     i < servers.size();
                     i++) {

                    ServerInfo server =
                            servers.get(i);

                    System.out.println(
                            (i + 1)
                            + ". "
                            + server
                    );
                }

                // =========================================
                // KET NOI TCP SERVER DA TIM THAY
                // =========================================

                ServerInfo selected =
                        servers.get(0);

                System.out.println();
                System.out.println(
                        "Ket noi TCP toi:"
                );

                System.out.println(
                        selected.host
                        + ":"
                        + selected.tcpPort
                );

                connectTCP(selected);
            }

        } catch (IOException e) {

            System.out.println(
                    "Loi Discovery Client: "
                    + e.getMessage()
            );
        }
    }

    // =====================================================
    // TCP CONNECTION
    // =====================================================

    private static void connectTCP(
            ServerInfo server) {

        try (
                Socket socket =
                        new Socket(
                                server.host,
                                server.tcpPort
                        );

                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(
                                        socket.getInputStream()
                                )
                        );

                PrintWriter out =
                        new PrintWriter(
                                socket.getOutputStream(),
                                true
                        )
        ) {

            System.out.println(
                    "TCP da ket noi."
            );

            String welcome =
                    in.readLine();

            System.out.println(
                    "Server: " + welcome
            );

            out.println(
                    "Hello from Discovery Client"
            );

            String response =
                    in.readLine();

            System.out.println(
                    "Server: " + response
            );

            out.println("QUIT");

            response =
                    in.readLine();

            System.out.println(
                    "Server: " + response
            );

        } catch (IOException e) {

            System.out.println(
                    "Khong the ket noi TCP Server: "
                    + e.getMessage()
            );
        }
    }

    // =====================================================
    // SERVER INFO
    // =====================================================

    static class ServerInfo {

        String host;
        String serviceName;
        int tcpPort;
        String version;

        public ServerInfo(
                String host,
                String serviceName,
                int tcpPort,
                String version) {

            this.host = host;
            this.serviceName = serviceName;
            this.tcpPort = tcpPort;
            this.version = version;
        }

        @Override
        public String toString() {

            return "Host="
                    + host
                    + ", Service="
                    + serviceName
                    + ", TCP Port="
                    + tcpPort
                    + ", Version="
                    + version;
        }
    }
}