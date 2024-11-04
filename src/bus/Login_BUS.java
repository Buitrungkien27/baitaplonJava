package bus;
import dao.Account_DAO;
import dao.NhanVien_DAO;
import entity.Account;
import entity.NhanVien;

import java.util.regex.Pattern;
import utilities.PasswordHash;

public class Login_BUS {

    private final Account_DAO tkDAO = new Account_DAO();
    private final NhanVien_DAO nvDAO = new NhanVien_DAO();

    public NhanVien login(String id, String password) throws Exception {
        Account acc = tkDAO.getOne(id);
        if (!nvDAO.getOne(id).getTrangThai()) {
            throw new Exception("Tài khoản đã bị vô hiệu hóa doa nhân viên đã thôi việc!");

        }
        if (acc == null) {
            throw new Exception("Tài khoản không tồn tại!");
        } else if (!PasswordHash.comparePasswords(password, acc.getPassWord())) {
            throw new Exception("Mật khẩu không chính xác!");
        } else {
            return nvDAO.getOne(acc.getNhanVien().getMaNV());
        }
    }

    public boolean changePassword(String id, String pass, String passNew) throws Exception {

//        System.out.println(PasswordHash.hashPassword(pass));
        pass = PasswordHash.hashPassword(pass);
        String passOld = tkDAO.getOne(id).getPassWord();
//        System.out.println("Pass csdl: "+passOld);
        if (!pass.equals(passOld)) {
            throw new Exception("Mật khẩu không chính xác");
        }
        Account acc = tkDAO.getOne(id);
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
        if (!Pattern.matches(regex, passNew)) {
            throw new Exception("Mật khẩu phải ít nhất có 8 kí tự, bao gồm chữ hoa, chữ thường và số");
        }
        String passNewHash = PasswordHash.hashPassword(passNew);
        if (acc == null) {
            throw new Exception("Tài khoản không tồn tại!");
        } else if (acc.getPassWord().equals(passNewHash)) {
            throw new Exception("Mật khẩu không chính xác!");
        }
        return tkDAO.updatePass(acc.getNhanVien().getMaNV(), passNewHash);
    }

}
