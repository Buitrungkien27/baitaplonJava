/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connectDB.*;
import entity.Account;
import entity.Phien;
import interfaces.DAOBase;
import java.security.CodeSigner;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class Phien_DAO implements DAOBase<Phien> {

    private NhanVien_DAO employee_DAO = new NhanVien_DAO();

    @Override
    public Phien getOne(String id) {
    	Phien shift = null;
        try {
            String sql = "SELECT * FROM Phien WHERE maPhien = ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setString(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String maNV = rs.getString("maNV");
                String maPhien = rs.getString("maPhien");
                Timestamp startTimestamp = rs.getTimestamp("thoiGianBD");
                Timestamp endTimestamp = rs.getTimestamp("thoiGianKT");

                Date started = new java.sql.Date(startTimestamp.getTime());
                Date ended = new java.sql.Date(endTimestamp.getTime());

                shift = new Phien(maPhien, started, ended, new Account(employee_DAO.getOne(maNV)));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shift;
    }

   public ArrayList<Phien> getShiftsByDate(Date date) {
        ArrayList<Phien> shifts = new ArrayList<>();

        try {
            // Chuyển đổi ngày thành LocalDate để sử dụng trong truy vấn SQL
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Timestamp startOfDay = Timestamp.valueOf(localDate.atStartOfDay());
            Timestamp endOfDay = Timestamp.valueOf(localDate.plusDays(1).atStartOfDay().minusSeconds(1));

            String sql = "SELECT * FROM Phien WHERE thoiGianBD >= ? AND thoiGianBD <= ?";
            PreparedStatement preparedStatement = ConnectDB.conn.prepareStatement(sql);
            preparedStatement.setTimestamp(1, startOfDay);
            preparedStatement.setTimestamp(2, endOfDay);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String maPhien = rs.getString("maPhien");
                String maNV = rs.getString("maNV");
                Timestamp startTimestamp = rs.getTimestamp("thoiGianBD");
                Timestamp endTimestamp = rs.getTimestamp("thoiGianKT");

                Date started = new java.sql.Date(startTimestamp.getTime());
                Date ended = new java.sql.Date(endTimestamp.getTime());

                Phien shift = new Phien(maPhien, started, ended, new Account(employee_DAO.getOne(maNV)));
                shifts.add(shift);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return shifts;
    }

    @Override
    public ArrayList<Phien> getAll() {
        ArrayList<Phien> result = new ArrayList<>();
        try {
            Statement st = ConnectDB.conn.createStatement();
            ResultSet rs = st.executeQuery("Select * from Phien");
            while (rs.next()) {
                String shiftID = rs.getString("maPhien");
                result.add(getOne(shiftID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String generateID() {
        String code = "PH";
        String maxID = "";
        String newID = "";
        Date date = new Date();
        // Tạo một đối tượng SimpleDateFormat với định dạng chỉ chứa giờ
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        // Sử dụng đối tượng SimpleDateFormat để chuyển đổi Date thành chuỗi giờ
        String hour = sdf.format(date);
        if (Integer.parseInt(hour) < 14) {
            code += "01";
        } else {
            code += "02";
        }
        SimpleDateFormat datef = new SimpleDateFormat("ddMMyyyy");
        code += datef.format(date);
        newID = code;
        try {
            code += "%";
            String sql = "  SELECT TOP 1  * FROM [Phien] WHERE maPhien LIKE '" + code + "' ORDER BY maPhien DESC;";
            PreparedStatement st = ConnectDB.conn.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                maxID = rs.getString("maPhien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (maxID.equals("")) {
            newID += "0000";
        } else {
            String lastFourChars = maxID.substring(maxID.length() - 4);
            int num = Integer.parseInt(lastFourChars);
            num++;
            newID += String.format("%04d", num);
        }
        return newID;

    }

    @Override
    public Boolean create(Phien shift) {
        int n = 0;
        try {
            PreparedStatement st = ConnectDB.conn.prepareStatement("insert into Phien(maPhien, thoiGianBD, thoiGianKT, maNV) "
                    + " values(?,?,?,?)");
            st.setString(1, shift.getShiftID());

//            System.out.println(shift.getAccount().getNhanVien().getMaNV());
            Timestamp end = new Timestamp(shift.getStartedAt().getTime());
            st.setTimestamp(2, end);

            Timestamp start = new Timestamp(shift.getEndedAt().getTime());
            st.setTimestamp(3, start);
            
            st.setString(4, shift.getAccount().getNhanVien().getMaNV());
            n = st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    @Override
    public Boolean update(String id, Phien newObject) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
