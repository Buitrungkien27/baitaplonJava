package gui;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
//import utilities.FormatNumber;
import com.formdev.flatlaf.FlatClientProperties;

import connectDB.ConnectDB;
import entity.KhachHang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import raven.toast.Notifications;
import utilities.SVGIcon;
public final class HoaDon_GUI extends javax.swing.JPanel {
    private static final String PASSWORD = "sa123";
	private static final String USER = "sa";
	private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QLBanVe";;
	private DefaultTableModel tblModel_order;
    private DefaultTableModel tblModel_orderDetail;
    private int currentPage;
    private int lastPage;
    public HoaDon_GUI() {
        initComponents();
        init();
        alterTable();
        loadDanhSachHoaDon(); // Tải dữ liệu từ bảng VeTau
    }

    private void loadDanhSachHoaDon() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Truy vấn SQL để lấy dữ liệu từ bảng HoaDon
            String sqlHoaDon = "SELECT maHD, maNV, maKH, trangThai, ngayLap, tongTien, tienKhachDua FROM HoaDon";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlHoaDon);
                 ResultSet rsHoaDon = pstmt.executeQuery()) {

                // Xóa toàn bộ dữ liệu cũ trên bảng trước khi thêm dữ liệu mới
                tblModel_order.setRowCount(0);

                // Duyệt qua từng dòng dữ liệu từ kết quả truy vấn
                while (rsHoaDon.next()) {
                    String maHD = rsHoaDon.getString("maHD");
                    String maNV = rsHoaDon.getString("maNV");
                    String maKH = rsHoaDon.getString("maKH");
                    int trangThai = rsHoaDon.getInt("trangThai");
                    Date ngayLap = rsHoaDon.getDate("ngayLap");
                    double tongTien = rsHoaDon.getDouble("tongTien");
                    double tienKhachDua = rsHoaDon.getDouble("tienKhachDua");

                    // Thêm dữ liệu vào model của JTable
                    tblModel_order.addRow(new Object[]{maHD, maNV, maKH, ngayLap, trangThai == 1 ? "Đã thanh toán" : "Chưa thanh toán"});
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            // Hiển thị thông báo lỗi khi xảy ra lỗi truy vấn hoặc kết nối
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu từ cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

	public void init() {
        tblModel_order = new DefaultTableModel(new String[]{"Mã hoá đơn", "Mã Nhân viên", "Mã Khách hàng", "Ngày Tạo", "Trạng thái"}, 0);
        tbl_order.setModel(tblModel_order);
        tblModel_orderDetail = new DefaultTableModel(new String[]{"Mã vé tàu", "Mã hóa đơn", "Số lượng", "Giá Vé", "Thuế" , "Tổng tiền"}, 0);
        tbl_orderDetail.setModel(tblModel_orderDetail);
        tbl_orderDetail.getSelectionModel().addListSelectionListener((ListSelectionEvent event) -> {
            int rowIndex = tbl_orderDetail.getSelectedRow();
            if (rowIndex == -1) {
                return;
            }
        });
        this.currentPage = 1;
//      Gắn sự kiện xem lại hóa đơn pdf
        tbl_order.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                }
            }
        });
        
     // Thiết lập ListSelectionListener cho bảng hóa đơn
        tbl_order.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = tbl_order.getSelectedRow();
            if (selectedRow == -1) {
                return;  // Không có dòng nào được chọn
            }

            // Lấy mã hóa đơn từ dòng đã chọn
            String maHD = (String) tblModel_order.getValueAt(selectedRow, 0);

            // Gọi phương thức để load chi tiết hóa đơn dựa trên mã hóa đơn
            loadChiTietHoaDon(maHD);
            
            String maKH = (String) tblModel_order.getValueAt(selectedRow, 2);
            KhachHang kh = getInforCustomer(maKH);
            txt_customerName.setText(kh.getTenKH());
            System.out.println("check sdt: " + kh.getSdt());
            txt_phone.setText(kh.getSdt());
        });
    }
	
	private KhachHang getInforCustomer(String maKH) {
	    KhachHang kh = null; // Bắt đầu với null để xử lý trường hợp không tìm thấy
	    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
	        String sql = "SELECT tenKH, sdt FROM KhachHang WHERE maKH = ?";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, maKH);  // Đặt mã khách hàng vào câu truy vấn

	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next()) { // Dùng if vì chỉ có một dòng dữ liệu
	                    String tenKH = rs.getString("tenKH");
	                    String sdt = rs.getString("sdt");
	                    kh = new KhachHang(); // Tạo đối tượng KhachHang mới
	                    kh.setTenKH(tenKH);
	                    kh.setSdt(sdt);
	                } else {
	                    System.out.println("Không tìm thấy khách hàng với mã: " + maKH);
	                }
	            }
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Lỗi khi tải thông tin khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	    }
	    return kh;
	}

	
	// Phương thức để tải chi tiết hóa đơn theo mã hóa đơn
	private void loadChiTietHoaDon(String maHD) {
	    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
	        // Truy vấn SQL để lấy dữ liệu từ bảng ChiTietHoaDon dựa trên mã hóa đơn
	        String sql = "SELECT maHD, maVT, soLuong, gia, thue, tongTien FROM ChiTietHoaDon WHERE maHD = ?";
	        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setString(1, maHD);  // Đặt mã hóa đơn vào câu truy vấn

	            try (ResultSet rs = pstmt.executeQuery()) {
	                // Xóa toàn bộ dữ liệu cũ trong bảng chi tiết hóa đơn
	                tblModel_orderDetail.setRowCount(0);
	                
	                double tongHoaDon = 0;
	                // Duyệt qua từng dòng dữ liệu và thêm vào model của bảng chi tiết hóa đơn
	                while (rs.next()) {
	                    String maVT = rs.getString("maVT");
	                    int soLuong = rs.getInt("soLuong");
	                    double gia = rs.getDouble("gia");
	                    double thue = rs.getDouble("thue");
	                    double tongTien = rs.getDouble("tongTien");
	                    tongHoaDon += tongTien;

	                    // Thêm dữ liệu vào model của bảng chi tiết hóa đơn
	                    tblModel_orderDetail.addRow(new Object[]{maVT, maHD, soLuong, gia, thue, tongTien});
	                }
	                txt_total.setText(tongHoaDon + " đ");
	            }

	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu chi tiết hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	    }
	}
    public void alterTable() {
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

        tbl_order.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tbl_order.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbl_order.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbl_order.getColumnModel().getColumn(2).setPreferredWidth(200);
        tbl_order.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbl_order.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbl_order.getColumnModel().getColumn(4).setCellRenderer(rightAlign);
        tbl_order
                .setDefaultEditor(Object.class,
                         null);

        tbl_orderDetail.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tbl_orderDetail.getColumnModel().getColumn(0).setPreferredWidth(100);
        tbl_orderDetail.getColumnModel().getColumn(1).setPreferredWidth(200);
        tbl_orderDetail.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbl_orderDetail.getColumnModel().getColumn(2).setCellRenderer(rightAlign);
        tbl_orderDetail.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbl_orderDetail.getColumnModel().getColumn(3).setCellRenderer(rightAlign);
        tbl_orderDetail.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbl_orderDetail.getColumnModel().getColumn(4).setCellRenderer(rightAlign);
        tbl_orderDetail
                .setDefaultEditor(Object.class,
                         null);

    }

    public boolean validateFields() {
        return true;
    }
    @SuppressWarnings("unchecked")                       
    private void initComponents() {

        pnl_header = new javax.swing.JPanel();
        pnl_orderFilter = new javax.swing.JPanel();
        lbl_orderID = new javax.swing.JLabel();
        txt_orderID = new javax.swing.JTextField();
        pnl_orderStatusFilter = new javax.swing.JPanel();
        lbl_orderStatusFilter = new javax.swing.JLabel();
        cmb_orderPriceFilter = new javax.swing.JComboBox<>();
        pnl_orderDate = new javax.swing.JPanel();
        lbl_orderDate = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        pnl_searchButton = new javax.swing.JPanel();
        btn_search = new javax.swing.JButton();
        pnl_customerFilter = new javax.swing.JPanel();
        lbl_customerID = new javax.swing.JLabel();
        txt_customerID = new javax.swing.JTextField();
        pnl_customerPhone = new javax.swing.JPanel();
        lbl_customerPhone = new javax.swing.JLabel();
        txt_customerPhone = new javax.swing.JTextField();
        pnl_orderDateTo = new javax.swing.JPanel();
        lbl_orderDateTo = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        pnl_refreshButton = new javax.swing.JPanel();
        btn_refresh = new javax.swing.JButton();
        btn_wfile = new javax.swing.JButton();
        splitPane = new javax.swing.JSplitPane();
        pnl_center = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_order = new javax.swing.JTable();
        pnl_cartFooter = new javax.swing.JPanel();
        btn_previous = new javax.swing.JButton();
        lbl_pageNumber = new javax.swing.JLabel();
        btn_next = new javax.swing.JButton();
        btn_viewPDF = new javax.swing.JButton();
        pnl_infomation = new javax.swing.JPanel();
        pnl_orderDetail = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbl_orderDetail = new javax.swing.JTable();
        pnl_info = new javax.swing.JPanel();
        pnl_customerName = new javax.swing.JPanel();
        lbl_customerName = new javax.swing.JLabel();
        txt_customerName = new javax.swing.JTextField();
        pnl_phone = new javax.swing.JPanel();
        lbl_phone = new javax.swing.JLabel();
        txt_phone = new javax.swing.JTextField();
        pnl_total = new javax.swing.JPanel();
        lbl_customerName1 = new javax.swing.JLabel();
        txt_total = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        pnl_header.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm: ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185))); // NOI18N
        pnl_header.setMinimumSize(new java.awt.Dimension(10, 100));
        pnl_header.setPreferredSize(new java.awt.Dimension(1366, 130));
        pnl_header.setLayout(new java.awt.GridLayout(2, 4));

        pnl_orderFilter.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_orderFilter.setLayout(new javax.swing.BoxLayout(pnl_orderFilter, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderID.setText("Mã hóa  đơn: ");
        lbl_orderID.setPreferredSize(new java.awt.Dimension(130, 130));
        pnl_orderFilter.add(lbl_orderID);

        txt_orderID.setMinimumSize(null);
        txt_orderID.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderFilter.add(txt_orderID);

        pnl_header.add(pnl_orderFilter);

        pnl_orderStatusFilter.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_orderStatusFilter.setLayout(new javax.swing.BoxLayout(pnl_orderStatusFilter, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderStatusFilter.setText("Tổng doanh thu:");
        lbl_orderStatusFilter.setMaximumSize(new java.awt.Dimension(160, 150));
        lbl_orderStatusFilter.setMinimumSize(new java.awt.Dimension(130, 130));
        lbl_orderStatusFilter.setPreferredSize(new java.awt.Dimension(140, 150));
        pnl_orderStatusFilter.add(lbl_orderStatusFilter);

        cmb_orderPriceFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả","Dưới 500.000VNĐ", "Trên 500.000VNĐ & Dưới 1.000.000VNĐ", "Trên 1.000.000VNĐ" }));
        cmb_orderPriceFilter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cmb_orderPriceFilter.setMaximumSize(new java.awt.Dimension(10000, 32767));
        cmb_orderPriceFilter.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_orderStatusFilter.add(cmb_orderPriceFilter);

        pnl_header.add(pnl_orderStatusFilter);

        pnl_orderDate.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_orderDate.setMaximumSize(new java.awt.Dimension(20000, 2147483647));
        pnl_orderDate.setMinimumSize(new java.awt.Dimension(200, 200));
        pnl_orderDate.setPreferredSize(new java.awt.Dimension(300, 43));
        pnl_orderDate.setLayout(new javax.swing.BoxLayout(pnl_orderDate, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderDate.setText("Từ ngày: ");
        lbl_orderDate.setPreferredSize(new java.awt.Dimension(80, 0));
        pnl_orderDate.add(lbl_orderDate);

        jDateChooser1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        jDateChooser1.setDateFormatString("dd/MM/yyyy");
        jDateChooser1.setDate(Calendar.getInstance().getTime());
        jDateChooser1.setMinimumSize(new java.awt.Dimension(150, 30));
        jDateChooser1.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_orderDate.add(jDateChooser1);

        pnl_header.add(pnl_orderDate);

        pnl_searchButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 50, 8, 10));
        pnl_searchButton.setMaximumSize(new java.awt.Dimension(800, 2147483647));
        pnl_searchButton.setPreferredSize(new java.awt.Dimension(80, 100));
        pnl_searchButton.setLayout(new java.awt.BorderLayout());

        btn_search.setText("Tìm kiếm");
        btn_search.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchActionPerformed(evt);
            }
        });
        pnl_searchButton.add(btn_search, java.awt.BorderLayout.CENTER);

        pnl_header.add(pnl_searchButton);

        pnl_customerFilter.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_customerFilter.setLayout(new javax.swing.BoxLayout(pnl_customerFilter, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerID.setText("Mã khách hàng: ");
        lbl_customerID.setPreferredSize(new java.awt.Dimension(130, 130));
        pnl_customerFilter.add(lbl_customerID);

        txt_customerID.setMinimumSize(null);
        txt_customerID.setPreferredSize(new java.awt.Dimension(30, 30));
        txt_customerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_customerIDActionPerformed(evt);
            }
        });
        pnl_customerFilter.add(txt_customerID);

        pnl_header.add(pnl_customerFilter);

        pnl_customerPhone.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_customerPhone.setLayout(new javax.swing.BoxLayout(pnl_customerPhone, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerPhone.setText("Số điện thoại:");
        lbl_customerPhone.setMaximumSize(new java.awt.Dimension(160, 150));
        lbl_customerPhone.setMinimumSize(new java.awt.Dimension(130, 130));
        lbl_customerPhone.setPreferredSize(new java.awt.Dimension(140, 150));
        pnl_customerPhone.add(lbl_customerPhone);

        txt_customerPhone.setMinimumSize(null);
        txt_customerPhone.setPreferredSize(new java.awt.Dimension(30, 30));
        pnl_customerPhone.add(txt_customerPhone);

        pnl_header.add(pnl_customerPhone);

        pnl_orderDateTo.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnl_orderDateTo.setMaximumSize(new java.awt.Dimension(200000, 2147483647));
        pnl_orderDateTo.setMinimumSize(new java.awt.Dimension(200, 200));
        pnl_orderDateTo.setPreferredSize(new java.awt.Dimension(300, 43));
        pnl_orderDateTo.setLayout(new javax.swing.BoxLayout(pnl_orderDateTo, javax.swing.BoxLayout.LINE_AXIS));

        lbl_orderDateTo.setText("Đến ngày:");
        lbl_orderDateTo.setPreferredSize(new java.awt.Dimension(80, 0));
        pnl_orderDateTo.add(lbl_orderDateTo);

        jDateChooser2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        jDateChooser2.setDateFormatString("dd/MM/yyyy");
        jDateChooser2.setDate(Calendar.getInstance().getTime());
        jDateChooser2.setMinimumSize(new java.awt.Dimension(100, 30));
        jDateChooser2.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_orderDateTo.add(jDateChooser2);

        pnl_header.add(pnl_orderDateTo);

        pnl_refreshButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 50, 8, 10));
        pnl_refreshButton.setMaximumSize(new java.awt.Dimension(800, 2147483647));
        pnl_refreshButton.setPreferredSize(new java.awt.Dimension(80, 100));
        pnl_refreshButton.setLayout(new java.awt.GridLayout(1, 0));

        btn_refresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_refresh.setIcon(SVGIcon.getSVGIcon("imgs/public/refresh.svg"));
        btn_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_refreshActionPerformed(evt);
            }
        });
        pnl_refreshButton.add(btn_refresh);

        btn_wfile.setText("Xuất file");
        btn_wfile.setPreferredSize(new java.awt.Dimension(80, 23));
//        btn_wfile.setIcon(SVGIcon.getSVGIcon("imgs/public/excel.svg"));
        btn_wfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_wfileActionPerformed(evt);
            }
        });
        pnl_refreshButton.add(btn_wfile);

        pnl_header.add(pnl_refreshButton);

        add(pnl_header, java.awt.BorderLayout.NORTH);

        pnl_center.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách hoá đơn: ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185))); // NOI18N
        pnl_center.setDoubleBuffered(false);
        pnl_center.setMinimumSize(new java.awt.Dimension(600, 40));
        pnl_center.setLayout(new java.awt.BorderLayout());

        tbl_order.setMinimumSize(new java.awt.Dimension(400, 80));
        tbl_order.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tbl_order);

        pnl_center.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pnl_cartFooter.setMinimumSize(new java.awt.Dimension(282, 50));
        pnl_cartFooter.setPreferredSize(new java.awt.Dimension(800, 40));
        pnl_cartFooter.setLayout(new javax.swing.BoxLayout(pnl_cartFooter, javax.swing.BoxLayout.LINE_AXIS));
        btn_previous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });
        pnl_cartFooter.add(btn_previous);

        lbl_pageNumber.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lbl_pageNumber.setText("1/10");
        lbl_pageNumber.setToolTipText("");
        lbl_pageNumber.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        pnl_cartFooter.add(lbl_pageNumber);
        btn_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });
        pnl_cartFooter.add(btn_next);

        btn_viewPDF.setText("Xem bản in");
        btn_viewPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_viewPDFActionPerformed(evt);
            }
        });
        pnl_cartFooter.add(btn_viewPDF);

        pnl_center.add(pnl_cartFooter, java.awt.BorderLayout.PAGE_END);

        splitPane.setLeftComponent(pnl_center);

        pnl_infomation.setMinimumSize(new java.awt.Dimension(400, 0));
        pnl_infomation.setPreferredSize(new java.awt.Dimension(500, 768));
        pnl_infomation.setLayout(new java.awt.BorderLayout());

        pnl_orderDetail.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Chi tiết hoá đơn:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185))); // NOI18N
        pnl_orderDetail.setLayout(new java.awt.BorderLayout());

        tbl_orderDetail.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tbl_orderDetail);

        pnl_orderDetail.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        pnl_infomation.add(pnl_orderDetail, java.awt.BorderLayout.CENTER);

        pnl_info.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185))); // NOI18N
        pnl_info.setPreferredSize(new java.awt.Dimension(400, 200));
        pnl_info.setLayout(new javax.swing.BoxLayout(pnl_info, javax.swing.BoxLayout.Y_AXIS));

        pnl_customerName.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 20, 10));
        pnl_customerName.setLayout(new javax.swing.BoxLayout(pnl_customerName, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerName.setText("Tên khách hàng:");
        lbl_customerName.setPreferredSize(new java.awt.Dimension(120, 30));
        pnl_customerName.add(lbl_customerName);

        txt_customerName.setEditable(false);
        txt_customerName.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_customerName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_customerNameActionPerformed(evt);
            }
        });
        pnl_customerName.add(txt_customerName);

        pnl_info.add(pnl_customerName);

        pnl_phone.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 20, 10));
        pnl_phone.setLayout(new javax.swing.BoxLayout(pnl_phone, javax.swing.BoxLayout.LINE_AXIS));

        lbl_phone.setText("Số điện thoại:");
        lbl_phone.setPreferredSize(new java.awt.Dimension(120, 30));
        pnl_phone.add(lbl_phone);

        txt_phone.setEditable(false);
        txt_phone.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_phone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_phoneActionPerformed(evt);
            }
        });
        pnl_phone.add(txt_phone);

        pnl_info.add(pnl_phone);

        pnl_total.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 10));
        pnl_total.setLayout(new javax.swing.BoxLayout(pnl_total, javax.swing.BoxLayout.LINE_AXIS));

        lbl_customerName1.setText("Tổng tiền: ");
        lbl_customerName1.setPreferredSize(new java.awt.Dimension(120, 30));
        pnl_total.add(lbl_customerName1);

        txt_total.setEditable(false);
        txt_total.setPreferredSize(new java.awt.Dimension(64, 30));
        txt_total.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalActionPerformed(evt);
            }
        });
        pnl_total.add(txt_total);

        pnl_info.add(pnl_total);

        pnl_infomation.add(pnl_info, java.awt.BorderLayout.SOUTH);

        splitPane.setRightComponent(pnl_infomation);

        add(splitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>                        

    private void txt_customerNameActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
    }                                                

    private void txt_phoneActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                         

    private void txt_totalActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
    }                                                                          
    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt) {                                            

        txt_customerID.setText("");
        txt_customerPhone.setText("");
        txt_orderID.setText("");
        
        txt_phone.setText("");
        txt_customerName.setText("");
        txt_total.setText("");
        cmb_orderPriceFilter.setSelectedIndex(0);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        jDateChooser1.setDate(cal.getTime());

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        jDateChooser2.setDate(cal.getTime());

        init();
        alterTable();
    }                                           

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // Xóa thông tin cũ
        txt_phone.setText("");
        txt_customerName.setText("");
        txt_total.setText("");

        // Lấy giá trị từ các trường tìm kiếm
        String orderID = txt_orderID.getText().trim();
        String customerID = txt_customerID.getText().trim();
        String phone = txt_customerPhone.getText().trim();
        Date beginDate = jDateChooser1.getDate();
        Date endDate = jDateChooser2.getDate();

        // Xây dựng câu truy vấn SQL động
        StringBuilder sql = new StringBuilder("SELECT maHD, maNV, maKH, ngayLap, trangThai FROM HoaDon WHERE 1=1");

        // Danh sách tham số cho PreparedStatement
        ArrayList<Object> params = new ArrayList<>();

        // Thêm điều kiện tìm kiếm vào câu truy vấn nếu người dùng nhập dữ liệu
        if (!orderID.isEmpty()) {
            sql.append(" AND maHD LIKE ?");
            params.add("%" + orderID + "%");
        }
        if (!customerID.isEmpty()) {
            sql.append(" AND maKH LIKE ?");
            params.add("%" + customerID + "%");
        }
        if (!phone.isEmpty()) {
            sql.append(" AND maKH IN (SELECT maKH FROM KhachHang WHERE soDienThoai LIKE ?)");
            params.add("%" + phone + "%");
        }
        if (beginDate != null) {
            sql.append(" AND ngayLap >= ?");
            params.add(new java.sql.Date(beginDate.getTime()));
        }
        if (endDate != null) {
            sql.append(" AND ngayLap <= ?");
            params.add(new java.sql.Date(endDate.getTime()));
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
                // Gán các tham số vào PreparedStatement
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    // Xóa dữ liệu cũ trong bảng
                    tblModel_order.setRowCount(0);

                    // Duyệt kết quả truy vấn và thêm vào bảng
                    while (rs.next()) {
                        String maHD = rs.getString("maHD");
                        String maNV = rs.getString("maNV");
                        String maKH = rs.getString("maKH");
                        Date ngayLap = rs.getDate("ngayLap");
                        int trangThai = rs.getInt("trangThai");

                        tblModel_order.addRow(new Object[]{
                            maHD, maNV, maKH, ngayLap, trangThai == 1 ? "Đã thanh toán" : "Chưa thanh toán"
                        });
                    }

                    // Hiển thị thông báo nếu không có kết quả
                    if (tblModel_order.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy hóa đơn phù hợp!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
                          

    private void txt_customerIDActionPerformed(java.awt.event.ActionEvent evt) {                                               
        // TODO add your handling code here:
    }                                              

    private void btn_wfileActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn và tên file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        txt_phone.setText("");
        txt_customerName.setText("");
        txt_total.setText("");
        if (validateFields()) {
            String priceFrom, priceTo;
            String orderID = txt_orderID.getText();
            String customerID = txt_customerID.getText();
            String phone = txt_customerPhone.getText();
            if (orderID.trim().length() <= 0 && customerID.trim().length() <= 0 && phone.trim().length() <= 0) {
                if (JOptionPane.showConfirmDialog(this, "Bạn có muốn xuất toàn bộ hoá đơn ?", "Xuất file excel", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    int userSelection = fileChooser.showSaveDialog(null);
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        // Lấy đường dẫn và tên file được chọn
                        File fileToSave = fileChooser.getSelectedFile();
                        String filePath = fileToSave.getAbsolutePath();

                        // Gọi phương thức để tạo file Excel với đường dẫn và tên file đã chọn
//                        createExcel(bus.getAll(), filePath + ".xlsx");
                    }
                }
            }
            if (cmb_orderPriceFilter.getSelectedIndex() == 0) {
                priceFrom = "";
                priceTo = "";
            } else if (cmb_orderPriceFilter.getSelectedIndex() == 1) {
                priceFrom = "";
                priceTo = "100000";
            } else if (cmb_orderPriceFilter.getSelectedIndex() == 2) {
                priceFrom = "100000";
                priceTo = "500000";
            } else if (cmb_orderPriceFilter.getSelectedIndex() == 3) {
                priceFrom = "500000";
                priceTo = "1000000";
            } else {
                priceFrom = "1000000";
                priceTo = "";
            }

            Date begin = jDateChooser1.getDate();
            begin.setHours(0);
            begin.setMinutes(0);
            Date end = jDateChooser2.getDate();
            end.setHours(23);
            end.setMinutes(59);
//            ArrayList<Order> list = bus.orderListWithFilter(orderID, customerID, phone, priceFrom, priceTo, begin, end);
//            if (list.isEmpty()) {
//                Notifications.getInstance().show(Notifications.Type.INFO, "Không có hoá đơn !");
//            } else {
//                int userSelection = fileChooser.showSaveDialog(null);
//                if (userSelection == JFileChooser.APPROVE_OPTION) {
//                    // Lấy đường dẫn và tên file được chọn
//                    File fileToSave = fileChooser.getSelectedFile();
//                    String filePath = fileToSave.getAbsolutePath();
//
//
//                    // Gọi phương thức để tạo file Excel với đường dẫn và tên file đã chọn
//                    createExcel(list, filePath + ".xlsx");
//                }
//            }
        }
        // Hiển thị hộp thoại và kiểm tra nếu người dùng chọn OK
        
    }                                         

    private void btn_viewPDFActionPerformed(java.awt.event.ActionEvent evt) {                                            
//        viewPDFFile();
    }                                           

   

    // Variables declaration - do not modify                     
    private javax.swing.JButton btn_next;
    private javax.swing.JButton btn_previous;
    private javax.swing.JButton btn_refresh;
    private javax.swing.JButton btn_search;
    private javax.swing.JButton btn_viewPDF;
    private javax.swing.JButton btn_wfile;
    private javax.swing.JComboBox<String> cmb_orderPriceFilter;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_customerID;
    private javax.swing.JLabel lbl_customerName;
    private javax.swing.JLabel lbl_customerName1;
    private javax.swing.JLabel lbl_customerPhone;
    private javax.swing.JLabel lbl_orderDate;
    private javax.swing.JLabel lbl_orderDateTo;
    private javax.swing.JLabel lbl_orderID;
    private javax.swing.JLabel lbl_orderStatusFilter;
    private javax.swing.JLabel lbl_pageNumber;
    private javax.swing.JLabel lbl_phone;
    private javax.swing.JPanel pnl_cartFooter;
    private javax.swing.JPanel pnl_center;
    private javax.swing.JPanel pnl_customerFilter;
    private javax.swing.JPanel pnl_customerName;
    private javax.swing.JPanel pnl_customerPhone;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JPanel pnl_info;
    private javax.swing.JPanel pnl_infomation;
    private javax.swing.JPanel pnl_orderDate;
    private javax.swing.JPanel pnl_orderDateTo;
    private javax.swing.JPanel pnl_orderDetail;
    private javax.swing.JPanel pnl_orderFilter;
    private javax.swing.JPanel pnl_orderStatusFilter;
    private javax.swing.JPanel pnl_phone;
    private javax.swing.JPanel pnl_refreshButton;
    private javax.swing.JPanel pnl_searchButton;
    private javax.swing.JPanel pnl_total;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTable tbl_order;
    private javax.swing.JTable tbl_orderDetail;
    private javax.swing.JTextField txt_customerID;
    private javax.swing.JTextField txt_customerName;
    private javax.swing.JTextField txt_customerPhone;
    private javax.swing.JTextField txt_orderID;
    private javax.swing.JTextField txt_phone;
    private javax.swing.JTextField txt_total;
    // End of variables declaration     
}
