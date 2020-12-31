package backend.model;

public class Employee {
    private Integer personId;

    private String nick;
    private String password;
    private String name;
    private String surname;
    private int age;
    private String address;
    private String city;
    private String zipcode;
    private String phoneNumber;
    private String email;

    private double numberOfWorkedHours;
    private double hourlyRate;
    private double bonus;

    public Employee() {
    }

    public Employee(Integer personId, String nick, String password, String name, String surname, int age, String address, String city, String zipcode, String phoneNumber, String email, double numberOfWorkedHours, double hourlyRate, double bonus) {
        this.personId = personId;
        this.nick = nick;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.numberOfWorkedHours = numberOfWorkedHours;
        this.hourlyRate = hourlyRate;
        this.bonus = bonus;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getNumberOfWorkedHours() {
        return numberOfWorkedHours;
    }

    public void setNumberOfWorkedHours(double numberOfWorkedHours) {
        this.numberOfWorkedHours = numberOfWorkedHours;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
}
