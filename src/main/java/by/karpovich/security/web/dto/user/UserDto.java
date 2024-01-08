package by.karpovich.security.web.dto.user;

import by.karpovich.security.web.validation.OnCreate;
import by.karpovich.security.web.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User DTO")
public class UserDto {

    @Schema(description = "User id", example = "1")
    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "User name", example = "John Doe")
    @NotNull(message = "Username must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Username length must be smaller or then 255 symbol")
    private String username;

    @Schema(description = "Email", example = "karpovich@gmail.com")
    @Length(max = 255, message = "Name length must be smaller or then 255 symbol", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @Schema(description = "User crypted password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @Schema(description = "User password confirmation")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null", groups = {OnUpdate.class})
    private String passwordConfirmation;

}
