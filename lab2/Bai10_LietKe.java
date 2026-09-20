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

public class Bai10_LietKe {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap so phan tu cua mang n: ");
        int n = sc.nextInt();
        
        int[] arr = new int[n];
        System.out.println("Nhap các phan tu:");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }
        
        System.out.print("Cac phan tu duy nhat xuat hien trong mang: ");
        // Dung thuat toan danh dau de liet ke khong trung lap
        Arrays.sort(arr);
        for (int i = 0; i < n; i++) {
            if (i == 0 || arr[i] != arr[i - 1]) {
                System.out.print(arr[i] + " ");
            }
        }
        System.out.println();
    }
}
