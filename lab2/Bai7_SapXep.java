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
import java.util.Arrays;
import java.util.Scanner;

public class Bai7_SapXep {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap so luong phan tu n: ");
        int n = sc.nextInt();
        
        int[] arr = new int[n];
        System.out.println("Nhap cac phan tu cua mang:");
        for (int i = 0; i < n; i++) {
            System.out.print("arr[" + i + "] = ");
            arr[i] = sc.nextInt();
        }
        
        System.out.print("\nDay so da nhap: ");
        inMang(arr);
        
        // Sắp xếp tăng dần
        int[] tangDan = arr.clone();
        Arrays.sort(tangDan);
        System.out.print("Day so tang dan: ");
        inMang(tangDan);
        
        // Sắp xếp giảm dần
        System.out.print("Day so giam dan: ");
        for (int i = n - 1; i >= 0; i--) {
            System.out.print(tangDan[i] + " ");
        }
        System.out.println();
    }
    
    public static void inMang(int[] arr) {
        for (int x : arr) {
            System.out.print(x + " ");
        }
        System.out.println();
    }
}
