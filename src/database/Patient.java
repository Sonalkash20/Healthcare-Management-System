package database;

public class Patient {
    private int id;
    private String name;
    private int age;
    private String gender;
    private String contactInfo;

    // Constructor
    public Patient(int id, String name, int age, String gender, String contactInfo) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getContactInfo() { return contactInfo; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setGender(String gender) { this.gender = gender; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }
}
