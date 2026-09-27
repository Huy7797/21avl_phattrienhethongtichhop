/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab4pthtth;



import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {

    private static final int PORT = 7001;

    // Thread pool
    private static final ExecutorService threadPool =
            Executors.newFixedThreadPool(20);

    // Cấu trúc thread-safe lưu danh sách client
    // Key = nickname
    // Value = ClientHandler
    private static final ConcurrentHashMap<String, ClientHandler> clients =
            new ConcurrentHashMap<>();

    public static void main(String[] args) {

        System.out.println("===== CHAT TCP SERVER =====");
        System.out.println("Server dang chay...");
        System.out.println("Port: " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {

                Socket socket = serverSocket.accept();

                System.out.println(
                        "Client ket noi: "
                        + socket.getInetAddress()
                        + ":"
                        + socket.getPort()
                );

                ClientHandler client = new ClientHandler(socket);

                // Đưa client vào thread pool
                threadPool.execute(client);
            }

        } catch (IOException e) {

            System.out.println("Loi Server: " + e.getMessage());

        } finally {

            threadPool.shutdown();
        }
    }

    // =====================================================
    // THEM CLIENT
    // =====================================================

    public static boolean addClient(String nickname,
                                    ClientHandler client) {

        // putIfAbsent đảm bảo thread-safe
        // Nếu nickname đã tồn tại thì không ghi đè
        return clients.putIfAbsent(nickname, client) == null;
    }

    // =====================================================
    // XOA CLIENT
    // =====================================================

    public static void removeClient(String nickname) {

        if (nickname != null) {
            clients.remove(nickname);
        }
    }

    // =====================================================
    // LAY DANH SACH USERS
    // =====================================================

    public static String getUsers() {

        List<String> usernames =
                new ArrayList<>(clients.keySet());

        Collections.sort(usernames);

        if (usernames.isEmpty()) {
            return "USERS";
        }

        return "USERS " + String.join(", ", usernames);
    }

    // =====================================================
    // BROADCAST
    // =====================================================

    public static void broadcast(String sender,
                                 String message) {

        String text = sender + ": " + message;

        for (ClientHandler client : clients.values()) {

            // Không gửi lại cho chính người gửi
            if (!client.getNickname().equals(sender)) {
                client.sendMessage(text);
            }
        }
    }

    // =====================================================
    // CLIENT HANDLER
    // =====================================================

    static class ClientHandler implements Runnable {

        private final Socket socket;

        private BufferedReader in;
        private PrintWriter out;

        private String nickname;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public String getNickname() {
            return nickname;
        }

        // Gửi message cho client
        public synchronized void sendMessage(String message) {

            if (out != null) {
                out.println(message);
            }
        }

        @Override
        public void run() {

            try {

                in = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                );

                out = new PrintWriter(
                        socket.getOutputStream(),
                        true
                );

                // =========================================
                // NHAN NICKNAME
                // =========================================

                out.println("ENTER_NICKNAME");

                while (true) {

                    String name = in.readLine();

                    // Client ngắt bất thường
                    if (name == null) {
                        return;
                    }

                    name = name.trim();

                    if (name.isEmpty()) {
                        out.println("ERR INVALID_NICKNAME");
                        continue;
                    }

                    // Không cho nickname chứa khoảng trắng
                    if (name.contains(" ")) {
                        out.println("ERR INVALID_NICKNAME");
                        continue;
                    }

                    // Kiểm tra nickname duy nhất
                    if (!addClient(name, this)) {

                        out.println("ERR NICKNAME_EXISTS");
                        continue;
                    }

                    nickname = name;

                    out.println("OK WELCOME " + nickname);

                    System.out.println(
                            "Client [" + nickname + "] da tham gia."
                    );

                    break;
                }

                // =========================================
                // NHAN LENH
                // =========================================

                String request;

                while ((request = in.readLine()) != null) {

                    request = request.trim();

                    if (request.isEmpty()) {
                        continue;
                    }

                    // -------------------------------------
                    // USERS
                    // -------------------------------------

                    if (request.equalsIgnoreCase("USERS")) {

                        out.println(getUsers());
                    }

                    // -------------------------------------
                    // MSG
                    // -------------------------------------

                    else if (request.regionMatches(
                            true, 0, "MSG ", 0, 4)) {

                        String message =
                                request.substring(4).trim();

                        if (message.isEmpty()) {

                            out.println("ERR INVALID_FORMAT");

                        } else {

                            System.out.println(
                                    "[" + nickname + "] "
                                    + message
                            );

                            broadcast(nickname, message);
                        }
                    }

                    // -------------------------------------
                    // QUIT
                    // -------------------------------------

                    else if (request.equalsIgnoreCase("QUIT")) {

                        out.println("BYE");

                        break;
                    }

                    // -------------------------------------
                    // LENH KHONG HOP LE
                    // -------------------------------------

                    else {

                        out.println("ERR INVALID_COMMAND");
                    }
                }

            } catch (IOException e) {

                System.out.println(
                        "Client [" + nickname
                        + "] ngat ket noi bat thuong."
                );

            } finally {

                // =========================================
                // CLEANUP
                // =========================================

                if (nickname != null) {

                    removeClient(nickname);

                    System.out.println(
                            "Da xoa client [" + nickname + "]"
                    );
                }

                try {

                    socket.close();

                } catch (IOException e) {
                    // Bo qua
                }
            }
        }
    }
}