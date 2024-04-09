package ovh.homecitadel.uni.techbazar.Helper.Exceptions;

public class DateException extends Exception{
    private String message;

    public DateException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
