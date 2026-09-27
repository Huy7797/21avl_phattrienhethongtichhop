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

public class TCPClient {

    public static void main(String[] args) {

        String host = "localhost";
        int port = 9000;

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== TCP CLIENT =====");
        System.out.println("Ket noi den Server "
                + host + ":" + port);

        try (
                Socket socket = new Socket(host, port);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );

                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                )
        ) {

            System.out.println("Da ket noi Server.");

            System.out.print("Nhap yeu cau: ");

            String request = scanner.nextLine();

            // Gửi yêu cầu cho Server
            out.println(request);

            // Nhận kết quả
            String response = in.readLine();

            System.out.println("Server tra ve: " + response);

        } catch (IOException e) {

            System.out.println("Khong the ket noi Server.");
            System.out.println("Chi tiet: " + e.getMessage());
        }

        scanner.close();
    }
}
