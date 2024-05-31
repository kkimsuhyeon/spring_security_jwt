package demo.spring_security_jwt.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestLogin {

    @NotEmpty
    private String id;
}
