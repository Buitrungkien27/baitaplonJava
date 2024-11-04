package gui;
import com.formdev.flatlaf.FlatClientProperties;
import entity.Employee;
import entity.NhanVien;
import entity.Store;
import java.awt.Desktop;
import java.awt.event.KeyEvent;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import main.Application;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.*;
import raven.toast.Notifications;
import utilities.SVGIcon;

public class NhanVien_GUI extends javax.swing.JPanel {
    private DefaultTableModel tblModel_NhanVien;
    private NhanVien nhanVien;
    private DefaultComboBoxModel cmbModel_chucVu;
    private DefaultComboBoxModel cmbModel_trangThai;
    private DefaultComboBoxModel cmbModel_thongTin;
    private DefaultButtonModel btnModel_gioiTinh;
    private DefaultButtonModel btnModel_trangThai;
    private static CellStyle cellStyleFormatNumber = null;
    // Khai báo thông tin kết nối cơ sở dữ liệu
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QLBanVe";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa123";
    public NhanVien_GUI() {
        initComponents();
        init();
        loadDataToTable();

    }
    
    private void init() {
        tblModel_NhanVien = new DefaultTableModel(new String[] {"Mã nhân viên", "Tên nhân viên", "Ngày sinh", "Trạng thái"}, 0);
        tbl_dsNV.setModel(tblModel_NhanVien);
        tbl_dsNV.getSelectionModel().addListSelectionListener((e) -> {
            int rowIndex = tbl_dsNV.getSelectedRow();
            if(rowIndex == -1)
                return;
            
            String maNV = tblModel_NhanVien.getValueAt(rowIndex, 0).toString();
            renderCurrentEmployee();
        });
        cmbModel_chucVu = new DefaultComboBoxModel(new String[]{"Chức vụ", "Nhân viên Bán Vé", "Quản lý"});
        cmb_timChucVu.setModel(cmbModel_chucVu);
        cmbModel_thongTin = new DefaultComboBoxModel(new String[]{"Nhân viên Bán Vé", "Quản lý"});
        cmb_chucVu.setModel(cmbModel_thongTin);
        cmbModel_trangThai = new DefaultComboBoxModel(new String[]{"Trạng thái", "Đang làm việc", "Đã nghỉ"});
        cmb_trangThai.setModel(cmbModel_trangThai);
        //radio button
        group_gender.add(rdb_nam);
        group_gender.add(rdb_nu);
        group_statusEmp.add(rdb_dangLam);
        group_statusEmp.add(rdb_daNghi);
    }
    private void renderCurrentEmployee() {
        txt_maNV.setText(nhanVien.getMaNV());
        txt_tenNV.setText(nhanVien.getTenNV());
        txt_diaChi.setText(nhanVien.getDiaChi());
        if(nhanVien.isGioiTinh())
            rdb_nam.setSelected(true);
        else
            rdb_nu.setSelected(true);
        txt_sdt.setText(nhanVien.getSdt());
        txt_cccd.setText(nhanVien.getCccd());
        if(nhanVien.getTrangThai())
            rdb_dangLam.setSelected(true);
        else
            rdb_daNghi.setSelected(true);
        if(nhanVien.getChucVu().equals("Nhân Viên Bán Vé"))
            cmbModel_thongTin.setSelectedItem("Nhân Viên Bán Vé");
        else
        	cmbModel_thongTin.setSelectedItem("Quản Lý");
        chonNgaySinh.setDate(nhanVien.getNgaySinh());
        chonNgayVaoLam.setDate(nhanVien.getNgayVaoLam());
        //chooseDateStart.setDate();
    }
    
    private void renderEmployeeTable(ArrayList<Employee> employeeList) {
        tblModel_NhanVien.setRowCount(0);
        String status;
        for (Employee employee : employeeList) {
            if(employee.isStatus())
                status = "Đang làm việc";
            else
                status = "Đã nghỉ";
            String[] newRow = {employee.getEmployeeID(), employee.getName(), employee.getDateOfBirth().toString(), status};
            tblModel_NhanVien.addRow(newRow);
        }        
    }
    private void renderEmployeeInfor() {
        txt_maNV.setText("");
        txt_tenNV.setText("");
        txt_diaChi.setText("");
        group_gender.clearSelection();
        txt_sdt.setText("");
        txt_cccd.setText("");
        rdb_nu.setSelected(true);
        cmbModel_thongTin.setSelectedItem("Nhân Viên Bán Vé");
        chonNgaySinh.setDate(java.sql.Date.valueOf(LocalDate.now()));
        chonNgayVaoLam.setDate(java.sql.Date.valueOf(LocalDate.now()));
    }
    private boolean validEmployee() {
        if(txt_tenNV.getText().equals("")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập điền tên nhân viên");
            txt_tenNV.requestFocus();
            return false;
        }
        if(txt_diaChi.getText().equals("")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập địa chỉ nhân viên");
            txt_diaChi.requestFocus();
            return false;
        }
        if(txt_sdt.getText().equals("")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập SDT tên nhân viên");
            txt_sdt.requestFocus();
            return false;
        }
        if(txt_cccd.getText().equals("")) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng nhập mã CCCD của nhân viên");
            txt_cccd.requestFocus();
            return false;
        }
        if(!Pattern.matches("[0-9]{12}", txt_cccd.getText())) {
            Notifications.getInstance().show(Notifications.Type.INFO, "CCCD gồm 12 chữ số");
            txt_cccd.requestFocus();
            return false;
        }
        if(group_gender.isSelected(btnModel_gioiTinh)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn giới tính nhân viên");
            return false;
        }
        if(group_statusEmp.isSelected(btnModel_trangThai)) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng chọn trạng thái làm việc của nhân viên");
            return false;
        }
        return true;
    }
    private NhanVien getCurrentValue() {
    	String ma = txt_maNV.getText();
        String ten = txt_tenNV.getText();
        String sdt = txt_sdt.getText();
        String dc = txt_diaChi.getText();
        String cccd = txt_cccd.getText();
        String cv = (String) cmb_chucVu.getSelectedItem();
        boolean gt = false;
        if(rdb_nam.isSelected())
            gt = true;
        boolean tt = false;
        if(rdb_dangLam.isSelected())
            tt = true;
        Date ns = chonNgaySinh.getDate();
        Date nvl = chonNgayVaoLam.getDate();
        
        NhanVien nv = new NhanVien(ma, cccd, cv, tt, ten, sdt, gt, ns, dc, nvl);
        return nv;
    }
    private void createExcel(ArrayList<Employee> listEmp, String filePath) {
        try {
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            SXSSFSheet sheet = workbook.createSheet("Danh Sách Nhân Viên");
            
            sheet.trackAllColumnsForAutoSizing();
            int rowIndex = 0;
            writeHeader(sheet, rowIndex);
            rowIndex = 4;
            for (Employee employee : listEmp) {
                SXSSFRow row = sheet.createRow(rowIndex);
                writeEmployee(employee, row);
                rowIndex++;
            }
            writeFooter(sheet, rowIndex);
            
            createOutputFile(workbook, filePath);
            Notifications.getInstance().show(Notifications.Type.SUCCESS, "Xuất file thành công!");
         
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private static void writeHeader(SXSSFSheet sheet, int rowIndex) {
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

        String[] title = {"MÃ NHÂN VIÊN", "TÊN NHÂN VIÊN", "GIỚI TÍNH", "NGÀY SINH", "CHỨC VỤ", "TRẠNG THÁI", "SỐ ĐIỆN THOẠI", "CĂN CƯỚC CÔNG DÂN", "ĐỊA CHỈ"};

        CellStyle cellStyle = createStyleForHeader(sheet);
        CellStyle headerCellStyle = createStyleForTitle(sheet);
        
        SXSSFRow headerRow = sheet.createRow(rowIndex++);
        SXSSFCell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("DANH SÁCH NHÂN VIÊN");
        headerCell.setCellStyle(headerCellStyle);
        
        SXSSFRow dateRow = sheet.createRow(rowIndex++);
        SXSSFCell dateCell = dateRow.createCell(0);
        dateCell.setCellValue("Ngày in: " + LocalDate.now());
        
        SXSSFRow empRow = sheet.createRow(rowIndex++);
        SXSSFCell empCell = empRow.createCell(0);
        empCell.setCellValue("Nhân viên in: " + Application.nhanVien.getTenNV());
        
        SXSSFRow row = sheet.createRow(rowIndex++);
        
        // Create cells
        SXSSFCell cell = row.createCell(0);
        cell.setCellStyle(cellStyle);
        cell.setCellValue("MÃ NHÂN VIÊN");
        for (int i = 0; i < title.length - 1; i++) {
            cell = row.createCell(i+1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(title[i+1]);
        }        
    }
    private void writeEmployee(Employee emp, SXSSFRow row) {
        if (cellStyleFormatNumber == null) {
            short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");
            SXSSFWorkbook workbook = row.getSheet().getWorkbook();
            cellStyleFormatNumber = workbook.createCellStyle();
            cellStyleFormatNumber.setDataFormat(format);
        }
 
        SXSSFCell cell = row.createCell(0);
        cell.setCellValue(emp.getEmployeeID());
 
        cell = row.createCell(1);
        cell.setCellValue(emp.getName());
 
        cell = row.createCell(2);
        String gender = "Nam";
        if(!emp.isGender())
            gender = "Nữ";
        cell.setCellValue(gender);
        
 
        cell = row.createCell(3);
        cell.setCellValue(emp.getDateOfBirth().toString());
        
        cell = row.createCell(4);
        cell.setCellValue(emp.getRole());
        
        cell = row.createCell(5);
        String status = "Đã nghỉ";
        if(emp.isStatus())
            status = "Đang làm việc";
        cell.setCellValue(status);
        
        cell = row.createCell(6);
        cell.setCellValue(emp.getPhoneNumber());
        
        cell = row.createCell(7);
        cell.setCellValue(emp.getCitizenIdentification());
        
        cell = row.createCell(8);
        cell.setCellValue(emp.getAddress());
    }
    private void writeFooter(SXSSFSheet sheet, int rowIndex) {
        SXSSFRow row = sheet.createRow(rowIndex);
        SXSSFCell cell = row.createCell(9, CellType.FORMULA);
        cell.setCellFormula("COUNT(A2:A11)");
    }
    private static CellStyle createStyleForTitle(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 20); // font size
        font.setColor(IndexedColors.GREEN.getIndex()); // text color
        
 
        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }
    private static CellStyle createStyleForHeader(Sheet sheet) {
        // Create font
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setBold(true);
        font.setFontHeightInPoints((short) 14); // font size
        font.setColor(IndexedColors.VIOLET.getIndex()); // text color
 
        // Create CellStyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        return cellStyle;
    }
    private static void createOutputFile(SXSSFWorkbook workbook, String excelFilePath) throws FileNotFoundException, IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void initComponents() {

        group_gender = new javax.swing.ButtonGroup();
        group_statusEmp = new javax.swing.ButtonGroup();
        group_roleEmp = new javax.swing.ButtonGroup();
        pnl_topEmp = new javax.swing.JPanel();
        pnl_searchEmp = new javax.swing.JPanel();
        txt_tim = new javax.swing.JTextField();
        pnl_btnSearchEmp = new javax.swing.JPanel();
        btn_tim = new javax.swing.JButton();
        pnl_cmb = new javax.swing.JPanel();
        cmb_timChucVu = new javax.swing.JComboBox<>();
        cmb_trangThai = new javax.swing.JComboBox<>();
        btn_loc = new javax.swing.JButton();
        btn_lamMoi = new javax.swing.JButton();
        pnl_centerEmp = new javax.swing.JPanel();
        spl_inforEmp = new javax.swing.JSplitPane();
        scr_bangNV = new javax.swing.JScrollPane();
        tbl_dsNV = new javax.swing.JTable();
        pnl_inforDetailEmp = new javax.swing.JPanel();
        pnl_txtInforEmp = new javax.swing.JPanel();
        pnl_empID = new javax.swing.JPanel();
        lbl_maNV = new javax.swing.JLabel();
        txt_maNV = new javax.swing.JTextField();
        pnl_nameEmp = new javax.swing.JPanel();
        lbl_tenNV = new javax.swing.JLabel();
        txt_tenNV = new javax.swing.JTextField();
        pnl_addressEmp = new javax.swing.JPanel();
        lbl_diaChi = new javax.swing.JLabel();
        txt_diaChi = new javax.swing.JTextField();
        pnl_genderEmp = new javax.swing.JPanel();
        lbl_gioiTinh = new javax.swing.JLabel();
        pnl_genderRadioEmp = new javax.swing.JPanel();
        rdb_nu = new javax.swing.JRadioButton();
        rdb_nam = new javax.swing.JRadioButton();
        pnl_dateOfBirth = new javax.swing.JPanel();
        lbl_ngaySinh = new javax.swing.JLabel();
        pnl_chooseDateOfBirth = new javax.swing.JPanel();
        chonNgaySinh = new com.toedter.calendar.JDateChooser();
        pnl_roleEmp = new javax.swing.JPanel();
        lbl_chucVu = new javax.swing.JLabel();
        cmb_chucVu = new javax.swing.JComboBox<>();
        pnl_phoneNumberEmp = new javax.swing.JPanel();
        lbl_sdt = new javax.swing.JLabel();
        txt_sdt = new javax.swing.JTextField();
        pnl_citizenIDEmp = new javax.swing.JPanel();
        lbl_cccd = new javax.swing.JLabel();
        txt_cccd = new javax.swing.JTextField();
        pnl_dateStart = new javax.swing.JPanel();
        lbl_ngayVaoLam = new javax.swing.JLabel();
        pnl_chooseDateStart = new javax.swing.JPanel();
        chonNgayVaoLam = new com.toedter.calendar.JDateChooser();
        pnl_statusEmp = new javax.swing.JPanel();
        lbl_trangThai = new javax.swing.JLabel();
        pnl_statusRadioEmp = new javax.swing.JPanel();
        rdb_dangLam = new javax.swing.JRadioButton();
        rdb_daNghi = new javax.swing.JRadioButton();
        pnl_btnEmp = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btn_xoaTrang = new javax.swing.JButton();
        btn_capNhat = new javax.swing.JButton();
        btn_datLaiMK = new javax.swing.JButton();
        btn_xuatFile = new javax.swing.JButton();
        btn_them = new javax.swing.JButton();

        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        pnl_topEmp.setMinimumSize(new java.awt.Dimension(20, 20));
        pnl_topEmp.setPreferredSize(new java.awt.Dimension(1368, 50));
        pnl_topEmp.setLayout(new javax.swing.BoxLayout(pnl_topEmp, javax.swing.BoxLayout.X_AXIS));

        pnl_searchEmp.setLayout(new javax.swing.BoxLayout(pnl_searchEmp, javax.swing.BoxLayout.X_AXIS));

        txt_tim.setPreferredSize(new java.awt.Dimension(500, 30));
        txt_tim.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Nhập mã nhân viên");
        txt_tim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_searchEmpActionPerformed(evt);
            }
        });
        txt_tim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_searchEmpKeyPressed(evt);
            }
        });
        pnl_searchEmp.add(txt_tim);

        pnl_btnSearchEmp.setLayout(new java.awt.BorderLayout());

        btn_tim.setText("Tìm kiếm");
        btn_tim.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_tim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmpActionPerformed(evt);
            }
        });
        pnl_btnSearchEmp.add(btn_tim, java.awt.BorderLayout.CENTER);

        pnl_searchEmp.add(pnl_btnSearchEmp);

        pnl_topEmp.add(pnl_searchEmp);

        pnl_cmb.setMaximumSize(new java.awt.Dimension(500, 32767));
        pnl_cmb.setPreferredSize(new java.awt.Dimension(500, 100));
        pnl_cmb.setLayout(new java.awt.GridLayout(1, 0));

        cmb_timChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chức vụ", "NV Bán Hàng", "Cửa Hàng Trưởng", "Kiểm Sát Viên" }));
        cmb_timChucVu.setPreferredSize(new java.awt.Dimension(128, 32));
        pnl_cmb.add(cmb_timChucVu);

        cmb_trangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Trạng thái", "Đang làm việc", "Đã nghỉ" }));
        cmb_trangThai.setPreferredSize(new java.awt.Dimension(128, 32));
        pnl_cmb.add(cmb_trangThai);

        btn_loc.setText("Lọc");
        btn_loc.setMaximumSize(new java.awt.Dimension(72, 40));
        btn_loc.setPreferredSize(new java.awt.Dimension(72, 40));
        btn_loc.setIcon(SVGIcon.getSVGIcon("imgs/public/filter.svg"));
        btn_loc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchFilterEmpActionPerformed(evt);
            }
        });
        pnl_cmb.add(btn_loc);

        btn_lamMoi.setIcon(SVGIcon.getSVGIcon("imgs/public/refresh.svg"));
        btn_lamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reloadEmpActionPerformed(evt);
            }
        });
        pnl_cmb.add(btn_lamMoi);

        pnl_topEmp.add(pnl_cmb);

        add(pnl_topEmp, java.awt.BorderLayout.PAGE_START);

        pnl_centerEmp.setLayout(new javax.swing.BoxLayout(pnl_centerEmp, javax.swing.BoxLayout.LINE_AXIS));

        spl_inforEmp.setResizeWeight(0.6);

        scr_bangNV.setMinimumSize(new java.awt.Dimension(400, 20));
        scr_bangNV.setPreferredSize(new java.awt.Dimension(800, 402));

        tbl_dsNV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã NV", "Họ tên", "Ngày sinh", "Chức vụ"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_dsNV.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbl_dsNV.setShowGrid(false);
        scr_bangNV.setViewportView(tbl_dsNV);
        if (tbl_dsNV.getColumnModel().getColumnCount() > 0) {
            tbl_dsNV.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        spl_inforEmp.setLeftComponent(scr_bangNV);

        pnl_inforDetailEmp.setMinimumSize(new java.awt.Dimension(400, 379));
        pnl_inforDetailEmp.setPreferredSize(new java.awt.Dimension(250, 100));
        pnl_inforDetailEmp.setLayout(new java.awt.BorderLayout());

        pnl_txtInforEmp.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin nhân viên", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(71, 118, 185)), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))); // NOI18N
        pnl_txtInforEmp.setLayout(new javax.swing.BoxLayout(pnl_txtInforEmp, javax.swing.BoxLayout.Y_AXIS));

        pnl_empID.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_empID.setMinimumSize(new java.awt.Dimension(118, 6));
        pnl_empID.setPreferredSize(new java.awt.Dimension(210, 40));
        pnl_empID.setLayout(new javax.swing.BoxLayout(pnl_empID, javax.swing.BoxLayout.LINE_AXIS));

        lbl_maNV.setText("Mã NV:");
        lbl_maNV.setMaximumSize(new java.awt.Dimension(45, 16));
        lbl_maNV.setMinimumSize(new java.awt.Dimension(45, 16));
        lbl_maNV.setPreferredSize(new java.awt.Dimension(100, 16));
        pnl_empID.add(lbl_maNV);

        txt_maNV.setEditable(false);
        txt_maNV.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_maNV.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        txt_maNV.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_maNV.setPreferredSize(new java.awt.Dimension(150, 40));
        pnl_empID.add(txt_maNV);

        pnl_txtInforEmp.add(pnl_empID);

        pnl_nameEmp.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_nameEmp.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_nameEmp.setLayout(new javax.swing.BoxLayout(pnl_nameEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_tenNV.setText("Họ tên:");
        lbl_tenNV.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_nameEmp.add(lbl_tenNV);

        txt_tenNV.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txt_tenNV.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_tenNV.setPreferredSize(new java.awt.Dimension(64, 30));
        pnl_nameEmp.add(txt_tenNV);

        pnl_txtInforEmp.add(pnl_nameEmp);

        pnl_addressEmp.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_addressEmp.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_addressEmp.setLayout(new javax.swing.BoxLayout(pnl_addressEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_diaChi.setText("Địa chỉ:");
        lbl_diaChi.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_addressEmp.add(lbl_diaChi);

        txt_diaChi.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txt_diaChi.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_diaChi.setPreferredSize(new java.awt.Dimension(64, 30));
        pnl_addressEmp.add(txt_diaChi);

        pnl_txtInforEmp.add(pnl_addressEmp);

        pnl_genderEmp.setMaximumSize(new java.awt.Dimension(32815, 40));
        pnl_genderEmp.setPreferredSize(new java.awt.Dimension(183, 40));
        pnl_genderEmp.setLayout(new javax.swing.BoxLayout(pnl_genderEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_gioiTinh.setText("Giới tính:");
        lbl_gioiTinh.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_genderEmp.add(lbl_gioiTinh);

        pnl_genderRadioEmp.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_genderRadioEmp.setMinimumSize(new java.awt.Dimension(103, 30));
        pnl_genderRadioEmp.setPreferredSize(new java.awt.Dimension(103, 30));
        pnl_genderRadioEmp.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        group_gender.add(rdb_nu);
        rdb_nu.setText("Nữ");
        rdb_nu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdb_femaleActionPerformed(evt);
            }
        });
        pnl_genderRadioEmp.add(rdb_nu);

        group_gender.add(rdb_nam);
        rdb_nam.setText("Nam");
        pnl_genderRadioEmp.add(rdb_nam);

        pnl_genderEmp.add(pnl_genderRadioEmp);

        pnl_txtInforEmp.add(pnl_genderEmp);

        pnl_dateOfBirth.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_dateOfBirth.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_dateOfBirth.setLayout(new javax.swing.BoxLayout(pnl_dateOfBirth, javax.swing.BoxLayout.LINE_AXIS));

        lbl_ngaySinh.setText("Ngày sinh:");
        lbl_ngaySinh.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_dateOfBirth.add(lbl_ngaySinh);

        pnl_chooseDateOfBirth.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pnl_chooseDateOfBirth.setMaximumSize(new java.awt.Dimension(32767, 40));
        pnl_chooseDateOfBirth.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_chooseDateOfBirth.setLayout(new java.awt.GridLayout(1, 0));

        chonNgaySinh.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        chonNgaySinh.setPreferredSize(new java.awt.Dimension(148, 30));
        pnl_chooseDateOfBirth.add(chonNgaySinh);

        pnl_dateOfBirth.add(pnl_chooseDateOfBirth);

        pnl_txtInforEmp.add(pnl_dateOfBirth);

        pnl_roleEmp.setMaximumSize(new java.awt.Dimension(32815, 40));
        pnl_roleEmp.setPreferredSize(new java.awt.Dimension(183, 40));
        pnl_roleEmp.setLayout(new javax.swing.BoxLayout(pnl_roleEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_chucVu.setText("Chức vụ:");
        lbl_chucVu.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_roleEmp.add(lbl_chucVu);

        cmb_chucVu.setModel(new DefaultComboBoxModel(new String[] {"Quản lý", "Nhân viên Bán Vé"}));
        cmb_chucVu.setMaximumSize(new java.awt.Dimension(32767, 40));
        cmb_chucVu.setMinimumSize(new java.awt.Dimension(144, 30));
        cmb_chucVu.setPreferredSize(new java.awt.Dimension(144, 30));
        pnl_roleEmp.add(cmb_chucVu);

        pnl_txtInforEmp.add(pnl_roleEmp);

        pnl_phoneNumberEmp.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_phoneNumberEmp.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_phoneNumberEmp.setLayout(new javax.swing.BoxLayout(pnl_phoneNumberEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_sdt.setText("SDT:");
        lbl_sdt.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_phoneNumberEmp.add(lbl_sdt);

        txt_sdt.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txt_sdt.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_sdt.setPreferredSize(new java.awt.Dimension(64, 30));
        pnl_phoneNumberEmp.add(txt_sdt);

        pnl_txtInforEmp.add(pnl_phoneNumberEmp);

        pnl_citizenIDEmp.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_citizenIDEmp.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_citizenIDEmp.setLayout(new javax.swing.BoxLayout(pnl_citizenIDEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_cccd.setText("CCCD:");
        lbl_cccd.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_citizenIDEmp.add(lbl_cccd);

        txt_cccd.setMaximumSize(new java.awt.Dimension(2147483647, 30));
        txt_cccd.setMinimumSize(new java.awt.Dimension(64, 30));
        txt_cccd.setPreferredSize(new java.awt.Dimension(64, 30));
        pnl_citizenIDEmp.add(txt_cccd);

        pnl_txtInforEmp.add(pnl_citizenIDEmp);

        pnl_dateStart.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_dateStart.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_dateStart.setLayout(new javax.swing.BoxLayout(pnl_dateStart, javax.swing.BoxLayout.LINE_AXIS));

        lbl_ngayVaoLam.setText("Ngày vào làm:");
        lbl_ngayVaoLam.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_dateStart.add(lbl_ngayVaoLam);

        pnl_chooseDateStart.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        pnl_chooseDateStart.setMaximumSize(new java.awt.Dimension(32767, 40));
        pnl_chooseDateStart.setPreferredSize(new java.awt.Dimension(100, 30));
        pnl_chooseDateStart.setLayout(new java.awt.GridLayout(1, 0, 3, 3));
        pnl_chooseDateStart.add(chonNgayVaoLam);

        pnl_dateStart.add(pnl_chooseDateStart);

        pnl_txtInforEmp.add(pnl_dateStart);

        pnl_statusEmp.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_statusEmp.setPreferredSize(new java.awt.Dimension(230, 40));
        pnl_statusEmp.setLayout(new javax.swing.BoxLayout(pnl_statusEmp, javax.swing.BoxLayout.LINE_AXIS));

        lbl_trangThai.setText("Trạng thái:");
        lbl_trangThai.setPreferredSize(lbl_maNV.getPreferredSize());
        pnl_statusEmp.add(lbl_trangThai);

        pnl_statusRadioEmp.setMaximumSize(new java.awt.Dimension(32767, 30));
        pnl_statusRadioEmp.setMinimumSize(new java.awt.Dimension(103, 30));
        pnl_statusRadioEmp.setPreferredSize(new java.awt.Dimension(103, 30));
        pnl_statusRadioEmp.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        group_statusEmp.add(rdb_dangLam);
        rdb_dangLam.setText("Đang làm việc");
        rdb_dangLam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdb_wokingActionPerformed(evt);
            }
        });
        pnl_statusRadioEmp.add(rdb_dangLam);

        group_statusEmp.add(rdb_daNghi);
        rdb_daNghi.setText("Đã nghỉ");
        pnl_statusRadioEmp.add(rdb_daNghi);

        pnl_statusEmp.add(pnl_statusRadioEmp);

        pnl_txtInforEmp.add(pnl_statusEmp);

        pnl_inforDetailEmp.add(pnl_txtInforEmp, java.awt.BorderLayout.CENTER);

        pnl_btnEmp.setMaximumSize(new java.awt.Dimension(2147483647, 100));
        pnl_btnEmp.setMinimumSize(new java.awt.Dimension(176, 100));
        pnl_btnEmp.setPreferredSize(new java.awt.Dimension(100, 150));
        pnl_btnEmp.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(2, 2, 5, 5));

        btn_xoaTrang.setText("Xóa trắng");
        btn_xoaTrang.setIcon(SVGIcon.getSVGIcon("imgs/public/clear.svg"));
        btn_xoaTrang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearValueActionPerformed(evt);
            }
        });
        jPanel1.add(btn_xoaTrang);

        btn_capNhat.setText("Cập nhật");
        btn_capNhat.setIcon(SVGIcon.getSVGIcon("imgs/public/update.svg"));
        btn_capNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateEmpActionPerformed(evt);
            }
        });
        jPanel1.add(btn_capNhat);

        btn_datLaiMK.setText("Đặt lại mật khẩu");
        btn_datLaiMK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_changePassActionPerformed(evt);
            }
        });
        jPanel1.add(btn_datLaiMK);

        btn_xuatFile.setText("Xuất Excel");
        btn_xuatFile.setIcon(SVGIcon.getSVGIcon("imgs/public/excel.svg"));
        btn_xuatFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_printFileActionPerformed(evt);
            }
        });
        jPanel1.add(btn_xuatFile);

        pnl_btnEmp.add(jPanel1, java.awt.BorderLayout.CENTER);

        btn_them.setText("Thêm mới nhân viên");
        btn_them.setPreferredSize(new java.awt.Dimension(72, 50));
        btn_them.putClientProperty(FlatClientProperties.STYLE,""
            + "background:$Menu.background;"
            + "foreground:$Menu.foreground;");
        btn_them.setIcon(SVGIcon.getPrimarySVGIcon("imgs/public/add.svg"));
        btn_them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addEmpActionPerformed(evt);
            }
        });
        pnl_btnEmp.add(btn_them, java.awt.BorderLayout.SOUTH);

        pnl_inforDetailEmp.add(pnl_btnEmp, java.awt.BorderLayout.SOUTH);

        spl_inforEmp.setRightComponent(pnl_inforDetailEmp);

        pnl_centerEmp.add(spl_inforEmp);

        add(pnl_centerEmp, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_searchEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_searchEmpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_searchEmpActionPerformed

    private void rdb_femaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdb_femaleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdb_femaleActionPerformed

    private void rdb_wokingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdb_wokingActionPerformed
        // TODO add your handling code here:
    }

    private void btn_searchEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmpActionPerformed
        String searchQuery = txt_tim.getText();
        if (searchQuery.isBlank()) {
            Notifications.getInstance().show(Notifications.Type.INFO, "Vui lòng điền mã nhân viên");
            return;
        }

    }

    private void btn_searchFilterEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchFilterEmpActionPerformed
        int role = cmb_timChucVu.getSelectedIndex();
        int status = cmb_trangThai.getSelectedIndex();

    }

    private void btn_reloadEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reloadEmpActionPerformed

        renderEmployeeInfor();
    }

    private void btn_updateEmpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateEmpActionPerformed
       
        if(tbl_dsNV.getSelectedRow() == -1) {
            Notifications.getInstance().show(Notifications.Type.ERROR, "Vui lòng chọn nhân viên để cập nhật");
            return;
        }

    }

   
    private void btn_addEmpActionPerformed(java.awt.event.ActionEvent evt) {
        // Kiểm tra tính hợp lệ của dữ liệu nhập vào
        if (!validEmployee()) {
            return; // Nếu dữ liệu không hợp lệ, dừng hàm
        }

        // Lấy dữ liệu của nhân viên từ các trường nhập liệu
        NhanVien newNhanVien = getCurrentValue();

        // Thêm dữ liệu vào JTable (tbl_dsNV)
        String status = newNhanVien.getTrangThai() ? "Đang làm việc" : "Đã nghỉ";
        tblModel_NhanVien.addRow(new Object[]{
            newNhanVien.getMaNV(),              // Mã nhân viên
            newNhanVien.getTenNV(),             // Tên nhân viên
            newNhanVien.getNgaySinh().toString(), // Ngày sinh
            status                              // Trạng thái làm việc
        });
        // Thêm dữ liệu vào cơ sở dữ liệu
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            // Câu lệnh SQL để thêm dữ liệu vào bảng NhanVien
            String sql = "INSERT INTO NhanVien (maNV, tenNV, chucVu, trangThai, ngaySinh, gioiTinh, sdt, cccd, ngayVaoLam, diaChi) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newNhanVien.getMaNV());          // Mã nhân viên
                pstmt.setString(2, newNhanVien.getTenNV());         // Tên nhân viên
                pstmt.setString(3, newNhanVien.getChucVu());        // Chức vụ
                pstmt.setBoolean(4, newNhanVien.getTrangThai());    // Trạng thái
                pstmt.setDate(5, new java.sql.Date(newNhanVien.getNgaySinh().getTime())); // Ngày sinh
                pstmt.setBoolean(6, newNhanVien.isGioiTinh());      // Giới tính
                pstmt.setString(7, newNhanVien.getSdt());           // Số điện thoại
                pstmt.setString(8, newNhanVien.getCccd());          // CCCD
                pstmt.setDate(9, new java.sql.Date(newNhanVien.getNgayVaoLam().getTime())); // Ngày vào làm
                pstmt.setString(10, newNhanVien.getDiaChi());       // Địa chỉ
                pstmt.executeUpdate(); // Thực thi câu lệnh thêm dữ liệu
                Notifications.getInstance().show(Notifications.Type.SUCCESS, "Nhân viên đã được thêm thành công!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lưu dữ liệu vào cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


    

    private void txt_searchEmpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_searchEmpKeyPressed

    }

    private void btn_changePassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_changePassActionPerformed
    }

    private void btn_clearValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearValueActionPerformed

    }

    private void btn_printFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_printFileActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn đường dẫn và tên file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Hiển thị hộp thoại và kiểm tra nếu người dùng chọn OK
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                // Lấy đường dẫn và tên file được chọn
                File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();
                // Gọi phương thức để tạo file Excel với đường dẫn và tên file đã chọn
//                createExcel(bus.getAllEmployee(), filePath+".xlsx");
                Desktop.getDesktop().open(new File(filePath+".xlsx"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }//GEN-LAST:event_btn_printFileActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_them;
    private javax.swing.JButton btn_datLaiMK;
    private javax.swing.JButton btn_xoaTrang;
    private javax.swing.JButton btn_xuatFile;
    private javax.swing.JButton btn_lamMoi;
    private javax.swing.JButton btn_tim;
    private javax.swing.JButton btn_loc;
    private javax.swing.JButton btn_capNhat;
    private com.toedter.calendar.JDateChooser chonNgaySinh;
    private com.toedter.calendar.JDateChooser chonNgayVaoLam;
    private javax.swing.JComboBox<String> cmb_timChucVu;
    private javax.swing.JComboBox<String> cmb_chucVu;
    private javax.swing.JComboBox<String> cmb_trangThai;
    private javax.swing.ButtonGroup group_gender;
    private javax.swing.ButtonGroup group_roleEmp;
    private javax.swing.ButtonGroup group_statusEmp;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_diaChi;
    private javax.swing.JLabel lbl_cccd;
    private javax.swing.JLabel lbl_ngaySinh;
    private javax.swing.JLabel lbl_ngayVaoLam;
    private javax.swing.JLabel lbl_maNV;
    private javax.swing.JLabel lbl_gioiTinh;
    private javax.swing.JLabel lbl_tenNV;
    private javax.swing.JLabel lbl_sdt;
    private javax.swing.JLabel lbl_chucVu;
    private javax.swing.JLabel lbl_trangThai;
    private javax.swing.JPanel pnl_addressEmp;
    private javax.swing.JPanel pnl_btnEmp;
    private javax.swing.JPanel pnl_btnSearchEmp;
    private javax.swing.JPanel pnl_centerEmp;
    private javax.swing.JPanel pnl_chooseDateOfBirth;
    private javax.swing.JPanel pnl_chooseDateStart;
    private javax.swing.JPanel pnl_citizenIDEmp;
    private javax.swing.JPanel pnl_cmb;
    private javax.swing.JPanel pnl_dateOfBirth;
    private javax.swing.JPanel pnl_dateStart;
    private javax.swing.JPanel pnl_empID;
    private javax.swing.JPanel pnl_genderEmp;
    private javax.swing.JPanel pnl_genderRadioEmp;
    private javax.swing.JPanel pnl_inforDetailEmp;
    private javax.swing.JPanel pnl_nameEmp;
    private javax.swing.JPanel pnl_phoneNumberEmp;
    private javax.swing.JPanel pnl_roleEmp;
    private javax.swing.JPanel pnl_searchEmp;
    private javax.swing.JPanel pnl_statusEmp;
    private javax.swing.JPanel pnl_statusRadioEmp;
    private javax.swing.JPanel pnl_topEmp;
    private javax.swing.JPanel pnl_txtInforEmp;
    private javax.swing.JRadioButton rdb_nu;
    private javax.swing.JRadioButton rdb_nam;
    private javax.swing.JRadioButton rdb_daNghi;
    private javax.swing.JRadioButton rdb_dangLam;
    private javax.swing.JScrollPane scr_bangNV;
    private javax.swing.JSplitPane spl_inforEmp;
    private javax.swing.JTable tbl_dsNV;
    private javax.swing.JTextField txt_diaChi;
    private javax.swing.JTextField txt_cccd;
    private javax.swing.JTextField txt_maNV;
    private javax.swing.JTextField txt_tenNV;
    private javax.swing.JTextField txt_sdt;
    private javax.swing.JTextField txt_tim;
    // End of variables declaration//GEN-END:variables

    private void loadDataToTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT maNV, tenNV, ngaySinh, trangThai FROM NhanVien";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                tblModel_NhanVien.setRowCount(0); // Xóa các hàng cũ trong bảng
                while (rs.next()) {
                    String maNV = rs.getString("maNV");
                    String tenNV = rs.getString("tenNV");
                    Date ngaySinh = rs.getDate("ngaySinh");
                    boolean trangThai = rs.getBoolean("trangThai");
                    String statusText = trangThai ? "Đang làm việc" : "Đã nghỉ";
                    
                    tblModel_NhanVien.addRow(new Object[]{maNV, tenNV, ngaySinh.toString(), statusText});
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu từ cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


    

    
}
