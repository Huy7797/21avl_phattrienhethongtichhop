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

public class Bai8_DeleteFolder {
    public static void main(String[] args) {
        File folder = new File("D:\\fnote\\ktvthht"); // Thay bằng đường dẫn thư mục cần xóa

        if (folder.exists()) {
            deleteFolderRecursively(folder);
            System.out.println("Đã xóa thư mục thành công.");
        } else {
            System.out.println("Thư mục không tồn tại.");
        }
    }

    // Hàm đệ quy để xóa tất cả file và thư mục con bên trong trước khi xóa thư mục chính
    public static void deleteFolderRecursively(File fileOrFolder) {
        if (fileOrFolder.isDirectory()) {
            File[] files = fileOrFolder.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    deleteFolderRecursively(subFile);
                }
            }
        }
        fileOrFolder.delete();
    }
}
