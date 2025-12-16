package gui;

import SinhVien.sinhvien;
import db.StudentDAO;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainForm extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private boolean dbAvailable = false;

    public MainForm() {
        super("Quản lý sinh viên");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 400);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new Object[] {"MSSV","Ho ten","Phone","Lop","Khoa","Toan","Hoa","Ly","DTB"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);

        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Thêm");
        JButton editBtn = new JButton("Sửa");
        JButton delBtn = new JButton("Xóa");
        JButton refreshBtn = new JButton("Làm mới");
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(delBtn);
        btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(ev -> onAdd());
        editBtn.addActionListener(ev -> onEdit());
        delBtn.addActionListener(ev -> onDelete());
        refreshBtn.addActionListener(ev -> refreshTable());

        // Try initialize DB
        try {
            StudentDAO.init();
            dbAvailable = true;
        } catch (SQLException e) {
            dbAvailable = false;
            JOptionPane.showMessageDialog(this, "Không kết nối được MySQL. Ứng dụng sẽ sử dụng dữ liệu trong data.txt (chỉ đọc).\nLỗi: " + e.getMessage(), "DB lỗi", JOptionPane.WARNING_MESSAGE);
        }

        refreshTable();

        if (!dbAvailable) {
            addBtn.setEnabled(false);
            editBtn.setEnabled(false);
            delBtn.setEnabled(false);
        }
    }

    private void refreshTable() {
        model.setRowCount(0);
        try {
            List<sinhvien> list = dbAvailable ? StudentDAO.loadAll() : StudentDAO.readFromFileFallback();
            for (sinhvien s : list) {
                model.addRow(new Object[] {s.getMssv(), s.getName(), s.getPhone(), s.getLop(), s.getKhoa(), s.getDiemToan(), s.getDiemHoa(), s.getDiemLy(), String.format("%.2f", s.getDiemTrungBinh())});
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi load dữ liệu: " + e.getMessage());
        }
    }

    private void onAdd() {
        JTextField nameF = new JTextField();
        JTextField phoneF = new JTextField();
        JTextField lopF = new JTextField();
        JTextField khoaF = new JTextField();
        JTextField toanF = new JTextField();
        JTextField hoaF = new JTextField();
        JTextField lyF = new JTextField();
        JPanel p = new JPanel(new GridLayout(0,1));
        p.add(new JLabel("Ho va ten:")); p.add(nameF);
        p.add(new JLabel("So dien thoai:")); p.add(phoneF);
        p.add(new JLabel("Lop:")); p.add(lopF);
        p.add(new JLabel("Khoa:")); p.add(khoaF);
        p.add(new JLabel("Diem Toan (tuỳ chọn):")); p.add(toanF);
        p.add(new JLabel("Diem Hoa (tuỳ chọn):")); p.add(hoaF);
        p.add(new JLabel("Diem Ly (tuỳ chọn):")); p.add(lyF);
        int res = JOptionPane.showConfirmDialog(this, p, "Thêm sinh viên", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            sinhvien s = new sinhvien(nameF.getText(), phoneF.getText(), lopF.getText(), khoaF.getText(), parseFloatOr0(toanF.getText()), parseFloatOr0(hoaF.getText()), parseFloatOr0(lyF.getText()));
            try {
                StudentDAO.addStudent(s);
                refreshTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm: " + e.getMessage());
            }
        }
    }

    private void onEdit() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn sinh viên để sửa"); return; }
        int mssv = (int) model.getValueAt(row, 0);
        String curName = (String) model.getValueAt(row, 1);
        String curPhone = (String) model.getValueAt(row, 2);
        String curLop = (String) model.getValueAt(row, 3);
        String curKhoa = (String) model.getValueAt(row, 4);
        float curToan = Float.parseFloat(model.getValueAt(row,5).toString());
        float curHoa = Float.parseFloat(model.getValueAt(row,6).toString());
        float curLy = Float.parseFloat(model.getValueAt(row,7).toString());

        JTextField nameF = new JTextField(curName);
        JTextField phoneF = new JTextField(curPhone);
        JTextField lopF = new JTextField(curLop);
        JTextField khoaF = new JTextField(curKhoa);
        JTextField toanF = new JTextField(String.valueOf(curToan));
        JTextField hoaF = new JTextField(String.valueOf(curHoa));
        JTextField lyF = new JTextField(String.valueOf(curLy));

        JPanel p = new JPanel(new GridLayout(0,1));
        p.add(new JLabel("Ho va ten:")); p.add(nameF);
        p.add(new JLabel("So dien thoai:")); p.add(phoneF);
        p.add(new JLabel("Lop:")); p.add(lopF);
        p.add(new JLabel("Khoa:")); p.add(khoaF);
        p.add(new JLabel("Diem Toan:")); p.add(toanF);
        p.add(new JLabel("Diem Hoa:")); p.add(hoaF);
        p.add(new JLabel("Diem Ly:")); p.add(lyF);

        int res = JOptionPane.showConfirmDialog(this, p, "Sua sinh vien (MSSV="+mssv+")", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            sinhvien s = new sinhvien(mssv, nameF.getText(), phoneF.getText(), lopF.getText(), khoaF.getText(), parseFloatOr0(toanF.getText()), parseFloatOr0(hoaF.getText()), parseFloatOr0(lyF.getText()));
            try {
                StudentDAO.updateStudent(s);
                refreshTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + e.getMessage());
            }
        }
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn sinh viên để xóa"); return; }
        int mssv = (int) model.getValueAt(row, 0);
        int res = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn xóa MSSV="+mssv+"?", "Xóa", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {
            try {
                StudentDAO.deleteStudent(mssv);
                refreshTable();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage());
            }
        }
    }

    private float parseFloatOr0(String s) {
        try { return Float.parseFloat(s); } catch (Exception e) { return 0f; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainForm().setVisible(true);
        });
    }
}
