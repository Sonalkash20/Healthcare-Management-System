package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import database.DatabaseHelper;
import database.Medicine;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PharmacyManagementPage {
    private JTable medicineTable;
    private DefaultTableModel tableModel;

    public PharmacyManagementPage() {
        // Create the JFrame for Pharmacy Management
        JFrame frame = new JFrame("Manage Pharmacy Inventory");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Create table model for displaying medicine data
        tableModel = new DefaultTableModel(new Object[] { "ID", "Name", "Description", "Price", "Actions" }, 0);
        
        // Create the JTable with the table model
        medicineTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(medicineTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Create buttons
        JButton addButton = new JButton("Add Medicine");
        JButton updateButton = new JButton("Update Medicine");
        JButton deleteButton = new JButton("Delete Medicine");
        JButton viewButton = new JButton("View Medicine");
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
                // Open the Add Medicine form (AddMedicineDialog)
                new AddMedicineDialog(frame);
            }
        });

        // ActionListener for Update Button
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected medicine from the table and open the Update form
                int selectedRow = medicineTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int medicineId = (int) tableModel.getValueAt(selectedRow, 0);
                    Medicine medicine = null;
                    List<Medicine> medicines = DatabaseHelper.getMedicines();
                    for (Medicine m : medicines) {
                        if (m.getId() == medicineId) {
                            medicine = m;
                            break;
                        }
                    }

                    if (medicine != null) {
                        new UpdateMedicineDialog(frame, medicine);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a medicine to update.");
                }
            }
        });

        // ActionListener for Delete Button
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = medicineTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int medicineId = (int) tableModel.getValueAt(selectedRow, 0);
                    DatabaseHelper.deleteMedicine(medicineId);
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a medicine to delete.");
                }
            }
        });

        // ActionListener for View Button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = medicineTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int medicineId = (int) tableModel.getValueAt(selectedRow, 0);
                    JOptionPane.showMessageDialog(frame, "Viewing medicine with ID: " + medicineId);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a medicine to view.");
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

        // Fetch medicines from the database and populate the table
        refreshTable();

        // Make the JFrame visible
        frame.setVisible(true);
    }

    // Method to refresh the table with latest medicine data
    private void refreshTable() {
        List<Medicine> medicines = DatabaseHelper.getMedicines();
        tableModel.setRowCount(0);  // Clear existing rows in the table
        for (Medicine medicine : medicines) {
            tableModel.addRow(new Object[] {
                medicine.getId(),
                medicine.getName(),
                medicine.getDescription(),
                medicine.getPrice(),
                ""  // Empty cell for actions (Edit/Delete buttons will be handled in the dialog)
            });
        }
    }
}
