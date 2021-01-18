package utils;

public abstract class TravelUtils {

    public static String roundOffToStr(double value) {
        return String.valueOf(roundOff(value));
    }

    public static Double roundOff(double value) {
        return Math.round(100 * value) / 100.0;
    }

    public static Double roundOffStrToDouble(String valueAsStr) {
        var value = Double.parseDouble(valueAsStr);
        value = Math.round(100 * value) / 100.0;
        return value;
    }


}
