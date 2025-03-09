
package gui;
import javax.swing.*;

import database.DatabaseHelper;
import database.Patient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdatePatientDialog extends JDialog {

    private JTextField nameField, ageField, genderField, contactField;
    private JButton updateButton, cancelButton;
    private int patientId;

    public UpdatePatientDialog(JFrame parentFrame, Patient patient) {
        super(parentFrame, "Update Patient", true);  // true makes this a modal dialog
        this.patientId = patient.getId();

        setLayout(new GridLayout(5, 2));  // 5 rows, 2 columns layout

        // Create labels and text fields for updating patient data
        add(new JLabel("Name:"));
        nameField = new JTextField(patient.getName(), 20);  // Pre-fill with current patient data
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField(String.valueOf(patient.getAge()), 20);  // Pre-fill with current patient age
        add(ageField);

        add(new JLabel("Gender:"));
        genderField = new JTextField(patient.getGender(), 20);  // Pre-fill with current patient gender
        add(genderField);

        add(new JLabel("Contact Info:"));
        contactField = new JTextField(patient.getContactInfo(), 20);  // Pre-fill with current patient contact info
        add(contactField);

        // Add buttons
        updateButton = new JButton("Update Patient");
        cancelButton = new JButton("Cancel");

        add(updateButton);
        add(cancelButton);

        // Action listener for the Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());  // Convert age to integer
                String gender = genderField.getText();
                String contactInfo = contactField.getText();

                // Update the patient data in the database
                DatabaseHelper.updatePatient(patientId, name, age, gender, contactInfo);

                // Close the dialog after updating the patient
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
