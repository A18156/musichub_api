package musichub.demo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.Arrays;

public enum ERole {
    ROLE_ADMIN("admin"),
    ROLE_MANAGER("mod"),
    ROLE_USER("user");

    private final String value;

    ERole(String value) {
        this.value = value;
    }

    public static ERole of(String role) {
        if (!StringUtils.hasText(role)) {
            return ROLE_USER;
        }
        return Arrays.stream(values())
                .filter(i -> i.value.equalsIgnoreCase(role))
                .findFirst()
                .orElse(ROLE_USER);
    }

    public String getValue() {
        return this.value;
    }
}
