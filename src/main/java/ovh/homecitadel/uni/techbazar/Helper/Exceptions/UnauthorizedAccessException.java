package ovh.homecitadel.uni.techbazar.Helper.Exceptions;


public class UnauthorizedAccessException extends Exception {
    String message;


    public UnauthorizedAccessException(String message) {
        this.message = message;
    }

    public UnauthorizedAccessException() {

    }

    public String getMessage() {
        return this.message;
    }
}
