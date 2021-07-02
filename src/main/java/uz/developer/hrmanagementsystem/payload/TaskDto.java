package uz.developer.hrmanagementsystem.payload;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class TaskDto {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Timestamp expireDate;
    @NotNull
    private String userEmail;





}
