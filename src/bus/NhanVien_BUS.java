package bus;
import dao.Account_DAO;
import dao.NhanVien_DAO;
import entity.Employee;
import entity.NhanVien;
import entity.Store;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NhanVien_BUS {
    private NhanVien_DAO dao = new NhanVien_DAO();
    private Account_DAO accountDAO = new Account_DAO();
    
    public String generateID(boolean gender, Date dateOfBirth, Date dateStart) {
        //Khởi tạo mã nhâm viên NV
        String prefix = "NV";
        //Kí tự tiếp theo là giới tính
        if(gender)
            prefix += 1;
        else
            prefix += 0;
        //4 kí tự tiếp theo là ngày sinh của nhân viên
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        simpleDateFormat.applyPattern("yyyy");
        String formatBirth = simpleDateFormat.format(dateOfBirth);
        String formatStart = simpleDateFormat.format(dateStart);
        
        prefix += formatBirth;
        //4 kí tự tiếp theo là ngày tháng 
        prefix += formatStart;
        //Tìm mã có tiền tố là code và xxx lớn nhất
        String maxID = NhanVien_DAO.getMaxSequence(prefix);
        if (maxID == null) {
            prefix += "000";
        } else {
            String lastFourChars = maxID.substring(maxID.length() - 4);
            int num = Integer.parseInt(lastFourChars);
            num++;
            prefix += String.format("%04d", num);
        }
        return prefix;
    }
    public ArrayList<NhanVien> getAllNhanVien(){
        ArrayList<NhanVien> employeeList = new NhanVien_DAO().getAll();
        return employeeList;
    }
    public NhanVien getNhanVien(String maNV) {
        return dao.getOne(maNV);
    }
    public ArrayList<NhanVien> searchById(String searchQuery) {
        return dao.findById(searchQuery);
    }
    public ArrayList<NhanVien> filter(int cv, int tt) {
        return dao.filter(cv, tt);
    }

    public boolean addNewNV(NhanVien nv) {
        return dao.create(nv);
    }

//    public String getPassword(NhanVien nv) throws Exception {
//        return dao.getPassword(nv);
//    }

    public boolean createNewAccount(NhanVien nv) throws Exception {
        return dao.createAccount(nv);
    }

    public boolean updateNV(NhanVien nvMoi) {
        return dao.update(nvMoi.getMaNV(), nvMoi);
    }

    public boolean updatePassword(String id) {
        return accountDAO.updatePass(id, "3e6c7d141e32189c917761138b026b74");
    }
}
