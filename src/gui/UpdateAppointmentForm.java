package gui;

import javax.swing.*;

import database.DatabaseConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateAppointmentForm {
    private JFrame frame;
    private JComboBox<String> doctorComboBox, patientComboBox;
    private JSpinner dateSpinner, timeSpinner;
    private JButton updateButton, cancelButton;
    private int appointmentId;

    public UpdateAppointmentForm(JFrame parentFrame, int appointmentId) {
        this.appointmentId = appointmentId;
        frame = new JFrame("Update Appointment");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 2));

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

        // Update and Cancel buttons
        updateButton = new JButton("Update Appointment");
        cancelButton = new JButton("Cancel");

        frame.add(updateButton);
        frame.add(cancelButton);

        // Load current appointment details
        loadAppointmentDetails();

        // Action listener for Update Appointment button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAppointment();
            }
        });

        // Action listener for Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the form
            }
        });

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

    // Load the current appointment details
    private void loadAppointmentDetails() {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT p.name as patient_name, d.name as doctor_name, a.appointment_date, a.appointment_time " +
                           "FROM appointments a " +
                           "JOIN patients p ON a.patient_id = p.id " +
                           "JOIN doctors d ON a.doctor_id = d.id " +
                           "WHERE a.id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, appointmentId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String patientName = resultSet.getString("patient_name");
                String doctorName = resultSet.getString("doctor_name");
                Date appointmentDate = resultSet.getDate("appointment_date");
                Time appointmentTime = resultSet.getTime("appointment_time");

                patientComboBox.setSelectedItem(patientName);
                doctorComboBox.setSelectedItem(doctorName);
                dateSpinner.setValue(appointmentDate);
                timeSpinner.setValue(appointmentTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle appointment update logic
    private void updateAppointment() {
        String patientName = (String) patientComboBox.getSelectedItem();
        String doctorName = (String) doctorComboBox.getSelectedItem();

        java.util.Date selectedDate = (java.util.Date) dateSpinner.getValue();
        java.util.Date selectedTime = (java.util.Date) timeSpinner.getValue();

        // Convert the selected time and date to SQL format
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
        java.sql.Time sqlTime = new java.sql.Time(selectedTime.getTime());

        int patientId = getPatientId(patientName);  // Get patient ID
        int doctorId = getDoctorId(doctorName);    // Get doctor ID

        try (Connection connection = DatabaseConnection.connect()) {
            String updateAppointmentQuery = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_date = ?, appointment_time = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(updateAppointmentQuery);
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setDate(3, sqlDate);
            stmt.setTime(4, sqlTime);
            stmt.setInt(5, appointmentId);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Appointment updated successfully!");
            frame.dispose();  // Close the form
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating appointment.");
        }
    }

    // Get patient ID by name
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
        return -1;
    }

    // Get doctor ID by name
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
        return -1;
    }
}
