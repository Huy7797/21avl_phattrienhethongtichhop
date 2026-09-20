/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab1_ptthht_21avl;

import java.util.Scanner;
import java.util.Stack;

import java.util.Scanner; // Nhập thư viện Scanner để hỗ trợ nhập dữ liệu từ bàn phím
import java.util.Stack;   // Nhập thư viện Stack để sử dụng cấu trúc dữ liệu Ngăn xếp

public class KiemTraDauNgoacV2 {

    /**
     * THUẬT TOÁN: Kiểm tra chuỗi dấu ngoặc nhập vào có hợp lệ hay không.
     * 
     * @param bieuThuc: [Cấu trúc dữ liệu gốc] Chuỗi ký tự cần kiểm tra (Ví dụ: "{[()]}")
     * @return true nếu chuỗi ngoặc đúng quy tắc đóng/mở, ngược lại trả về false.
     */
    public static boolean isKiemTraHopLe(String bieuThuc) {
        
        // -------------------------------------------------------------------------
        // [CẤU TRÚC DỮ LIỆU PHỤ TRỢ (B)]
        // Khởi tạo một Ngăn xếp (Stack) để lưu giữ tạm thời các dấu ngoặc mở.
        // Đặc tính của Stack là LIFO (Vào sau - Ra trước), giúp lấy ra dấu ngoặc gần nhất.
        // -------------------------------------------------------------------------
        Stack<Character> nganXep = new Stack<>();

        // -------------------------------------------------------------------------
        // [THUẬT TOÁN: DUYỆT VÀ XỬ LÝ KÝ TỰ]
        // Sử dụng vòng lặp `for` để duyệt qua từng ký tự từ đầu đến cuối chuỗi `bieuThuc`.
        // -------------------------------------------------------------------------
        for (int i = 0; i < bieuThuc.length(); i++) {
            
            // Thuật toán: Trích xuất ký tự tại vị trí (chỉ số) thứ `i` trong chuỗi
            char kyTu = bieuThuc.charAt(i);

            // BƯỚC 1 CỦA THUẬT TOÁN: NẾU GẶP NGOẶC MỞ
            if (kyTu == '(' || kyTu == '[' || kyTu == '{') {
                // Hành động: Đẩy ký tự ngoặc mở này vào đỉnh của Ngăn xếp (Stack)
                nganXep.push(kyTu);
            } 
            
            // BƯỚC 2 CỦA THUẬT TOÁN: NẾU GẶP NGOẶC ĐÓNG
            else if (kyTu == ')' || kyTu == ']' || kyTu == '}') {
                
                // Trường hợp đặc biệt: Nếu gặp ngoặc đóng mà Stack đang trống rỗng.
                // Điều này chứng tỏ: Dấu ngoặc đóng này bị thừa, hoàn toàn không có ngoặc mở đi trước.
                // Thuật toán lập tức kết luận: Chuỗi KHÔNG hợp lệ (trả về false).
                if (nganXep.isEmpty()) {
                    return false;
                }

                // Nếu Stack không trống, thuật toán tiến hành rút ký tự ở đỉnh Stack ra (Pop).
                // Ký tự được rút ra chính là dấu ngoặc mở được xuất hiện muộn nhất (gần nhất).
                char ngoacMoGanNhat = nganXep.pop();

                // Thuật toán tiến hành so khớp xem dấu ngoặc đóng hiện tại và ngoặc mở vừa lấy ra có thành cặp không.
                // Nếu cặp đôi KHÔNG khớp nhau (Ví dụ: mở là `[` nhưng đóng lại là `)`) -> Trả về false.
                if (kyTu == ')' && ngoacMoGanNhat != '(') return false;
                if (kyTu == ']' && ngoacMoGanNhat != '[') return false;
                if (kyTu == '}' && ngoacMoGanNhat != '{') return false;
            }
            
            // Mẹo: Nếu chuỗi có chứa chữ cái hoặc số (Ví dụ: 'a', '1', '+'), 
            // thuật toán sẽ tự động bỏ qua vì không rơi vào hai điều kiện `if` trên.
        }

        // BƯỚC 3 CỦA THUẬT TOÁN: KIỂM TRA ĐẦU RA CUỐI CÙNG
        // Sau khi đã chạy hết vòng lặp để kiểm tra toàn bộ chuỗi:
        // - Nếu Stack trống rỗng (isEmpty == true): Tức là mọi ngoặc mở đều đã tìm được ngoặc đóng khớp với nó -> HỢP LỆ.
        // - Nếu Stack vẫn còn phần tử: Tức là có ngoặc mở bị mồ côi, không có ngoặc đóng tương ứng -> KHÔNG HỢP LỆ.
        return nganXep.isEmpty();
    }

    /**
     * HÀM MAIN: Điểm khởi đầu của chương trình Java.
     * Chịu trách nhiệm tương tác nhập/xuất dữ liệu với người dùng.
     */
    public static void main(String[] args) {
        
        // -------------------------------------------------------------------------
        // [CẤU TRÚC DỮ LIỆU ĐẦU VÀO]
        // Khởi tạo đối tượng `Scanner` để làm cầu nối quản lý luồng dữ liệu nhập từ bàn phím.
        // -------------------------------------------------------------------------
        Scanner scanner = new Scanner(System.in);
        
        // Thuật toán hiển thị: In ra dòng thông báo hướng dẫn người dùng nhập liệu
        System.out.print("Nhập vào chuỗi dấu ngoặc cần kiểm tra (Ví dụ: {[()]}): ");
        
        // -------------------------------------------------------------------------
        // [CẤU TRÚC DỮ LIỆU LƯU TRỮ CHÍNH (A)]
        // Thuật toán sẽ đọc (hấp thụ) toàn bộ dòng chữ người dùng gõ từ bàn phím
        // và lưu trữ nó vào một biến kiểu Chuỗi tên là `chuoiNhap`.
        // -------------------------------------------------------------------------
        String chuoiNhap = scanner.nextLine();
        
        // Thuật toán xử lý: Gọi hàm `isKiemTraHopLe` ở trên, truyền dữ liệu đầu vào `chuoiNhap` vào.
        // Kết quả trả về (true hoặc false) sẽ được lưu vào biến logic `ketQua`.
        boolean ketQua = isKiemTraHopLe(chuoiNhap);
        
        // Thuật toán hiển thị đầu ra: Sử dụng cấu trúc rẽ nhánh `if-else` để in thông báo rõ ràng cho người dùng.
        if (ketQua) {
            System.out.println("=> Kết quả: Chuỗi dấu ngoặc bạn nhập HỢP LỆ!");
        } else {
            System.out.println("=> Kết quả: Chuỗi dấu ngoặc bạn nhập KHÔNG hợp lệ.");
        }
        
        // Thuật toán dọn dẹp bộ nhớ: Đóng luồng `scanner` để giải phóng tài nguyên hệ thống sau khi dùng xong.
        scanner.close();
    }
}
