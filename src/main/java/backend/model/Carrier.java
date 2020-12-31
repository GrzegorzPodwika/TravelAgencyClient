package backend.model;

public class Carrier {
    private Integer carrierId;

    private String name;
    private String phoneNumber;
    private String email;

    public Carrier() {
    }

    public Carrier(Integer carrierId, String name, String phoneNumber, String email) {
        this.carrierId = carrierId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public Integer getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Integer carrierId) {
        this.carrierId = carrierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
