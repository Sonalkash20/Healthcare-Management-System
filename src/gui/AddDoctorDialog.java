package gui;

import javax.swing.*;

import database.DatabaseHelper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddDoctorDialog extends JDialog {

    private JTextField nameField, specialtyField, contactField;
    private JButton addButton, cancelButton;

    public AddDoctorDialog(JFrame parentFrame) {
        super(parentFrame, "Add New Doctor", true);  // true makes this a modal dialog
        setLayout(new GridLayout(5, 2));  // 5 rows, 2 columns layout

        // Create labels and text fields
        add(new JLabel("Name:"));
        nameField = new JTextField(20);
        add(nameField);

        add(new JLabel("Specialty:"));
        specialtyField = new JTextField(20);
        add(specialtyField);

        add(new JLabel("Contact Info:"));
        contactField = new JTextField(20);
        add(contactField);

        // Add buttons
        addButton = new JButton("Add Doctor");
        cancelButton = new JButton("Cancel");

        add(addButton);
        add(cancelButton);

        // Action listener for the Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String specialty = specialtyField.getText();
                String contactInfo = contactField.getText();

                // Add the doctor to the database
                DatabaseHelper.addDoctor(name, specialty, contactInfo);

                // Close the dialog after adding the doctor
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

