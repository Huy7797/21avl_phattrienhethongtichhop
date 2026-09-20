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

public class Bai6_ThongKe {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap so luong phan tu n: ");
        int n = sc.nextInt();
        
        int[] arr = new int[n];
        int sum = 0;
        
        System.out.println("Nhap cac phan tu cua mang:");
        for (int i = 0; i < n; i++) {
            System.out.print("arr[" + i + "] = ");
            arr[i] = sc.nextInt();
            sum += arr[i];
        }
        
        int max = arr[0];
        int min = arr[0];
        for (int i = 1; i < n; i++) {
            if (arr[i] > max) max = arr[i];
            if (arr[i] < min) min = arr[i];
        }
        
        double avg = (double) sum / n;
        
        int lonHonAvg = 0;
        int nhoHonAvg = 0;
        for (int x : arr) {
            if (x > avg) lonHonAvg++;
            else if (x < avg) nhoHonAvg++;
        }
        
        System.out.println("\n--- KET QUA ---");
        System.out.println("Gia tri lon nhat: " + max);
        System.out.println("Gia tri nho nhat: " + min);
        System.out.printf("Gia tri trung binh: %.2f\n", avg);
        System.out.println("So luong phan tu > trung binh: " + lonHonAvg);
        System.out.println("So luong phan tu < trung binh: " + nhoHonAvg);
    }
}
