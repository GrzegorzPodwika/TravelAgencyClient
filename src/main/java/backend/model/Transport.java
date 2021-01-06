package backend.model;

public class Transport {
    private Integer transportId;

    private String transportType;
    private double journeyTime;
    private String journeyDescription;

    private Carrier carrier;

    public Transport() {
    }

    public Transport(Integer transportId, String transportType, double journeyTime, String journeyDescription, Carrier carrier) {
        this.transportId = transportId;
        this.transportType = transportType;
        this.journeyTime = journeyTime;
        this.journeyDescription = journeyDescription;
        this.carrier = carrier;
    }

    public Integer getTransportId() {
        return transportId;
    }

    public void setTransportId(Integer transportId) {
        this.transportId = transportId;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    public double getJourneyTime() {
        return journeyTime;
    }

    public void setJourneyTime(double journeyTime) {
        this.journeyTime = journeyTime;
    }

    public String getJourneyDescription() {
        return journeyDescription;
    }

    public void setJourneyDescription(String journeyDescription) {
        this.journeyDescription = journeyDescription;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }
}
