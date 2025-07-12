/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.hotelreservationsystem;

import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.DefaultCellEditor;
import java.text.SimpleDateFormat; 
import com.mycompany.hotelreservationsystem.LoggedInUser;



/**
 *
 * @author Hp
 */
public class searchrooms extends javax.swing.JFrame {
    
    // Renders button in a JTable cell
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "Book" : value.toString());
        return this;
    }
}

// Handles the click event for the button
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String roomId;
    private boolean clicked;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton("Book");
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        this.roomId = table.getValueAt(row, 0).toString(); // Room ID from column 0
        button.setText("Book");
        clicked = true;
        return button;
    }

    @Override
public Object getCellEditorValue() {
    if (clicked) {
        try {
            String status = table.getValueAt(table.getSelectedRow(), 2).toString(); // STATUS column (index 2)
if (status.equalsIgnoreCase("Booked")) {
    JOptionPane.showMessageDialog(null, "❌ This room is already booked on the selected dates.");
    clicked = false;
    return "Booked";  // prevent further processing
}

            // Fetch booking data from selected row and form
            String roomId = table.getValueAt(table.getSelectedRow(), 0).toString();
            Selectedroom.roomId = roomId; 
            String price = table.getValueAt(table.getSelectedRow(), 1).toString();
            String roomType = jComboBox1.getSelectedItem().toString();
            String persons = jTextField1.getText();

            java.util.Date checkInDate = jDateChooser1.getDate();
            java.util.Date checkOutDate = jDateChooser2.getDate();

            if (checkInDate == null || checkOutDate == null || persons.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill all booking fields.");
                return "Book";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String checkIn = sdf.format(checkInDate);
            String checkOut = sdf.format(checkOutDate);

            // Show confirmation dialog
            int option = JOptionPane.showConfirmDialog(null,
                "Do you want to confirm this booking?\n"
                + "Room ID: " + roomId + "\n"
                + "Room Type: " + roomType + "\n"
                + "Check-In: " + checkIn + "\n"
                + "Check-Out: " + checkOut + "\n"
                + "Persons: " + persons + "\n"
                + "Price: " + price + " PKR",
                "Confirm Booking",
                JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                // Connect to DB and insert booking
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/hotel reservation system", "root", "");

                String insertQuery = "INSERT INTO bookings (`user_id`, `room id`, `check_in`, `check_out`, `persons`) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = con.prepareStatement(insertQuery);
                pst.setInt(1, LoggedInUser.userId);
                pst.setString(2, roomId);
                pst.setString(3, checkIn);
                pst.setString(4, checkOut);
                pst.setString(5, persons);

                int rowsInserted = pst.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null, "✅ Booking successful!");
                    // You can redirect to a receipt page here if needed
                      // Now open receipt page
    Bookingpage receipt = new Bookingpage();
    receipt.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "⚠️ Booking failed.");
                }

                con.close();
            } 
  
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error during booking: " + e.getMessage());
        }
    }

    clicked = false;
    return "Book";
}




    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

       @Override
protected void fireEditingStopped() {
    super.fireEditingStopped();
}

    }


    /**
     * Creates new form searchrooms
     */
    public searchrooms() {
        initComponents();
         this.setLocationRelativeTo(null);
         
        jScrollPane2.setVisible(false);


jDateChooser1.setMinSelectableDate(new java.util.Date());
jDateChooser2.setMinSelectableDate(new java.util.Date());

jDateChooser1.addPropertyChangeListener("date", evt -> {
    java.util.Date checkInDate = jDateChooser1.getDate();
    if (checkInDate != null) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(checkInDate);
        cal.add(java.util.Calendar.DATE, 1); // One day ahead
        jDateChooser2.setMinSelectableDate(cal.getTime());
    }
});

        DefaultTableModel model = new DefaultTableModel(
    new Object[]{"ROOM ID", "PRICE (PKR)", "STATUS", "ACTION"}, 0
);
// Auto search when fields are filled
jComboBox1.addActionListener(e -> loadAvailableRoomsIfReady());

jTextField1.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    public void insertUpdate(javax.swing.event.DocumentEvent e) { loadAvailableRoomsIfReady(); }
    public void removeUpdate(javax.swing.event.DocumentEvent e) { loadAvailableRoomsIfReady(); }
    public void changedUpdate(javax.swing.event.DocumentEvent e) { loadAvailableRoomsIfReady(); }
});

jDateChooser1.addPropertyChangeListener("date", evt -> loadAvailableRoomsIfReady());
jDateChooser2.addPropertyChangeListener("date", evt -> loadAvailableRoomsIfReady());

jTable2.setModel(model);

// Add button renderer and editor
jTable2.getColumn("ACTION").setCellRenderer(new ButtonRenderer());
jTable2.getColumn("ACTION").setCellEditor((TableCellEditor) new ButtonEditor(new JCheckBox()));

    }
public void loadAvailableRoomsIfReady() {
    try {
        String roomType = jComboBox1.getSelectedItem().toString();
        String persons = jTextField1.getText();
        java.util.Date checkIn = jDateChooser1.getDate();
        java.util.Date checkOut = jDateChooser2.getDate();

        // Check if all fields are filled
        if (roomType == null || persons.isEmpty() || checkIn == null || checkOut == null) {
            return;
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/hotel reservation system", "root", "");
String query = "SELECT r.`room id`, r.`Price`, " +
               "CASE WHEN EXISTS (" +
               "  SELECT 1 FROM `bookings` b " +
               "  WHERE b.`room id` = r.`room id` " +
               "    AND b.`check_in` < ? " +
               "    AND b.`check_out` > ?" +
               ") THEN 'Booked' ELSE 'Available' END AS availability " +
               "FROM `room details` r " +
               "WHERE r.`room type` = ? AND r.`persons` = ?";


        PreparedStatement pst = con.prepareStatement(query);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String checkInStr = sdf.format(checkIn);
String checkOutStr = sdf.format(checkOut);

pst.setString(1, checkOutStr);  // b.check_in < ?
pst.setString(2, checkInStr);   // b.check_out > ?
pst.setString(3, roomType);
pst.setString(4, persons);


        ResultSet rs = pst.executeQuery();

        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ROOM ID", "PRICE (PKR)", "STATUS", "ACTION"}, 0);
        while (rs.next()) {
            String roomId = rs.getString("room id");
            String price = rs.getString("Price");
           String status = rs.getString("availability");
String actionText = status.equals("Booked") ? "Booked" : "Book";
model.addRow(new Object[]{roomId, price, status, actionText});

        }

        jTable2.setModel(model);
        jTable2.getColumn("ACTION").setCellRenderer(new ButtonRenderer());
        jTable2.getColumn("ACTION").setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // ✅ Show table only if data is found
if (model.getRowCount() > 0) {
    jScrollPane2.setVisible(true);
} else {
    jScrollPane2.setVisible(false);
}
        con.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 2, 14)); // NOI18N
        jLabel2.setText("\"Your comfort, our priority — Start your journey now!");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 540, -1, -1));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 48)); // NOI18N
        jLabel1.setText("        DREAM STAY HOTEL");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 58));

        jLabel3.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        jLabel3.setText("           SEARCH FOR ROOMS");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(171, 64, 278, 51));

        jComboBox1.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Standard", "Deluxe", "Suite" }));
        jPanel1.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, -1, -1));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        jLabel4.setIcon(new javax.swing.ImageIcon("C:\\Users\\Hp\\OneDrive\\Desktop\\JAVA INTERNSHIP CODE ALPHA\\room types.png")); // NOI18N
        jLabel4.setText("ROOM TYPE");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        jLabel5.setIcon(new javax.swing.ImageIcon("C:\\Users\\Hp\\OneDrive\\Desktop\\JAVA INTERNSHIP CODE ALPHA\\check in.png")); // NOI18N
        jLabel5.setText("CHECK-IN DATE");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\Hp\\OneDrive\\Desktop\\JAVA INTERNSHIP CODE ALPHA\\check out.png")); // NOI18N
        jLabel6.setText("CHECK-OUT DATE");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 140, -1, -1));

        jButton1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Hp\\OneDrive\\Desktop\\JAVA INTERNSHIP CODE ALPHA\\BACK.png")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 6, 42, -1));
        jPanel1.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 130, -1));
        jPanel1.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(368, 180, 140, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI Emoji", 1, 14)); // NOI18N
        jLabel7.setIcon(new javax.swing.ImageIcon("C:\\Users\\Hp\\OneDrive\\Desktop\\JAVA INTERNSHIP CODE ALPHA\\persons.png")); // NOI18N
        jLabel7.setText("PERSONS");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 140, -1, -1));
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 180, 70, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI Emoji", 1, 18)); // NOI18N
        jLabel10.setText("MATCHING ROOMS RESULT");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 260, -1, -1));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ROOMS ID", "PRICE (PKR)", "STATUS"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 620, 170));

        jLabel8.setIcon(new javax.swing.ImageIcon("C:\\Users\\Hp\\OneDrive\\Desktop\\JAVA INTERNSHIP CODE ALPHA\\bg of search room.jpg")); // NOI18N
        jLabel8.setText("jLabel8");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new page2().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(searchrooms.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(searchrooms.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(searchrooms.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(searchrooms.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new searchrooms().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    public static javax.swing.JComboBox<String> jComboBox1;
    public static com.toedter.calendar.JDateChooser jDateChooser1;
    public static com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTable jTable2;
    public static javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
