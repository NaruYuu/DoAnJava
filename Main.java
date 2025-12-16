import SinhVien.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;


public class Main {
    static Scanner sc = new Scanner(System.in);
    static LinkedList<sinhvien> listSV = new LinkedList<>();
    static boolean isHasStudent = false;

    public static void main(String argv[]) {
        loadData();
        try {
            gui.MainForm.main(argv);
        } catch (Exception e) {
            System.out.println("Loi khi khoi dong GUI: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        sc.close();
    }

    public static sinhvien createStudent() {
        System.out.print("+ Nhap ho va ten sinh vien: ");
        String name = sc.nextLine();
        System.out.print("+ Nhap so dien thoai: ");
        String phone = sc.nextLine();
        System.out.print("+ Nhap lop: ");
        String lop = sc.nextLine();
        System.out.print("+ Nhap khoa: ");
        String khoa = sc.nextLine();
        sinhvien sv = new sinhvien(name, phone, lop, khoa);
        System.out.println("Them sinh vien thanh cong!");
        return sv;
    }

    public static boolean removeStudent() {
        System.out.print("> Nhap MSSV muon xoa: ");
        int pos = sc.nextInt();
        if (pos < 1 || pos > sinhvien.getTotal()) {
            return false;
        }
        listSV.forEach(item -> {
            if (item.getMssv() == pos) {
                isHasStudent = true;
                System.out.println(
                        "Ban co muon xoa sinh vien " + item.getName() + " (mssv: " + item.getMssv() + ") khong ?");
                System.out.println("1. Co");
                System.out.println("2. Khong");
                System.out.print("> Lua chon cua ban: ");
                int confirm = sc.nextInt();
                sc.nextLine();
                if (confirm == 1) {
                    if (listSV.remove(item) == true) {
                        System.out.println("Xoa thanh cong " + item.getName() + " !");
                    }
                } else {
                    System.out.println("Huy xoa thanh cong!");
                }
            }
        });
        if (isHasStudent) {
            isHasStudent = false;
            return true;
        }
        return false;
    }
    
    public static boolean changeSinhVien() {
        System.out.print("> Nhap mssv muon thay doi: ");
        int pos = sc.nextInt();

        if (pos < 1 || pos > sinhvien.getTotal()) {
            return false;
        }
        listSV.forEach(item -> {
            if (item.getMssv() == pos) {
                isHasStudent = true;
                System.out.println("Ban co muon thay doi sinh vien " + item.getName() + " (mssv: " + item.getMssv()
                        + " ) khong ?");
                System.out.println("1. Co");
                System.out.println("2. Khong");
                System.out.print("> Lua chon cua ban: ");
                int confirm = sc.nextInt();
                sc.nextLine();
                if (confirm == 1) {
                    System.out.print("+ Nhap ho va ten sinh vien: ");
                    String name = sc.nextLine();
                    System.out.print("+ Nhap so dien thoai: ");
                    String phone = sc.nextLine();
                    System.out.print("+ Nhap lop: ");
                    String lop = sc.nextLine();
                    System.out.print("+ Nhap khoa: ");
                    String khoa = sc.nextLine();
                    item.setInfo(name, phone, lop, khoa);
                    System.out.println("Thay doi thong tin sinh vien thanh cong!");
                } else {
                    System.out.println("Huy thay doi thanh cong!");
                }
            }
        });
        if (isHasStudent) {
            isHasStudent = false;
            return true;
        }
        return false;
    }
    
    public static boolean addPointSinhVien() {
        System.out.print("> Nhap mssv muon them diem: ");
        int pos = sc.nextInt();
        sc.nextLine();
        if (pos < 1 || pos > sinhvien.getTotal()) {
            return false;
        }
        listSV.forEach(item -> {
            if (item.getMssv() == pos) {
                isHasStudent = true;
                System.out.println("Sinh vien: " + item.getName() + " (mssv: " + item.getMssv() + ")");
                System.out.print("+ Nhap diem toan: ");
                int diemToan = sc.nextInt();
                System.out.print("+ Nhap diem hoa: ");
                int diemHoa = sc.nextInt();
                System.out.print("+ Nhap diem ly: ");
                int diemLy = sc.nextInt();
                sc.nextLine();
                item.setPoint(diemToan, diemHoa, diemLy);
                System.out.println("Them diem thanh cong!");
            }
        });
        if (isHasStudent) {
            isHasStudent = false;
            return true;
        }
        return false;
    }

    public static boolean showStudentInfo() {
        System.out.print("> Nhap MSSV de xem thong tin: ");
        int pos = sc.nextInt();
        sc.nextLine();
        if (pos < 1 || pos > sinhvien.getTotal()) {
            return false;
        }
        listSV.forEach(item -> {
            if (item.getMssv() == pos) {
                isHasStudent = true;
                item.showInformation();
            }
        });
        if (isHasStudent) {
            isHasStudent = false;
            return true;
        }
        return false;
    }

    public static void showListOfStudent() {
        System.out.println("Danh sach co " + listSV.size() + " sinh vien:");
        System.out.printf("%-15s%-25s%-10s%-15s%-8s%-8s%-8s%-8s\n","MSSV","Ho ten","Lop","Khoa","Toan","Ly","Hoa","DTB");
        listSV.forEach(item -> {
            item.showInList();
        });
    }

    public static boolean searchSutdent() {
        System.out.print("> Nhap MSSV hoac ten de xem thong tin: ");
        String input = sc.nextLine();
        int mssv = 0;
        boolean isMssv = true;
        try {
            mssv = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            isMssv = false;
            input.toLowerCase();
        }
        if (isMssv) {
            //Neu la mssv
            if (mssv < 1 || mssv > sinhvien.getTotal()) {
                return false;
            }
            for (sinhvien sv : listSV) {
                if (sv.getMssv() == mssv) {
                    isHasStudent = true;
                    sv.showInformation();
                }
            }
        } else {
            for (sinhvien sv : listSV) {
                if (sv.getName().toLowerCase().contains(input)) {
                    isHasStudent = true;
                    sv.showInformation();
                }
            }
        }

        if (isHasStudent) {
            isHasStudent = false;
            return true;
        }
        return false;
    }

    //Sort
    public static void sortStudent() {
        int n = listSV.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                sinhvien sv1 = listSV.get(i);
                sinhvien sv2 = listSV.get(j);
                if (sv1.getDiemTrungBinh() > sv2.getDiemTrungBinh()) {
                    listSV.set(i, sv2);
                    listSV.set(j, sv1);
                }
            }
        }
        showListOfStudent();
    }

    public static void checkExistStudent(boolean isHasStudent) {
        if (!isHasStudent) {
            System.out.println("Sinh vien khong ton tai!");
        }
    }

    public static void inUpdate() {
        System.out.println("Chuc nang nay dang duoc phat trien!");
    }

    public static void loadData() {
        String path = "data.txt";
        File myFile = new File(path);
        try (Scanner myReader = new Scanner(myFile)) {
            String mydata;
            String[] myDataArr;
            while (myReader.hasNextLine()) {
                mydata = myReader.nextLine();
                myDataArr = mydata.split(",", -2);
                int mssv = Integer.parseInt(myDataArr[0]);
                if (myDataArr.length == 5) {
                    listSV.add(new sinhvien(mssv, myDataArr[1], myDataArr[2], myDataArr[3], myDataArr[4]));
                } else if (myDataArr.length == 8) {
                    float toan = Float.parseFloat(myDataArr[5]);
                    float hoa = Float.parseFloat(myDataArr[6]);
                    float ly = Float.parseFloat(myDataArr[7]);
                    listSV.add(new sinhvien(mssv, myDataArr[1], myDataArr[2], myDataArr[3], myDataArr[4], toan, hoa, ly));
                }
            }
        } catch (FileNotFoundException e) {
            // File not found
        } catch (NumberFormatException e) {
            System.out.println("Loi doc file!");
        }
    }
    
    public static void saveData() {
        String path = "data.txt";
        File myFile = new File(path);
        try {
            if (!myFile.exists()) {
                if (!myFile.createNewFile()) {
                    System.out.println("Khong the tao file!");
                    return;
                }
            }
            if (myFile.exists()) {
                try (FileWriter writer = new FileWriter(myFile)) {
                    listSV.forEach(item -> {
                        if (item.getisSet()) {
                            try {
                                writer.write(item.getMssv() + "," + item.getName() + "," + item.getPhone() + ","
                                        + item.getLop() + "," + item.getKhoa() + "," + item.getDiemToan() + ","
                                        + item.getDiemHoa() + "," + item.getDiemLy() + "\n");
                            } catch (IOException e) {
                                System.out.println("Khong the ghi du lieu, loi: " + e.getMessage());
                            }
                        } else {
                            try {
                                writer.write(item.getMssv() + "," + item.getName() + "," + item.getPhone() + ","
                                        + item.getLop() + "," + item.getKhoa() + "\n");
                            } catch (IOException e) {
                                System.out.println("Khong the ghi du lieu, loi: " + e.getMessage());
                            }
                        }
                    });
                }
            }
        } catch (IOException e) {
            System.out.println("Khong the tao file, loi: " + e.getMessage());
        }
    }
}
