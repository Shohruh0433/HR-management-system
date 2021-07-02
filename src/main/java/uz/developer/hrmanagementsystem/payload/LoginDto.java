package uz.developer.hrmanagementsystem.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LoginDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 2,max = 10)
    private String password;
}
