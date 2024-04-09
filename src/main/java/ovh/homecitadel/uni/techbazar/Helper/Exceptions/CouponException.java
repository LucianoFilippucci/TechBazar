package ovh.homecitadel.uni.techbazar.Helper.Exceptions;

public class CouponException extends Exception {
    private final String message;

    public CouponException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
