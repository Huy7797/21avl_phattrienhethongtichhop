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
import java.util.Arrays;

public class CompareClient {

    private static final String HOST = "localhost";

    private static final int TCP_PORT = 8000;
    private static final int UDP_PORT = 8001;

    // Số message
    private static final int MESSAGE_COUNT = 1000;

    // Kích thước message
    private static final int MESSAGE_SIZE = 64;

    // Số lần thực nghiệm
    private static final int RUN_COUNT = 5;

    // UDP timeout
    private static final int UDP_TIMEOUT = 100;

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("      THUC NGHIEM TCP VA UDP");
        System.out.println("======================================");

        System.out.println("Moi truong: localhost");
        System.out.println("So message: " + MESSAGE_COUNT);
        System.out.println("Kich thuoc: " + MESSAGE_SIZE + " bytes");
        System.out.println("So lan lap: " + RUN_COUNT);
        System.out.println("UDP timeout: " + UDP_TIMEOUT + " ms");
        System.out.println("Phuong phap: System.nanoTime()");
        System.out.println();

        long totalTCPTime = 0;
        long totalUDPTime = 0;

        int totalTCPResponse = 0;
        int totalUDPResponse = 0;

        // =================================================
        // TCP
        // =================================================

        System.out.println("========== TCP ==========");

        for (int i = 1; i <= RUN_COUNT; i++) {

            Result result = testTCP();

            totalTCPTime += result.time;
            totalTCPResponse += result.responses;

            System.out.println(
                    "Lan " + i
                    + ": "
                    + formatTime(result.time)
                    + " | Phan hoi: "
                    + result.responses
                    + "/"
                    + MESSAGE_COUNT
            );
        }

        System.out.println();

        // =================================================
        // UDP
        // =================================================

        System.out.println("========== UDP ==========");

        for (int i = 1; i <= RUN_COUNT; i++) {

            Result result = testUDP();

            totalUDPTime += result.time;
            totalUDPResponse += result.responses;

            System.out.println(
                    "Lan " + i
                    + ": "
                    + formatTime(result.time)
                    + " | Phan hoi: "
                    + result.responses
                    + "/"
                    + MESSAGE_COUNT
            );
        }

        // =================================================
        // KET QUA TRUNG BINH
        // =================================================

        System.out.println();
        System.out.println("======================================");
        System.out.println("           KET QUA TRUNG BINH");
        System.out.println("======================================");

        double averageTCP =
                (double) totalTCPTime / RUN_COUNT;

        double averageUDP =
                (double) totalUDPTime / RUN_COUNT;

        double averageTCPResponse =
                (double) totalTCPResponse / RUN_COUNT;

        double averageUDPResponse =
                (double) totalUDPResponse / RUN_COUNT;

        System.out.println(
                "TCP:");
        System.out.println(
                "  Thoi gian TB: "
                + formatTime((long) averageTCP)
        );

        System.out.println(
                "  Phan hoi TB: "
                + averageTCPResponse
                + "/"
                + MESSAGE_COUNT
        );

        System.out.println();

        System.out.println(
                "UDP:");
        System.out.println(
                "  Thoi gian TB: "
                + formatTime((long) averageUDP)
        );

        System.out.println(
                "  Phan hoi TB: "
                + averageUDPResponse
                + "/"
                + MESSAGE_COUNT
        );

        System.out.println();

        System.out.println(
                "Khong ket luan UDP luon nhanh hon TCP "
                + "chi tu mot lan chay."
        );
    }

    // =====================================================
    // TCP TEST
    // =====================================================

    public static Result testTCP() {

        int responses = 0;

        byte[] message = createMessage();

        long start = System.nanoTime();

        try (
                Socket socket =
                        new Socket(HOST, TCP_PORT)
        ) {

            socket.setSoTimeout(1000);

            InputStream in =
                    socket.getInputStream();

            OutputStream out =
                    socket.getOutputStream();

            byte[] response =
                    new byte[MESSAGE_SIZE];

            for (int i = 0;
                 i < MESSAGE_COUNT;
                 i++) {

                // Gửi message
                out.write(message);
                out.flush();

                // Nhận response đủ 64 bytes
                int received = 0;

                while (received < MESSAGE_SIZE) {

                    int n = in.read(
                            response,
                            received,
                            MESSAGE_SIZE - received
                    );

                    if (n == -1) {
                        break;
                    }

                    received += n;
                }

                if (received == MESSAGE_SIZE) {
                    responses++;
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "TCP loi: " + e.getMessage()
            );
        }

        long end = System.nanoTime();

        return new Result(
                end - start,
                responses
        );
    }

    // =====================================================
    // UDP TEST
    // =====================================================

    public static Result testUDP() {

        int responses = 0;

        byte[] message = createMessage();

        try (
                DatagramSocket socket =
                        new DatagramSocket()
        ) {

            socket.setSoTimeout(UDP_TIMEOUT);

            InetAddress address =
                    InetAddress.getByName(HOST);

            long start = System.nanoTime();

            for (int i = 0;
                 i < MESSAGE_COUNT;
                 i++) {

                // Tạo packet gửi
                DatagramPacket request =
                        new DatagramPacket(
                                message,
                                message.length,
                                address,
                                UDP_PORT
                        );

                socket.send(request);

                // Packet nhận
                byte[] buffer =
                        new byte[MESSAGE_SIZE];

                DatagramPacket response =
                        new DatagramPacket(
                                buffer,
                                buffer.length
                        );

                try {

                    socket.receive(response);

                    if (response.getLength()
                            == MESSAGE_SIZE) {

                        responses++;
                    }

                } catch (SocketTimeoutException e) {

                    // Không nhận được phản hồi
                    // tiếp tục message kế tiếp
                }
            }

            long end = System.nanoTime();

            return new Result(
                    end - start,
                    responses
            );

        } catch (IOException e) {

            System.out.println(
                    "UDP loi: " + e.getMessage()
            );

            return new Result(0, responses);
        }
    }

    // =====================================================
    // TAO MESSAGE 64 BYTES
    // =====================================================

    public static byte[] createMessage() {

        byte[] message =
                new byte[MESSAGE_SIZE];

        Arrays.fill(message, (byte) 'A');

        return message;
    }

    // =====================================================
    // FORMAT TIME
    // =====================================================

    public static String formatTime(long nanoTime) {

        double milliseconds =
                nanoTime / 1_000_000.0;

        return String.format(
                "%.3f ms",
                milliseconds
        );
    }

    // =====================================================
    // RESULT CLASS
    // =====================================================

    static class Result {

        long time;
        int responses;

        public Result(long time, int responses) {

            this.time = time;
            this.responses = responses;
        }
    }
}