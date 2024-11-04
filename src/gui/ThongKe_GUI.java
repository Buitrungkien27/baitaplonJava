package gui;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import java.awt.Color;

public final class ThongKe_GUI extends JPanel {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JTable tableThongKe;
    private DefaultTableModel modelThongKe;
    private DefaultCategoryDataset dataset; // Dữ liệu biểu đồ

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
        JTextField txtTongHoaDon = new JTextField("50", 10);
        

        // Tổng hóa đơn đổi trả
        JLabel lblTongHoaDonDoiTra = new JLabel("Tổng hóa đơn đổi trả:");
        JTextField txtHoaDonDoiTra = new JTextField("4", 10);


        // Tổng doanh thu tháng
        JLabel lblTongDoanhThu = new JLabel("Tổng doanh thu tháng:");
        JTextField txtDoanhThu = new JTextField("2.000.000.000", 10);
        

        infoPanel.add(lblTongHoaDon);
        infoPanel.add(txtTongHoaDon);
        infoPanel.add(lblTongHoaDonDoiTra);
        infoPanel.add(txtHoaDonDoiTra);
        infoPanel.add(lblTongDoanhThu);
        infoPanel.add(txtDoanhThu);
        

        // Thêm bảng thống kê vào giao diện chính
        thongKePanel.add(filterPanel);
        thongKePanel.add(infoPanel);

        // Tạo bảng thống kê có 3 cột: Loại vé, Số lượng, Số tiền
        String[] columnNames = {"Loại vé", "Số lượng", "Số tiền"};
        Object[][] data = {
            {"Ngồi cứng", 50, 2500000},  // Giá vé 50,000
            {"Ngồi mềm", 30, 3000000},   // Giá vé 100,000
            {"Nằm cứng", 20, 3000000},   // Giá vé 150,000
            {"Nằm mềm", 10, 2000000}     // Giá vé 200,000
        };
        modelThongKe = new DefaultTableModel(data, columnNames);
        tableThongKe = new JTable(modelThongKe);
        JScrollPane scrollPaneThongKe = new JScrollPane(tableThongKe);

        // Tạo biểu đồ cột cho số lượng vé với nhãn 4 loại vé trên trục X
        dataset = new DefaultCategoryDataset();
        dataset.addValue(50, "Ngồi cứng", "Ngồi cứng");
        dataset.addValue(30, "Ngồi mềm", "Ngồi mềm");
        dataset.addValue(20, "Nằm cứng", "Nằm cứng");
        dataset.addValue(10, "Nằm mềm", "Nằm mềm");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Thống kê vé và doanh thu",
                "Loại vé",  // Label cho trục x (loại vé)
                "Số lượng vé",  // Label cho trục y (số lượng)
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
        renderer.setSeriesPaint(0, Color.GREEN);  // Ngồi cứng
        renderer.setSeriesPaint(1, Color.YELLOW); // Ngồi mềm
        renderer.setSeriesPaint(2, Color.ORANGE); // Nằm cứng
        renderer.setSeriesPaint(3, Color.PINK);   // Nằm mềm

        ChartPanel chartPanel = new ChartPanel(barChart);

        // Thêm bảng và biểu đồ vào panel
        thongKePanel.add(scrollPaneThongKe);
        thongKePanel.add(chartPanel);
        
        // Thêm các panel vào mainPanel
        mainPanel.add(thongKePanel, "ThongKe");

        add(mainPanel, BorderLayout.CENTER);

        // Lắng nghe sự thay đổi của bảng để tính tiền tự động khi người dùng thay đổi số lượng
        modelThongKe.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 1) {  // Chỉ lắng nghe thay đổi ở cột "Số lượng"
                    updateTien();
                    updateChart();  // Cập nhật biểu đồ khi thay đổi dữ liệu
                }
            }
        });
    }

    private void init() {
        // Các thiết lập khác nếu cần
    }

    // Hàm tính tiền và cập nhật cột "Số tiền" dựa trên số lượng vé
    private void updateTien() {
        try {
            int ngoiCung = Integer.parseInt(tableThongKe.getValueAt(0, 1).toString());
            int ngoiMem = Integer.parseInt(tableThongKe.getValueAt(1, 1).toString());
            int namCung = Integer.parseInt(tableThongKe.getValueAt(2, 1).toString());
            int namMem = Integer.parseInt(tableThongKe.getValueAt(3, 1).toString());

            // Tính tổng tiền theo giá vé
            int tongTienNgoiCung = ngoiCung * 50000;
            int tongTienNgoiMem = ngoiMem * 100000;
            int tongTienNamCung = namCung * 150000;
            int tongTienNamMem = namMem * 200000;

            // Cập nhật cột "Số tiền" trong bảng
            modelThongKe.setValueAt(tongTienNgoiCung, 0, 2);
            modelThongKe.setValueAt(tongTienNgoiMem, 1, 2);
            modelThongKe.setValueAt(tongTienNamCung, 2, 2);
            modelThongKe.setValueAt(tongTienNamMem, 3, 2);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng hợp lệ!", "Lỗi nhập dữ liệu", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Hàm cập nhật biểu đồ dựa trên số lượng vé
    private void updateChart() {
        try {
            int ngoiCung = Integer.parseInt(tableThongKe.getValueAt(0, 1).toString());
            int ngoiMem = Integer.parseInt(tableThongKe.getValueAt(1, 1).toString());
            int namCung = Integer.parseInt(tableThongKe.getValueAt(2, 1).toString());
            int namMem = Integer.parseInt(tableThongKe.getValueAt(3, 1).toString());

            // Cập nhật dataset của biểu đồ với nhãn 4 loại vé
            dataset.setValue(ngoiCung, "Ngồi cứng", "Ngồi cứng");
            dataset.setValue(ngoiMem, "Ngồi mềm", "Ngồi mềm");
            dataset.setValue(namCung, "Nằm cứng", "Nằm cứng");
            dataset.setValue(namMem, "Nằm mềm", "Nằm mềm");
        } catch (NumberFormatException ex) {
            // Xử lý lỗi nếu dữ liệu nhập vào không hợp lệ
        }
    }

    private void showThongKePanel() {
        cardLayout.show(mainPanel, "ThongKe");
    }

    public void showHomePanel() {
        cardLayout.show(mainPanel, "Home");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Giao diện chính");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.add(new ThongKe_GUI());
        frame.setVisible(true);
    }
}
