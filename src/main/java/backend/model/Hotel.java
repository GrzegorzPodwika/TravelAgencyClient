package backend.model;

public class Hotel {
    private Integer hotelId;

    private String hotelName;
    private int numberOfStars;
    private String address;
    private String zipcode;
    private String city;
    private String country;

    public Hotel() {
    }

    public Hotel(Integer hotelId, String hotelName, int numberOfStars, String address, String zipcode, String city, String country) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.numberOfStars = numberOfStars;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.country = country;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(int numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
