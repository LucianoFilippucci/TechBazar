package ovh.homecitadel.uni.techbazar.Helper.Exceptions;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends Exception {
    private final String message;

    public ObjectNotFoundException(String message) {
        this.message = message;
    }
}
