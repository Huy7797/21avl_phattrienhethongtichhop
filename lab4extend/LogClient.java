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

public class LogClient {

    private static final String HOST = "localhost";
    private static final int PORT = 2000;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try (
                Socket socket =
                        new Socket(HOST, PORT);

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
                    "===== LOG CLIENT ====="
            );

            System.out.println(
                    "Da ket noi Server "
                    + HOST + ":" + PORT
            );

            // =========================================
            // NHAP CLIENT ID
            // =========================================

            System.out.print(
                    "Nhap clientId: "
            );

            String clientId =
                    scanner.nextLine().trim();

            // =========================================
            // GUI HELLO
            // =========================================

            out.println(
                    "HELLO " + clientId
            );

            String response =
                    in.readLine();

            System.out.println(
                    "Server: " + response
            );

            if (!"OK HELLO".equals(response)) {

                return;
            }

            // =========================================
            // GUI TIN NHAN
            // =========================================

            System.out.println();
            System.out.println(
                    "Nhap tin nhan."
            );

            System.out.println(
                    "Nhap QUIT de thoat."
            );

            while (true) {

                System.out.print("> ");

                String message =
                        scanner.nextLine();

                out.println(message);

                response =
                        in.readLine();

                System.out.println(
                        "Server: " + response
                );

                if (message.equalsIgnoreCase("QUIT")) {
                    break;
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Loi Client: "
                    + e.getMessage()
            );

        } finally {

            scanner.close();
        }
    }
}