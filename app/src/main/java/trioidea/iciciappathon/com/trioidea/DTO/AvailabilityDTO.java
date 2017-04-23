package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 22/04/2017.
 */
public class AvailabilityDTO {
    String AvailabilityType;
    String MinimumHours;
    String MaximumHours;

    public String getAvailabilityType() {
        return AvailabilityType;
    }

    public void setAvailabilityType(String availabilityType) {
        AvailabilityType = availabilityType;
    }

    public String getMinimumHours() {
        return MinimumHours;
    }

    public void setMinimumHours(String minimumHours) {
        MinimumHours = minimumHours;
    }

    public String getMaximumHours() {
        return MaximumHours;
    }

    public void setMaximumHours(String maximumHours) {
        MaximumHours = maximumHours;
    }
}
