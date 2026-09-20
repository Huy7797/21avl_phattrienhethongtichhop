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

public class Bai3_TinhTong {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Nhập số A: ");
        double a = scanner.nextDouble();
        
        System.out.print("Nhập số B: ");
        double b = scanner.nextDouble();
        
        double tong = a + b;
        System.out.println("Tổng của A và B là: " + tong);
        
        scanner.close();
    }
}
