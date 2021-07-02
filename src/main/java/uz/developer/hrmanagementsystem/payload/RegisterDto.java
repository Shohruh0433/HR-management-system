package uz.developer.hrmanagementsystem.payload;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {

    @NotNull
    @Size(min = 4,max = 30)
    private String firstName;

    @NotNull
    @Size(min = 4,max = 30)
    private String lastName;

    @Email
    private String email;
    @NotNull
    private String password;

}
