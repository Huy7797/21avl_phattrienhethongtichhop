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
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;

public class HostURIInspector {

    public static void main(String[] args) {

        // Kiểm tra đủ 2 tham số
        if (args.length < 2) {
            System.out.println("Loi: Thieu tham so.");
            System.out.println("Cach dung:");
            System.out.println("java HostURIInspector <hostname> <URI>");
            return;
        }

        String hostname = args[0];
        String uriString = args[1];

        // ==============================
        // PHAN 1: HOSTNAME INSPECTOR
        // ==============================

        System.out.println("===== HOST INSPECTOR =====");
        System.out.println("Hostname: " + hostname);

        try {
            InetAddress[] addresses =
                    InetAddress.getAllByName(hostname);

            System.out.println("So luong IP: " + addresses.length);

            for (InetAddress address : addresses) {

                String ip = address.getHostAddress();

                System.out.println("IP: " + ip);

                // Kiểm tra IPv4 / IPv6
                if (address instanceof java.net.Inet4Address) {
                    System.out.println("Loai: IPv4");
                } else if (address instanceof java.net.Inet6Address) {
                    System.out.println("Loai: IPv6");
                }

                // Kiểm tra loopback
                System.out.println(
                        "Loopback: " + address.isLoopbackAddress()
                );

                // Kiểm tra site local
                System.out.println(
                        "Site local: " + address.isSiteLocalAddress()
                );

                System.out.println("-------------------------");
            }

        } catch (Exception e) {

            System.out.println(
                    "Loi: Khong the phan giai hostname."
            );
        }

        // ==============================
        // PHAN 2: URI INSPECTOR
        // ==============================

        System.out.println();
        System.out.println("===== URI INSPECTOR =====");
        System.out.println("URI: " + uriString);

        try {

            URI uri = new URI(uriString);

            System.out.println("Scheme: " + uri.getScheme());
            System.out.println("Host: " + uri.getHost());
            System.out.println("Port: " + uri.getPort());
            System.out.println("Path: " + uri.getPath());
            System.out.println("Query: " + uri.getQuery());
            System.out.println("Fragment: " + uri.getFragment());

        } catch (URISyntaxException e) {

            System.out.println("Loi: URI khong hop le.");
        }
    }
}
