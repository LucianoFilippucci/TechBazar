package ovh.homecitadel.uni.techbazar.Helper.Exceptions;

public class NotNullException extends Exception {
    private String message;

    public NotNullException(String message) {
        this.message = message;
    }

    public NotNullException() {}

    @Override
    public String getMessage() {
        return message;
    }
}
