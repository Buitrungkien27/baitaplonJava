package bus;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import dao.KhachHang_DAO;
import entity.KhachHang;

public class KhachHang_BUS {

    private KhachHang_DAO kh_DAO = new KhachHang_DAO();

    public ArrayList<KhachHang> getAllKH() {
        ArrayList<KhachHang> customerList = new KhachHang_DAO().getAll();
        return customerList;
    }

    public KhachHang getOne(String maKH) {
        return kh_DAO.getOne(maKH);
    }

    public String generateID(Date date, boolean gt) {
        // Khởi tạo tiền tố cho mã khách hàng
        String prefix = "KH";

        // Định dạng ngày tháng để lấy năm sinh
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String year = simpleDateFormat.format(date);
        prefix += year;

        // Thêm ký tự đại diện cho giới tính vào tiền tố
        prefix += gt ? "1" : "0"; // '1' cho nam, '0' cho nữ
        System.out.println("prefix: " + prefix);
        // Tìm mã lớn nhất đã tồn tại với cùng tiền tố
        String maxID = kh_DAO.getMaxSequence(prefix);
        System.out.println("maxID: " + maxID);
        // Tạo số thứ tự tiếp theo
        if (maxID == null) {
            prefix += "0000"; // Bắt đầu từ 0000 nếu không có mã trước đó
        } else {
            // Kiểm tra maxID có ít nhất 4 ký tự
            if (maxID.length() < 4) {
                throw new IllegalStateException("Định dạng ID không hợp lệ trong cơ sở dữ liệu.");
            }
            String lastFourChars = maxID.substring(maxID.length() - 4);
            int num;
            try {
                num = Integer.parseInt(lastFourChars); // Chuyển đổi 4 ký tự cuối thành số
            } catch (NumberFormatException e) {
                num = 0; // Đặt lại về 0 nếu phân tích thất bại
            }
            num++; // Tăng số lên
            prefix += String.format("%04d", num); // Định dạng thành bốn chữ số
        }
        return prefix; // Trả về mã đã tạo
    }


    public boolean createKH(String ten, Date ns, String sdt, String dc, Boolean gt, String cccd) throws Exception {
        System.out.println("Test3:" + sdt);
        String idMoi = generateID(ns, gt);

        // Create new customer object
        KhachHang kh = new KhachHang(idMoi, ten, gt, ns, sdt, dc, cccd);
        
        // Return the result of the creation operation
       
        return kh_DAO.create(kh);
    }

    public void updateKH(KhachHang kh, String maKH) throws Exception {
        kh_DAO.update(maKH, kh);
    }

    public KhachHang searchByPhoneNumber(String phoneNumber) {
        ArrayList<KhachHang> list = kh_DAO.getAll();
        for (KhachHang kh : list) {
            if (kh.getSdt().equals(phoneNumber)) {
                return kh;
            }
        }
        return null;
    }

    public ArrayList<KhachHang> filterKH(String gt, String age, String phone) {

        ArrayList<KhachHang> list = kh_DAO.getAll();
        if (!phone.equals("")) {
            ArrayList<KhachHang> tempList = new ArrayList<>();
            tempList.add(searchByPhoneNumber(phone));
            list = tempList;
        }
        ArrayList<KhachHang> listRemove = new ArrayList<>();
        if (gt.equals("Nam")) {
            for (KhachHang kh : list) {
                if (!kh.isGioiTinh()) {
                    listRemove.add(kh);
                }
            }
            list.removeAll(listRemove);
        }
        if (gt.equals("Nữ")) {
            for (KhachHang kh : list) {
                if (kh.isGioiTinh()) {
                    listRemove.add(kh);
                }
            }
            list.removeAll(listRemove);
        }

        if (!age.equals("Tất cả")) {
            if (age.equals("Dưới 18 tuổi")) {
                for (KhachHang kh : list) {
                    if (getAge(kh.getNgaySinh()) > 18) {
                        listRemove.add(kh);
                    }
                }
                list.removeAll(listRemove);
            } else if (age.equals("Trên 40 tuổi")) {
                for (KhachHang kh : list) {
                    if (getAge(kh.getNgaySinh()) < 40) {
                        listRemove.add(kh);
                    }
                }
                list.removeAll(listRemove);
            } else if (age.equals("Từ 18 đến 40 tuổi")) {
                for (KhachHang kh : list) {
                    if (getAge(kh.getNgaySinh()) > 40 || getAge(kh.getNgaySinh()) < 18) {
                        listRemove.add(kh);
                    }
                }
                list.removeAll(listRemove);
            }

        }

        return list;
    }
    public int getAge(Date dateOfBirth) {
        long diffInMillies = Math.abs(new Date().getTime() - dateOfBirth.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int) (diff / 365);
    }

}
