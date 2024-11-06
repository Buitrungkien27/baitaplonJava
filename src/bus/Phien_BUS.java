package bus;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import dao.Phien_DAO;
import entity.Phien;

public class Phien_BUS {

    private Phien_DAO shift_DAO = new Phien_DAO();

    public Phien getOne(String id) {
        return shift_DAO.getOne(id);
    }

    public ArrayList<Phien> getAll() {
        return shift_DAO.getAll();
    }

    public boolean createShifts(Phien shift) {
        return shift_DAO.create(shift);
    }

    public String renderID() {
        return shift_DAO.generateID();
    }

    public ArrayList<Phien> getShiftsByDate(Date date) {
        return shift_DAO.getShiftsByDate(date);
    }

    public ArrayList<Phien> filter(String emloyeeID, String role, Date date) {
        ArrayList<Phien> list = shift_DAO.getShiftsByDate(date);

        // Lọc theo `employeeID`
        if (!emloyeeID.equals("")) {
            Iterator<Phien> iterator = list.iterator();
            while (iterator.hasNext()) {
                Phien shift = iterator.next();
                if (!shift.getAccount().getNhanVien().getMaNV().equals(emloyeeID)) {
                    iterator.remove(); // Sử dụng iterator để xóa phần tử
                }
            }
        }

        // Lọc theo `role`
        if (!role.equals("Tất Cả")) {
            Iterator<Phien> iterator = list.iterator();
            while (iterator.hasNext()) {
                Phien shift = iterator.next();
                if (!shift.getAccount().getNhanVien().getChucVu().equals(role)) {
                    iterator.remove(); // Sử dụng iterator để xóa phần tử
                }
            }
        }

        return list;
    }
}
