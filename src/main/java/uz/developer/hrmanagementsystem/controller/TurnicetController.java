package uz.developer.hrmanagementsystem.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uz.developer.hrmanagementsystem.responce.ApiResponse;
import uz.developer.hrmanagementsystem.service.TurnicetService;

import java.util.UUID;

@RestController
@RequestMapping("/api/turnicet")
public class TurnicetController {

    @Autowired
    TurnicetService turnicetService;


    @PostMapping("/in/{id}")
    public ApiResponse input(@PathVariable UUID id){
        return turnicetService.input(id);
    }


    @PostMapping("/out/{id}")
    public ApiResponse output(@PathVariable UUID id){
        return turnicetService.output(id);
    }


//userid orqali shu userning kelib ketish vaqtlari
    @GetMapping("/byUserId/{id}")
    public ApiResponse getTurnicet(@PathVariable UUID id){
        return turnicetService.getTurnicetById(id);
    }

}
