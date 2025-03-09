package database;

public class Doctor {
    private int id;
    private String name;
    private String specialty;
    private String contactInfo;

    // Constructor
    public Doctor(int id, String name, String specialty, String contactInfo) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public String getContactInfo() { return contactInfo; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
}
