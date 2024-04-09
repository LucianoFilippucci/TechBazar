package ovh.homecitadel.uni.techbazar.Helper.Exceptions;

public class AuctionException extends Exception {
    String message;

    public AuctionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
