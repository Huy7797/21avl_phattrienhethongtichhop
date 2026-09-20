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

public class Bai9_NhiPhan {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap so bit n: ");
        int n = sc.nextInt();
        
        int[] arr = new int[n];
        System.out.println("Cac chuoi nhi phan " + n + " bit:");
        sinhNhiPhan(0, n, arr);
    }
    
    public static void sinhNhiPhan(int i, int n, int[] arr) {
        for (int j = 0; j <= 1; j++) {
            arr[i] = j;
            if (i == n - 1) {
                inKetQua(arr);
            } else {
                sinhNhiPhan(i + 1, n, arr);
            }
        }
    }
    
    public static void inKetQua(int[] arr) {
        for (int val : arr) {
            System.out.print(val);
        }
        System.out.println();
    }
}
