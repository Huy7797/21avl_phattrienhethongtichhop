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
import java.nio.file.*;
import java.security.MessageDigest;

public class FileClient {

    private static final String HOST = "localhost";
    private static final int PORT = 10000;

    public static void main(String[] args) {

        if (args.length < 1) {

            System.out.println(
                    "Cach dung:"
            );

            System.out.println(
                    "java FileClient <duong_dan_file>"
            );

            return;
        }

        String filePath = args[0];

        File file = new File(filePath);

        // =================================================
        // KIEM TRA FILE
        // =================================================

        if (!file.exists()) {

            System.out.println(
                    "ERR FILE_NOT_FOUND"
            );

            return;
        }

        if (!file.isFile()) {

            System.out.println(
                    "ERR NOT_A_FILE"
            );

            return;
        }

        long fileSize = file.length();

        String fileName = file.getName();

        try {

            // =================================================
            // TINH SHA-256 TRUOC KHI GUI
            // =================================================

            String sha256 =
                    calculateSHA256(file);

            System.out.println(
                    "===== FILE TCP CLIENT ====="
            );

            System.out.println(
                    "File: " + fileName
            );

            System.out.println(
                    "Size: " + fileSize + " bytes"
            );

            System.out.println(
                    "SHA-256: " + sha256
            );

            // =================================================
            // KET NOI SERVER
            // =================================================

            try (
                    Socket socket =
                            new Socket(HOST, PORT);

                    DataOutputStream out =
                            new DataOutputStream(
                                    new BufferedOutputStream(
                                            socket.getOutputStream()
                                    )
                            );

                    DataInputStream in =
                            new DataInputStream(
                                    new BufferedInputStream(
                                            socket.getInputStream()
                                    )
                            );

                    FileInputStream fileIn =
                            new FileInputStream(file)
            ) {

                System.out.println(
                        "Da ket noi Server."
                );

                // =================================================
                // 1. GUI TEN FILE
                // =================================================

                out.writeUTF(fileName);

                // =================================================
                // 2. GUI SIZE KIEU LONG
                // =================================================

                out.writeLong(fileSize);

                // =================================================
                // 3. GUI SHA-256
                // =================================================

                out.writeUTF(sha256);

                // =================================================
                // 4. GUI DU LIEU FILE
                // =================================================

                byte[] buffer =
                        new byte[8192];

                int bytesRead;

                while ((bytesRead =
                        fileIn.read(buffer)) != -1) {

                    out.write(
                            buffer,
                            0,
                            bytesRead
                    );
                }

                out.flush();

                System.out.println(
                        "Da gui xong file."
                );

                // =================================================
                // 5. NHAN KET QUA SERVER
                // =================================================

                String response =
                        in.readUTF();

                System.out.println(
                        "Server: " + response
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Loi Client: "
                    + e.getMessage()
            );

        } catch (Exception e) {

            System.out.println(
                    "Loi: "
                    + e.getMessage()
            );
        }
    }

    // =====================================================
    // TINH SHA-256
    // =====================================================

    public static String calculateSHA256(
            File file)
            throws Exception {

        MessageDigest digest =
                MessageDigest.getInstance(
                        "SHA-256"
                );

        try (
                InputStream in =
                        new BufferedInputStream(
                                new FileInputStream(file)
                        )
        ) {

            byte[] buffer =
                    new byte[8192];

            int bytesRead;

            while ((bytesRead =
                    in.read(buffer)) != -1) {

                digest.update(
                        buffer,
                        0,
                        bytesRead
                );
            }
        }

        return bytesToHex(
                digest.digest()
        );
    }

    // =====================================================
    // BYTE[] -> HEX
    // =====================================================

    public static String bytesToHex(
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