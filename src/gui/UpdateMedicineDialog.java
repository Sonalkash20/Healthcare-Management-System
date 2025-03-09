package gui;

import javax.swing.*;

import database.DatabaseHelper;
import database.Medicine;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateMedicineDialog extends JDialog {

    private JTextField nameField, descriptionField, priceField;
    private JButton updateButton, cancelButton;
    private int medicineId;

    public UpdateMedicineDialog(JFrame parentFrame, Medicine medicine) {
        super(parentFrame, "Update Medicine", true);  // true makes this a modal dialog
        this.medicineId = medicine.getId();

        setLayout(new GridLayout(5, 2));  // 5 rows, 2 columns layout

        // Create labels and text fields for updating medicine data
        add(new JLabel("Name:"));
        nameField = new JTextField(medicine.getName(), 20);  // Pre-fill with current medicine data
        add(nameField);

        add(new JLabel("Description:"));
        descriptionField = new JTextField(medicine.getDescription(), 20);  // Pre-fill with current medicine description
        add(descriptionField);

        add(new JLabel("Price:"));
        priceField = new JTextField(String.valueOf(medicine.getPrice()), 20);  // Pre-fill with current medicine price
        add(priceField);

        // Add buttons
        updateButton = new JButton("Update Medicine");
        cancelButton = new JButton("Cancel");

        add(updateButton);
        add(cancelButton);

        // Action listener for the Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String description = descriptionField.getText();
                double price = Double.parseDouble(priceField.getText());  // Convert price to double

                // Update the medicine data in the database
                DatabaseHelper.updateMedicine(medicineId, name, description, price);

                // Close the dialog after updating the medicine
                dispose();
            }
        });

        // Action listener for the Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close the dialog without doing anything
            }
        });

        // Set the dialog size and make it visible
        setSize(300, 200);
        setLocationRelativeTo(parentFrame);  // Center the dialog relative to the parent frame
        setVisible(true);  // Make the dialog visible
    }
}

