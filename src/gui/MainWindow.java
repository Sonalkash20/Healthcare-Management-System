package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow {
    private JFrame frame;

    public MainWindow() {
        // Create the main frame with a title
        frame = new JFrame("MediCarePlus");
        frame.setSize(800, 500);  // Increase the window size for better spacing
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set layout to BorderLayout for better control over component placement
        frame.setLayout(new BorderLayout(10, 10));  // Space between components
        
        // Add a header panel with a welcome message
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // Center the text
        JLabel welcomeLabel = new JLabel("Welcome to MediCare Plus Hospital");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));  // Large bold font
        welcomeLabel.setForeground(Color.BLUE);  // Set the font color
        headerPanel.add(welcomeLabel);
        
        // Add the header panel to the frame
        frame.add(headerPanel, BorderLayout.NORTH);
        
        // Create a panel for buttons and use GridLayout for organizing them neatly
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 3, 20, 20));  // 2 rows, 3 columns with spacing
        
        // Create buttons for navigating to different sections with customized appearance
        JButton patientButton = createStyledButton("Manage Patients");
        JButton doctorButton = createStyledButton("Manage Doctors");
        JButton pharmacyButton = createStyledButton("Manage Pharmacy");
        JButton appointmentButton = createStyledButton("Manage Appointments");
        JButton reportButton = createStyledButton("Generate Report");
        JButton remindersButton = createStyledButton("Reminders");

        // Add action listeners to the buttons
        patientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PatientManagementPage();  // Open the Patient Management Page
                frame.dispose();
            }
        });

        doctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 new DoctorManagementPage();  // Open the Doctor Management Page
                frame.dispose();
            }
        });

        pharmacyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PharmacyManagementPage();  // Open the Pharmacy Management Page
                frame.dispose();
            }
        });

        appointmentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 new AppointmentManagementPage();  // Open the Appointment Management Page
                frame.dispose();
            }
        });

        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 new ReportGenerator();  // Open the Report Generation Window
                frame.dispose();
            }
        });

        remindersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // new ReminderActivityWindow();  // Open the Reminder Activity Window
            }
        });

        // Add buttons to the button panel
        buttonPanel.add(patientButton);
        buttonPanel.add(doctorButton);
        buttonPanel.add(pharmacyButton);
        buttonPanel.add(appointmentButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(remindersButton);

        // Add the button panel to the frame
        frame.add(buttonPanel, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }

    // Method to create a styled button with custom font and background color
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));  // Set font size and style
        button.setBackground(new Color(0, 123, 255));  // Set background color (blue)
        button.setForeground(Color.BLACK);  // Set text color (white)
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));  // Add padding inside button
        button.setFocusPainted(false);  // Remove focus border when clicked
        return button;
    }

    public static void main(String[] args) {
        new MainWindow();  // Open the main window
    }
}
