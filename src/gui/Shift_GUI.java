package gui;

//import bus.ShiftsManagemant_BUS;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import bus.Phien_BUS;
import entity.Phien;
import utilities.SVGIcon;
public class Shift_GUI extends javax.swing.JPanel {
    private DefaultTableModel tblModel_shift;
    private Phien_BUS shift_bus = new Phien_BUS();

    public Shift_GUI() {
        initTableModel();
        initComponents();
        renderShiftsTable(shift_bus.getShiftsByDate(new Date()));
        alterTable();
    }

    public void initTableModel() {
        // Products
        tblModel_shift = new DefaultTableModel(new String[]{"Mã", "Mã nhân viên", "Tên nhân viên", "Chức vụ", "Thời gian đăng nhập", "Thời gian đăng xuất"
        }, 0);
    }

    public void renderShiftsTable(ArrayList<Phien> list) {
        tblModel_shift.setRowCount(0);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        for (Phien shift : list) {
            Object[] row = new Object[]{shift.getShiftID(), shift.getAccount().getNhanVien().getMaNV(), shift.getAccount().getNhanVien().getTenNV(), shift.getAccount().getNhanVien().getChucVu(), formatter.format(shift.getStartedAt()), formatter.format(shift.getEndedAt())};
            tblModel_shift.addRow(row);
        }
    }

    public void alterTable() {
        DefaultTableCellRenderer rightAlign = new DefaultTableCellRenderer();
        rightAlign.setHorizontalAlignment(JLabel.RIGHT);

        //Align
        tbl_shifts.getColumnModel().getColumn(4).setCellRenderer(rightAlign);
        tbl_shifts.getColumnModel().getColumn(5).setCellRenderer(rightAlign);
       
    }

    public void reloadForm() {
    	txt_empID.setText("");
    	date_dateAt.setDate(new Date());
    	cbo_role.setSelectedIndex(0);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scr_shifts = new javax.swing.JScrollPane();
        tbl_shifts = new javax.swing.JTable();
        pnl_header = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_empID = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        date_dateAt = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cbo_role = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        btn_filter = new javax.swing.JButton();
        btn_reload = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        tbl_shifts.setModel(tblModel_shift);
        tbl_shifts.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        scr_shifts.setViewportView(tbl_shifts);

        add(scr_shifts, java.awt.BorderLayout.CENTER);

        pnl_header.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 10, 1));
        pnl_header.setMaximumSize(new java.awt.Dimension(2147483647, 40));
        pnl_header.setPreferredSize(new java.awt.Dimension(626, 60));
        pnl_header.setLayout(new javax.swing.BoxLayout(pnl_header, javax.swing.BoxLayout.X_AXIS));

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jLabel1.setText("Mã nhân viên: ");
        jPanel1.add(jLabel1);
        jPanel1.add(txt_empID);

        pnl_header.add(jPanel1);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

        jLabel2.setText("Ngày: ");
        jPanel2.add(jLabel2);
        jPanel2.add(date_dateAt);

        pnl_header.add(jPanel2);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.X_AXIS));

        jLabel3.setText("Chức vụ: ");
        jPanel3.add(jLabel3);

        cbo_role.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất Cả", "Quản Lý", "Nhân Viên Bán Vé" }));
        jPanel3.add(cbo_role);

        pnl_header.add(jPanel3);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        btn_filter.setText("Lọc");
        btn_filter.setIcon(SVGIcon.getSVGIcon("imgs/public/filter.svg"));
        btn_filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_filterActionPerformed(evt);
            }
        });
        jPanel4.add(btn_filter);

        btn_reload.setIcon(SVGIcon.getSVGIcon("imgs/public/update.svg"));
        btn_reload.setText("Làm mới");
        btn_reload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_reloadActionPerformed(evt);
            }
        });
        jPanel4.add(btn_reload);

        pnl_header.add(jPanel4);

        add(pnl_header, java.awt.BorderLayout.PAGE_START);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_filterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_filterActionPerformed
        // TODO add your handling code here:e
        Date date = date_dateAt.getDate();
        String id = txt_empID.getText().trim();
        String role = (String) cbo_role.getSelectedItem();
        renderShiftsTable(shift_bus.filter(id, role, date));
    }//GEN-LAST:event_btn_filterActionPerformed

    private void btn_reloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_reloadActionPerformed
    	renderShiftsTable(shift_bus.getShiftsByDate(new Date()));
    	reloadForm();
    }//GEN-LAST:event_btn_reloadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_filter;
    private javax.swing.JButton btn_reload;
    private javax.swing.JComboBox<String> cbo_role;
    private com.toedter.calendar.JDateChooser date_dateAt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel pnl_header;
    private javax.swing.JScrollPane scr_shifts;
    private javax.swing.JTable tbl_shifts;
    private javax.swing.JTextField txt_empID;
    // End of variables declaration//GEN-END:variables
}
