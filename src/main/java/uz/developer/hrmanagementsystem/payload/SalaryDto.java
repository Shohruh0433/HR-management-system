package uz.developer.hrmanagementsystem.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class SalaryDto {
    private UUID userId;
    private double amount;
    private int forMonth;
    private int forYear;
}
