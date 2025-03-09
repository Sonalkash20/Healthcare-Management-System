package gui;

import javax.swing.*;

import database.DatabaseHelper;
import database.Doctor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateDoctorDialog extends JDialog {

    private JTextField nameField, specialtyField, contactField;
    private JButton updateButton, cancelButton;
    private int doctorId;

    public UpdateDoctorDialog(JFrame parentFrame, Doctor doctor) {
        super(parentFrame, "Update Doctor", true);
        this.doctorId = doctor.getId();

        setLayout(new GridLayout(5, 2));

        // Create labels and text fields for updating doctor data
        add(new JLabel("Name:"));
        nameField = new JTextField(doctor.getName(), 20);  // Pre-fill with current doctor data
        add(nameField);

        add(new JLabel("Specialty:"));
        specialtyField = new JTextField(doctor.getSpecialty(), 20);
        add(specialtyField);

        add(new JLabel("Contact Info:"));
        contactField = new JTextField(doctor.getContactInfo(), 20);
        add(contactField);

        // Add buttons
        updateButton = new JButton("Update Doctor");
        cancelButton = new JButton("Cancel");

        add(updateButton);
        add(cancelButton);

        // Action listener for the Update button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String specialty = specialtyField.getText();
                String contactInfo = contactField.getText();

                // Update the doctor data in the database
                DatabaseHelper.updateDoctor(doctorId, name, specialty, contactInfo);

                // Close the dialog after updating the doctor
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
        setLocationRelativeTo(parentFrame);
        setVisible(true);
    }
}

