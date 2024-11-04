/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connectDB.ConnectDB;
import entity.Account;
import entity.NhanVien;
import interfaces.DAOBase;
import java.util.ArrayList;
import java.sql.*;


public class NhanVien_DAO implements DAOBase<NhanVien> {

    public static String getMaxSequence(String prefix) {
        try {
        prefix += "%";
        String sql = "  SELECT TOP 1  * FROM NhanVien WHERE maNV LIKE '"+prefix+"' ORDER BY maNV DESC;";
        PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            String maNV = rs.getString("maNV");
            return maNV;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }

    @Override
    public NhanVien getOne(String id) {
    	NhanVien nv = null;

        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("Select * from NhanVien where maNV = ?");
            st.setString(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String tenNV = rs.getString("tenNV");
                String chucVu = rs.getString("chucVu");
                Boolean trangThai = rs.getBoolean("trangThai");
                Date ngaySinh = rs.getDate("ngaySinh");
                Boolean gioiTinh = rs.getBoolean("gioiTinh");
                String sdt = rs.getString("sdt");
                String cccd = rs.getString("cccd");
                Date ngayVaoLam = rs.getDate("ngayVaoLam");
                String diaChi = rs.getString("diaChi");

                nv = new NhanVien(id, cccd, chucVu, trangThai, tenNV, sdt, gioiTinh, ngaySinh, diaChi, ngayVaoLam);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return nv;
    }

    @Override
    public ArrayList<NhanVien> getAll() {
        ArrayList<NhanVien> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from NhanVien");
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                String tenNV = rs.getString("tenNV");
                String chucVu = rs.getString("chucVu");
                Boolean trangThai = rs.getBoolean("trangThai");
                Date ngaySinh = rs.getDate("ngaySinh");
                Boolean gioiTinh = rs.getBoolean("gioiTinh");
                String sdt = rs.getString("sdt");
                String cccd = rs.getString("cccd");
                Date ngayVaoLam = rs.getDate("ngayVaoLam");
                String diaChi = rs.getString("diaChi");

                NhanVien nv = new NhanVien(maNV, cccd, chucVu, trangThai, tenNV, sdt, gioiTinh, ngaySinh, diaChi, ngayVaoLam);
                result.add(nv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String generateID() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean create(NhanVien object) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("INSERT INTO [dbo].[NhanVien]"
                    + "([maNV],[tenNV],[chucVu],[trangThai],[ngaySinh],[gioiTinh],[sdt],[cccd],[ngayVaoLam],[diaChi])"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            st.setString(1, object.getMaNV());
            st.setString(2, object.getTenNV());
            st.setString(3, object.getChucVu());
            st.setBoolean(4, object.getTrangThai());
            st.setDate(5, new java.sql.Date(object.getNgaySinh().getTime()));
            st.setBoolean(6, object.isGioiTinh());
            st.setString(7, object.getSdt());
            st.setString(8, object.getCccd());
            st.setDate(9, new java.sql.Date(object.getNgayVaoLam().getTime()));
            st.setString(10, object.getDiaChi());

            n = st.executeUpdate();
            String password = "e10adc3949ba59abbe56e057f20f883e";
            Account account = new Account(password, object);
            new Account_DAO().create(account);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, NhanVien newObject) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("UPDATE [dbo].[NhanVien]"
                    + "   SET [maNV] = ?"
                    + "      ,[tenNV] = ?"
                    + "      ,[chucVu] = ?"
                    + "      ,[trangThai] = ?"
                    + "      ,[ngaySinh] = ?"
                    + "      ,[gioiTinh] = ?"
                    + "      ,[sdt] = ?"
                    + "      ,[cccd] = ?"
                    + "      ,[ngayVaoLam] = ?"
                    + "      ,[diaChi] = ?"
                    + " WHERE maNV = ?");
            st.setString(1, id);
            st.setString(2, newObject.getMaNV());
            st.setString(3, newObject.getChucVu());
            st.setBoolean(4, newObject.getTrangThai());
            st.setDate(5, new java.sql.Date(newObject.getNgaySinh().getTime()));
            st.setBoolean(6, newObject.isGioiTinh());
            st.setString(7, newObject.getSdt());
            st.setString(8, newObject.getCccd());
            st.setDate(9, new java.sql.Date(newObject.getNgaySinh().getTime()));
            st.setString(10, newObject.getDiaChi());
            st.setString(11, id);

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
    
    public ArrayList<NhanVien> findById(String searchQuery) {
        ArrayList<NhanVien> result = new ArrayList<>();
        String query = """
                       SELECT * FROM NhanVien
                       where maNV LIKE ?
                       """;
        try {

            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            st.setString(1, searchQuery + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    result.add(getNhanVienData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    public ArrayList<NhanVien> filter(int role, int status) {
        ArrayList<NhanVien> result = new ArrayList<>();
//        Index tự động tăng phụ thuộc vào số lượng biến số có
        int index = 1;
        String query = "select * from NhanVien WHERE tenNV like '%'";
//        Xét chức vụ nhân viên
        if (role != 0)
            query += " and chucVu = ?";
//            Xét trạng thái làm việc
        if (status != 0)
            query += " and trangThai = ?";
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement(query);
            //st.setString(index++, name + "%");
            if(role == 1)
                st.setString(index++, "Nhân Viên Bán Vé");
            else if(role == 2)
                st.setString(index++, "Quản Lí");
            if(status == 1)
                st.setInt(index++, 1);
            else if(status == 2)
                st.setInt(index++, 0);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                if (rs != null) {
                    result.add(getNhanVienData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    
    private NhanVien getNhanVienData(ResultSet rs) throws SQLException, Exception {
    	NhanVien result = null;
        //Lấy thông tin tổng quát của lớp employee
    	String maNV = rs.getString("maNV");
        String tenNV = rs.getString("tenNV");
        String chucVu = rs.getString("chucVu");
        Boolean trangThai = rs.getBoolean("trangThai");
        Date ngaySinh = rs.getDate("ngaySinh");
        Boolean gioiTinh = rs.getBoolean("gioiTinh");
        String sdt = rs.getString("sdt");
        String cccd = rs.getString("cccd");
        Date ngayVaoLam = rs.getDate("ngayVaoLam");
        String diaChi = rs.getString("diaChi");
        
        result = new NhanVien(maNV, cccd, chucVu, trangThai, tenNV, sdt, gioiTinh, ngaySinh, diaChi, ngayVaoLam);
        return result;
    }

    public boolean createAccount(NhanVien nv) throws Exception {
        Account_DAO account_dao = new Account_DAO();
        Account account = new Account(nv);
        if(account_dao.create(account))
            return true;
        return false;
    }
}
