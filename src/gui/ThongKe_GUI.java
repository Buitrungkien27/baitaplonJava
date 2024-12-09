package gui;

import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.*;
import java.sql.*;

public final class ThongKe_GUI extends JPanel {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private DefaultCategoryDataset dataset; // Dữ liệu biểu đồ
    private JTextField txtTongHoaDon, txtHoaDonDoiTra, txtDoanhThu;

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QLBanVe";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa123";

    public ThongKe_GUI() {
        initComponents();
        init();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tạo màn hình thống kê
        JPanel thongKePanel = new JPanel();
        thongKePanel.setLayout(new BoxLayout(thongKePanel, BoxLayout.Y_AXIS));

        // Phần lọc tháng và năm
        JPanel filterPanel = new JPanel();
        JComboBox<String> monthComboBox = new JComboBox<>(new String[]{"Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"});
        JComboBox<String> yearComboBox = new JComboBox<>(new String[]{"2023", "2024", "2025"});
        filterPanel.add(new JLabel("Lọc"));
        filterPanel.add(monthComboBox);
        filterPanel.add(yearComboBox);

        // Tạo bảng thống kê chi tiết
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(3, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        // Tổng số hóa đơn
        JLabel lblTongHoaDon = new JLabel("Tổng số hóa đơn:");
        txtTongHoaDon = new JTextField(10);

        // Tổng hóa đơn đổi trả
        JLabel lblTongHoaDonDoiTra = new JLabel("Tổng hóa đơn đổi trả:");
        txtHoaDonDoiTra = new JTextField(10);

        // Tổng doanh thu tháng
        JLabel lblTongDoanhThu = new JLabel("Tổng doanh thu tháng:");
        txtDoanhThu = new JTextField(10);

        infoPanel.add(lblTongHoaDon);
        infoPanel.add(txtTongHoaDon);
        infoPanel.add(lblTongHoaDonDoiTra);
        infoPanel.add(txtHoaDonDoiTra);
        infoPanel.add(lblTongDoanhThu);
        infoPanel.add(txtDoanhThu);

        // Thêm bảng thống kê vào giao diện chính
        thongKePanel.add(filterPanel);
        thongKePanel.add(infoPanel);

        // Tạo biểu đồ cột cho doanh thu theo ngày
        dataset = new DefaultCategoryDataset();

        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê doanh thu theo ngày",
                "Ngày",  // Label cho trục x (ngày)
                "Doanh thu",  // Label cho trục y (doanh thu)
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Tùy chỉnh plot để đảm bảo việc căn chỉnh tốt hơn
        CategoryPlot plot = barChart.getCategoryPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();

        // Điều chỉnh khoảng cách giữa các cột và nhãn
        domainAxis.setCategoryMargin(0.2);
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);

        // Tùy chỉnh BarRenderer để giảm độ rộng cột
        renderer.setMaximumBarWidth(0.1);

        // Đặt màu cho các cột
        renderer.setSeriesPaint(0, Color.GREEN);

        ChartPanel chartPanel = new ChartPanel(barChart);

        // Thêm biểu đồ vào panel
        thongKePanel.add(chartPanel);

        // Thêm các panel vào mainPanel
        mainPanel.add(thongKePanel, "ThongKe");

        add(mainPanel, BorderLayout.CENTER);

        // Lắng nghe sự thay đổi của monthComboBox và yearComboBox
        monthComboBox.addActionListener(e -> loadThongKeData(monthComboBox, yearComboBox));
        yearComboBox.addActionListener(e -> loadThongKeData(monthComboBox, yearComboBox));
    }

    private void init() {
        // Các thiết lập khác nếu cần
    }

    private void loadThongKeData(JComboBox<String> monthComboBox, JComboBox<String> yearComboBox) {
        String selectedMonth = monthComboBox.getSelectedItem().toString();  // Tháng người dùng chọn
        String selectedYear = yearComboBox.getSelectedItem().toString();    // Năm người dùng chọn

        int month = Integer.parseInt(selectedMonth.split(" ")[1]);
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT DAY(ngayLap) AS ngay, SUM(tongTien) AS doanhThu FROM HoaDon WHERE MONTH(ngayLap) = ? AND YEAR(ngayLap) = ? GROUP BY DAY(ngayLap)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, month);
                pstmt.setInt(2, Integer.parseInt(selectedYear));
                ResultSet rs = pstmt.executeQuery();

                // Xóa dữ liệu cũ trên biểu đồ
                dataset.clear();

                // Lưu doanh thu từng ngày vào biểu đồ
                while (rs.next()) {
                    int day = rs.getInt("ngay");  // Ngày
                    double dailyRevenue = rs.getDouble("doanhThu");  // Doanh thu ngày

                    // Thêm dữ liệu vào dataset cho biểu đồ
                    dataset.addValue(dailyRevenue, "Doanh thu", "Ngày " + day);
                }

                updateThongKeSummary(month, Integer.parseInt(selectedYear));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateThongKeSummary(int month, int year) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Truy vấn tổng số hóa đơn
            String sqlHoaDon = "SELECT COUNT(*) AS tongHoaDon FROM HoaDon WHERE MONTH(ngayLap) = ? AND YEAR(ngayLap) = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlHoaDon)) {
                pstmt.setInt(1, month);
                pstmt.setInt(2, year);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int tongHoaDon = rs.getInt("tongHoaDon");
                    txtTongHoaDon.setText(String.valueOf(tongHoaDon));
                }
            }

            // Truy vấn tổng số hóa đơn đổi trả
            String sqlHoaDonDoiTra = "SELECT COUNT(*) AS tongHoaDonDoiTra FROM HoaDon WHERE MONTH(ngayLap) = ? AND YEAR(ngayLap) = ? AND trangThai = 0";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlHoaDonDoiTra)) {
                pstmt.setInt(1, month);
                pstmt.setInt(2, year);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int tongHoaDonDoiTra = rs.getInt("tongHoaDonDoiTra");
                    txtHoaDonDoiTra.setText(String.valueOf(tongHoaDonDoiTra));
                }
            }

            // Truy vấn tổng doanh thu
            String sqlDoanhThu = "SELECT SUM(tongTien) AS tongDoanhThu FROM HoaDon WHERE MONTH(ngayLap) = ? AND YEAR(ngayLap) = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDoanhThu)) {
                pstmt.setInt(1, month);
                pstmt.setInt(2, year);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    double tongDoanhThu = rs.getDouble("tongDoanhThu");
                    txtDoanhThu.setText(String.format("%.0f", tongDoanhThu));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
