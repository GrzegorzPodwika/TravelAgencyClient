package backend.model;

public class TourGuide {
    private Integer tourGuideId;

    private String name;
    private String surname;
    private String phoneNumber;

    public TourGuide() {
    }

    public TourGuide(Integer tourGuideId, String name, String surname, String phoneNumber) {
        this.tourGuideId = tourGuideId;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public Integer getTourGuideId() {
        return tourGuideId;
    }

    public void setTourGuideId(Integer tourGuideId) {
        this.tourGuideId = tourGuideId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
