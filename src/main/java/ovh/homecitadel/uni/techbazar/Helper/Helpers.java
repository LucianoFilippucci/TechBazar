package ovh.homecitadel.uni.techbazar.Helper;

import org.keycloak.admin.client.Keycloak;
import ovh.homecitadel.uni.techbazar.Security.KeycloakSecurityUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Helpers {

    public static String GenerateUID() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmssSSS");
        String timestamp = currentTime.format(formatter);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return timestamp + uuid;
    }
}
