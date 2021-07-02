package uz.developer.hrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uz.developer.hrmanagementsystem.entity.Task;
import uz.developer.hrmanagementsystem.entity.User;
import uz.developer.hrmanagementsystem.payload.TaskDto;
import uz.developer.hrmanagementsystem.repository.TaskRepository;
import uz.developer.hrmanagementsystem.repository.UserRepository;
import uz.developer.hrmanagementsystem.responce.ApiResponse;
import uz.developer.hrmanagementsystem.security.JwtFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    EmailService  emailService;

    @Autowired
    JwtFilter jwtFilter;


    @PreAuthorize(value = "hasAnyRole('ROLL_DIRECTOR','ROLL_HR_MANAGER','ROLL_MANAGER')")
    public ApiResponse addTask(TaskDto taskDto,String email){

        Task task=new Task();

        task.setNewTask(true);
        task.setDescription(task.getDescription());
        task.setExpireDate(taskDto.getExpireDate());
        task.setName(task.getName());
        task.setOwnerTaskEmail(email);
        Optional<User> optionalUser = userRepository.findByEmail(taskDto.getUserEmail());
        if (!optionalUser.isPresent())
            return new ApiResponse("Bunday xodim mavjud emas",false);

        String code=UUID.randomUUID().toString();
        task.setCodeForEmail(code);
        task.setUser(optionalUser.get());
        Task saved = taskRepository.save(task);

        boolean sendEmail = emailService.sendEmailforTask(taskDto.getUserEmail(), task.getCodeForEmail(),saved.getId(),email);
        if (sendEmail)
            return new ApiResponse("Vazifa xodimga yuklatildi. Xodimning elektron manziliga yuborildi. Olganini tasdiqlashi kerak",true);
        else
            return new ApiResponse("Emailga xabar yuborishda xatolik",false);
    }



    @PreAuthorize(value = "hasAnyRole('ROLL_EMPLOYEE','ROLL_HR_MANAGER','ROLL_MANAGER')")
    public ResponseEntity<?> myTasks(String email,int page){

        Pageable pageable= PageRequest.of(page,10);
        Page<Task> allByUser_email = taskRepository.findAllByUser_Email(email, pageable);
        return ResponseEntity.ok(allByUser_email);
    }

    @PreAuthorize(value = "hasAnyRole('ROLL_HR_MANAGER','ROLL_MANAGER','ROLL_DIRECTOR')")

    public ResponseEntity<?> tasksByUserId(UUID id,int page){

        User user1 = userRepository.getById(id);


        Pageable pageable= PageRequest.of(page,10);
        Page<Task> allByUser_email = taskRepository.findAllByUser_Email(user1.getEmail(), pageable);
        return ResponseEntity.ok(allByUser_email);
    }


    @PreAuthorize(value = "hasAnyRole('ROLL_EMPLOYEE','ROLL_HR_MANAGER','ROLL_MANAGER')")
    public ResponseEntity<?> complTask(long id,String email){

        Optional<Task> optionalTask = taskRepository.findByUser_EmailAndId(email, id);
        if (!optionalTask.isPresent())
            return ResponseEntity.status(404).body("sizda bunday vazifa mavjud emas");
        Task task= optionalTask.get();
        String code=UUID.randomUUID().toString();
        task.setCodeForEmail(code);
        Task saved = taskRepository.save(task);
        boolean sendEmail = emailService.sendEmailforTaskCheck(task.getOwnerTaskEmail(), task.getCodeForEmail(),saved.getId(),task.getUser().getEmail());
        if (sendEmail)
            return ResponseEntity.ok("Vazifa tekshirishga yuborildi. Agar to'gri bo'lsa tasdiqlanadi");
        else
            return ResponseEntity.status(401).body("Emailga xabar yuborishda xatolik");
    }

}
