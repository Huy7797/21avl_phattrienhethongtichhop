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

public class Bai11_ThaoTacMang {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Khoi tao mang da sap xep san
        int[] arr = {10, 20, 30, 40, 50};
        System.out.println("Mang ban dau da sap xep: " + Arrays.toString(arr));
        
        // 1. TIM KIEM (Dung kiem nhi phan Binary Search)
        System.out.print("\nNhap phan tu can tim kiem: ");
        int x = sc.nextInt();
        int index = Arrays.binarySearch(arr, x);
        if (index >= 0) {
            System.out.println("Tim thay " + x + " tai vi tri index: " + index);
        } else {
            System.out.println("Khong tim thay phan tu " + x);
        }
        
        // 2. THEM PHAN TU (Giu nguyen thu tu sap xep)
        System.out.print("\nNhap phan tu muon them: ");
        int valueToAdd = sc.nextInt();
        arr = themPhanTu(arr, valueToAdd);
        System.out.println("Mang sau khi them: " + Arrays.toString(arr));
        
        // 3. XOA PHAN TU
        System.out.print("\nNhap gia tri phan tu muon xoa: ");
        int valueToDelete = sc.nextInt();
        arr = xoaPhanTu(arr, valueToDelete);
        System.out.println("Mang sau khi xoa: " + Arrays.toString(arr));
    }
    
    // Ham them phan tu vao mang da sap xep
    public static int[] themPhanTu(int[] arr, int val) {
        int[] newArr = new int[arr.length + 1];
        int i = 0;
        // Copy cac phan tu nho hon val
        while (i < arr.length && arr[i] < val) {
            newArr[i] = arr[i];
            i++;
        }
        // Chen val vao
        newArr[i] = val;
        // Copy cac phan tu con lai
        while (i < arr.length) {
            newArr[i + 1] = arr[i];
            i++;
        }
        return newArr;
    }
    
    // Ham xoa phan tu khoi mang
    public static int[] xoaPhanTu(int[] arr, int val) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == val) {
                index = i;
                break;
            }
        }
        
        if (index == -1) {
            System.out.println("Khong tim thay phan tu can xoa!");
            return arr;
        }
        
        int[] newArr = new int[arr.length - 1];
        for (int i = 0, k = 0; i < arr.length; i++) {
            if (i == index) continue;
            newArr[k++] = arr[i];
        }
        return newArr;
    }
}
