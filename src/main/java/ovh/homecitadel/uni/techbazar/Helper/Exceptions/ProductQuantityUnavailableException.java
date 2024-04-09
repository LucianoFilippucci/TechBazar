package ovh.homecitadel.uni.techbazar.Helper.Exceptions;

public class ProductQuantityUnavailableException extends Exception {
    private final String message;

    public ProductQuantityUnavailableException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
