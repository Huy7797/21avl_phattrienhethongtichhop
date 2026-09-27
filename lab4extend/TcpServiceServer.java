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

public class TcpServiceServer {

    private static final int TCP_PORT = 11000;

    public static void main(String[] args) {

        System.out.println("===== TCP SERVICE SERVER =====");
        System.out.println(
                "TCP Server dang chay port "
                + TCP_PORT
        );

        try (ServerSocket serverSocket =
                     new ServerSocket(TCP_PORT)) {

            while (true) {

                Socket socket =
                        serverSocket.accept();

                System.out.println(
                        "Client TCP ket noi: "
                        + socket.getRemoteSocketAddress()
                );

                Thread thread =
                        new Thread(
                                new ClientHandler(socket)
                        );

                thread.start();
            }

        } catch (IOException e) {

            System.out.println(
                    "Loi TCP Server: "
                    + e.getMessage()
            );
        }
    }

    static class ClientHandler
            implements Runnable {

        private final Socket socket;

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

                out.println(
                        "WELCOME TO TCP SERVICE"
                );

                String message;

                while ((message = in.readLine())
                        != null) {

                    System.out.println(
                            "Client: " + message
                    );

                    if (message.equalsIgnoreCase(
                            "QUIT")) {

                        out.println("BYE");
                        break;
                    }

                    out.println(
                            "TCP SERVER: " + message
                    );
                }

            } catch (IOException e) {

                System.out.println(
                        "Client TCP ngat ket noi."
                );

            } finally {

                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}