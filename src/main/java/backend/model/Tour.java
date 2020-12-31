package backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Tour {
    private Integer tourId;

    private String tourName;
    private String country;
    private int availableTickets;
    private int takenTickets;
    private double price;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate departureDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate arrivalDate;

    private String imgName;

    private Employee employee;
    private Hotel hotel;
    private Transport transport;
    private TourGuide tourGuide;
    private Set<Attraction> attractions;
    private Set<AdditionalService> additionalServices;

    public Integer getTourId() {
        return tourId;
    }

    public void setTourId(Integer tourId) {
        this.tourId = tourId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAvailableTickets() {
        return availableTickets;
    }

    public void setAvailableTickets(int availableTickets) {
        this.availableTickets = availableTickets;
    }

    public int getTakenTickets() {
        return takenTickets;
    }

    public void setTakenTickets(int takenTickets) {
        this.takenTickets = takenTickets;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public TourGuide getTourGuide() {
        return tourGuide;
    }

    public void setTourGuide(TourGuide tourGuide) {
        this.tourGuide = tourGuide;
    }

    public Set<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(Set<Attraction> attractions) {
        this.attractions = attractions;
    }

    public Set<AdditionalService> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(Set<AdditionalService> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public void addService(AdditionalService additionalService) {
        additionalServices.add(additionalService);
    }

    public void removeService(AdditionalService additionalService) {
        additionalServices.remove(additionalService);
    }

    public void addAttraction(Attraction attraction) {
        attractions.add(attraction);
    }

    public void removeAttraction(Attraction attraction) {
        attractions.remove(attraction);
    }


    @Override
    public String toString() {
        return "Tour{" +
                "tourId=" + tourId +
                ", tourName='" + tourName + '\'' +
                ", country='" + country + '\'' +
                ", availableTickets=" + availableTickets +
                ", takenTickets=" + takenTickets +
                ", price=" + price +
                ", departureDate=" + departureDate +
                ", arrivalDate=" + arrivalDate +
                ", imgName='" + imgName + '\'' +
                ", employee=" + employee +
                ", hotel=" + hotel +
                ", transport=" + transport +
                ", tourGuide=" + tourGuide +
                '}';
    }
}
