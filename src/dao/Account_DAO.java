
package dao;

import connectDB.ConnectDB;
import entity.Account;
import entity.NhanVien;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Account_DAO implements DAOBase<Account> {

//    Lấy tài khoản dựa vào mã nhân viên
    @Override
    public Account getOne(String id) {
        Account result = null;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from TaiKhoan where maNV = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String password = rs.getString("password");
                result = new Account(password, new NhanVien(maNV));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(Account_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean validateAccount(String id, String password) {
        boolean isValid = false;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from TaiKhoan where maNV = ? and password = ?");
            st.setString(1, id);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                isValid = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(Account_DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isValid;
    }

    @Override
    public ArrayList<Account> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(Account object) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("insert into TaiKhoan "
                    + " values(?,?)");
            st.setString(1, object.getNhanVien().getMaNV());
            st.setString(2, object.getPassWord());

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Account newObject) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("update TaiKhoan set "
                    + "password = ? where maNV = ?");
            st.setString(1, id);
            st.setString(2, newObject.getPassWord());

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public Boolean updatePass(String id, String newPass) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("update TaiKhoan set "
                    + "password = ? where maNV = ?");
            st.setString(1, newPass);
            st.setString(2, id);

            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
