/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2;

/**
 *
 * @author User
 */
import java.util.Scanner;

public class Bai8_Fibonacci {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap so phan tu Fibonacci can xuat (n >= 1): ");
        int n = sc.nextInt();
        
        System.out.print("Day so Fibonacci: ");
        if (n >= 1) System.out.print("1 ");
        if (n >= 2) System.out.print("1 ");
        
        long f0 = 1, f1 = 1, fn;
        for (int i = 3; i <= n; i++) {
            fn = f0 + f1;
            System.out.print(fn + " ");
            f0 = f1;
            f1 = fn;
        }
        System.out.println();
    }
}
