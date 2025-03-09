package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.DatabaseConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AppointmentManagementPage {

    private JFrame frame;
    private JTable appointmentTable;
    private DefaultTableModel tableModel;

    public AppointmentManagementPage() {
        frame = new JFrame("Manage Appointments");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create table model for displaying appointment data
        tableModel = new DefaultTableModel(new Object[]{"ID", "Patient", "Doctor", "Date", "Time", "Status"}, 0);

        // Create JTable and add it to scroll pane
        appointmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons
        JButton addPrescriptionButton = new JButton("Add Prescription");
        JButton refreshButton = new JButton("Refresh");
        JButton homeButton = new JButton("Home");
        JButton addAppointmentButton = new JButton("Add Appointment");  // New Add Appointment button

        buttonPanel.add(addAppointmentButton);  // Add Add Appointment button to the panel
        buttonPanel.add(addPrescriptionButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(homeButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Load appointments into the table
        loadAppointments();

        // Action listener for Add Prescription button
        addPrescriptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = appointmentTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int appointmentId = (int) tableModel.getValueAt(selectedRow, 0);
                    // Open Add Prescription form
                    new AddPrescriptionForm(frame, appointmentId);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select an appointment to add prescription.");
                }
            }
        });

        // Action listener for Add Appointment button
        addAppointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the Book Appointment form
                new BookAppointmentForm(frame);  // Open the book appointment form (make sure this form exists)
            }
        });

        // Action listener for Refresh button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAppointments();  // Refresh the data in the table
            }
        });

        // Action listener for Home button
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the appointment management page and go back to home
                new MainWindow();  // Open the main window
            }
        });

        frame.setVisible(true);
    }

    // Method to load all appointments from the database and populate the table
    private void loadAppointments() {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT a.id, p.name as patient_name, d.name as doctor_name, a.appointment_date, a.appointment_time, a.status " +
                    "FROM appointments a " +
                    "JOIN patients p ON a.patient_id = p.id " +
                    "JOIN doctors d ON a.doctor_id = d.id";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Clear existing rows
            tableModel.setRowCount(0);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String patientName = resultSet.getString("patient_name");
                String doctorName = resultSet.getString("doctor_name");
                Date appointmentDate = resultSet.getDate("appointment_date");
                Time appointmentTime = resultSet.getTime("appointment_time");
                String status = resultSet.getString("status");

                tableModel.addRow(new Object[]{id, patientName, doctorName, appointmentDate, appointmentTime, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading appointments.");
        }
    }
}
