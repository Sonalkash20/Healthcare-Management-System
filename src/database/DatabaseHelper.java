package database;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class DatabaseHelper {

    // --- CRUD for Patients ---
    
    // Add Patient
    public static void addPatient(String name, int age, String gender, String contactInfo) {
        String query = "INSERT INTO patients (name, age, gender, contact_info) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, gender);
            statement.setString(4, contactInfo);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Patient added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding patient.");
        }
    }

    // Get all Patients
    public static ArrayList<Patient> getPatients() {
        ArrayList<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String contactInfo = resultSet.getString("contact_info");

                patients.add(new Patient(id, name, age, gender, contactInfo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // Update Patient
    public static void updatePatient(int id, String name, int age, String gender, String contactInfo) {
        String query = "UPDATE patients SET name = ?, age = ?, gender = ?, contact_info = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, gender);
            statement.setString(4, contactInfo);
            statement.setInt(5, id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Patient updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating patient.");
        }
    }

    // Delete Patient
    public static void deletePatient(int id) {
        String query = "DELETE FROM patients WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Patient deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting patient.");
        }
    }

    // --- CRUD for Doctors ---
    
    // Add Doctor
    public static void addDoctor(String name, String specialty, String contactInfo) {
        String query = "INSERT INTO doctors (name, specialty, contact_info) VALUES (?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, specialty);
            statement.setString(3, contactInfo);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding doctor.");
        }
    }

    // Get all Doctors
    public static ArrayList<Doctor> getDoctors() {
        ArrayList<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctors";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialty = resultSet.getString("specialty");
                String contactInfo = resultSet.getString("contact_info");

                doctors.add(new Doctor(id, name, specialty, contactInfo));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Update Doctor
    public static void updateDoctor(int id, String name, String specialty, String contactInfo) {
        String query = "UPDATE doctors SET name = ?, specialty = ?, contact_info = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, specialty);
            statement.setString(3, contactInfo);
            statement.setInt(4, id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating doctor.");
        }
    }

    // Delete Doctor
    public static void deleteDoctor(int id) {
        String query = "DELETE FROM doctors WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Doctor deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting doctor.");
        }
    }

    // --- CRUD for Pharmacy (Example, same as Patient/Doctor CRUD) ---

    // Add Medicine
    public static void addMedicine(String name, String description, double price) {
        String query = "INSERT INTO pharmacy (name, description, price) VALUES (?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Medicine added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding medicine.");
        }
    }

    // Get all Medicines
    public static ArrayList<Medicine> getMedicines() {
        ArrayList<Medicine> medicines = new ArrayList<>();
        String query = "SELECT * FROM pharmacy";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");

                medicines.add(new Medicine(id, name, description, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicines;
    }

    // Update Medicine
    public static void updateMedicine(int id, String name, String description, double price) {
        String query = "UPDATE pharmacy SET name = ?, description = ?, price = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDouble(3, price);
            statement.setInt(4, id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Medicine updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating medicine.");
        }
    }

    // Delete Medicine
    public static void deleteMedicine(int id) {
        String query = "DELETE FROM pharmacy WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Medicine deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting medicine.");
        }
    }
    
    

    // --- CRUD for Appointments ---

    // Add Appointment
    public static void addAppointment(int patientId, int doctorId, String date, String time) {
        String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, patientId);
            statement.setInt(2, doctorId);
            statement.setString(3, date);
            statement.setString(4, time);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Appointment added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding appointment.");
        }
    }

    // Get all Appointments
    public static ArrayList<Appointment> getAppointments() {
        ArrayList<Appointment> appointments = new ArrayList<>();
        String query = "SELECT * FROM appointments";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int patientId = resultSet.getInt("patient_id");
                int doctorId = resultSet.getInt("doctor_id");
                String date = resultSet.getString("appointment_date");
                String time = resultSet.getString("appointment_time");

                appointments.add(new Appointment(id, patientId, doctorId, date, time));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    // Update Appointment
    public static void updateAppointment(int id, int patientId, int doctorId, String date, String time) {
        String query = "UPDATE appointments SET patient_id = ?, doctor_id = ?, appointment_date = ?, appointment_time = ? WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, patientId);
            statement.setInt(2, doctorId);
            statement.setString(3, date);
            statement.setString(4, time);
            statement.setInt(5, id);

            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Appointment updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating appointment.");
        }
    }

    // Delete Appointment
    public static void deleteAppointment(int id) {
        String query = "DELETE FROM appointments WHERE id = ?";
        
        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Appointment deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting appointment.");
        }
    }
}
