package dao;
import java.sql.*;
import entity.KhachHang;

import java.util.ArrayList;
import connectDB.ConnectDB;

public class KhachHang_DAO implements interfaces.DAOBase<KhachHang> {

    public KhachHang_DAO() {
    }

    @Override
    public KhachHang getOne(String maKh) {
        KhachHang kh = null;
        try {
            String sql = "SELECT * FROM KhachHang WHERE maKH = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, maKh);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String tenKH = resultSet.getString("tenKH");
                Date ns = resultSet.getDate("ngaySinh");
                boolean gt = resultSet.getBoolean("gioiTinh");
                String sdt = resultSet.getString("sdt");
                String cccd = resultSet.getString("cccd");
                String dc = resultSet.getString("diaChi");
                // In ra để kiểm tra
                System.out.println("Tên KH: " + tenKH);
                System.out.println("Ngày sinh: " + ns);
                System.out.println("Giới tính: " + gt);
                System.out.println("SĐT: " + sdt);
                System.out.println("CCCD: " + cccd);  
                System.out.println("Địa chỉ: " + dc);
                kh = new KhachHang(maKh, tenKH, gt, ns, sdt, dc, cccd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    public KhachHang getByPhone(String phone) {
    	KhachHang kh = null;
        try {
            String sql = "SELECT * FROM KhachHang WHERE sdt = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, phone);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String maKH = resultSet.getString("maKH");
                String tenKH = resultSet.getString("tenKH");
                Date ns = resultSet.getDate("ngaySinh");
                boolean gt = resultSet.getBoolean("gioiTinh");
                String sdt = resultSet.getString("sdt");
                String cccd = resultSet.getString("cccd");
                String dc = resultSet.getString("diaChi");

                kh = new KhachHang(maKH, tenKH, gt, ns, sdt, cccd, dc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    public KhachHang getOneByNumberPhone(String phoneNumber) {
    	KhachHang kh = null;
        try {
            String sql = "SELECT * FROM KhachHang WHERE sdt = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, phoneNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
            	String maKH = resultSet.getString("maKH");
                String tenKH = resultSet.getString("tenKH");
                Date ns = resultSet.getDate("ngaySinh");
                boolean gt = resultSet.getBoolean("gioiTinh");
                String cccd = resultSet.getString("cccd");
                String dc = resultSet.getString("diaChi");

                kh = new KhachHang(maKH, tenKH, gt, ns, phoneNumber, dc, cccd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kh;
    }

    @Override
    public ArrayList<KhachHang> getAll() {
        ArrayList<KhachHang> result = new ArrayList<>();
        try {
            Statement statement = ConnectDB.conn.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM KhachHang");

            while (resultSet.next()) {
            	String maKH = resultSet.getString("maKH");
                String tenKH = resultSet.getString("tenKH");
                Date ns = resultSet.getDate("ngaySinh");
                boolean gt = resultSet.getBoolean("gioiTinh");
                String sdt = resultSet.getString("sdt");
                String cccd = resultSet.getString("cccd");
                String dc = resultSet.getString("diaChi");

                KhachHang kh = new KhachHang(maKH, tenKH, gt, ns, sdt, dc, cccd);
                result.add(kh);
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

    public String getMaxSequence(String code) {
        String maKH = null; // Khởi tạo biến maKH
        try {
            code += "%";
            String sql = "SELECT TOP 1 * FROM KhachHang WHERE maKH LIKE ? ORDER BY maKH DESC;";
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            st.setString(1, code); // Sử dụng PreparedStatement để tránh SQL Injection
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                maKH = rs.getString("maKH"); // Lấy mã khách hàng
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In ra thông báo lỗi
        }
        return maKH; // Trả về mã khách hàng hoặc null nếu không tìm thấy
    }


    @Override
    public Boolean create(KhachHang object) {
    	System.out.println("Test 2:" + object.getSdt());
        try {
            String phoneCheck = "select * from KhachHang where sdt = ?";
            PreparedStatement phoneStatement = ConnectDB.conn.prepareStatement(phoneCheck);
            phoneStatement.setString(1, object.getSdt());
            if (phoneStatement.executeQuery().next()) {
                return false;
            }
           
            String sql = "INSERT INTO KhachHang (maKH, tenKH, ngaySinh, gioiTinh, sdt, cccd, diaChi,email) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?,'test@gmail.com')";
            System.out.printf(object.getDiaChi());
            
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            preparedStatement.setString(1, object.getMaKH());
            preparedStatement.setString(2, object.getTenKH());
            preparedStatement.setDate(3, new java.sql.Date(object.getNgaySinh().getTime()));
            preparedStatement.setBoolean(4, object.isGioiTinh());
            preparedStatement.setString(5, object.getSdt());
            preparedStatement.setString(6, object.getCccd());
            preparedStatement.setString(7, object.getDiaChi() != null ? object.getDiaChi() : "");

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean update(String id, KhachHang newObject) {
        try {
            String sql = "UPDATE KhachHang SET tenKH=?, ngaySinh=?, gioiTinh=?, sdt=?, cccd=?,  diaChi=? "
                    + "WHERE maKH=?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);

            preparedStatement.setString(1, newObject.getTenKH());
            preparedStatement.setDate(2, new java.sql.Date(newObject.getNgaySinh().getTime()));
            preparedStatement.setBoolean(3, newObject.isGioiTinh());
            preparedStatement.setString(4, newObject.getSdt());
            preparedStatement.setString(5, newObject.getCccd());
            preparedStatement.setString(6, newObject.getDiaChi());
            preparedStatement.setString(7, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int countMaleCustomers() {
        int count = 0;
        try (Statement statement = ConnectDB.conn.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) AS maleCount FROM KhachHang WHERE gioiTinh = 1")) {

            if (resultSet.next()) {
                count = resultSet.getInt("maleCount");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Xử lý Exception, ví dụ: ghi log hoặc ném lên lớp gọi để xử lý chính xác hơn
        }
        return count;
    }

}
