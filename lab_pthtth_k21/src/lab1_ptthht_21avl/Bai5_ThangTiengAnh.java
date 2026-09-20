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
import java.util.Scanner;

public class Bai5_ThangTiengAnh {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nhập vào một tháng trong năm (1-12): ");
        int thang = scanner.nextInt();
        
        String tenThang;
        switch (thang) {
            case 1: tenThang = "January"; break;
            case 2: tenThang = "February"; break;
            case 3: tenThang = "March"; break;
            case 4: tenThang = "April"; break;
            case 5: tenThang = "May"; break;
            case 6: tenThang = "June"; break;
            case 7: tenThang = "July"; break;
            case 8: tenThang = "August"; break;
            case 9: tenThang = "September"; break;
            case 10: tenThang = "October"; break;
            case 11: tenThang = "November"; break;
            case 12: tenThang = "December"; break;
            default: tenThang = "Tháng không hợp lệ! Vui lòng nhập từ 1 đến 12."; break;
            
        }
        
        System.out.println("Kết quả: " + tenThang);
        
        scanner.close();
    }
}
