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
import java.util.Scanner;

public class ChatClient {

    private static final String HOST = "localhost";
    private static final int PORT = 7001;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try (
                Socket socket = new Socket(HOST, PORT);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                );

                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(),
                        true
                )
        ) {

            System.out.println("===== CHAT TCP CLIENT =====");
            System.out.println(
                    "Da ket noi Server "
                    + HOST + ":" + PORT
            );

            // =========================================
            // NHAN YEU CAU NICKNAME
            // =========================================

            String serverMessage = in.readLine();

            if (!"ENTER_NICKNAME".equals(serverMessage)) {

                System.out.println(
                        "Loi: " + serverMessage
                );

                return;
            }

            // =========================================
            // CHON NICKNAME
            // =========================================

            while (true) {

                System.out.print("Nhap nickname: ");

                String nickname = scanner.nextLine();

                out.println(nickname);

                String response = in.readLine();

                System.out.println("Server: " + response);

                if (response != null
                        && response.startsWith("OK WELCOME")) {

                    break;
                }
            }

            // =========================================
            // THREAD NHAN TIN NHAN
            // =========================================

            Thread receiver = new Thread(() -> {

                try {

                    String message;

                    while ((message = in.readLine()) != null) {

                        System.out.println();
                        System.out.println(message);
                        System.out.print("> ");
                    }

                } catch (IOException e) {

                    System.out.println(
                            "\nServer da ngat ket noi."
                    );
                }
            });

            receiver.setDaemon(true);
            receiver.start();

            // =========================================
            // GUI PHIA CLIENT
            // =========================================

            System.out.println();
            System.out.println("Cac lenh:");
            System.out.println("USERS");
            System.out.println("MSG noi_dung");
            System.out.println("QUIT");
            System.out.println();

            while (true) {

                System.out.print("> ");

                String command = scanner.nextLine();

                out.println(command);

                if (command.equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Khong the ket noi Server."
            );

            System.out.println(
                    "Chi tiet: " + e.getMessage()
            );

        } finally {

            scanner.close();
        }
    }
}