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
// Lớp cha đại diện cho động vật chung
class Animal {
    String name;
    double weight;

    public Animal(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public void displayInfo() {
        System.out.print("Tên: " + name + ", Cân nặng: " + weight + " kg");
    }
}

// Lớp Sư tử kế thừa từ Động vật
class Lion extends Animal {
    double eatAmount; // Lượng thức ăn mỗi ngày

    public Lion(String name, double weight, double eatAmount) {
        super(name, weight);
        this.eatAmount = eatAmount;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(", Thức ăn tiêu thụ/ngày: " + eatAmount + " kg");
    }
}

// Lớp Rắn kế thừa từ Động vật
class Snake extends Animal {
    double length; // Chiều dài của rắn

    public Snake(String name, double weight, double length) {
        super(name, weight);
        this.length = length;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(", Chiều dài: " + length + " m");
    }
}

// Lớp Khỉ kế thừa từ Động vật
class Monkey extends Animal {
    String favoriteFood; // Thức ăn yêu thích

    public Monkey(String name, double weight, String favoriteFood) {
        super(name, weight);
        this.favoriteFood = favoriteFood;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println(", Thức ăn yêu thích: " + favoriteFood);
    }
}

// Lớp chứa hàm main để chạy chương trình
public class Bai6_QuanLyVuonThu {
    public static void main(String[] args) {
        System.out.println("--- THÔNG TIN VẬT NUÔI TRONG VƯỜN THÚ ---");
        
        Lion lion = new Lion("Sư tử Simba", 190.5, 7.5);
        Snake snake = new Snake("Rắn Hổ Mang", 3.2, 2.5);
        Monkey monkey = new Monkey("Khỉ Abu", 12.0, "Chuối");

        lion.displayInfo();
        snake.displayInfo();
        monkey.displayInfo();
    }
}
