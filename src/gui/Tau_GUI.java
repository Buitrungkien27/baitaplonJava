package gui;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public final class Tau_GUI extends JPanel {
    private JLabel lblMaTau, lblTenTau, lblLoaiToa, lblTrangThai, lblGaDi, lblGaDen, lblTGDi, lblTGDen;
    private JTextField txtMaTau, txtTenTau, txtLoaiToa;
    private DefaultTableModel modelTau;
    private JTable tbl_DsTau;
    private JTextField txtTrangThai; 
    private JTextField  txtSoGhe;
    private JComboBox<String> comboTrangThai, comboGaDi, comboGaDen, comboLoaiToa;
    private JButton btnThem, btnXoa, btnCapNhat, btnXoaTrang;
    private JDateChooser dateChooserDi, dateChooserDen;
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QLBanVe";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa123";

    public Tau_GUI() {
        initComponents();
        init();
        loadDataToTable(); // Tải dữ liệu từ cơ sở dữ liệu khi khởi động
    }

    private void loadDataToTable() {
        modelTau.setRowCount(0); // Xóa dữ liệu cũ trong bảng

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "SELECT maTau, tenTau, loaiToa, gaDi, gaDen, thoiGianDi, thoiGianDen, soGhe FROM Tau";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Object[] row = {
                        rs.getString("maTau"),
                        rs.getString("tenTau"),
                        rs.getString("loaiToa"),
                        rs.getString("gaDi"),
                        rs.getString("gaDen"),
                        rs.getTimestamp("thoiGianDi"),
                        rs.getTimestamp("thoiGianDen"),
                        rs.getInt("soGhe")
                    };
                    modelTau.addRow(row); // Thêm dữ liệu vào bảng
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu từ cơ sở dữ liệu: " + e.getMessage());
        }
    }

    public void init() {
        modelTau = new DefaultTableModel(new String[]{
            "Mã tàu", "Tên tàu", "Loại Toa", "Ga đi", "Ga đến", "Thời gian đi", "Thời gian đến", "Số ghế"}, 0);

        tbl_DsTau = new JTable(modelTau);
        tbl_DsTau.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] columnWidths = {80, 150, 150, 100, 100, 150, 150};
        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn column = tbl_DsTau.getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidths[i]);
        }

        tbl_DsTau.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int rowIndex = tbl_DsTau.getSelectedRow();
                if (rowIndex == -1) return;
                renderCurrentTrain(rowIndex);
            }
        });
    }

    private void renderCurrentTrain(int rowIndex) {
        txtMaTau.setText(modelTau.getValueAt(rowIndex, 0).toString());
        txtTenTau.setText(modelTau.getValueAt(rowIndex, 1).toString());
        comboLoaiToa.setSelectedItem(modelTau.getValueAt(rowIndex, 2).toString());
        comboGaDi.setSelectedItem(modelTau.getValueAt(rowIndex, 3).toString());
        comboGaDen.setSelectedItem(modelTau.getValueAt(rowIndex, 4).toString());
        dateChooserDi.setDate((java.util.Date) modelTau.getValueAt(rowIndex, 5));
        dateChooserDen.setDate((java.util.Date) modelTau.getValueAt(rowIndex, 6));
        txtSoGhe.setText(modelTau.getValueAt(rowIndex, 7).toString());

        comboTrangThai.setSelectedIndex(0);
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Quản lý tàu", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        add(lblTitle, BorderLayout.NORTH);

        modelTau = new DefaultTableModel(new String[]{
            "Mã tàu", "Tên tàu", "Loại Toa", "Ga đi", "Ga đến", "Thời gian đi", "Thời gian đến" , "số Ghế"}, 0);

        tbl_DsTau = new JTable(modelTau);
        JScrollPane scrollPane = new JScrollPane(tbl_DsTau, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelEast = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelInfoTau = new JPanel(new GridLayout(4, 2, 10, 10));
        panelInfoTau.setBorder(BorderFactory.createTitledBorder("Thông tin tàu"));

        panelInfoTau.add(new JLabel("Mã tàu:"));
        txtMaTau = new JTextField();
        txtMaTau.setEditable(true);
        panelInfoTau.add(txtMaTau);

        panelInfoTau.add(new JLabel("Tên tàu:"));
        panelInfoTau.add(txtTenTau = new JTextField());

        panelInfoTau.add(new JLabel("Loại Toa:"));
        String[] danhSachToa = { "Toa 1", "Toa 2", "Toa 3", "Toa 4", "Toa 5" };
        comboLoaiToa = new JComboBox<>(danhSachToa);
        panelInfoTau.add(comboLoaiToa);

        panelInfoTau.add(new JLabel("Số ghế:"));
        txtSoGhe = new JTextField();
        panelInfoTau.add(txtSoGhe);

        JPanel panelGa = new JPanel(new GridLayout(2, 2, 10, 10));
        panelGa.setBorder(BorderFactory.createTitledBorder("Ga đi/Ga đến"));

        panelGa.add(new JLabel("Ga đi:"));
        comboGaDi = new JComboBox<>(new String[]{"Sài Gòn"});
        panelGa.add(comboGaDi);

        panelGa.add(new JLabel("Ga đến:"));
        comboGaDen = new JComboBox<>(new String[]{"An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bắc Giang", "Bắc Kạn", "Bắc Ninh"});
        panelGa.add(comboGaDen);

        JPanel panelThoiGian = new JPanel(new GridLayout(2, 2, 10, 10));
        panelThoiGian.setBorder(BorderFactory.createTitledBorder("Thời gian"));

        panelThoiGian.add(new JLabel("Thời gian đi:"));
        dateChooserDi = new JDateChooser();
        dateChooserDi.setPreferredSize(new Dimension(180, 30));
        dateChooserDi.setDateFormatString("dd/MM/yyyy");
        dateChooserDi.setDate(Calendar.getInstance().getTime());
        panelThoiGian.add(dateChooserDi);

        panelThoiGian.add(new JLabel("Thời gian đến:"));
        dateChooserDen = new JDateChooser();
        dateChooserDen.setPreferredSize(new Dimension(180, 30));
        dateChooserDen.setDateFormatString("dd/MM/yyyy");
        dateChooserDen.setDate(Calendar.getInstance().getTime());
        panelThoiGian.add(dateChooserDen);

        JPanel panelTrangThai = new JPanel(new GridLayout(1, 2, 10, 10));
        panelTrangThai.setBorder(BorderFactory.createTitledBorder("Trạng thái"));

        panelTrangThai.add(new JLabel("Trạng thái:"));
        comboTrangThai = new JComboBox<>(new String[]{"còn chỗ", "Đầy chỗ"});
        panelTrangThai.add(comboTrangThai);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelEast.add(panelInfoTau, gbc);

        gbc.gridy = 1;
        panelEast.add(panelGa, gbc);

        gbc.gridy = 2;
        panelEast.add(panelThoiGian, gbc);

        gbc.gridy = 3;
        panelEast.add(panelTrangThai, gbc);

        add(panelEast, BorderLayout.EAST);

        JPanel panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnCapNhat = new JButton("Cập nhật");
        btnXoaTrang = new JButton("Xóa trắng");

        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtMaTau.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Mã tàu không được để trống!");
                    return;
                }

                if (txtTenTau.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tên tàu không được để trống!");
                    return;
                }

                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                    String sql = "INSERT INTO Tau (maTau, tenTau, loaiToa, gaDi, gaDen, thoiGianDi, thoiGianDen, trangThai, soGhe) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, txtMaTau.getText());
                        pstmt.setString(2, txtTenTau.getText());
                        pstmt.setString(3, comboLoaiToa.getSelectedItem().toString());
                        pstmt.setString(4, comboGaDi.getSelectedItem().toString());
                        pstmt.setString(5, comboGaDen.getSelectedItem().toString());
                        pstmt.setTimestamp(6, new java.sql.Timestamp(dateChooserDi.getDate().getTime()));
                        pstmt.setTimestamp(7, new java.sql.Timestamp(dateChooserDen.getDate().getTime()));
                        pstmt.setString(8, comboTrangThai.getSelectedItem().toString());
                        pstmt.setInt(9, Integer.parseInt(txtSoGhe.getText()));
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Dữ liệu đã được lưu vào cơ sở dữ liệu!");

                        // Thêm dữ liệu mới vào bảng ngay lập tức
                        Object[] row = {
                            txtMaTau.getText(),
                            txtTenTau.getText(),
                            comboLoaiToa.getSelectedItem().toString(),
                            comboGaDi.getSelectedItem(),
                            comboGaDen.getSelectedItem(),
                            dateChooserDi.getDate(),
                            dateChooserDen.getDate(),
                            txtSoGhe.getText(),
                            comboTrangThai.getSelectedItem().toString()
                        };
                        modelTau.addRow(row); // Thêm dòng vào model của bảng
                        clearInputFields(); // Xóa các trường nhập liệu
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Lỗi khi lưu dữ liệu vào cơ sở dữ liệu: " + ex.getMessage());
                }
            }
        });

        panelSouth.add(btnThem);
        panelSouth.add(btnXoa);
        panelSouth.add(btnCapNhat);
        panelSouth.add(btnXoaTrang);

        add(panelSouth, BorderLayout.SOUTH);
    }

    private void clearInputFields() {
        txtMaTau.setText("");
        txtTenTau.setText("");
       // txtLoaiToa.setText("");
        comboGaDi.setSelectedIndex(0);
        comboGaDen.setSelectedIndex(0);
        dateChooserDi.setDate(Calendar.getInstance().getTime());
        dateChooserDen.setDate(Calendar.getInstance().getTime());
        comboTrangThai.setSelectedIndex(0);
    }

    class NumericDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && isNumeric(string)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null && isNumeric(text)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean isNumeric(String text) {
            try {
                Integer.parseInt(text);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Hệ thống quản lý tàu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.add(new Tau_GUI());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
