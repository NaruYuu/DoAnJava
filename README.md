# QuanLySinhVien
 Quản lý sinh viên bằng Java
 
<h2>Các bước để chạy file Java</h2>
<p>1. Clone file rar về máy của bạn</p>
<p>2. Giải nén</p>
<p>3. Sử dụng cmd hoặc IDE, text editor, NetBean mở folder đã giải nén</p>
<p>4. Với cmd: java Main để run,với ide: chọn Run trên ide</p>
<p>5. Nếu bị lỗi thì chạy bằng cmd nhé :></p>

## Giao diện GUI và MySQL

Ứng dụng mới kèm theo một giao diện Swing và kết nối MySQL. Các file mới:
- `db/DBConnection.java` — cấu hình kết nối MySQL (HOST, PORT, DATABASE, USER, PASSWORD).
- `db/StudentDAO.java` — CRUD với bảng `students`.
- `gui/MainForm.java` — giao diện ứng dụng (JFrame) để xem/thêm/sửa/xóa.

### Tạo database và bảng
Truy cập MySQL (ví dụ dùng `mysql` cli hoặc Workbench) và chạy:

```sql
CREATE DATABASE IF NOT EXISTS quanlysinhvien CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE quanlysinhvien;
CREATE TABLE IF NOT EXISTS students (
	mssv INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255),
	phone VARCHAR(50),
	lop VARCHAR(50),
	khoa VARCHAR(50),
	toan FLOAT,
	hoa FLOAT,
	ly FLOAT
);
```

### Thêm MySQL Connector/J
Tải `mysql-connector-java` (JAR) từ trang chính thức của MySQL và đặt file JAR vào thư mục dự án hoặc cung cấp đường dẫn tới JAR khi biên dịch.

### Biên dịch và chạy (Windows PowerShell)
Ví dụ nếu bạn có `mysql-connector-java-8.x.x.jar` trong thư mục gốc dự án:

```powershell
javac -cp .;mysql-connector-java-8.0.33.jar Main.java SinhVien\sinhvien.java db\DBConnection.java db\StudentDAO.java gui\MainForm.java
java -cp .;mysql-connector-java-8.0.33.jar gui.MainForm
```

Chú ý: chỉnh `DBConnection` (file `db/DBConnection.java`) để đặt `HOST`, `PORT`, `DATABASE`, `USER`, `PASSWORD` đúng với môi trường MySQL của bạn.

Nếu chương trình không thể kết nối tới MySQL, GUI sẽ đọc dữ liệu từ `data.txt` ở dạng chỉ đọc (không lưu thay đổi).
