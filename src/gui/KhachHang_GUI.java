package gui;
import com.formdev.flatlaf.FlatClientProperties;
import bus.KhachHang_BUS;
import entity.Customer;
import entity.KhachHang;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import raven.toast.Notifications;
import utilities.SVGIcon;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;

public class KhachHang_GUI extends javax.swing.JPanel {

    private DefaultTableModel tblModel_customer;
    private KhachHang_BUS kh_BUS = new KhachHang_BUS();
    public KhachHang_GUI() {
        initTableModel();
        initComponents();
        alterTable();
        renderCustomerTable(kh_BUS.getAllKH());
        tbl_KH.getSelectionModel().addListSelectionListener((e) -> {
            int row = tbl_KH.getSelectedRow();
            if (row != -1) {
                String maKH = tblModel_customer.getValueAt(row, 0).toString();
                KhachHang kh = kh_BUS.getOne(maKH);
                txt_maKH.setText(maKH);
                txt_tenKH.setText(kh.getTenKH());
                txt_sdt.setText(kh.getSdt());
                txt_cccd.setText(kh.getCccd());       
                txt_dc.setText(kh.getDiaChi());      
                if (kh.isGioiTinh()) {
                    rad_men.setSelected(true);
                } else {
                    rad_women.setSelected(true);
                }
                ;
                date_dateOfBirth.setDate(kh.getNgaySinh());
                
            }
        });
    }

    public void renderCustomerTable(ArrayList<KhachHang> list) {
        tblModel_customer.setRowCount(0);
        
        for (KhachHang kh : list) {
        	System.out.println("CCCD: " + kh.getCccd());
            Object[] row = new Object[]{kh.getMaKH(), kh.getTenKH(), kh.getNgaySinh(),
                kh.isGioiTinh() ? "Nam" : "Nữ", kh.getDiaChi() , kh.getSdt(), kh.getCccd()};
            tblModel_customer.addRow(row);
        }
    }

    public void alterTable() {
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

        //// Align
        tbl_KH.getColumnModel().getColumn(2).setCellRenderer(rightAlign);
    }

    public void initTableModel() {
        // Products
        tblModel_customer = new DefaultTableModel(
                new String[]{"Mã", "Tên khách hàng", "Ngày sinh", "Giới tính", "Địa chỉ" , "Sđt","CCCD"
                }, 0);
    }

    public void reloadForm() {
        txt_maKH.setText("");
        txt_tenKH.setText("");
        txt_sdt.setText("");
        txt_cccd.setText("");
        txt_dc.setText("");
        date_dateOfBirth.setDate(Calendar.getInstance().getTime());
        rad_men.setSelected(true);
    }

//    private boolean validKH() {
//        if(txt_tenKH.getText().equals("")) {
//            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập điền tên khách hàng");
//            txt_tenKH.requestFocus();
//            return false;
//        }
//        if(txt_dc.getText().equals("")) {
//            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập địa chỉ khách hàng");
//            txt_dc.requestFocus();
//            return false;
//        }
//        if(txt_sdt.getText().equals("")) {
//            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập SDT khách hàng");
//            txt_sdt.requestFocus();
//            return false;
//        }
//        if(txt_cccd.getText().equals("")) {
//            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập mã CCCD của khách hàng");
//            txt_cccd.requestFocus();
//            return false;
//        }
//        if(!Pattern.matches("[0-9]{12}", txt_cccd.getText())) {
//            Notifications.getInstance().show(Notifications.Type.INFO, "CCCD gồm 12 chữ số");
//            txt_cccd.requestFocus();
//            return false;
//        }
////        if(grp_gender.isSelected(btnModel_gioiTinh)) {
////            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn giới tính nhân viên");
////            return false;
////        }
////        if(group_statusEmp.isSelected(btnModel_trangThai)) {
////            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn trạng thái làm việc của nhân viên");
////            return false;
////        }
//        return true;
//    }
    
    public static void createExcel(ArrayList<KhachHang> list, String filePath) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Customer Data");

        // Gộp ô cho tiêu đề
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

        // Thêm dòng thông tin đầu tiên
        Row infoRow = sheet.createRow(0);
        Cell infoCell = infoRow.createCell(0);
        infoCell.setCellValue("Danh sách khách hàng");

        // Thiết lập style cho phần tiêu đề
        CellStyle titleStyle = workbook.createCellStyle();
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 18);
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        infoCell.setCellStyle(titleStyle);

        Row row_date = sheet.createRow(1);
        Cell cell_date = row_date.createCell(0);
        cell_date.setCellValue("Ngày in: " + new Date());

        // Tạo header row
        Row headerRow = sheet.createRow(2);
        String[] columns = {"Mã khách hàng", "Tên", "Gới tính", "Ngày sinh", "Số điện thoại", "Căn cước công dân", "Địa chỉ"};

        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Đổ dữ liệu từ ArrayList vào Excel
        int rowNum = 3;
        for (KhachHang kh : list) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(kh.getMaKH());
            row.createCell(1).setCellValue(kh.getTenKH());
            row.createCell(2).setCellValue(kh.isGioiTinh() ? "Nam" : "Nữ");
            row.createCell(3).setCellValue(kh.getNgaySinh().toString());
            row.createCell(4).setCellValue(kh.getSdt()); 
            row.createCell(5).setCellValue(kh.getCccd());
            row.createCell(7).setCellValue(kh.getDiaChi());
        }

        // Ghi vào file
        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @SuppressWarnings("unchecked")
   
    private void initComponents() {

        grp_gender = new javax.swing.ButtonGroup();
        pnl_timKH = new javax.swing.JPanel();
        pnl_filterCustomer = new javax.swing.JPanel();
        pnl_timSDT = new javax.swing.JPanel();
        lbl_timSDT = new javax.swing.JLabel();
        txt_timSDT = new javax.swing.JTextField();
        pnl_locGT = new javax.swing.JPanel();
        lbl_locGT = new javax.swing.JLabel();
        cbo_locGT = new javax.swing.JComboBox<>();
        pnl_locDT = new javax.swing.JPanel();
        lbl_locDT = new javax.swing.JLabel();
        cbo_locDT = new javax.swing.JComboBox<>();
        btn_loc = new javax.swing.JButton();
        btn_lamMoi = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        pnl_info = new javax.swing.JPanel();
        pnl_infoBody = new javax.swing.JPanel();
        pnl_maKH = new javax.swing.JPanel();
        lbl_maKH = new javax.swing.JLabel();
        txt_maKH = new javax.swing.JTextField();
        pnl_tenKH = new javax.swing.JPanel();
        lbl_tenKH = new javax.swing.JLabel();
        txt_tenKH = new javax.swing.JTextField();
        pnl_ngaySinh = new javax.swing.JPanel();
        lbl_ngaySinh = new javax.swing.JLabel();
        date_dateOfBirth = new com.toedter.calendar.JDateChooser();
        pnl_gioiTinh = new javax.swing.JPanel();
        lbl_gioiTinh = new javax.swing.JLabel();
        pnl_genderGr = new javax.swing.JPanel();
        rad_men = new javax.swing.JRadioButton();
        rad_women = new javax.swing.JRadioButton();
        pnl_sdt = new javax.swing.JPanel();
        lbl_sdt = new javax.swing.JLabel();
        txt_sdt = new javax.swing.JTextField();
        pnl_cccd = new javax.swing.JPanel();
        lbl_cccd = new javax.swing.JLabel();
        pnl_infoFooter = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        btn_reloadForm = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btn_create = new javax.swing.JButton();
        btn_exportExcel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_KH = new javax.swing.JTable();

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        pnl_timKH.setMinimumSize(new java.awt.Dimension(859, 50));
        pnl_timKH.setPreferredSize(new java.awt.Dimension(845, 50));
        pnl_timKH.setLayout(new javax.swing.BoxLayout(pnl_timKH, javax.swing.BoxLayout.LINE_AXIS));

        pnl_filterCustomer.setLayout(new javax.swing.BoxLayout(pnl_filterCustomer, javax.swing.BoxLayout.X_AXIS));

        pnl_timSDT.setMinimumSize(new java.awt.Dimension(195, 30));
        pnl_timSDT.setPreferredSize(new java.awt.Dimension(181, 30));
        pnl_timSDT.setLayout(new javax.swing.BoxLayout(pnl_timSDT, javax.swing.BoxLayout.X_AXIS));

        lbl_timSDT.setFont(lbl_timSDT.getFont().deriveFont((float)14));
        lbl_timSDT.setText("Số điện thoại:");
        lbl_timSDT.setMinimumSize(new java.awt.Dimension(100, 20));
        lbl_timSDT.setPreferredSize(new java.awt.Dimension(100, 20));
        pnl_timSDT.add(lbl_timSDT);

        txt_timSDT.setPreferredSize(new java.awt.Dimension(50, 22));
        txt_timSDT.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Số điện thoại người dùng");
        pnl_timSDT.add(txt_timSDT);

        pnl_filterCustomer.add(pnl_timSDT);

        pnl_locGT.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        pnl_locGT.setLayout(new javax.swing.BoxLayout(pnl_locGT, javax.swing.BoxLayout.LINE_AXIS));

        lbl_locGT.setFont(lbl_locGT.getFont().deriveFont((float)14));
        lbl_locGT.setText("Giới tính: ");
        pnl_locGT.add(lbl_locGT);

        cbo_locGT.setFont(cbo_locGT.getFont().deriveFont((float)14));
        cbo_locGT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Nam", "Nữ" }));
        pnl_locGT.add(cbo_locGT);

        pnl_filterCustomer.add(pnl_locGT);

        pnl_locDT.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        pnl_locDT.setLayout(new javax.swing.BoxLayout(pnl_locDT, javax.swing.BoxLayout.LINE_AXIS));

        lbl_locDT.setFont(lbl_locDT.getFont().deriveFont((float)14));
        lbl_locDT.setText("Độ tuổi: ");
        lbl_locDT.setMaximumSize(new java.awt.Dimension(40, 20));
        pnl_locDT.add(lbl_locDT);

        cbo_locDT.setFont(cbo_locDT.getFont().deriveFont((float)14));
        cbo_locDT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Dưới 18 tuổi", "Từ 18 đến 40 tuổi", "Trên 40 tuổi" }));
        cbo_locDT.setToolTipText("");
        pnl_locDT.add(cbo_locDT);

        pnl_filterCustomer.add(pnl_locDT);

        pnl_timKH.add(pnl_filterCustomer);

        btn_loc.setText("Lọc");
        btn_loc.setIcon(SVGIcon.getSVGIcon("imgs/public/filter.svg"));
        btn_loc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_filterActionPerformed(evt);
            }
        });
        pnl_timKH.add(btn_loc);

        btn_lamMoi.setIcon(SVGIcon.getSVGIcon("imgs/public/refresh.svg"));
        btn_lamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reloadListActionPerformed(evt);
            }
        });
        pnl_timKH.add(btn_lamMoi);

        add(pnl_timKH, java.awt.BorderLayout.NORTH);

        jSplitPane1.setResizeWeight(0.95);

        pnl_info.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        pnl_info.setMinimumSize(new java.awt.Dimension(400, 337));
        pnl_info.setPreferredSize(new java.awt.Dimension(400, 464));
        pnl_info.setLayout(new java.awt.BorderLayout());

        pnl_infoBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnl_infoBody.setLayout(new javax.swing.BoxLayout(pnl_infoBody, javax.swing.BoxLayout.Y_AXIS));

        pnl_maKH.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_maKH.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_maKH.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_maKH.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_maKH.setLayout(new javax.swing.BoxLayout(pnl_maKH, javax.swing.BoxLayout.LINE_AXIS));

        lbl_maKH.setText("Mã:");
        lbl_maKH.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_maKH.add(lbl_maKH);

        txt_maKH.setEditable(false);
        txt_maKH.setFont(txt_maKH.getFont().deriveFont((float)16));
        txt_maKH.setToolTipText("");
        txt_maKH.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0)));
        txt_maKH.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txt_maKH.setFocusable(false);
        txt_maKH.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_maKH.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_maKH.setPreferredSize(new java.awt.Dimension(100, 30));
        txt_maKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_customerIDActionPerformed(evt);
            }
        });
        pnl_maKH.add(txt_maKH);

        pnl_infoBody.add(pnl_maKH);

        pnl_tenKH.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_tenKH.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_tenKH.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_tenKH.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_tenKH.setLayout(new javax.swing.BoxLayout(pnl_tenKH, javax.swing.BoxLayout.X_AXIS));

        lbl_tenKH.setText("Họ và tên:");
        lbl_tenKH.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_tenKH.add(lbl_tenKH);

        txt_tenKH.setFont(txt_tenKH.getFont().deriveFont((float)16));
        txt_tenKH.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_tenKH.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_tenKH.add(txt_tenKH);

        pnl_infoBody.add(pnl_tenKH);

        pnl_ngaySinh.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_ngaySinh.setMaximumSize(new java.awt.Dimension(1000, 50));
        pnl_ngaySinh.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_ngaySinh.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_ngaySinh.setLayout(new javax.swing.BoxLayout(pnl_ngaySinh, javax.swing.BoxLayout.LINE_AXIS));

        lbl_ngaySinh.setText("Ngày sinh:");
        lbl_ngaySinh.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_ngaySinh.add(lbl_ngaySinh);

        date_dateOfBirth.setDateFormatString("MMMM d, yyyy");
        date_dateOfBirth.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        date_dateOfBirth.setPreferredSize(new java.awt.Dimension(100, 40));
        pnl_ngaySinh.add(date_dateOfBirth);
        date_dateOfBirth.setDateFormatString("dd/MM/yyyy");

        pnl_infoBody.add(pnl_ngaySinh);

        pnl_gioiTinh.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_gioiTinh.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_gioiTinh.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_gioiTinh.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_gioiTinh.setLayout(new javax.swing.BoxLayout(pnl_gioiTinh, javax.swing.BoxLayout.LINE_AXIS));

        lbl_gioiTinh.setText("Giới tính:");
        lbl_gioiTinh.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_gioiTinh.add(lbl_gioiTinh);

        pnl_genderGr.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 90, 1, 1));
        pnl_genderGr.setLayout(new javax.swing.BoxLayout(pnl_genderGr, javax.swing.BoxLayout.X_AXIS));

        grp_gender.add(rad_men);
        rad_men.setSelected(true);
        rad_men.setText("Nam");
        pnl_genderGr.add(rad_men);

        grp_gender.add(rad_women);
        rad_women.setText("Nữ");
        rad_women.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rad_womenActionPerformed(evt);
            }
        });
        pnl_genderGr.add(rad_women);

        pnl_gioiTinh.add(pnl_genderGr);

        pnl_infoBody.add(pnl_gioiTinh);

        pnl_sdt.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_sdt.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_sdt.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_sdt.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_sdt.setLayout(new javax.swing.BoxLayout(pnl_sdt, javax.swing.BoxLayout.LINE_AXIS));

        lbl_sdt.setText("Số điện thoại:");
        lbl_sdt.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_sdt.add(lbl_sdt);
        txt_sdt.setFont(txt_sdt.getFont().deriveFont((float)16));
        txt_sdt.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_sdt.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_sdt.add(txt_sdt);
        txt_sdt.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                
                // Chỉ cho phép các ký tự số từ 0-9
                if (!Character.isDigit(c) || txt_sdt.getText().length() >= 10) {
                    evt.consume(); // Nếu không phải số hoặc vượt quá độ dài 10 ký tự, không cho nhập
                }
            }
        });
        pnl_infoBody.add(pnl_sdt);

        pnl_cccd.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 0, 0));
        pnl_cccd.setMaximumSize(new java.awt.Dimension(1000, 40));
        pnl_cccd.setMinimumSize(new java.awt.Dimension(300, 30));
        pnl_cccd.setPreferredSize(new java.awt.Dimension(200, 30));
        pnl_cccd.setLayout(new javax.swing.BoxLayout(pnl_cccd, javax.swing.BoxLayout.LINE_AXIS));

        lbl_cccd.setText("CCCD\r\n");
        lbl_cccd.setPreferredSize(new java.awt.Dimension(150, 16));
        pnl_cccd.add(lbl_cccd);

        pnl_infoBody.add(pnl_cccd);
        
        txt_cccd = new JTextField();
        txt_cccd.setText((String) null);
        txt_cccd.setPreferredSize(new Dimension(100, 30));
        txt_cccd.setMaximumSize(new Dimension(2147483647, 40));
        txt_cccd.setFont(txt_cccd.getFont().deriveFont(16f));
        pnl_cccd.add(txt_cccd);

        pnl_info.add(pnl_infoBody, java.awt.BorderLayout.CENTER);
        
        pnl_dc = new JPanel();
        pnl_dc.setPreferredSize(new Dimension(200, 30));
        pnl_dc.setMinimumSize(new Dimension(300, 30));
        pnl_dc.setMaximumSize(new Dimension(1000, 40));
        pnl_dc.setBorder(new EmptyBorder(5, 0, 0, 0));
        pnl_infoBody.add(pnl_dc);
        pnl_dc.setLayout(new BoxLayout(pnl_dc, BoxLayout.X_AXIS));
        
        lbl_dc = new JLabel();
        lbl_dc.setText("Địa chỉ:");
        lbl_dc.setPreferredSize(new Dimension(150, 16));
        pnl_dc.add(lbl_dc);
        
        txt_dc = new JTextField();
        txt_dc.setText((String) null);
        txt_dc.setPreferredSize(new Dimension(100, 30));
        txt_dc.setMaximumSize(new Dimension(2147483647, 40));
        txt_dc.setFont(txt_dc.getFont().deriveFont(16f));
        pnl_dc.add(txt_dc);

        pnl_infoFooter.setLayout(new javax.swing.BoxLayout(pnl_infoFooter, javax.swing.BoxLayout.Y_AXIS));

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 80));
        jPanel9.setPreferredSize(new java.awt.Dimension(200, 50));
        jPanel9.setLayout(new java.awt.GridLayout(1, 2));

        btn_reloadForm.setFont(btn_reloadForm.getFont().deriveFont((float)14));
        btn_reloadForm.setText("Xóa trắng");
        btn_reloadForm.setIcon(SVGIcon.getSVGIcon("imgs/public/clear.svg"));
        btn_reloadForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reloadFormActionPerformed(evt);
            }
        });
        jPanel9.add(btn_reloadForm);

        btn_update.setFont(btn_update.getFont().deriveFont((float)14));
        btn_update.setText("Cập nhật");
        btn_update.setIcon(SVGIcon.getSVGIcon("imgs/public/update.svg"));
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        jPanel9.add(btn_update);

        pnl_infoFooter.add(jPanel9);

        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel10.setMaximumSize(new java.awt.Dimension(32767, 80));
        jPanel10.setPreferredSize(new java.awt.Dimension(200, 50));
        jPanel10.setLayout(new java.awt.GridLayout(1, 1));

        btn_create.setFont(btn_create.getFont().deriveFont((float)14));
        btn_create.setText("Thêm mới");
        btn_create.putClientProperty(FlatClientProperties.STYLE, "background: $Menu.background;"+"foreground: $Menu.foreground");
        btn_create.setIcon(SVGIcon.getPrimarySVGIcon("imgs/public/add.svg"));
        btn_create.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createActionPerformed(evt);
            }
        });
        jPanel10.add(btn_create);

        btn_exportExcel.setFont(btn_exportExcel.getFont().deriveFont((float)14));
        btn_exportExcel.setText("Xuất file");
        btn_exportExcel.setIcon(SVGIcon.getSVGIcon("imgs/public/excel.svg"));
        btn_exportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exportExcelActionPerformed(evt);
            }
        });
        jPanel10.add(btn_exportExcel);

        pnl_infoFooter.add(jPanel10);

        pnl_info.add(pnl_infoFooter, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setRightComponent(pnl_info);

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185))); // NOI18N
        jScrollPane1.setMinimumSize(new java.awt.Dimension(900, 41));

        tbl_KH.setFont(tbl_KH.getFont().deriveFont((float)14));
        tbl_KH.setModel(tblModel_customer);
        tbl_KH.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tbl_KH);

        jSplitPane1.setLeftComponent(jScrollPane1);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_exportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exportExcelActionPerformed
        // Hiển thị hộp thoại chọn file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn và tên file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Hiển thị hộp thoại và kiểm tra nếu người dùng chọn OK
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Lấy đường dẫn và tên file được chọn
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Gọi phương thức để tạo file Excel với đường dẫn và tên file đã chọn
            createExcel(kh_BUS.getAllKH(), filePath + ".xlsx");
        }
    }//GEN-LAST:event_btn_exportExcelActionPerformed

    private void btn_reloadListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reloadListActionPerformed
        renderCustomerTable(kh_BUS.getAllKH());        // TODO add your handling code here:
    }//GEN-LAST:event_btn_reloadListActionPerformed

    private void btn_searchActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_searchActionPerformed
        // TODO add your handling code here:
        KhachHang kh = kh_BUS.searchByPhoneNumber(txt_timSDT.getText().trim());
        if (kh == null) {
            JOptionPane.showConfirmDialog(null, "Khách hàng chưa phải là thành viên");
            txt_timSDT.setFocusable(true);
        } else {
            ArrayList<KhachHang> list = new ArrayList<>();
            list.add(kh);
            renderCustomerTable(list);
            txt_timSDT.setText("");
        }
    }// GEN-LAST:event_btn_searchActionPerformed

    private void txt_customerIDActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txt_customerIDActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txt_customerIDActionPerformed

    private void btn_reloadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_reloadActionPerformed
        // TODO add your handling code here:
        renderCustomerTable(kh_BUS.getAllKH());
    }// GEN-LAST:event_btn_reloadActionPerformed

    private void rad_womenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_rad_womenActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_rad_womenActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_updateActionPerformed
        int row = tbl_KH.getSelectedRow();
        if (row != -1)
        try {
            // TODO add your handling code here:
            if (row != -1) {
            	String ma = txt_maKH.getText();
                kh_BUS.updateKH(getValueForm(), ma);
                renderCustomerTable(kh_BUS.getAllKH());

            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Chưa chọn khách hàng muốn cập nhật thông tin!");
                return;
            }
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Cập nhật thông tin khách hàng thành công!");

        } catch (Exception ex) {
            Logger.getLogger(KhachHang_GUI.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }// GEN-LAST:event_btn_updateActionPerformed

    private void btn_createActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_createActionPerformed
        try {
            // TODO add your handling code here:
            boolean isCompleted = kh_BUS.createKH(txt_tenKH.getText(), date_dateOfBirth.getDate(), txt_sdt.getText(),
                    txt_dc.getText(), rad_men.isSelected(), txt_cccd.getText());
//            System.out.println("test" + txt_sdt.getText());
            if (!isCompleted) {
                Notifications.getInstance().show(Notifications.Type.ERROR, "Khách hàng đã tồn tại");
                return;
            }
            renderCustomerTable(kh_BUS.getAllKH());
            reloadForm();
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Thêm khách hàng thành công!");

        } catch (Exception ex) {
        	System.out.print(ex);
            Notifications.getInstance().show(Notifications.Type.ERROR, ex.getMessage());
        }

    }
    private void btn_reloadFormActionPerformed(java.awt.event.ActionEvent evt) {
        reloadForm();
        cbo_locDT.setSelectedIndex(0);
        cbo_locGT.setSelectedIndex(0);
        txt_timSDT.setText("");
    }

    private void btn_filterActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btn_filterActionPerformed
        // TODO add your handling code here:
        String gender = cbo_locGT.getSelectedItem().toString();
        String age = cbo_locDT.getSelectedItem().toString();
        String phone = txt_timSDT.getText();
        ArrayList<KhachHang> listFilter = kh_BUS.filterKH(gender, age, phone);
        renderCustomerTable(listFilter);

    }// GEN-LAST:event_btn_filterActionPerformed

    private KhachHang getValueForm() throws Exception {
        String ma = txt_maKH.getText();
        String ten = txt_tenKH.getText();
        Date ns = date_dateOfBirth.getDate();
        String dc = txt_dc.getText();
        String sdt = txt_sdt.getText().trim();
        boolean gt;
        if (rad_men.isSelected()) {
            gt = true;
        } else {
            gt = false;
        }
        String cccd = txt_cccd.getText();
        return new KhachHang(ma, ten, gt, ns, sdt, dc, cccd);
    }

    public boolean checkValueForm() {
        if (txt_tenKH == null || txt_dc == null || txt_sdt == null || txt_cccd == null) {
            return false;
        }
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_create;
    private javax.swing.JButton btn_exportExcel;
    private javax.swing.JButton btn_loc;
    private javax.swing.JButton btn_reloadForm;
    private javax.swing.JButton btn_lamMoi;
    private javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> cbo_locDT;
    private javax.swing.JComboBox<String> cbo_locGT;
    private com.toedter.calendar.JDateChooser date_dateOfBirth;
    private javax.swing.ButtonGroup grp_gender;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel lbl_maKH;
    private javax.swing.JLabel lbl_ngaySinh;
    private javax.swing.JLabel lbl_locGT;
    private javax.swing.JLabel lbl_locDT;
    private javax.swing.JLabel lbl_timSDT;
    private javax.swing.JLabel lbl_gioiTinh;
    private javax.swing.JLabel lbl_tenKH;
    private javax.swing.JLabel lbl_sdt;
    private javax.swing.JLabel lbl_cccd;
    private javax.swing.JPanel pnl_maKH;
    private javax.swing.JPanel pnl_ngaySinh;
    private javax.swing.JPanel pnl_locDT;
    private javax.swing.JPanel pnl_filterCustomer;
    private javax.swing.JPanel pnl_locGT;
    private javax.swing.JPanel pnl_gioiTinh;
    private javax.swing.JPanel pnl_genderGr;
    private javax.swing.JPanel pnl_info;
    private javax.swing.JPanel pnl_infoBody;
    private javax.swing.JPanel pnl_infoFooter;
    private javax.swing.JPanel pnl_tenKH;
    private javax.swing.JPanel pnl_sdt;
    private javax.swing.JPanel pnl_cccd;
    private javax.swing.JPanel pnl_timKH;
    private javax.swing.JPanel pnl_timSDT;
    private javax.swing.JRadioButton rad_men;
    private javax.swing.JRadioButton rad_women;
    private javax.swing.JTable tbl_KH;
    private javax.swing.JTextField txt_maKH;
    private javax.swing.JTextField txt_tenKH;
    private javax.swing.JTextField txt_sdt;
    private javax.swing.JTextField txt_timSDT;
    private JPanel pnl_email;
    private JLabel lbl_email;
    private JTextField txt_email;
    private JPanel pnl_dc;
    private JLabel lbl_dc;
    private JTextField txt_cccd;
    private JTextField txt_dc;
}
