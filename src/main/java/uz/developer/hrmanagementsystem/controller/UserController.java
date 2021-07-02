package uz.developer.hrmanagementsystem.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.developer.hrmanagementsystem.service.TaskService;
import uz.developer.hrmanagementsystem.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    //barcha xodimlar
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam int page){
        return userService.getAll(page);
    }

    //shu id ga ega xodimning barcha tasklari
    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getTasksbyUserId(@PathVariable UUID id, @RequestParam int page){
        return taskService.tasksByUserId(id, page);
    }

}
