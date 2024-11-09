package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.toedter.calendar.JDateChooser;
import javax.swing.table.DefaultTableModel;

public final class Tau_GUI extends JPanel {
    private JTextField txtMaTau, txtTenTau, txtSoGhe;
    private DefaultTableModel modelTau;
    private JTable tbl_DsTau;
    private JComboBox<String> comboTrangThai, comboGaDi, comboGaDen, comboLoaiToa;
    private JButton btnThem, btnXoa, btnCapNhat, btnXoaTrang;
    private JDateChooser dateChooserDi, dateChooserDen;
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QLBanVe";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa123";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 

    public Tau_GUI() {
        initComponents();
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
                        dateFormat.format(rs.getTimestamp("thoiGianDi")),
                        dateFormat.format(rs.getTimestamp("thoiGianDen")),
                        rs.getInt("soGhe")
                    };
                    modelTau.addRow(row); // Thêm dữ liệu vào bảng
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu từ cơ sở dữ liệu: " + e.getMessage());
        }
    }

    private void renderCurrentTrain(int rowIndex) {
        if (rowIndex != -1) { // Đảm bảo một dòng hợp lệ được chọn
            txtMaTau.setText(modelTau.getValueAt(rowIndex, 0).toString());
            txtTenTau.setText(modelTau.getValueAt(rowIndex, 1).toString());
            comboLoaiToa.setSelectedItem(modelTau.getValueAt(rowIndex, 2).toString());
            comboGaDi.setSelectedItem(modelTau.getValueAt(rowIndex, 3).toString());
            comboGaDen.setSelectedItem(modelTau.getValueAt(rowIndex, 4).toString());

            // Chuyển đổi chuỗi thành ngày
            try {
                dateChooserDi.setDate(dateFormat.parse(modelTau.getValueAt(rowIndex, 5).toString()));
                dateChooserDen.setDate(dateFormat.parse(modelTau.getValueAt(rowIndex, 6).toString()));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Lỗi chuyển đổi ngày: " + e.getMessage());
            }

            txtSoGhe.setText(modelTau.getValueAt(rowIndex, 7).toString());
        }
    }

    private void clearInputFields() {
        txtMaTau.setText("");
        txtTenTau.setText("");
        comboLoaiToa.setSelectedIndex(0);
        comboGaDi.setSelectedIndex(0);
        comboGaDen.setSelectedIndex(0);
        dateChooserDi.setDate(Calendar.getInstance().getTime());
        dateChooserDen.setDate(Calendar.getInstance().getTime());
        txtSoGhe.setText("");
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Quản lý tàu", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        add(lblTitle, BorderLayout.NORTH);

        modelTau = new DefaultTableModel(new String[]{
            "Mã tàu", "Tên tàu", "Loại Toa", "Ga đi", "Ga đến", "Thời gian đi", "Thời gian đến", "Số ghế"}, 0);

        tbl_DsTau = new JTable(modelTau);
        tbl_DsTau.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
        tbl_DsTau.setFillsViewportHeight(true); 

        JScrollPane scrollPane = new JScrollPane(tbl_DsTau);
        add(scrollPane, BorderLayout.CENTER);

        tbl_DsTau.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = tbl_DsTau.getSelectedRow();
                if (selectedRow != -1) {
                    renderCurrentTrain(selectedRow);
                }
            }
        });

        JPanel panelEast = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelInfoTau = new JPanel(new GridLayout(4, 2, 10, 10));
        panelInfoTau.setBorder(BorderFactory.createTitledBorder("Thông tin tàu"));

        panelInfoTau.add(new JLabel("Mã tàu:"));
        txtMaTau = new JTextField();
        panelInfoTau.add(txtMaTau);

        panelInfoTau.add(new JLabel("Tên tàu:"));
        txtTenTau = new JTextField();
        panelInfoTau.add(txtTenTau);

        panelInfoTau.add(new JLabel("Loại Toa:"));
        comboLoaiToa = new JComboBox<>(new String[]{"Toa 1", "Toa 2", "Toa 3", "Toa 4", "Toa 5"});
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
        comboGaDen = new JComboBox<>(new String[]{"An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bắc Giang", "Bắc Kạn", "Bắc Ninh",
        		"Bến Tre", "Bình Dương", "Bình Định", "Bình Phước", "Bình Thuận", "Cà Mau",
        		"Cần Thơ", "Cao Bằng", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên",
        		"Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội",
        		"Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên",
        		"Khánh Hòa", "Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn",
        		"Lào Cai", "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận",
        		"Phú Thọ", "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh",
        		"Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên",
        		"Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang", "Sài Gòn", "Trà Vinh",
        		"Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
});
        panelGa.add(comboGaDen);

        JPanel panelThoiGian = new JPanel(new GridLayout(2, 2, 10, 10));
        panelThoiGian.setBorder(BorderFactory.createTitledBorder("Thời gian"));

        panelThoiGian.add(new JLabel("Thời gian đi:"));
        dateChooserDi = new JDateChooser();
        dateChooserDi.setDateFormatString("dd/MM/yyyy");
        panelThoiGian.add(dateChooserDi);

        panelThoiGian.add(new JLabel("Thời gian đến:"));
        dateChooserDen = new JDateChooser();
        dateChooserDen.setDateFormatString("dd/MM/yyyy"); 
        panelThoiGian.add(dateChooserDen);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelEast.add(panelInfoTau, gbc);

        gbc.gridy = 1;
        panelEast.add(panelGa, gbc);

        gbc.gridy = 2;
        panelEast.add(panelThoiGian, gbc);

        add(panelEast, BorderLayout.EAST);

        JPanel panelSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnCapNhat = new JButton("Cập nhật");
        btnXoaTrang = new JButton("Xóa trắng");

        btnThem.addActionListener(e -> addTrain());
        btnXoa.addActionListener(e -> deleteSelectedRow());
        btnXoaTrang.addActionListener(e -> clearInputFields());
        btnCapNhat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tbl_DsTau.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng cần cập nhật!");
                    return;
                }

                // Lấy mã tàu từ dòng được chọn
                String maTau = modelTau.getValueAt(selectedRow, 0).toString();

                try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                    String sql = "UPDATE Tau SET tenTau = ?, loaiToa = ?, gaDi = ?, gaDen = ?, thoiGianDi = ?, thoiGianDen = ?, soGhe = ? WHERE maTau = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, txtTenTau.getText());
                        pstmt.setString(2, comboLoaiToa.getSelectedItem().toString());
                        pstmt.setString(3, comboGaDi.getSelectedItem().toString());
                        pstmt.setString(4, comboGaDen.getSelectedItem().toString());
                        pstmt.setTimestamp(5, new java.sql.Timestamp(dateChooserDi.getDate().getTime()));
                        pstmt.setTimestamp(6, new java.sql.Timestamp(dateChooserDen.getDate().getTime()));
                        pstmt.setInt(7, Integer.parseInt(txtSoGhe.getText()));
                        pstmt.setString(8, maTau);

                        int affectedRows = pstmt.executeUpdate();
                        if (affectedRows > 0) {
                            JOptionPane.showMessageDialog(null, "Dữ liệu đã được cập nhật thành công!");

                            // Cập nhật dữ liệu trong bảng hiển thị
                            modelTau.setValueAt(txtTenTau.getText(), selectedRow, 1);
                            modelTau.setValueAt(comboLoaiToa.getSelectedItem().toString(), selectedRow, 2);
                            modelTau.setValueAt(comboGaDi.getSelectedItem().toString(), selectedRow, 3);
                            modelTau.setValueAt(comboGaDen.getSelectedItem().toString(), selectedRow, 4);
                            modelTau.setValueAt(dateFormat.format(dateChooserDi.getDate()), selectedRow, 5);
                            modelTau.setValueAt(dateFormat.format(dateChooserDen.getDate()), selectedRow, 6);
                            modelTau.setValueAt(txtSoGhe.getText(), selectedRow, 7);
                        } else {
                            JOptionPane.showMessageDialog(null, "Không có dòng nào được cập nhật. Kiểm tra lại mã tàu hoặc dữ liệu cần cập nhật.");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); // Hiển thị chi tiết lỗi trong console
                    JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật dữ liệu: " + ex.getMessage());
                }
            }
        });


        panelSouth.add(btnThem);
        panelSouth.add(btnXoa);
        panelSouth.add(btnCapNhat);
        panelSouth.add(btnXoaTrang);

        add(panelSouth, BorderLayout.SOUTH);
    }

    private void addTrain() {
        if (txtMaTau.getText().isEmpty() || txtTenTau.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Mã tàu và tên tàu không được để trống!");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO Tau (maTau, tenTau, loaiToa, gaDi, gaDen, thoiGianDi, thoiGianDen, soGhe) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, txtMaTau.getText());
                pstmt.setString(2, txtTenTau.getText());
                pstmt.setString(3, comboLoaiToa.getSelectedItem().toString());
                pstmt.setString(4, comboGaDi.getSelectedItem().toString());
                pstmt.setString(5, comboGaDen.getSelectedItem().toString());
                pstmt.setTimestamp(6, new java.sql.Timestamp(dateChooserDi.getDate().getTime()));
                pstmt.setTimestamp(7, new java.sql.Timestamp(dateChooserDen.getDate().getTime()));
                pstmt.setInt(8, Integer.parseInt(txtSoGhe.getText()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Dữ liệu đã được lưu vào cơ sở dữ liệu!");

                Object[] row = {
                    txtMaTau.getText(),
                    txtTenTau.getText(),
                    comboLoaiToa.getSelectedItem().toString(),
                    comboGaDi.getSelectedItem(),
                    comboGaDen.getSelectedItem(),
                    dateFormat.format(dateChooserDi.getDate()),
                    dateFormat.format(dateChooserDen.getDate()),
                    txtSoGhe.getText()
                };
                modelTau.addRow(row);
                clearInputFields();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu dữ liệu vào cơ sở dữ liệu: " + ex.getMessage());
        }
    }

    private void deleteSelectedRow() {
        int selectedRow = tbl_DsTau.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn dòng cần xóa!");
            return;
        }

        // Lấy mã tàu từ dòng được chọn
        String maTau = modelTau.getValueAt(selectedRow, 0).toString();

        // Hiển thị hộp thoại xác nhận
        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn xóa dòng này khỏi cơ sở dữ liệu và bảng?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                String sql = "DELETE FROM Tau WHERE maTau = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, maTau);

                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        // Xóa dòng khỏi model của bảng sau khi xóa khỏi cơ sở dữ liệu thành công
                        modelTau.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(null, "Xóa thành công khỏi cơ sở dữ liệu và bảng!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Không thể xóa dữ liệu trong cơ sở dữ liệu!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi xóa dữ liệu: " + ex.getMessage());
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
