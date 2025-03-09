package gui;

import javax.swing.*;

import database.DatabaseHelper;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddPatientDialog extends JDialog {

    private JTextField nameField, ageField, genderField, contactField;
    private JButton addButton, cancelButton;

    public AddPatientDialog(JFrame parentFrame) {
        super(parentFrame, "Add New Patient", true);  // true makes this a modal dialog
        setLayout(new GridLayout(5, 2));  // 5 rows, 2 columns layout

        // Create labels and text fields
        add(new JLabel("Name:"));
        nameField = new JTextField(20);
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField(20);
        add(ageField);

        add(new JLabel("Gender:"));
        genderField = new JTextField(20);
        add(genderField);

        add(new JLabel("Contact Info:"));
        contactField = new JTextField(20);
        add(contactField);

        // Add buttons
        addButton = new JButton("Add Patient");
        cancelButton = new JButton("Cancel");

        add(addButton);
        add(cancelButton);

        // Action listener for the Add button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());  // Convert age to integer
                String gender = genderField.getText();
                String contactInfo = contactField.getText();

                // Add the patient to the database using DatabaseHelper
                DatabaseHelper.addPatient(name, age, gender, contactInfo);

                // Close the dialog after adding the patient
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
