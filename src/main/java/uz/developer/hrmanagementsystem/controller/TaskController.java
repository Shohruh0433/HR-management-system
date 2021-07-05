package uz.developer.hrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uz.developer.hrmanagementsystem.entity.User;
import uz.developer.hrmanagementsystem.payload.TaskDto;
import uz.developer.hrmanagementsystem.responce.ApiResponse;
import uz.developer.hrmanagementsystem.service.EmailService;
import uz.developer.hrmanagementsystem.service.TaskService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    EmailService emailService;


    //Vazifa berish. Buni faqat director va managerlar qila oladi
    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody TaskDto taskDto
                                 ){

        ApiResponse apiResponse = taskService.addTask(taskDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse.getMessage());

    }

    //bu methodni vazifasi employe ni emailiga xabar borganda  tasdiqlasa  task bajarish rejimi true boladi
    @GetMapping("/verifyEmailforTask")
    public HttpEntity<?> verify(@RequestParam String emailCode, @RequestParam String employeEmail,@RequestParam long id){
        ApiResponse apiResponse = emailService.verifyEmailforTask(emailCode, employeEmail,id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);

    }


//Bu faqat xodimlar uchun
    @GetMapping("/myTasks")
    public ResponseEntity<?> getTasks(@RequestParam int page){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return taskService.myTasks( user.getEmail(),page);
    }

    //Vazifani bajarilganini tekshirishga jo'natish.
    // Bunda vazifa bergan odamni email ga xabar boradi agar u tasdiqlasa muvaffaqiyatli bajarailgan bo'ladi

    @PostMapping("/myTasks/completed/{id}")
    public ResponseEntity<?> taskCompleted(@PathVariable long id){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return taskService.complTask(id, user.getEmail());
    }



    // task ning egasiga task yechimi to'g'riligi to'g'risida xabar boradi agar u tasdiqlasa
    // shu yo'lga keladi va task comleted true bo'ladi
    @GetMapping("/verifyEmailforTaskChecked")
    public HttpEntity<?> verifyEmailforTaskChecked(@RequestParam long id){
        ApiResponse apiResponse = emailService.verifyEmailforTaskCheck(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);

    }

}
