/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab4pthtth;


import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.security.MessageDigest;

public class FileServer {

    private static final int PORT = 10000;

    // Thư mục upload cố định
    private static final String UPLOAD_FOLDER = "upload";

    public static void main(String[] args) {

        System.out.println("===== FILE TCP SERVER =====");
        System.out.println("Server dang chay...");
        System.out.println("Port: " + PORT);

        // Tạo thư mục upload nếu chưa có
        File folder = new File(UPLOAD_FOLDER);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        try (ServerSocket serverSocket =
                     new ServerSocket(PORT)) {

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println();
                System.out.println(
                        "Client ket noi: "
                        + socket.getRemoteSocketAddress()
                );

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

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {

            File outputFile = null;

            try (
                    DataInputStream in =
                            new DataInputStream(
                                    new BufferedInputStream(
                                            socket.getInputStream()
                                    )
                            );

                    DataOutputStream out =
                            new DataOutputStream(
                                    new BufferedOutputStream(
                                            socket.getOutputStream()
                                    )
                            )
            ) {

                // =================================================
                // 1. NHAN TEN FILE
                // =================================================

                String fileName = in.readUTF();

                System.out.println(
                        "File name: " + fileName
                );

                // =================================================
                // 2. KIEM TRA TEN FILE
                // =================================================

                if (!isSafeFileName(fileName)) {

                    out.writeUTF(
                            "ERR INVALID_FILENAME"
                    );

                    out.flush();

                    System.out.println(
                            "Tu choi ten file khong an toan."
                    );

                    return;
                }

                // =================================================
                // 3. NHAN KICH THUOC FILE - KIEU LONG
                // =================================================

                long fileSize = in.readLong();

                System.out.println(
                        "File size: " + fileSize
                        + " bytes"
                );

                if (fileSize < 0) {

                    out.writeUTF(
                            "ERR INVALID_SIZE"
                    );

                    out.flush();

                    return;
                }

                // =================================================
                // 4. NHAN SHA-256
                // =================================================

                String clientHash =
                        in.readUTF().trim().toLowerCase();

                System.out.println(
                        "Client SHA-256: "
                        + clientHash
                );

                // SHA-256 phải có 64 ký tự hex
                if (!clientHash.matches("[0-9a-f]{64}")) {

                    out.writeUTF(
                            "ERR INVALID_HASH"
                    );

                    out.flush();

                    return;
                }

                // =================================================
                // 5. TAO FILE TRONG THU MUC UPLOAD CO DINH
                // =================================================

                Path uploadDir =
                        Paths.get(UPLOAD_FOLDER)
                                .toAbsolutePath()
                                .normalize();

                Files.createDirectories(uploadDir);

                // Chỉ lấy tên file, không sử dụng path client gửi
                Path outputPath =
                        uploadDir.resolve(fileName)
                                .normalize();

                // Kiểm tra lần nữa để chắc chắn file nằm trong
                // thư mục upload
                if (!outputPath.getParent()
                        .equals(uploadDir)) {

                    out.writeUTF(
                            "ERR INVALID_FILENAME"
                    );

                    out.flush();

                    return;
                }

                outputFile = outputPath.toFile();

                // =================================================
                // 6. DOC DUNG fileSize BYTE
                // =================================================

                MessageDigest digest =
                        MessageDigest.getInstance("SHA-256");

                try (
                        FileOutputStream fileOut =
                                new FileOutputStream(outputFile)
                ) {

                    byte[] buffer = new byte[8192];

                    long remaining = fileSize;

                    while (remaining > 0) {

                        int bytesToRead =
                                (int) Math.min(
                                        buffer.length,
                                        remaining
                                );

                        int bytesRead =
                                in.read(
                                        buffer,
                                        0,
                                        bytesToRead
                                );

                        // EOF trước khi đủ số byte
                        if (bytesRead == -1) {

                            throw new IOException(
                                    "Client gui thieu du lieu."
                            );
                        }

                        fileOut.write(
                                buffer,
                                0,
                                bytesRead
                        );

                        digest.update(
                                buffer,
                                0,
                                bytesRead
                        );

                        remaining -= bytesRead;
                    }
                }

                // =================================================
                // 7. TINH LAI SHA-256
                // =================================================

                String serverHash =
                        bytesToHex(
                                digest.digest()
                        );

                System.out.println(
                        "Server SHA-256: "
                        + serverHash
                );

                // =================================================
                // 8. SO SANH HASH
                // =================================================

                if (!serverHash.equalsIgnoreCase(
                        clientHash)) {

                    // Xóa file nếu hash không đúng
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }

                    out.writeUTF(
                            "ERR HASH_MISMATCH"
                    );

                    out.flush();

                    System.out.println(
                            "HASH KHONG KHOP."
                    );

                    return;
                }

                // =================================================
                // 9. THANH CONG
                // =================================================

                out.writeUTF("OK");
                out.flush();

                System.out.println(
                        "Upload thanh cong: "
                        + outputFile.getAbsolutePath()
                );

            } catch (EOFException e) {

                System.out.println(
                        "Client ngat truoc khi gui du du lieu."
                );

                if (outputFile != null
                        && outputFile.exists()) {

                    outputFile.delete();
                }

            } catch (Exception e) {

                System.out.println(
                        "Loi Server: "
                        + e.getMessage()
                );

                if (outputFile != null
                        && outputFile.exists()) {

                    outputFile.delete();
                }

            } finally {

                try {
                    socket.close();
                } catch (IOException e) {
                }

                System.out.println(
                        "Client da ngat ket noi."
                );
            }
        }

        // =====================================================
        // KIEM TRA TEN FILE AN TOAN
        // =====================================================

        private static boolean isSafeFileName(
                String fileName) {

            if (fileName == null
                    || fileName.isEmpty()) {

                return false;
            }

            // Không cho phép path
            if (fileName.contains("/")
                    || fileName.contains("\\")) {

                return false;
            }

            // Không cho phép . hoặc ..
            if (fileName.equals(".")
                    || fileName.equals("..")) {

                return false;
            }

            // Không cho phép chuỗi ..
            // để kiểm thử ../ bị từ chối
            if (fileName.contains("..")) {

                return false;
            }

            return true;
        }

        // =====================================================
        // BYTE[] -> HEX
        // =====================================================

        private static String bytesToHex(
                byte[] bytes) {

            StringBuilder result =
                    new StringBuilder();

            for (byte b : bytes) {

                result.append(
                        String.format(
                                "%02x",
                                b
                        )
                );
            }

            return result.toString();
        }
    }
}