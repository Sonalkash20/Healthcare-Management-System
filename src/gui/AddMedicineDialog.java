package gui;

import javax.swing.*;

import database.DatabaseHelper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMedicineDialog extends JDialog {

    private JTextField nameField, descriptionField, priceField;
    private JButton addButton, cancelButton;

    public AddMedicineDialog(JFrame parentFrame) {
        super(parentFrame, "Add New Medicine", true);  // true makes this a modal dialog
        setLayout(new GridLayout(5, 2));  // 5 rows, 2 columns layout

        // Create labels and text fields
        add(new JLabel("Name:"));
        nameField = new JTextField(20);
        add(nameField);

        add(new JLabel("Description:"));
        descriptionField = new JTextField(20);
        add(descriptionField);

        add(new JLabel("Price:"));
        priceField = new JTextField(20);
        add(priceField);

        // Add buttons
        addButton = new JButton("Add Medicine");
        cancelButton = new JButton("Cancel");

        add(addButton);
        add(cancelButton);

        // Action listener for the Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String description = descriptionField.getText();
                double price = Double.parseDouble(priceField.getText());  // Convert price to double

                // Add the medicine to the database
                DatabaseHelper.addMedicine(name, description, price);

                // Close the dialog after adding the medicine
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
