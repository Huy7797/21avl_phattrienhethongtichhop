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

public class TCPServer {

    public static void main(String[] args) {

        int port = 9000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server dang chay...");
            System.out.println("Dang lang nghe port " + port);

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println("Client da ket noi: "
                        + socket.getInetAddress());

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );

                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                );

                // Nhận yêu cầu từ Client
                String request = in.readLine();

                System.out.println("Client: " + request);

                // Xử lý yêu cầu
                String response = calculate(request);

                // Gửi kết quả về Client
                out.println(response);

                System.out.println("Server: " + response);

                socket.close();

                System.out.println("Client da ngat ket noi.");
                System.out.println();
            }

        } catch (IOException e) {

            System.out.println("Loi Server: " + e.getMessage());
        }
    }

    public static String calculate(String request) {

        // Kiểm tra request có rỗng hay không
        if (request == null || request.trim().isEmpty()) {
            return "ERR INVALID_FORMAT";
        }

        // Tách chuỗi bằng khoảng trắng
        String[] parts = request.trim().split("\\s+");

        // Phải có đúng 4 thành phần:
        // CALC operator number1 number2
        if (parts.length != 4) {
            return "ERR INVALID_FORMAT";
        }

        // Thành phần đầu tiên phải là CALC
        if (!parts[0].equalsIgnoreCase("CALC")) {
            return "ERR INVALID_FORMAT";
        }

        String operator = parts[1];

        double number1;
        double number2;

        // Chuyển toán hạng sang số
        try {

            number1 = Double.parseDouble(parts[2]);
            number2 = Double.parseDouble(parts[3]);

        } catch (NumberFormatException e) {

            return "ERR INVALID_NUMBER";
        }

        // Kiểm tra toán tử
        if (!operator.equals("+")
                && !operator.equals("-")
                && !operator.equals("*")
                && !operator.equals("/")) {

            return "ERR UNSUPPORTED_OPERATOR";
        }

        // Kiểm tra chia cho 0
        if (operator.equals("/") && number2 == 0) {
            return "ERR DIVIDE_BY_ZERO";
        }

        double result;

        switch (operator) {

            case "+":
                result = number1 + number2;
                break;

            case "-":
                result = number1 - number2;
                break;

            case "*":
                result = number1 * number2;
                break;

            case "/":
                result = number1 / number2;
                break;

            default:
                return "ERR UNSUPPORTED_OPERATOR";
        }

        // Nếu kết quả là số nguyên thì in không có .0
        if (result == (long) result) {
            return "OK " + (long) result;
        }

        return "OK " + result;
    }
}
