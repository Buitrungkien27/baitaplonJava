package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    public static Connection conn = null;

    public static void connect() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=QLBanVe";
        String user = "sa";
        String password = "sa123"; // Thay thế bằng mật khẩu thực tế của bạn
        
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return conn;}

    public static void disconnect() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Đã ngắt kết nối!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
