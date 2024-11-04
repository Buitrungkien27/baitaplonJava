package gui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

    private JTextField txtHoTen, txtSDT, txtMaHD, txtTienKhachDua, txtTienThua, txtViTriGhe, txtGiaVe;
    private JComboBox<String> comboGaDi, comboGaDen, comboLoaiToa;
    private JDateChooser dateNgayTao;
    private JTable table;
    private DefaultTableModel model;

    private Map<String, Set<String>> gheDaDatTheoToa = new HashMap<>();  // Lưu trữ các ghế đã đặt theo từng toa
    private JButton[] buttonsGhe;  // Lưu các button ghế để có thể thay đổi màu
    private Set<String> gheTamDat = new HashSet<>(); // Lưu trữ ghế được chọn tạm thời trong lần đặt hiện tại

    public BanVe_GUI() {
        setLayout(new BorderLayout());
        loadTicketData();
        // Panel chính chia thành 2 phần
        JPanel pnlMain = new JPanel(new GridLayout(1, 2, 10, 10)); // Chia làm 2 bảng

        // Panel bên trái chứa bảng danh sách vé
        JPanel pnlDanhSachVe = new JPanel(new BorderLayout());
        String[] columnNames = {"Ga đi", "Ga đến", "Họ và tên", "Số điện thoại", "Mã vé tàu ", "Ngày tạo", "Tiền khách đưa", "Tiền thừa", "Loại Toa", "Vị trí ghế", "Giá vé"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  // Tắt tự động điều chỉnh kích thước cột

        // Đặt kích thước cho từng cột để hiển thị dữ liệu đầy đủ hơn
        setColumnWidths(table, new int[]{80, 100, 150, 100, 100, 100, 120, 100, 80, 80, 80});

        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pnlDanhSachVe.add(scrollPane, BorderLayout.CENTER);

        // Panel bên phải chứa phần thông tin và ghế ngồi
        JPanel pnlThongTinVaGhe = new JPanel(new BorderLayout());

        // Panel thông tin
        JPanel pnlThongTin = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Tạo các thành phần UI
        JLabel lblGaDi = new JLabel("Ga đi:");
        JLabel lblGaDen = new JLabel("Ga đến:");
        JLabel lblHoTen = new JLabel("Họ và tên:");
        JLabel lblSDT = new JLabel("Số điện thoại:");
        JLabel lblMaHD = new JLabel("Mã vé tàu :");
        JLabel lblTienKhachDua = new JLabel("Tiền khách đưa:");
        lblTienKhachDua.setPreferredSize(new Dimension(120, 20)); // Điều chỉnh kích thước để tránh bị cắt chữ
        JLabel lblTienThua = new JLabel("Tiền thừa:");
        JLabel lblNgayTao = new JLabel("Ngày tạo:");
        JLabel lblLoaiToa = new JLabel("Loại Toa:");
        JLabel lblViTriGhe = new JLabel("Vị trí ghế:");
        JLabel lblGiaVe = new JLabel("Giá vé:");

        txtHoTen = new JTextField(15);
        txtSDT = new JTextField(15);
        txtMaHD = new JTextField(15);
        txtTienKhachDua = new JTextField(15);
        txtTienThua = new JTextField(15);
        txtViTriGhe = new JTextField(15);
        txtGiaVe = new JTextField(15); // Thêm trường nhập cho Giá vé
        txtGiaVe.setEditable(false);   // Ô Giá vé chỉ hiển thị, không cho chỉnh sửa

        comboGaDi = new JComboBox<>(new String[]{"Sài Gòn"});
        comboGaDen = new JComboBox<>(new String[]{"An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bắc Giang", "Bắc Kạn", "Bắc Ninh",
                "Bến Tre", "Bình Dương", "Bình Định", "Bình Phước", "Bình Thuận", "Cà Mau",
                "Cao Bằng", "Cần Thơ", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên", "Đồng Nai",
                "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương",
                "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa", "Kiên Giang",
                "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai", "Long An", "Nam Định",
                "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình",
                "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La",
                "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa", "Thừa Thiên Huế", "Tiền Giang",
                "TP Hồ Chí Minh", "Trà Vinh", "Tuyên Quang", "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"});
        comboLoaiToa = new JComboBox<>(new String[]{"Toa 1", "Toa 2", "Toa 3", "Toa 4", "Toa 5"});

        dateNgayTao = new JDateChooser();
        dateNgayTao.setDateFormatString("dd/MM/yyyy");

        // Đặt các thành phần vào panel thông tin
        gbc.gridx = 0; gbc.gridy = 0; pnlThongTin.add(lblGaDi, gbc);
        gbc.gridx = 1; pnlThongTin.add(comboGaDi, gbc);
        gbc.gridx = 2; pnlThongTin.add(lblGaDen, gbc);
        gbc.gridx = 3; pnlThongTin.add(comboGaDen, gbc);

        gbc.gridx = 0; gbc.gridy = 1; pnlThongTin.add(lblHoTen, gbc);
        gbc.gridx = 1; pnlThongTin.add(txtHoTen, gbc);
        gbc.gridx = 2; pnlThongTin.add(lblSDT, gbc);
        gbc.gridx = 3; pnlThongTin.add(txtSDT, gbc);

        gbc.gridx = 0; gbc.gridy = 2; pnlThongTin.add(lblMaHD, gbc);
        gbc.gridx = 1; pnlThongTin.add(txtMaHD, gbc);
        gbc.gridx = 2; pnlThongTin.add(lblNgayTao, gbc);
        gbc.gridx = 3; pnlThongTin.add(dateNgayTao, gbc);

        gbc.gridx = 0; gbc.gridy = 3; pnlThongTin.add(lblTienKhachDua, gbc);
        gbc.gridx = 1; pnlThongTin.add(txtTienKhachDua, gbc);
        gbc.gridx = 2; pnlThongTin.add(lblTienThua, gbc);
        gbc.gridx = 3; pnlThongTin.add(txtTienThua, gbc);

        gbc.gridx = 0; gbc.gridy = 4; pnlThongTin.add(lblLoaiToa, gbc);
        gbc.gridx = 1; pnlThongTin.add(comboLoaiToa, gbc);
        gbc.gridx = 2; pnlThongTin.add(lblViTriGhe, gbc);
        gbc.gridx = 3; pnlThongTin.add(txtViTriGhe, gbc);

        // Thêm Giá vé vào dưới Loại Toa
        gbc.gridx = 0; gbc.gridy = 5; pnlThongTin.add(lblGiaVe, gbc);
        gbc.gridx = 1; pnlThongTin.add(txtGiaVe, gbc);

        // Nút Thanh toán
        JButton btnThanhToan = new JButton("Thanh toán");
        gbc.gridx = 2; gbc.gridy = 6; pnlThongTin.add(btnThanhToan, gbc);

        // Panel ghế ngồi
        JPanel pnlGhe = new JPanel(new GridLayout(5, 5, 5, 5));
        String[] ghe = {"A1", "A2", "A3", "A4", "A5", "B1", "B2", "B3", "B4", "B5",
                        "C1", "C2", "C3", "C4", "C5", "D1", "D2", "D3", "D4", "D5"};

        buttonsGhe = new JButton[ghe.length];

        // Màu ghế theo toa
        Color[] colors = {Color.GREEN, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK}; // Xanh: cứng, Vàng: mềm, Cam: nằm cứng, Hồng: nằm mềm

        // Khởi tạo ghế và màu ban đầu dựa trên toa
        for (int i = 0; i < ghe.length; i++) {
            buttonsGhe[i] = new JButton(ghe[i]);
            buttonsGhe[i].setBackground(colors[0]); // Đặt màu ban đầu cho tất cả ghế
            pnlGhe.add(buttonsGhe[i]);

            // Thêm sự kiện khi nhấn chọn hoặc hủy ghế
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

                    String gaDi = (String) comboGaDi.getItemAt(0);
                    String gaDen = (String) comboGaDen.getSelectedItem();
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

        // Thêm các panel vào pnlThongTinVaGhe
        pnlThongTinVaGhe.add(pnlThongTin, BorderLayout.NORTH);
        pnlThongTinVaGhe.add(pnlGhe, BorderLayout.CENTER);

        // Thêm pnlDanhSachVe và pnlThongTinVaGhe vào pnlMain
        pnlMain.add(pnlDanhSachVe);
        pnlMain.add(pnlThongTinVaGhe);

        // Thêm pnlMain vào giao diện chính
        add(pnlMain, BorderLayout.CENTER);

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
        String gaDen = (String) comboGaDen.getSelectedItem();
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
