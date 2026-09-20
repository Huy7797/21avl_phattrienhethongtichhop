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

public class Bai7_DeleteFile {
    public static void main(String[] args) {
        // Thay đổi đường dẫn file phù hợp với máy của bạn
        File file = new File("test.txt"); 
        
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Xóa file thành công: " + file.getName());
            } else {
                System.out.println("Xóa file thất bại.");
            }
        } else {
            System.out.println("File không tồn tại.");
        }
    }
}
