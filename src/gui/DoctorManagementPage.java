package gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.DatabaseHelper;
import database.Doctor;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DoctorManagementPage {
    private JTable doctorTable;
    private DefaultTableModel tableModel;

    public DoctorManagementPage() {
        // Create the JFrame for Doctor Management
        JFrame frame = new JFrame("Manage Doctors");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create table model for displaying doctor data
        tableModel = new DefaultTableModel(new Object[] { "ID", "Name", "Specialty", "Contact Info", "Actions" }, 0);
        
        // Create the JTable with the table model
        doctorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons
        JButton addButton = new JButton("Add Doctor");
        JButton updateButton = new JButton("Update Doctor");
        JButton deleteButton = new JButton("Delete Doctor");
        JButton viewButton = new JButton("View Doctor");
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
                // Open the Add Doctor form (AddDoctorDialog)
                new AddDoctorDialog(frame);
            }
        });

        // ActionListener for Update Button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected doctor from the table and open the Update form
                int selectedRow = doctorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
                    Doctor doctor = null;
                    List<Doctor> doctors = DatabaseHelper.getDoctors();
                    for (Doctor d : doctors) {
                        if (d.getId() == doctorId) {
                            doctor = d;
                            break;
                        }
                    }

                    if (doctor != null) {
                        new UpdateDoctorDialog(frame, doctor);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a doctor to update.");
                }
            }
        });

        // ActionListener for Delete Button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = doctorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
                    DatabaseHelper.deleteDoctor(doctorId);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a doctor to delete.");
                }
            }
        });

        // ActionListener for View Button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = doctorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
                    JOptionPane.showMessageDialog(frame, "Viewing doctor with ID: " + doctorId);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a doctor to view.");
                }
            }
        });

        // ActionListener for Refresh Button
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
            }
        });

        // ActionListener for Home Button
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new MainWindow();
            }
        });

        // Fetch doctors from the database and populate the table
        refreshTable();

        // Make the JFrame visible
        frame.setVisible(true);
    }

    // Method to refresh the table with latest doctor data
    private void refreshTable() {
        List<Doctor> doctors = DatabaseHelper.getDoctors();
        tableModel.setRowCount(0);  // Clear existing rows in the table
        for (Doctor doctor : doctors) {
            tableModel.addRow(new Object[] {
                doctor.getId(),
                doctor.getName(),
                doctor.getSpecialty(),
                doctor.getContactInfo(),
                ""  // Empty cell for actions (Edit/Delete buttons will be handled in the dialog)
            });
        }
    }
}
