package uz.developer.hrmanagementsystem.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.developer.hrmanagementsystem.payload.SalaryDto;
import uz.developer.hrmanagementsystem.responce.ApiResponse;
import uz.developer.hrmanagementsystem.service.SalaryService;

import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    SalaryService salaryService;

    @PostMapping("/give")
    public ApiResponse giveSalary(@RequestBody SalaryDto salaryDto){
    return  salaryService.give(salaryDto);
    }

    //user id orqali shu userning oyliklari
    @GetMapping("/byUserId/{id}")
    public ApiResponse getById(@PathVariable UUID id){
        return salaryService.getByUserId(id);
    }

    //oy va yil orqali oyliklar haqida ma'lumot
    @GetMapping("/byMonthAndYeat")
    public ApiResponse getById(@RequestParam int month, @RequestBody int year){
        return salaryService.getByMonthAndYear(month,year);
    }


}
