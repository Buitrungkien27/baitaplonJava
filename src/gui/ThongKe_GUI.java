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
    private DefaultCategoryDataset dataset;
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
        mainPanel = new JPanel(new CardLayout());

        JPanel thongKePanel = new JPanel();
        thongKePanel.setLayout(new BoxLayout(thongKePanel, BoxLayout.Y_AXIS));

        JPanel filterPanel = new JPanel();
        JComboBox<String> monthComboBox = new JComboBox<>(new String[]{
                "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", 
                "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        });
        JComboBox<String> yearComboBox = new JComboBox<>(new String[]{"2023", "2024", "2025"});
        filterPanel.add(new JLabel("Lọc"));
        filterPanel.add(monthComboBox);
        filterPanel.add(yearComboBox);

        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        JLabel lblTongHoaDon = new JLabel("Tổng số hóa đơn:");
        txtTongHoaDon = new JTextField(10);
        JLabel lblTongHoaDonDoiTra = new JLabel("Tổng hóa đơn đổi trả:");
        txtHoaDonDoiTra = new JTextField(10);
        JLabel lblTongDoanhThu = new JLabel("Tổng doanh thu:");
        txtDoanhThu = new JTextField(10);

        infoPanel.add(lblTongHoaDon);
        infoPanel.add(txtTongHoaDon);
        infoPanel.add(lblTongHoaDonDoiTra);
        infoPanel.add(txtHoaDonDoiTra);
        infoPanel.add(lblTongDoanhThu);
        infoPanel.add(txtDoanhThu);

        thongKePanel.add(filterPanel);
        thongKePanel.add(infoPanel);

        dataset = new DefaultCategoryDataset();
        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê doanh thu theo ngày",
                "Ngày",
                "Doanh thu",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        CategoryPlot plot = barChart.getCategoryPlot();
        CategoryAxis domainAxis = plot.getDomainAxis();
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        domainAxis.setCategoryMargin(0.2);
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);
        renderer.setMaximumBarWidth(0.1);
        renderer.setSeriesPaint(0, Color.GREEN);

        ChartPanel chartPanel = new ChartPanel(barChart);
        thongKePanel.add(chartPanel);

        mainPanel.add(thongKePanel, "ThongKe");
        add(mainPanel, BorderLayout.CENTER);

        monthComboBox.addActionListener(e -> loadThongKeData(monthComboBox, yearComboBox));
        yearComboBox.addActionListener(e -> loadThongKeData(monthComboBox, yearComboBox));
    }

    private void init() {
        // Thiết lập ban đầu nếu cần
    }

    private void loadThongKeData(JComboBox<String> monthComboBox, JComboBox<String> yearComboBox) {
        String selectedMonth = monthComboBox.getSelectedItem().toString();
        String selectedYear = yearComboBox.getSelectedItem().toString();
        int month = Integer.parseInt(selectedMonth.split(" ")[1]);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT DAY(ngayLap) AS ngay, SUM(tongTien) AS doanhThu FROM HoaDon " +
                         "WHERE MONTH(ngayLap) = ? AND YEAR(ngayLap) = ? GROUP BY DAY(ngayLap)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, month);
                pstmt.setInt(2, Integer.parseInt(selectedYear));
                ResultSet rs = pstmt.executeQuery();

                dataset.clear();
                while (rs.next()) {
                    int day = rs.getInt("ngay");
                    double dailyRevenue = rs.getDouble("doanhThu");
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

            String sqlHoaDonDoiTra = "SELECT COUNT(*) AS tongHoaDonDoiTra FROM HoaDonDoiTra WHERE MONTH(ngayLap) = ? AND YEAR(ngayLap) = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlHoaDonDoiTra)) {
                pstmt.setInt(1, month);
                pstmt.setInt(2, year);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int tongHoaDonDoiTra = rs.getInt("tongHoaDonDoiTra");
                    txtHoaDonDoiTra.setText(String.valueOf(tongHoaDonDoiTra));
                }
            }

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
