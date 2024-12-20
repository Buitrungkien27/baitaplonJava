package gui;

import bus.Login_BUS;
import com.formdev.flatlaf.FlatClientProperties;
import entity.Account;
import entity.NhanVien;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.AbstractButton;
import main.Application;
import raven.toast.Notifications;

public class Login_GUI extends javax.swing.JPanel {

    private final Login_BUS log_BUS = new Login_BUS();
    public Login_GUI() {
        initComponents();
        pnl_changePasswordForm.setVisible(false);
    }

    public Dimension getScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return screenSize;
    }

    public boolean checkValueFormChangePassword(String id, String pass, String passNew, String passConfirm)
            throws Exception {

        if (id.equals("")) {
            throw new Exception("Mã đăng nhập không được bỏ trống!");
        }
        if (pass.equals("")) {
            throw new Exception("Mật khẩu không được bỏ trống!");
        }
        if (passNew.equals("")) {
            throw new Exception("Mật khẩu mới không được bỏ trống!");
        }
        if (passConfirm.equals("")) {
            throw new Exception("Bạn chưa xác nhận mật khẩu mới");
        }
        if (!passNew.equals(passConfirm)) {
            throw new Exception("Mật khẩu xác nhận không trùng khớp!");
        }
        return true;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        pnl_loginPanel = new javax.swing.JPanel();
        pnl_center = new javax.swing.JPanel();
        pnl_loginForm = new javax.swing.JPanel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 150), new java.awt.Dimension(32767, 100));
        jPanel4 = new javax.swing.JPanel();
        lbl_titleBox1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lbl_account = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        txt_account = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        lbl_password = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        pwr_password = new javax.swing.JPasswordField();
        jPanel8 = new javax.swing.JPanel();
        btn_login = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        lbl_changeForm = new javax.swing.JLabel();
        pnl_changePasswordForm = new javax.swing.JPanel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 90), new java.awt.Dimension(32767, 100));
        jPanel1 = new javax.swing.JPanel();
        lbl_titleChangePasswordForm = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        lbl_accountC = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        txt_accountC = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        lbl_passwordC = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        pwr_passwordC = new javax.swing.JPasswordField();
        jPanel20 = new javax.swing.JPanel();
        lbl_passwordNew = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        pwr_passwordNew = new javax.swing.JPasswordField();
        jPanel22 = new javax.swing.JPanel();
        lbl_passwordSubmit = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        pwr_passwordSubmit = new javax.swing.JPasswordField();
        jPanel14 = new javax.swing.JPanel();
        btn_submitChange = new javax.swing.JButton();
        jPanel15 = new javax.swing.JPanel();
        lbl_changeForm2 = new javax.swing.JLabel();
        pnl_img = new javax.swing.JPanel();
        lbl_img = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1366, 768));
        setMinimumSize(new java.awt.Dimension(600, 600));
        setPreferredSize(new java.awt.Dimension(1366, 768));
        setLayout(new java.awt.BorderLayout());

        pnl_loginPanel.setMinimumSize(new java.awt.Dimension(550, 520));
        pnl_loginPanel.setPreferredSize(new java.awt.Dimension(550, 768));
        pnl_loginPanel.setLayout(new java.awt.BorderLayout());

        pnl_center.setBackground(new java.awt.Color(255, 255, 255));
        pnl_center.setMinimumSize(new java.awt.Dimension(500, 500));
        pnl_center.setPreferredSize(new java.awt.Dimension(600, 500));

        pnl_loginForm.setBackground(new java.awt.Color(255, 255, 255));
        pnl_loginForm.setPreferredSize(new java.awt.Dimension(400, 580));
        pnl_loginForm.setLayout(new javax.swing.BoxLayout(pnl_loginForm, javax.swing.BoxLayout.Y_AXIS));
        pnl_loginForm.add(filler1);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.BorderLayout());

        lbl_titleBox1.setBackground(new java.awt.Color(255, 255, 255));
        lbl_titleBox1.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        lbl_titleBox1.setForeground(new java.awt.Color(71, 118, 185));
        lbl_titleBox1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titleBox1.setText("Đăng Nhập");
        jPanel4.add(lbl_titleBox1, java.awt.BorderLayout.CENTER);

        pnl_loginForm.add(jPanel4);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jPanel3.setLayout(new java.awt.BorderLayout());

        lbl_account.setBackground(new java.awt.Color(255, 255, 255));
        lbl_account.setText("Tài khoản: ");
        jPanel3.add(lbl_account, java.awt.BorderLayout.CENTER);

        pnl_loginForm.add(jPanel3);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel5.setLayout(new java.awt.BorderLayout());

        txt_account.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tài khoản");
        txt_account.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_account.setText("NV120042024001");
        txt_account.setToolTipText("");
        jPanel5.add(txt_account, java.awt.BorderLayout.CENTER);

        pnl_loginForm.add(jPanel5);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jPanel6.setLayout(new java.awt.BorderLayout());

        lbl_password.setBackground(new java.awt.Color(255, 255, 255));
        lbl_password.setText("Mật khẩu: ");
        jPanel6.add(lbl_password, java.awt.BorderLayout.CENTER);

        pnl_loginForm.add(jPanel6);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel7.setLayout(new java.awt.BorderLayout());

        pwr_password.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mật khẩu");
        pwr_password.putClientProperty(FlatClientProperties.STYLE, ""
            + "showRevealButton:true;"
            + "showCapsLock:true");
        pwr_password.setText("123456Aa");
        jPanel7.add(pwr_password, java.awt.BorderLayout.CENTER);

        pnl_loginForm.add(jPanel7);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel8.setLayout(new java.awt.BorderLayout());

        btn_login.setBackground(new java.awt.Color(71, 118, 185));
        btn_login.setForeground(new java.awt.Color(255, 255, 255));
        btn_login.setText("Đăng nhập");
        btn_login.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_loginActionPerformed(evt);
            }
        });
        jPanel8.add(btn_login, java.awt.BorderLayout.CENTER);

        pnl_loginForm.add(jPanel8);

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        jPanel9.setLayout(new java.awt.BorderLayout());

        lbl_changeForm.setBackground(new java.awt.Color(255, 255, 255));
        lbl_changeForm.setForeground(new java.awt.Color(71, 118, 185));
        lbl_changeForm.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_changeForm.setText("Đổi mật khẩu ?");
        lbl_changeForm.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_changeForm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_changeFormMouseClicked(evt);
            }
        });
        jPanel9.add(lbl_changeForm, java.awt.BorderLayout.CENTER);

        pnl_loginForm.add(jPanel9);

        pnl_center.add(pnl_loginForm);

        pnl_changePasswordForm.setBackground(new java.awt.Color(255, 255, 255));
        pnl_changePasswordForm.setPreferredSize(new java.awt.Dimension(400, 700));
        pnl_changePasswordForm.setLayout(new javax.swing.BoxLayout(pnl_changePasswordForm, javax.swing.BoxLayout.Y_AXIS));
        pnl_changePasswordForm.add(filler2);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new java.awt.BorderLayout());

        lbl_titleChangePasswordForm.setFont(new java.awt.Font("Helvetica Neue", 0, 24)); // NOI18N
        lbl_titleChangePasswordForm.setForeground(new java.awt.Color(71, 118, 185));
        lbl_titleChangePasswordForm.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_titleChangePasswordForm.setText("Đổi Mật Khẩu");
        lbl_titleChangePasswordForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 1, 1, 1));
        jPanel1.add(lbl_titleChangePasswordForm, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel1);

        jPanel10.setBackground(new java.awt.Color(255, 255, 255));
        jPanel10.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jPanel10.setLayout(new java.awt.BorderLayout());

        lbl_accountC.setText("Tài khoản:");
        jPanel10.add(lbl_accountC, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel10);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));
        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel11.setLayout(new java.awt.BorderLayout());

        txt_accountC.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tài khoản");
        jPanel11.add(txt_accountC, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel11);

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jPanel12.setLayout(new java.awt.BorderLayout());

        lbl_passwordC.setBackground(new java.awt.Color(255, 255, 255));
        lbl_passwordC.setText("Mật khẩu cũ:");
        jPanel12.add(lbl_passwordC, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel12);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel13.setLayout(new java.awt.BorderLayout());

        pwr_passwordC.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mật khẩu");
        pwr_passwordC.putClientProperty(FlatClientProperties.STYLE, ""
            + "showRevealButton:true;"
            + "showCapsLock:true");
        jPanel13.add(pwr_passwordC, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel13);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jPanel20.setLayout(new java.awt.BorderLayout());

        lbl_passwordNew.setText("Mật khẩu mới:");
        jPanel20.add(lbl_passwordNew, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel20);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel21.setLayout(new java.awt.BorderLayout());

        pwr_passwordNew.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mật khẩu mới");
        pwr_passwordNew.putClientProperty(FlatClientProperties.STYLE, ""
            + "showRevealButton:true;"
            + "showCapsLock:true");
        jPanel21.add(pwr_passwordNew, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel21);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jPanel22.setLayout(new java.awt.BorderLayout());

        lbl_passwordSubmit.setBackground(new java.awt.Color(255, 255, 255));
        lbl_passwordSubmit.setText("Xác minh lại mật khẩu:");
        jPanel22.add(lbl_passwordSubmit, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel22);

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 10, 10));
        jPanel23.setLayout(new java.awt.BorderLayout());

        pwr_passwordSubmit.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Mật khẩu mới");
        pwr_passwordSubmit.putClientProperty(FlatClientProperties.STYLE, ""
            + "showRevealButton:true;"
            + "showCapsLock:true");
        jPanel23.add(pwr_passwordSubmit, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel23);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel14.setLayout(new java.awt.BorderLayout());

        btn_submitChange.setBackground(new java.awt.Color(71, 118, 185));
        btn_submitChange.setForeground(new java.awt.Color(255, 255, 255));
        btn_submitChange.setText("Đổi mật khẩu");
        btn_submitChange.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_submitChange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_submitChangeActionPerformed(evt);
            }
        });
        jPanel14.add(btn_submitChange, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel14);

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 20));
        jPanel15.setLayout(new java.awt.BorderLayout());

        lbl_changeForm2.setBackground(new java.awt.Color(255, 255, 255));
        lbl_changeForm2.setForeground(new java.awt.Color(71, 118, 185));
        lbl_changeForm2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_changeForm2.setText("Đăng nhập");
        lbl_changeForm2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_changeForm2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_changeForm2MouseClicked(evt);
            }
        });
        jPanel15.add(lbl_changeForm2, java.awt.BorderLayout.CENTER);

        pnl_changePasswordForm.add(jPanel15);

        pnl_center.add(pnl_changePasswordForm);

        pnl_loginPanel.add(pnl_center, java.awt.BorderLayout.CENTER);

        add(pnl_loginPanel, java.awt.BorderLayout.EAST);

        pnl_img.setBackground(new java.awt.Color(255, 255, 255));
        pnl_img.setMaximumSize(new java.awt.Dimension(1600, 2147483647));
        pnl_img.setPreferredSize(new java.awt.Dimension(600, 768));
        pnl_img.setLayout(new java.awt.BorderLayout());

        lbl_img.setBackground(new java.awt.Color(255, 255, 255));
        //ImageIcon imageIcon = new javax.swing.ImageIcon(getClass().getResource("/imgs/login/OmegaBook-2.png"));
        //Image image = imageIcon.getImage();
        //Image scaledImage = image.getScaledInstance(lbl_img.getWidth(), lbl_img.getHeight(), Image.SCALE_SMOOTH | Image.SCALE_AREA_AVERAGING);
        //
        //// Tạo lại đối tượng ImageIcon với kích thước mới
        //imageIcon = new ImageIcon(scaledImage);
        lbl_img.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imgs/login/bg-tau.png"))); // NOI18N
        lbl_img.setMaximumSize(new java.awt.Dimension(1600, 1789));
        lbl_img.setMinimumSize(new java.awt.Dimension(0, 0));
        lbl_img.setPreferredSize(new java.awt.Dimension(0, 0));
        pnl_img.add(lbl_img, java.awt.BorderLayout.CENTER);

        add(pnl_img, java.awt.BorderLayout.CENTER);
    }// </editor-fold>                        

    private void btn_loginActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
        String id = txt_account.getText();
        String password = String.copyValueOf(pwr_password.getPassword());

        try {
            NhanVien emp = log_BUS.login(id, password);
            if (emp != null && emp.getTrangThai()) {
                Application.login(emp);
            }
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, ex.getMessage());
        }
    }                                         

    private void btn_submitChangeActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        // TODO add your handling code here:
        String id = txt_account.getText();
        String pass = String.valueOf(pwr_passwordC.getPassword());
        String passNew = String.valueOf(pwr_passwordNew.getPassword());
        String passConfirm = String.valueOf(pwr_passwordSubmit.getPassword());

        try {
            if (checkValueFormChangePassword(id, pass, passNew, passConfirm)) {

//                Account acc = new Account(passNew, new Employee(id));
                if (log_BUS.changePassword(id, pass, passNew)) {
                    Notifications.getInstance().show(Notifications.Type.SUCCESS, "Đổi mật khẩu thành công!");
//                  Application.showForm(new Login_GUI());
                    txt_accountC.setText("");
                    pwr_passwordC.setText("");
                    pwr_passwordNew.setText("");
                    pwr_passwordSubmit.setText("");

                }
            }
        } catch (Exception ex) {
            Notifications.getInstance().show(Notifications.Type.ERROR, ex.getMessage());
        }
    }                                                

    private void lbl_changeFormMouseClicked(java.awt.event.MouseEvent evt) {                                            
        // TODO add your handling code here:
        pnl_changePasswordForm.setVisible(true);
        pnl_loginForm.setVisible(false);
    }                                           

    private void lbl_changeForm2MouseClicked(java.awt.event.MouseEvent evt) {                                             
        // TODO add your handling code here:
        pnl_changePasswordForm.setVisible(false);
        pnl_loginForm.setVisible(true);
    }                                            

    private void formAncestorResized(java.awt.event.HierarchyEvent evt) {

    }

    private void txt_accountLoginActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txt_accountLoginActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txt_accountLoginActionPerformed

    private void pwr_passwordLoginActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pwr_passwordLoginActionPerformed
        // TODO add your handling code here:
//        resizeCenterPanel();

    }// GEN-LAST:event_pwr_passwordLoginActionPerformed

    private void pnl_loginAncestorResized(java.awt.event.HierarchyEvent evt) {// GEN-FIRST:event_pnl_loginAncestorResized
        // TODO add your handling code here:
//        resizeCenterPanel();
    }// GEN-LAST:event_pnl_loginAncestorResized

    private void pwr_passwordNewActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pwr_passwordNewActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_pwr_passwordNewActionPerformed

    private void txt_accountActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txt_accountActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_txt_accountActionPerformed

    private void pwr_passwordConfirmActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pwr_passwordConfirmActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_pwr_passwordConfirmActionPerformed

    private void pwr_passwordActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pwr_passwordActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_pwr_passwordActionPerformed

//    private void resizeCenterPanel() {
//        if (pnl_login != null && pnl_loginForm != null) {
//            int centerX = (pnl_login.getWidth() - pnl_loginForm.getWidth()) / 2;
//            int centerY = (pnl_login.getHeight() - pnl_loginForm.getHeight()) / 2;
//
//            pnl_loginForm.setBounds(centerX, centerY, pnl_loginForm.getWidth(), pnl_loginForm.getHeight());
//        }
//        
//        if (pnl_login != null && pnl_changePasswordForm != null) {
//            int centerX = (pnl_login.getWidth() - pnl_changePasswordForm.getWidth()) / 2;
//            int centerY = (pnl_login.getHeight() - pnl_changePasswordForm.getHeight()) / 2;
//            
//            pnl_changePasswordForm.setBounds(centerX, centerY, pnl_changePasswordForm.getWidth(),
//                    pnl_changePasswordForm.getHeight());
//        }
//
//        pnl_info.setBounds((int)(getScreenSize().getWidth()-pnl_info.getSize().getWidth()), (int)(getScreenSize().getHeight()-pnl_info.getSize().getHeight()), -1, -1);
////        lbl_welcome.setBounds((int)(getScreenSize().getWidth()-lbl_welcome.getSize().getWidth())/2, 10, -1, -1);
////        lbl_icon.setBounds(10, (int)(getScreenSize().getHeight()-lbl_icon.getSize().getHeight()), -1, -1);
//    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btn_login;
    private javax.swing.JButton btn_submitChange;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel lbl_account;
    private javax.swing.JLabel lbl_accountC;
    private javax.swing.JLabel lbl_changeForm;
    private javax.swing.JLabel lbl_changeForm2;
    private javax.swing.JLabel lbl_img;
    private javax.swing.JLabel lbl_password;
    private javax.swing.JLabel lbl_passwordC;
    private javax.swing.JLabel lbl_passwordNew;
    private javax.swing.JLabel lbl_passwordSubmit;
    private javax.swing.JLabel lbl_titleBox1;
    private javax.swing.JLabel lbl_titleChangePasswordForm;
    private javax.swing.JPanel pnl_center;
    private javax.swing.JPanel pnl_changePasswordForm;
    private javax.swing.JPanel pnl_img;
    private javax.swing.JPanel pnl_loginForm;
    private javax.swing.JPanel pnl_loginPanel;
    private javax.swing.JPasswordField pwr_password;
    private javax.swing.JPasswordField pwr_passwordC;
    private javax.swing.JPasswordField pwr_passwordNew;
    private javax.swing.JPasswordField pwr_passwordSubmit;
    private javax.swing.JTextField txt_account;
    private javax.swing.JTextField txt_accountC;
    // End of variables declaration                   
}
