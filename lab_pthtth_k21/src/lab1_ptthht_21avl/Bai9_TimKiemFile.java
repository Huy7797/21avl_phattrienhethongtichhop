/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1_ptthht_21avl;

/**
 *
 * @author User
 */
import java.io.File;
import java.util.Scanner;

public class Bai9_TimKiemFile {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print(":d\\fnote\\ktvthht: ");
        String folderPath = scanner.nextLine();
        System.out.print("demo): ");
        String fileNameToSearch = scanner.nextLine();
        
        File dir = new File(folderPath);
        if (dir.exists() && dir.isDirectory()) {
            System.out.println("Kết quả tìm kiếm:");
            searchFile(dir, fileNameToSearch);
        } else {
            System.out.println("Đường dẫn thư mục không hợp lệ.");
        }
        
        scanner.close();
    }

    public static void searchFile(File folder, String keyword) {
        File[] list = folder.listFiles();
        if (list != null) {
            for (File file : list) {
                if (file.isDirectory()) {
                    searchFile(file, keyword); // Tiếp tục tìm trong thư mục con
                } else if (file.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    System.out.println("Tìm thấy: " + file.getAbsolutePath());
                }
            }
        }
    }
}
