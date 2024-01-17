package shopbaby.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import shopbaby.models.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;
}
