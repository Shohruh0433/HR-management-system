package uz.developer.hrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.developer.hrmanagementsystem.payload.LoginDto;
import uz.developer.hrmanagementsystem.payload.RegisterDto;
import uz.developer.hrmanagementsystem.responce.ApiResponse;
import uz.developer.hrmanagementsystem.service.AuthService;
import uz.developer.hrmanagementsystem.service.EmailService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @Autowired
    EmailService emailService;

    //Hr manager tayinlash . Bu faqat directorga ruhsat
    @PostMapping("/register/hrManager/{id}")
    public ApiResponse registerHrManager(@PathVariable UUID id){
        return authService.hrManager(id);
    }

    //manager qo'shish . Bu faqat directorga ruhsat
    @PostMapping("/register/manager")
    public ResponseEntity<?> registerManager(@Valid @RequestBody RegisterDto registerDto,HttpServletRequest httpServletRequest){

        ApiResponse register = authService.registerManager(registerDto,httpServletRequest);
        return ResponseEntity.status(register.isSuccess()?201:409).body(register);
    }

    //Xodim qo'shish. Bu faqat directorga va Hr managerga ruhsat
    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmploye(@Valid @RequestBody RegisterDto registerDto, HttpServletRequest httpServletRequest
                                             ){

        ApiResponse register = authService.registerEmployee(registerDto,httpServletRequest);
        return ResponseEntity.status(register.isSuccess()?201:409).body(register);
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verify(@RequestParam String emailCode, @RequestParam String email){
        ApiResponse apiResponse = emailService.verifyEmail(emailCode, email);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authService.login(loginDto));
    }



}
