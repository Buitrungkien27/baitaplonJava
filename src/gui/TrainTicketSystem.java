package gui;

import javax.swing.*;
import java.awt.*;

public class TrainTicketSystem {

	   public static void main(String[] args) {
	        JFrame frame = new JFrame("Hệ thống bán vé tàu");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setSize(800, 600);
	        
	        // Left Sidebar
	        JPanel sidebar = new JPanel();
	        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
	        sidebar.add(new JLabel("Ga Sài Gòn"));
	        sidebar.add(new JButton("Bán vé"));
	        sidebar.add(new JButton("Đổi trả"));
	        sidebar.add(new JButton("Hóa đơn"));
	        sidebar.add(new JButton("Quản lý"));
	        sidebar.add(new JButton("Nhân viên"));
	        sidebar.add(new JButton("Khách hàng"));
	        sidebar.add(new JButton("Thống kê"));
	        sidebar.add(new JButton("Khác"));
	        sidebar.add(new JButton("Đăng xuất"));
	        
	        // Main Section
	        JPanel mainSection = new JPanel();
	        mainSection.setLayout(new BorderLayout());
	        
	        JPanel searchPanel = new JPanel();
	        searchPanel.setLayout(new GridLayout(4, 2));
	        searchPanel.add(new JLabel("Ga đi"));
	        searchPanel.add(new JTextField());
	        searchPanel.add(new JLabel("Ga đến"));
	        searchPanel.add(new JTextField());
	        searchPanel.add(new JLabel("Ngày đi"));
	        searchPanel.add(new JTextField());
	        searchPanel.add(new JButton("Tìm"));
	        
	        JPanel trainListPanel = new JPanel();
	        trainListPanel.setLayout(new BoxLayout(trainListPanel, BoxLayout.Y_AXIS));
	        trainListPanel.add(new JLabel("Danh sách chuyến tàu"));
	        
	        mainSection.add(searchPanel, BorderLayout.NORTH);
	        mainSection.add(trainListPanel, BorderLayout.CENTER);
	        
	        // Right Sidebar
	        JPanel rightSidebar = new JPanel();
	        rightSidebar.setLayout(new BoxLayout(rightSidebar, BoxLayout.Y_AXIS));
	        rightSidebar.add(new JLabel("Thông tin khách hàng"));
	        rightSidebar.add(new JLabel("Số điện thoại"));
	        rightSidebar.add(new JTextField());
	        rightSidebar.add(new JLabel("Họ và tên"));
	        rightSidebar.add(new JTextField());
	        rightSidebar.add(new JLabel("Thông tin hóa đơn"));
	        rightSidebar.add(new JLabel("Mã hóa đơn"));
	        rightSidebar.add(new JTextField());
	        rightSidebar.add(new JLabel("Ngày tạo"));
	        rightSidebar.add(new JTextField());
	        rightSidebar.add(new JLabel("Khách phải trả"));
	        rightSidebar.add(new JLabel("Phương thức"));
	        rightSidebar.add(new JComboBox<>(new String[] {"Tiền mặt"}));
	        rightSidebar.add(new JLabel("Tiền khách đưa"));
	        rightSidebar.add(new JTextField());
	        rightSidebar.add(new JLabel("Tiền thừa"));
	        rightSidebar.add(new JTextField());
	        
	        // Bottom Section
	        JPanel bottomSection = new JPanel();
	        bottomSection.add(new JButton("THANH TOÁN"));
	        bottomSection.add(new JButton("HỦY"));
	        
	        // Adding sections to frame
	        frame.setLayout(new BorderLayout());
	        frame.add(sidebar, BorderLayout.WEST);
	        frame.add(mainSection, BorderLayout.CENTER);
	        frame.add(rightSidebar, BorderLayout.EAST);
	        frame.add(bottomSection, BorderLayout.SOUTH);
	        
	        frame.setVisible(true);
	    }
	}

