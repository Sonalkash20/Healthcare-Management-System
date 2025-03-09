package gui;
import javax.swing.*;

import database.DatabaseConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;

public class BookAppointmentForm {
    private JFrame frame;
    private JComboBox<String> doctorComboBox, patientComboBox;
    private JSpinner dateSpinner, timeSpinner;
    private JButton bookButton;

    public BookAppointmentForm(JFrame parentFrame) {
        frame = new JFrame("Book Appointment");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 2));

        // Patient selection (combo box)
        frame.add(new JLabel("Select Patient:"));
        patientComboBox = new JComboBox<>();
        loadPatients();  // Load patients from the database
        frame.add(patientComboBox);

        // Doctor selection (combo box)
        frame.add(new JLabel("Select Doctor:"));
        doctorComboBox = new JComboBox<>();
        loadDoctors();  // Load doctors from the database
        frame.add(doctorComboBox);

        // Appointment date selection
        frame.add(new JLabel("Select Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        frame.add(dateSpinner);

        // Appointment time selection
        frame.add(new JLabel("Select Time:"));
        timeSpinner = new JSpinner(new SpinnerDateModel());
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));
        frame.add(timeSpinner);

        // Book appointment button
        bookButton = new JButton("Book Appointment");
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookAppointment();
            }
        });
        frame.add(bookButton);

        frame.setVisible(true);
    }

    // Load patients into the combo box
    private void loadPatients() {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT id, name FROM patients";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                patientComboBox.addItem(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load doctors into the combo box
    private void loadDoctors() {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT id, name FROM doctors";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                doctorComboBox.addItem(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle appointment booking logic
    private void bookAppointment() {
        String doctorName = (String) doctorComboBox.getSelectedItem();
        String patientName = (String) patientComboBox.getSelectedItem();  // Get selected patient name

        java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
        java.util.Date selectedTime = (java.util.Date) timeSpinner.getValue();

        // Convert the selected time and date to SQL format
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
        java.sql.Time sqlTime = new java.sql.Time(selectedTime.getTime());

        int patientId = getPatientId(patientName);  // Get the patient ID from the selected name
        int doctorId = getDoctorId(doctorName);    // Get the doctor ID from the selected name

        try (Connection connection = DatabaseConnection.connect()) {
            // Check if the doctor is available at the selected time
            String checkAvailabilityQuery = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ? AND appointment_time = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkAvailabilityQuery);
            checkStmt.setInt(1, doctorId);
            checkStmt.setDate(2, sqlDate);
            checkStmt.setTime(3, sqlTime);
            ResultSet checkResult = checkStmt.executeQuery();
            checkResult.next();
            if (checkResult.getInt(1) > 0) {
                JOptionPane.showMessageDialog(frame, "This doctor is already booked for this time. Please choose another time.");
                return;
            }

            // Book the appointment if the doctor is available
            String bookAppointmentQuery = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(bookAppointmentQuery);
            stmt.setInt(1, patientId);  // Use dynamic patient ID
            stmt.setInt(2, doctorId);   // Use dynamic doctor ID
            stmt.setDate(3, sqlDate);
            stmt.setTime(4, sqlTime);
            stmt.setString(5, "Scheduled");

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Appointment booked successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error booking appointment.");
        }
    }

    // Get patient ID by patient name
    private int getPatientId(String patientName) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT id FROM patients WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, patientName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return an invalid ID if not found
    }

    // Get doctor ID by doctor name
    private int getDoctorId(String doctorName) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT id FROM doctors WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, doctorName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return an invalid ID if doctor not found
    }
}
