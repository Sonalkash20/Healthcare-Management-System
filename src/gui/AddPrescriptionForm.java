package gui;

import javax.swing.*;

import database.DatabaseConnection;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddPrescriptionForm {
    private JFrame frame;
    private JComboBox<String> medicineComboBox;
    private JTextField quantityField;
    private JButton addButton, cancelButton;
    private int appointmentId;  // The appointment for which this prescription is being added

    public AddPrescriptionForm(JFrame parentFrame, int appointmentId) {
        this.appointmentId = appointmentId;  // Set the appointment ID

        frame = new JFrame("Add Prescription");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 2));

        // Load medicines into combo box
        frame.add(new JLabel("Select Medicine:"));
        medicineComboBox = new JComboBox<>();
        loadMedicines();  // Load medicines from the database
        frame.add(medicineComboBox);

        // Quantity input field
        frame.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        frame.add(quantityField);

        // Add and Cancel buttons
        addButton = new JButton("Add Prescription");
        cancelButton = new JButton("Cancel");

        frame.add(addButton);
        frame.add(cancelButton);

        // Action listener for Add Prescription button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPrescription();
            }
        });

        // Action listener for Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the form without doing anything
            }
        });

        frame.setVisible(true);
    }

    // Load medicines into the combo box
    private void loadMedicines() {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT id, name FROM pharmacy";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                medicineComboBox.addItem(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle adding the prescription
    private void addPrescription() {
        String medicineName = (String) medicineComboBox.getSelectedItem();
        String quantityStr = quantityField.getText();
        int quantity = 0;

        // Validate the quantity input
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                throw new NumberFormatException("Quantity must be greater than 0");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid quantity.");
            return;
        }

        int medicineId = getMedicineId(medicineName);  // Get medicine ID from the selected name

        try (Connection connection = DatabaseConnection.connect()) {
            // Insert the prescription into the database
            String query = "INSERT INTO prescriptions (appointment_id, medicine_id, quantity) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, appointmentId);  // Set the appointment ID
            stmt.setInt(2, medicineId);     // Set the medicine ID
            stmt.setInt(3, quantity);       // Set the quantity

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Prescription added successfully!");
            frame.dispose();  // Close the form
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding prescription.");
        }
    }

    // Get medicine ID from medicine name
    private int getMedicineId(String medicineName) {
        try (Connection connection = DatabaseConnection.connect()) {
            String query = "SELECT id FROM pharmacy WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, medicineName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Return -1 if medicine not found
    }
}

