package db;

import SinhVien.sinhvien;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Data access object for sinhvien. Uses DBConnection; if DB not reachable,
 * falls back to reading `data.txt` in project root.
 */
public class StudentDAO {

    public static void init() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS students (" +
                "mssv INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(255), phone VARCHAR(50), lop VARCHAR(50), khoa VARCHAR(50), " +
                "toan FLOAT, hoa FLOAT, ly FLOAT)";
        try (Connection c = DBConnection.getConnection(); Statement st = c.createStatement()) {
            st.execute(sql);
        }
    }

    public static List<sinhvien> loadAll() throws SQLException {
        List<sinhvien> list = new ArrayList<>();
        String sql = "SELECT mssv, name, phone, lop, khoa, toan, hoa, ly FROM students ORDER BY mssv";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int mssv = rs.getInt("mssv");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String lop = rs.getString("lop");
                String khoa = rs.getString("khoa");
                float toan = rs.getFloat("toan");
                float hoa = rs.getFloat("hoa");
                float ly = rs.getFloat("ly");
                if (toan==0 && hoa==0 && ly==0) {
                    // could be absent — but we'll still set via constructor without points
                    list.add(new sinhvien(mssv, name, phone, lop, khoa));
                } else {
                    list.add(new sinhvien(mssv, name, phone, lop, khoa, toan, hoa, ly));
                }
            }
        }
        return list;
    }

    public static void addStudent(sinhvien s) throws SQLException {
        String sql = "INSERT INTO students (name, phone, lop, khoa, toan, hoa, ly) VALUES (?,?,?,?,?, ?,?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getLop());
            ps.setString(4, s.getKhoa());
            ps.setFloat(5, s.getDiemToan());
            ps.setFloat(6, s.getDiemHoa());
            ps.setFloat(7, s.getDiemLy());
            ps.executeUpdate();
        }
    }

    public static void updateStudent(sinhvien s) throws SQLException {
        String sql = "UPDATE students SET name=?, phone=?, lop=?, khoa=?, toan=?, hoa=?, ly=? WHERE mssv=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.getName());
            ps.setString(2, s.getPhone());
            ps.setString(3, s.getLop());
            ps.setString(4, s.getKhoa());
            ps.setFloat(5, s.getDiemToan());
            ps.setFloat(6, s.getDiemHoa());
            ps.setFloat(7, s.getDiemLy());
            ps.setInt(8, s.getMssv());
            ps.executeUpdate();
        }
    }

    public static void deleteStudent(int mssv) throws SQLException {
        String sql = "DELETE FROM students WHERE mssv=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, mssv);
            ps.executeUpdate();
        }
    }

    // Fallback: read data.txt if DB is not available
    public static List<sinhvien> readFromFileFallback() {
        List<sinhvien> list = new ArrayList<>();
        File f = new File("data.txt");
        if (!f.exists()) return list;
        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] arr = line.split(",", -2);
                int mssv = Integer.parseInt(arr[0]);
                if (arr.length == 5) {
                    list.add(new sinhvien(mssv, arr[1], arr[2], arr[3], arr[4]));
                } else if (arr.length >= 8) {
                    float toan = Float.parseFloat(arr[5]);
                    float hoa = Float.parseFloat(arr[6]);
                    float ly = Float.parseFloat(arr[7]);
                    list.add(new sinhvien(mssv, arr[1], arr[2], arr[3], arr[4], toan, hoa, ly));
                }
            }
        } catch (Exception e) {
            // ignore and return whatever parsed
        }
        return list;
    }
}
