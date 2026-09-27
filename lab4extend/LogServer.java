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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogServer {

    private static final int PORT = 2000;

    // Thư mục lưu log
    private static final String LOG_FOLDER = "data";

    // Định dạng timestamp
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {

        System.out.println("===== LOG SERVER =====");
        System.out.println("Server dang chay...");
        System.out.println("Port: " + PORT);

        // Tạo thư mục data nếu chưa có
        File folder = new File(LOG_FOLDER);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (ServerSocket serverSocket =
                     new ServerSocket(PORT)) {

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println(
                        "Client ket noi: "
                        + socket.getRemoteSocketAddress()
                );

                // Mỗi client chạy một thread
                Thread clientThread =
                        new Thread(
                                new ClientHandler(socket)
                        );

                clientThread.start();
            }

        } catch (IOException e) {

            System.out.println(
                    "Loi Server: " + e.getMessage()
            );
        }
    }

    // =====================================================
    // CLIENT HANDLER
    // =====================================================

    static class ClientHandler implements Runnable {

        private final Socket socket;

        private String clientId;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            try (
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

                // =========================================
                // NHAN HELLO
                // =========================================

                String hello = in.readLine();

                if (hello == null) {
                    return;
                }

                // Phải có dạng:
                // HELLO clientId

                if (!hello.startsWith("HELLO ")) {

                    out.println("ERR INVALID_HELLO");
                    return;
                }

                clientId = hello.substring(6).trim();

                if (clientId.isEmpty()) {

                    out.println("ERR INVALID_CLIENT_ID");
                    return;
                }

                // Kiểm tra clientId để tránh ký tự nguy hiểm
                if (!clientId.matches("[a-zA-Z0-9_-]+")) {

                    out.println("ERR INVALID_CLIENT_ID");
                    return;
                }

                System.out.println(
                        "Client ID: " + clientId
                );

                System.out.println(
                        "Remote: "
                        + socket.getRemoteSocketAddress()
                );

                out.println("OK HELLO");

                // =========================================
                // NHAN NOI DUNG
                // =========================================

                String message;

                while ((message = in.readLine()) != null) {

                    if (message.equalsIgnoreCase("QUIT")) {

                        out.println("BYE");
                        break;
                    }

                    // Lưu tin nhắn
                    saveLog(message);

                    System.out.println(
                            "[" + clientId + "] "
                            + message
                    );

                    out.println("OK SAVED");
                }

            } catch (IOException e) {

                System.out.println(
                        "Client bi ngat: "
                        + socket.getRemoteSocketAddress()
                );

            } finally {

                try {
                    socket.close();
                } catch (IOException e) {
                }

                System.out.println(
                        "Client [" + clientId
                        + "] da ngat ket noi."
                );
            }
        }

        // =================================================
        // LUU LOG
        // =================================================

        private void saveLog(String message)
                throws IOException {

            String timestamp =
                    LocalDateTime.now()
                            .format(FORMATTER);

            String remoteAddress =
                    socket.getRemoteSocketAddress()
                            .toString();

            String line =
                    timestamp
                    + " | "
                    + remoteAddress
                    + " | "
                    + message;

            String fileName =
                    LOG_FOLDER
                    + File.separator
                    + clientId
                    + ".txt";

            // true = append, không xóa log cũ
            try (FileWriter writer =
                         new FileWriter(fileName, true);

                 BufferedWriter buffer =
                         new BufferedWriter(writer)) {

                buffer.write(line);
                buffer.newLine();
            }
        }
    }
}