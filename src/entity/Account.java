/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author KienTran
 */
public class Account {
    
    private final String PASSWORD_ERROR = "Mật khẩu phải ít nhất 8 kí tự (Bao gồm chữ hoa, chữ thường và số)!";
    private final String EMPLOYEE_ERROR = "Employee không được rỗng !";

  
    private String passWord;
    private NhanVien nv;

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) throws Exception {
//        String regex = "^[A-Z][a-zA-Z0-9.,@&*^]{7,}.*\\d.*";
//        if(Pattern.matches(regex, passWord))
            this.passWord = passWord;
//        else
//            throw new Exception(PASSWORD_ERROR);
    }

    public NhanVien getNhanVien() {
        return nv;
    }

    public void setEmployee(NhanVien nv) throws Exception{
//        if(employee!=null)
            this.nv = nv;
//        else
//            throw new Exception(EMPLOYEE_ERROR);
    }

    public Account(String passWord, NhanVien nv) throws Exception {
        setPassWord(passWord);
        setEmployee(nv);
    }
    
    public Account(NhanVien nv) throws Exception {
        setEmployee(nv);
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" + "PASSWORD_ERROR=" + PASSWORD_ERROR + ", EMPLOYEE_ERROR=" + EMPLOYEE_ERROR + ", passWord=" + passWord + ", nhanVien=" + nv + '}';
    }
    
    
    
}
