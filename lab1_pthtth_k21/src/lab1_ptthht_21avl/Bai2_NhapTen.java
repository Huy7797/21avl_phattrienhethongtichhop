/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1_ptthht_21avl;
import java.util.Scanner;

public class Bai2_NhapTen {
    public static void main(String[] args) {
        // Sửa lại dòng này: Chỉ cần truyền trực tiếp System.in vào Scanner
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nhập vào tên của bạn: ");
        String ten = scanner.nextLine();
        
        System.out.println("Hi, I am " + ten);
        
        scanner.close();
    }
}
