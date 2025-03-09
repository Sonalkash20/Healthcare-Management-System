package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.DatabaseConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ReportGenerator {

    private JFrame frame;
    private JComboBox<String> monthComboBox, yearComboBox;
    private JTextArea reportTextArea;

    public ReportGenerator() {
        frame = new JFrame("Generate Monthly Reports");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create combo boxes for selecting month and year
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new FlowLayout());
        
        monthComboBox = new JComboBox<>(new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        yearComboBox = new JComboBox<>(new String[] {"2025", "2026", "2027"});
        
        JButton generateReportButton = new JButton("Generate Report");

        selectionPanel.add(new JLabel("Month:"));
        selectionPanel.add(monthComboBox);
        selectionPanel.add(new JLabel("Year:"));
        selectionPanel.add(yearComboBox);
        selectionPanel.add(generateReportButton);

        frame.add(selectionPanel, BorderLayout.NORTH);

        // Text area for displaying the report
        reportTextArea = new JTextArea();
        reportTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportTextArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create Home button
        JButton homeButton = new JButton("Home");
        selectionPanel.add(homeButton);  // Add Home button to the selection panel

        // Action listener for Generate Report button
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        // Action listener for Home button
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the report generator page and go back to the home
                new MainWindow();  // Open the main window
            }
        });

        frame.setVisible(true);
    }

    private void generateReport() {
        int selectedMonth = monthComboBox.getSelectedIndex() + 1;  // Get selected month (1-12)
        int selectedYear = Integer.parseInt((String) yearComboBox.getSelectedItem());

        String monthString = String.format("%02d", selectedMonth);  // Format month as two digits (e.g., 01 for January)
        String startDate = selectedYear + "-" + monthString + "-01";
        String endDate = selectedYear + "-" + monthString + "-31";

        try (Connection connection = DatabaseConnection.connect()) {
            // Revenue Report from appointments (1500 rupees per appointment)
            String revenueQuery = "SELECT COUNT(*) * 1500 AS appointment_revenue FROM appointments WHERE appointment_date BETWEEN ? AND ?";
            PreparedStatement revenueStmt = connection.prepareStatement(revenueQuery);
            revenueStmt.setString(1, startDate);
            revenueStmt.setString(2, endDate);
            ResultSet revenueResult = revenueStmt.executeQuery();

            double appointmentRevenue = 0;
            if (revenueResult.next()) {
                appointmentRevenue = revenueResult.getDouble("appointment_revenue");
            }

            // Revenue from pharmacy sales (prescriptions)
            String pharmacyQuery = "SELECT SUM(p.quantity * m.price) AS pharmacy_revenue " +
                                    "FROM prescriptions p " +
                                    "JOIN pharmacy m ON p.medicine_id = m.id " +
                                    "JOIN appointments a ON p.appointment_id = a.id " +
                                    "WHERE a.appointment_date BETWEEN ? AND ?";
            PreparedStatement pharmacyStmt = connection.prepareStatement(pharmacyQuery);
            pharmacyStmt.setString(1, startDate);
            pharmacyStmt.setString(2, endDate);
            ResultSet pharmacyResult = pharmacyStmt.executeQuery();

            double pharmacyRevenue = 0;
            if (pharmacyResult.next()) {
                pharmacyRevenue = pharmacyResult.getDouble("pharmacy_revenue");
            }

            // Total revenue
            double totalRevenue = appointmentRevenue + pharmacyRevenue;

            // Patient Visits Report
            String patientVisitsQuery = "SELECT COUNT(*) AS total_patient_visits FROM appointments WHERE appointment_date BETWEEN ? AND ?";
            PreparedStatement patientVisitsStmt = connection.prepareStatement(patientVisitsQuery);
            patientVisitsStmt.setString(1, startDate);
            patientVisitsStmt.setString(2, endDate);
            ResultSet patientVisitsResult = patientVisitsStmt.executeQuery();

            int totalPatientVisits = 0;
            if (patientVisitsResult.next()) {
                totalPatientVisits = patientVisitsResult.getInt("total_patient_visits");
            }

            // Inventory Usage Report (for prescriptions)
            String inventoryQuery = "SELECT m.name, SUM(p.quantity) AS total_used " +
                                    "FROM prescriptions p " +
                                    "JOIN pharmacy m ON p.medicine_id = m.id " +
                                    "JOIN appointments a ON p.appointment_id = a.id " +
                                    "WHERE a.appointment_date BETWEEN ? AND ? " +
                                    "GROUP BY m.name";
            PreparedStatement inventoryStmt = connection.prepareStatement(inventoryQuery);
            inventoryStmt.setString(1, startDate);
            inventoryStmt.setString(2, endDate);
            ResultSet inventoryResult = inventoryStmt.executeQuery();

            // Display the results
            StringBuilder reportBuilder = new StringBuilder();
            reportBuilder.append("Monthly Report for ").append(monthComboBox.getSelectedItem()).append(" ").append(selectedYear).append("\n\n");

            reportBuilder.append("Revenue from Appointments: ").append(appointmentRevenue).append(" rupees\n");
            reportBuilder.append("Revenue from Pharmacy Sales: ").append(pharmacyRevenue).append(" rupees\n");
            reportBuilder.append("Total Revenue: ").append(totalRevenue).append(" rupees\n");
            reportBuilder.append("Total Patient Visits: ").append(totalPatientVisits).append("\n\n");

            reportBuilder.append("Inventory Usage:\n");
            while (inventoryResult.next()) {
                String medicineName = inventoryResult.getString("name");
                int totalUsed = inventoryResult.getInt("total_used");
                reportBuilder.append(medicineName).append(": ").append(totalUsed).append(" units\n");
            }

            // Display the report in the text area
            reportTextArea.setText(reportBuilder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error generating report.");
        }
    }

    public static void main(String[] args) {
        new ReportGenerator();  // Open the report generator window
    }
}
