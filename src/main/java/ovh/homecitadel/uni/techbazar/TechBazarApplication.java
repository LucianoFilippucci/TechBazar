package ovh.homecitadel.uni.techbazar;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@SecurityScheme(
        name = "Keycloak",
        openIdConnectUrl = "http://localhost:8082/realms/techbazar/.well-known/openid-configuration",
        scheme = "bearer",
        type = SecuritySchemeType.OPENIDCONNECT,
        in = SecuritySchemeIn.HEADER
)
@EnableScheduling
public class TechBazarApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechBazarApplication.class, args);
    }

}
