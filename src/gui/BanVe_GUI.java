package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import com.toedter.calendar.JDateChooser;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BanVe_GUI extends JPanel {
    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=QLBanVe";
    private static final String USER = "sa";
    private static final String PASSWORD = "sa123";

    private JTextField txtHoTen, txtSDT, txtMaHD, txtTienKhachDua, txtTienThua, txtViTriGhe, txtGiaVe, txtgaDi, txtgaDen, txtMaTau;
    private JComboBox<String> comboLoaiToa;
    private JDateChooser dateNgayTao;
    private JTable table;
    private DefaultTableModel model;

    private Map<String, Set<String>> gheDaDatTheoToa = new HashMap<>();  // Lưu trữ các ghế đã đặt theo từng toa
    private JButton[] buttonsGhe;  // Lưu các button ghế để có thể thay đổi màu
    private Set<String> gheTamDat = new HashSet<>(); // Lưu trữ ghế được chọn tạm thời trong lần đặt hiện tại
    private Color[] colors = {Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK}; // Màu theo loại toa
    public BanVe_GUI() {
        setLayout(new BorderLayout());
        loadTicketData();
        
         // Phần trên - hiển thị bảng danh sách vé
        JPanel pnlDanhSachVe = new JPanel(new BorderLayout());
        String[] columnNames = {"Ga đi", "Ga đến", "Họ và tên", "Số điện thoại", "Mã vé tàu", "Ngày tạo", "Tiền khách đưa", "Tiền thừa", "Loại Toa", "Vị trí ghế", "Giá vé", "Mã Tàu"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
       // table.setPreferredScrollableViewportSize(new Dimension(900, 180));
        pnlDanhSachVe.add(new JScrollPane(table), BorderLayout.CENTER);
     // Đặt kích thước cho bảng để bảng dữ liệu dài hơn và hiển thị nhiều thông tin hơn
        table.setPreferredScrollableViewportSize(new Dimension(900, 300)); // Tăng chiều cao của bảng lên 350
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pnlDanhSachVe.add(tableScrollPane, BorderLayout.CENTER);
        add(pnlDanhSachVe, BorderLayout.NORTH);  // Đặt bảng lên trên cùng

        // Phần dưới - chia làm 2
        JPanel pnlDuoi = new JPanel(new GridLayout(1, 2, 10, 10)); // Chia làm 2 bảng
        
     // Bên trái - Phần nhập thông tin (sử dụng GridBagLayout)
        JPanel pnlNhapThongTin = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Thiết lập các nhãn và ô nhập liệu để chia thành 2 cột
        String[] labels = {"Ga đi:", "Ga đến:", "Họ và tên:", "Số điện thoại:", "Mã vé tàu:", "Ngày tạo:", "Tiền khách đưa:", "Tiền thừa:", "Loại Toa:", "Giá vé:" , "Mã Tàu:"};
        JComponent[] fields = {
            txtgaDi = new JTextField(), txtgaDen = new JTextField(),
            txtHoTen = new JTextField(), txtSDT = new JTextField(),
            txtMaHD = new JTextField(), dateNgayTao = new JDateChooser(),
            txtTienKhachDua = new JTextField(), txtTienThua = new JTextField(),
            comboLoaiToa = new JComboBox<>(new String[]{"Toa 1", "Toa 2", "Toa 3", "Toa 4", "Toa 5"}),
            txtGiaVe = new JTextField(), txtMaTau = new JTextField()
        };
        // Add this ActionListener for txtMaTau here to fetch data from the database when a train ID is entered
        txtMaTau.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String maTau = txtMaTau.getText().trim();
                
                if (!maTau.isEmpty()) {
                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                        String sql = "SELECT gaDi, gaDen, loaiToa FROM Tau WHERE maTau = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, maTau);
                            ResultSet rs = pstmt.executeQuery();
                            
                            if (rs.next()) {
                                // Populate the fields with the fetched data
                                txtgaDi.setText(rs.getString("gaDi"));
                                txtgaDen.setText(rs.getString("gaDen"));
                                comboLoaiToa.setSelectedItem(rs.getString("loaiToa"));
                            } else {
                                // Clear fields if no data is found
                                JOptionPane.showMessageDialog(null, "Không tìm thấy tàu với mã tàu này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                txtgaDi.setText("");
                                txtgaDen.setText("");
                                comboLoaiToa.setSelectedIndex(0);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm tàu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        txtSDT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sdt = txtSDT.getText();
                if (!sdt.isEmpty()) {
                    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                        String sql = "SELECT tenKH FROM KhachHang WHERE sdt = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setString(1, sdt);
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next()) {
                                // Lấy tên khách hàng từ kết quả truy vấn
                                String hoTen = rs.getString("tenKH");
                                txtHoTen.setText(hoTen);
                            } else {
                                // Nếu không tìm thấy khách hàng, thông báo cho người dùng
                                JOptionPane.showMessageDialog(null, "Không tìm thấy khách hàng với số điện thoại này.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                txtHoTen.setText(""); // Xóa nội dung trong txtHoTen nếu không tìm thấy
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm khách hàng: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // Sắp xếp các nhãn và trường nhập liệu theo 2 cột
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = (i % 2) * 2; // Cột nhãn
            gbc.gridy = i / 2;
            pnlNhapThongTin.add(new JLabel(labels[i]), gbc);
            
            gbc.gridx = (i % 2) * 2 + 1; // Cột ô nhập
            pnlNhapThongTin.add(fields[i], gbc);
        }

        pnlDuoi.add(pnlNhapThongTin); // Thêm phần nhập thông tin vào bên trái của pnlDuoi


        pnlDuoi.add(pnlNhapThongTin); // Thêm phần nhập thông tin vào bên trái của pnlDuoi
        JButton btnThanhToan = new JButton("Thanh Toán");
        gbc.gridx = 0;
        gbc.gridy = (labels.length / 2) + 1; // Đặt nó ở hàng cuối cùng
        gbc.gridwidth = 2; // Trải rộng qua hai cột
        gbc.anchor = GridBagConstraints.CENTER; 
        pnlNhapThongTin.add(btnThanhToan, gbc);
        // Bên phải - Phần chọn ghế
        JPanel pnlChonGhe = new JPanel(new GridLayout(5, 5, 5, 5));
        buttonsGhe = new JButton[25];
        for (int i = 0; i < 25; i++) {
            buttonsGhe[i] = new JButton("A" + (i + 1));
            buttonsGhe[i].setBackground(Color.GREEN);
            pnlChonGhe.add(buttonsGhe[i]);
        }
        pnlDuoi.add(pnlChonGhe);  // Thêm phần chọn ghế vào bên phải

        add(pnlDuoi, BorderLayout.CENTER);  // Đặt phần dưới vào giữa

            // Thêm sự kiện khi nhấn chọn hoặc hủy ghế
            for (int i = 0; i < buttonsGhe.length; i++) {
            buttonsGhe[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton btn = (JButton) e.getSource();
                    String loaiToa = (String) comboLoaiToa.getSelectedItem();
                    int toaIndex = comboLoaiToa.getSelectedIndex();
                    String viTriGhe = btn.getText();

                    // Kiểm tra nếu ghế đã được chọn (màu đỏ)
                    if (btn.getBackground().equals(Color.RED)) {
                        // Hủy đặt ghế, trả về màu ban đầu
                        btn.setBackground(colors[toaIndex]); // Màu theo loại toa
                        gheTamDat.remove(viTriGhe); // Loại bỏ ghế khỏi danh sách đã đặt tạm
                    } else {
                        // Đặt ghế nếu chưa được chọn
                        btn.setBackground(Color.RED); // Đổi màu ghế đã chọn thành đỏ
                        gheTamDat.add(viTriGhe); // Đánh dấu ghế đã được đặt tạm
                    }
                    updateGiaVe(); // Cập nhật giá vé ngay lập tức
                }
            });
        }

        // Tự động tính toán tiền thừa khi nhập tiền khách đưa
        txtTienKhachDua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int giaVe = Integer.parseInt(txtGiaVe.getText());
                    double tienKhachDua = Double.parseDouble(txtTienKhachDua.getText());

                    // Kiểm tra nếu tiền khách đưa không đủ
                    if (tienKhachDua < giaVe) {
                        JOptionPane.showMessageDialog(null, "Tiền khách đưa không đủ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        txtTienThua.setText(""); // Xóa ô tiền thừa nếu không đủ
                    } else {
                        double tienThua = tienKhachDua - giaVe;
                        txtTienThua.setText(String.valueOf(tienThua));
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng cho tiền khách đưa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Hành động khi nhấn nút Thanh toán
        btnThanhToan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String hoTen = txtHoTen.getText();
                    String sdt = txtSDT.getText();

                    // Kiểm tra họ và tên và số điện thoại
                    if (!hoTen.matches("[a-zA-ZÀ-Ỹà-ỹ\\s]+")) {
                        JOptionPane.showMessageDialog(null, "Họ và tên chỉ được chứa chữ cái, không được chứa số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!sdt.matches("\\d{10}")) {
                        JOptionPane.showMessageDialog(null, "Số điện thoại phải là 10 chữ số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Kiểm tra số tiền khách đưa
                    double tienKhachDua = txtTienKhachDua.getText().isEmpty() ? 0 : Double.parseDouble(txtTienKhachDua.getText());
                    int tongGiaVe = Integer.parseInt(txtGiaVe.getText()); // Tổng giá vé của các ghế đã chọn

                    if (tienKhachDua < tongGiaVe) {
                        JOptionPane.showMessageDialog(null, "Số tiền khách đưa không đủ để thanh toán!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String gaDi = (String) txtgaDi.getText();
                    String gaDen = (String) txtgaDen.getText();
                    Date ngayTao = dateNgayTao.getDate();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String strNgayTao = (ngayTao != null) ? sdf.format(ngayTao) : "";
                    double tienThua = tienKhachDua - tongGiaVe;
                    txtTienThua.setText(String.format("%.2f", tienThua)); // Cập nhật số tiền thừa

                    String loaiToa = (String) comboLoaiToa.getSelectedItem();

                    // Xử lý tất cả các ghế đã chọn trong lần đặt hiện tại
                    for (String viTriGhe : gheTamDat) {
                        JButton ghe = findButtonByViTri(viTriGhe);
                        if (ghe != null) {
                            // Tạo mã vé tự động cho từng ghế
                            String maHD = generateMaVe(gaDi, ngayTao, loaiToa, viTriGhe);
                            txtMaHD.setText(maHD);

                            // Lấy giá trị hiện tại của tổng giá vé từ txtGiaVe
                            int giaGhe = getGiaGhe(loaiToa, ghe) + getGiaCoBan(gaDen);

                            // Thêm dữ liệu của từng vé vào bảng
                            model.addRow(new Object[]{gaDi, gaDen, hoTen, sdt, maHD, strNgayTao, tienKhachDua, tienThua, loaiToa, viTriGhe, giaGhe});

                            // Lưu ghế đã đặt vào danh sách chính thức
                            if (!gheDaDatTheoToa.containsKey(loaiToa)) {
                                gheDaDatTheoToa.put(loaiToa, new HashSet<>());
                            }
                            gheDaDatTheoToa.get(loaiToa).add(viTriGhe);

                            // Lưu dữ liệu vào cơ sở dữ liệu
                            saveToDatabase(gaDi, gaDen, hoTen, sdt, maHD, ngayTao, tienKhachDua, tienThua, loaiToa, viTriGhe, giaGhe);

                            // Khóa ghế đã đặt sau khi thanh toán
                            ghe.setEnabled(false);
                        }
                    }

                    // Reset trạng thái sau khi thanh toán
                    gheTamDat.clear();
                    txtHoTen.setText("");
                    txtSDT.setText("");
                    txtMaHD.setText("");
                    txtTienKhachDua.setText("");
                    txtTienThua.setText("");
                    txtViTriGhe.setText("");
                    txtGiaVe.setText(""); // Xóa giá vé sau khi thanh toán
                    dateNgayTao.setDate(null);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng cho tiền khách đưa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Sự kiện khi panel bị ẩn hoặc người dùng rời đi mà không thanh toán
        this.addHierarchyListener(e -> {
            if ((e.getChangeFlags() & e.SHOWING_CHANGED) != 0 && !this.isShowing()) {
                resetTempSeats(); // Trả ghế chưa thanh toán về màu ban đầu
            }
        });

        JLabel lblGhiChu = new JLabel("Xanh: Ngồi cứng (50k), Vàng: Ngồi mềm (100k), Cam: Nằm cứng (150k), Hồng: Nằm mềm (200k), Trắng: Ghế đã đặt.");
        add(lblGhiChu, BorderLayout.PAGE_END);

        // Thêm sự kiện khi chọn loại toa
        comboLoaiToa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateGheColors();  // Gọi hàm để cập nhật màu ghế khi loại toa thay đổi
            }
        });

        // Gọi hàm để thiết lập màu ghế ban đầu
        updateGheColors();
    }

    private void loadTicketData() {
		// TODO Auto-generated method stub
		
	}

	// Phương thức tìm button theo vị trí ghế
    private JButton findButtonByViTri(String viTriGhe) {
        for (JButton btn : buttonsGhe) {
            if (btn.getText().equals(viTriGhe)) {
                return btn;
            }
        }
        return null;
    }

    // Phương thức xác định khu vực miền của ga đến và giá vé cơ bản
    private int getGiaCoBan(String gaDen) {
        String[] mienBac = {"Bắc Giang", "Bắc Kạn", "Bắc Ninh", "Cao Bằng", "Điện Biên", "Hà Giang", "Hà Nam",
                            "Hà Nội", "Hải Dương", "Hải Phòng", "Hòa Bình", "Hưng Yên", "Lạng Sơn", "Lào Cai",
                            "Nam Định", "Ninh Bình", "Phú Thọ", "Quảng Ninh", "Sơn La", "Thái Bình", "Thái Nguyên",
                            "Tuyên Quang", "Vĩnh Phúc", "Yên Bái"};
        String[] mienTrung = {"Đà Nẵng", "Bình Định", "Hà Tĩnh", "Khánh Hòa", "Nghệ An", "Phú Yên", "Quảng Bình",
                              "Quảng Nam", "Quảng Ngãi", "Quảng Trị", "Thừa Thiên Huế"};
        String[] mienNam = {"An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bình Dương", "Bình Phước", "Bình Thuận",
                            "Cà Mau", "Cần Thơ", "Đắk Lắk", "Đắk Nông", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hậu Giang",
                            "Kiên Giang", "Kon Tum", "Lâm Đồng", "Long An", "Sóc Trăng", "Tây Ninh", "Tiền Giang",
                            "Trà Vinh", "Vĩnh Long"};

        if (java.util.Arrays.asList(mienBac).contains(gaDen)) {
            return 20000; // Giá vé cơ bản cho miền Bắc
        } else if (java.util.Arrays.asList(mienTrung).contains(gaDen)) {
            return 15000; // Giá vé cơ bản cho miền Trung
        } else if (java.util.Arrays.asList(mienNam).contains(gaDen)) {
            return 10000; // Giá vé cơ bản cho miền Nam
        }
        return 0; // Mặc định nếu không xác định được khu vực
    }

    // Phương thức tính giá ghế dựa trên màu sắc và loại toa
    private int getGiaGhe(String loaiToa, JButton ghe) {
        Color gheMau = ghe.getBackground();

        // Giá vé cơ bản dựa vào loại toa
        int giaCoBanToa = 0;
        switch (loaiToa) {
            case "Toa 1":
                giaCoBanToa = 50000; // Giá cơ bản cho Toa 1
                break;
            case "Toa 2":
                giaCoBanToa = 50000; // Giá cơ bản cho Toa 2
                break;
            case "Toa 3":
                giaCoBanToa = 100000; // Giá cơ bản cho Toa 3
                break;
            case "Toa 4":
                giaCoBanToa = 150000; // Giá cơ bản cho Toa 4
                break;
            case "Toa 5":
                giaCoBanToa = 200000; // Giá cơ bản cho Toa 5
                break;
            default:
                giaCoBanToa = 0; // Mặc định nếu không xác định được loại toa
        }

        // Giá bổ sung dựa trên loại ghế
        int giaGhe = 0;
        if (gheMau.equals(Color.GREEN)) {
            giaGhe = 50000; // Giá ghế ngồi cứng
        } else if (gheMau.equals(Color.YELLOW)) {
            giaGhe = 100000; // Giá ghế ngồi mềm
        } else if (gheMau.equals(Color.ORANGE)) {
            giaGhe = 150000; // Giá ghế nằm cứng
        } else if (gheMau.equals(Color.PINK)) {
            giaGhe = 200000; // Giá ghế nằm mềm
        }

        // Tổng giá cho ghế là giá cơ bản của toa cộng với giá ghế cụ thể
        return giaCoBanToa + giaGhe;
    }

    // Phương thức tính tổng giá vé và hiển thị lên giao diện
    private void updateGiaVe() {
        String gaDen = (String) txtgaDen.getText();
        int giaCoBan = getGiaCoBan(gaDen);

        int giaGhe = 0;
        for (String viTriGhe : gheTamDat) {
            JButton ghe = findButtonByViTri(viTriGhe);
            if (ghe != null && ghe.getBackground().equals(Color.RED)) {
                // Lấy giá trị của ghế đã chọn (màu đỏ)
                giaGhe += getGiaGhe((String) comboLoaiToa.getSelectedItem(), ghe);
            }
        }

        // Tổng giá vé là giá cơ bản cho miền cộng với giá ghế đã chọn
        int tongGiaVe = giaCoBan + giaGhe;
        txtGiaVe.setText(String.valueOf(tongGiaVe)); // Hiển thị giá vé vào ô txtGiaVe
    }

    // Hàm để cập nhật màu sắc của ghế
    private void updateGheColors() {
        Color[] colors = {Color.GREEN, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK}; // Màu ghế theo loại toa
        int toaIndex = comboLoaiToa.getSelectedIndex();

        // Cập nhật trạng thái của ghế dựa trên toa hiện tại
        for (int i = 0; i < buttonsGhe.length; i++) {
            String viTriGhe = buttonsGhe[i].getText();
            if (isGheDaDat((String) comboLoaiToa.getSelectedItem(), viTriGhe)) {
                buttonsGhe[i].setBackground(Color.RED); // Ghế đã đặt màu đỏ
                buttonsGhe[i].setEnabled(false); // Khóa ghế đã đặt sau khi thanh toán
            } else {
                buttonsGhe[i].setBackground(colors[toaIndex]); // Màu ghế theo loại toa
                buttonsGhe[i].setEnabled(true); // Bỏ khóa nếu chưa đặt
            }
        }
    }

    // Hàm trả ghế tạm về màu ban đầu nếu không thanh toán
    private void resetTempSeats() {
        int toaIndex = comboLoaiToa.getSelectedIndex();
        Color[] colors = {Color.GREEN, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK}; // Màu ghế theo loại toa

        for (JButton btn : buttonsGhe) {
            if (!getGheDaDat().contains(btn.getText())) {
                btn.setBackground(colors[toaIndex]); // Trả ghế về màu theo loại toa hiện tại
            }
        }
    }

    // Hàm đặt kích thước cho từng cột trong bảng
    private void setColumnWidths(JTable table, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    // Hàm chuyển đổi tên địa điểm thành mã ga tự động
    private String getMaGa(String diaDiem) {
        if (diaDiem == null || diaDiem.isEmpty()) {
            return "UNK"; // Nếu không có địa điểm hợp lệ, trả về "UNK" (Unknown)
        }

        // Tách các từ trong địa điểm bằng dấu cách hoặc dấu gạch ngang
        String[] words = diaDiem.split("[\\s\\-]+");

        // Lấy ký tự đầu tiên của mỗi từ và nối lại thành mã ga
        StringBuilder maGa = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                maGa.append(Character.toUpperCase(word.charAt(0)));
            }
        }

        // Trả về mã ga (nếu dài hơn 3 ký tự, chỉ lấy 3 ký tự đầu tiên)
        return maGa.length() > 3 ? maGa.substring(0, 3) : maGa.toString();
    }

    // Hàm định dạng vị trí ghế
    private String formatViTriGhe(String viTriGhe) {
        if (viTriGhe != null && viTriGhe.length() == 2) {
            char hang = viTriGhe.charAt(0);
            char cot = viTriGhe.charAt(1);
            return String.format("%02d%02d", (hang - 'A' + 1), (cot - '0'));
        }
        return "0000"; // Mặc định nếu không xác định được vị trí
    }

    // Hàm tạo mã vé tự động
    private String generateMaVe(String gaDi, Date ngayTao, String loaiToa, String viTriGhe) {
        String maGa = getMaGa(gaDi);
        String ngayThang = new SimpleDateFormat("ddMM").format(ngayTao);
        String maToa = loaiToa.substring(loaiToa.length() - 1); // Lấy số toa
        String maGhe = formatViTriGhe(viTriGhe);
        String uniqueID = String.format("%03d", (getGheDaDat().size() + 1)); // Tạo số ID duy nhất

        return maGa + ngayThang + maToa + maGhe + uniqueID;
    }

    // Hàm lấy danh sách ghế đã đặt cho toa hiện tại
    private Set<String> getGheDaDat() {
        String loaiToa = (String) comboLoaiToa.getSelectedItem();
        return gheDaDatTheoToa.computeIfAbsent(loaiToa, k -> new HashSet<>());
    }

    // Phương thức kiểm tra xem ghế đã được đặt hay chưa
    private boolean isGheDaDat(String loaiToa, String viTriGhe) {
        return gheDaDatTheoToa.containsKey(loaiToa) && gheDaDatTheoToa.get(loaiToa).contains(viTriGhe);
    }

    // Thêm phương thức `saveToDatabase` ngay trước phương thức main trong lớp BanVe_GUI

 // Phương thức lưu dữ liệu vào cơ sở dữ liệu
    private void saveToDatabase(String gaDi, String gaDen, String hoTen, String sdt, String maHD, Date ngayTao, double tienKhachDua, double tienThua, String loaiToa, String viTriGhe, int giaVe) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO VeTau (maVT, gaDi, gaDen, hoTenKH, soDienThoai, ngayTao, tienKhachDua, tienThua, loaiToa, viTriGhe, giaVe) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, maHD);  // Mã vé tàu
                pstmt.setString(2, gaDi);  // Ga đi
                pstmt.setString(3, gaDen); // Ga đến
                pstmt.setString(4, hoTen); // Họ tên khách hàng
                pstmt.setString(5, sdt);   // Số điện thoại
                pstmt.setString(6, new SimpleDateFormat("yyyy-MM-dd").format(ngayTao)); // Ngày tạo
                pstmt.setDouble(7, tienKhachDua);  // Tiền khách đưa
                pstmt.setDouble(8, tienThua);      // Tiền thừa
                pstmt.setString(9, loaiToa);       // Loại toa
                pstmt.setString(10, viTriGhe);     // Vị trí ghế
                pstmt.setInt(11, giaVe);           // Giá vé

                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Lỗi khi lưu dữ liệu vào cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


    private String generateMaKH(String hoTen) {
        // TODO: Tạo mã khách hàng tự động từ họ tên
        return hoTen.substring(0, Math.min(3, hoTen.length())).toUpperCase() + String.valueOf(System.currentTimeMillis()).substring(10);
    }

    // Phần main vẫn giữ nguyên
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hệ thống bán vé tàu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.add(new BanVe_GUI());
        frame.setVisible(true);
    }
}
