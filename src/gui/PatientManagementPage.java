package gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.DatabaseHelper;
import database.Patient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PatientManagementPage {
    private JTable patientTable;
    private DefaultTableModel tableModel;

    public PatientManagementPage() {
        // Create the JFrame for Patient Management
        JFrame frame = new JFrame("Manage Patients");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create table model for displaying patient data
        tableModel = new DefaultTableModel(new Object[] { "ID", "Name", "Age", "Gender", "Contact Info", "Actions" }, 0);
        
        // Create the JTable with the table model
        patientTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());  // Flow layout to align buttons horizontally

        // Create buttons
        JButton addButton = new JButton("Add Patient");
        JButton updateButton = new JButton("Update Patient");
        JButton deleteButton = new JButton("Delete Patient");
        JButton viewButton = new JButton("View Patient");
        JButton refreshButton = new JButton("Refresh");
        JButton homeButton = new JButton("Home");

        // Add buttons to the panel
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(homeButton);
        
        // Add the panel to the frame
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // ActionListener for Add Button
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the Add Patient form (AddPatientDialog)
                new AddPatientDialog(frame);
            }
        });

        // ActionListener for Update Button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected patient from the table and open the Update form
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Get the patient ID from the selected row
                    int patientId = (int) tableModel.getValueAt(selectedRow, 0);  // Get the patient ID from the first column
                    
                    // Fetch the patient details using the patient ID (you can modify this line based on your patient list and IDs)
                    Patient patient = null;
                    List<Patient> patients = DatabaseHelper.getPatients();  // Get all patients from the database
                    for (Patient p : patients) {
                        if (p.getId() == patientId) {
                            patient = p;  // Find the patient with the matching ID
                            break;
                        }
                    }

                    if (patient != null) {
                        // If the patient is found, open the UpdatePatientDialog with the patient's details
                        new UpdatePatientDialog(frame, patient);  // Pass the parent frame and selected patient to the dialog
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a patient to update.");
                }
            }
        });

        // ActionListener for Delete Button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete selected patient
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int patientId = (int) tableModel.getValueAt(selectedRow, 0);
                    DatabaseHelper.deletePatient(patientId);  // Call delete method
                    refreshTable();  // Refresh table after deletion
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a patient to delete.");
                }
            }
        });

        // ActionListener for View Button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // View selected patient's details
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int patientId = (int) tableModel.getValueAt(selectedRow, 0);  // Get patient ID
                    JOptionPane.showMessageDialog(frame, "Viewing patient with ID: " + patientId);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a patient to view.");
                }
            }
        });

        // ActionListener for Refresh Button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Refresh the patient data from the database
                refreshTable();
            }
        });

        // ActionListener for Home Button
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Go back to the main page
                frame.dispose();  // Close current window
                new MainWindow();  // Open the main window again
            }
        });

        // Fetch patients from the database and populate the table
        refreshTable();

        // Make the JFrame visible
        frame.setVisible(true);
    }

    // Method to refresh the table with latest patient data
    private void refreshTable() {
        List<Patient> patients = DatabaseHelper.getPatients();
        tableModel.setRowCount(0);  // Clear existing rows in the table
        for (Patient patient : patients) {
            tableModel.addRow(new Object[] {
                patient.getId(),
                patient.getName(),
                patient.getAge(),
                patient.getGender(),
                patient.getContactInfo(),
                ""  // Empty cell for actions (Edit/Delete buttons will be handled in the dialog)
            });
        }
    }
}
