package uz.developer.hrmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> add(@RequestBody TaskDto taskDto, HttpServletRequest httpRequest
                                 ){

        ApiResponse apiResponse = taskService.addTask(taskDto,httpRequest);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse.getMessage());

    }

    @GetMapping("/verifyEmailforTask")
    public HttpEntity<?> verify(@RequestParam String emailCode, @RequestParam String email,@RequestParam long id){
        ApiResponse apiResponse = emailService.verifyEmailforTask(emailCode, email,id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);

    }


//Bu faqat xodimlar uchun
    @GetMapping("/myTasks")
    public ResponseEntity<?> getTasks(HttpServletRequest httpServletRequest,@RequestParam int page){
        return taskService.myTasks(httpServletRequest, page);
    }

    //Vazifani bajarilganini tekshirishga jo'natish.
    // Bunda vazifa bergan odamni email ga xabar boradi agar u tasdiqlasa muvaffaqiyatli bajarailgan bo'ladi

    @PostMapping("/myTasks/completed/{id}")
    public ResponseEntity<?> taskCompleted(@PathVariable long id,HttpServletRequest httpServletRequest){
        return taskService.complTask(id, httpServletRequest);
    }


    @GetMapping("/verifyEmailforTaskChecked")
    public HttpEntity<?> verifyEmailforTaskChecked(@RequestParam String emailCode, @RequestParam String email,@RequestParam long id){
        ApiResponse apiResponse = emailService.verifyEmailforTaskCheck(emailCode, email,id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);

    }

}
