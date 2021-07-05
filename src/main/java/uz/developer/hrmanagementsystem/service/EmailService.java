package uz.developer.hrmanagementsystem.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uz.developer.hrmanagementsystem.entity.Task;
import uz.developer.hrmanagementsystem.entity.User;
import uz.developer.hrmanagementsystem.repository.TaskRepository;
import uz.developer.hrmanagementsystem.repository.UserRepository;
import uz.developer.hrmanagementsystem.responce.ApiResponse;

import java.util.Optional;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;
    public boolean sendEmail(String sendingEmail,String emailCode,String fromEmail){
        try {
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("Akkountni tasdiqlash");
            simpleMailMessage.setText("<a href='http://10.100.201.234:8080/api/auth/verifyEmail" +
                    "?emailCode="+emailCode+"&email="+sendingEmail+"'>Tasdiqlang</a>");
            javaMailSender.send(simpleMailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }




    public ApiResponse verifyEmail(String emailCode, String email){
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()){
            User user= optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Aktivatsiya tasdiqlandi",true);


        }
        return new ApiResponse("Tasdiqlanmadi",false);

    }





    public boolean sendEmailforTask(String sendingEmail,String emailCode,Long id,String fromEmail){
        try {
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("Vazifani qabul qilganingizni tasdiqlash");
            simpleMailMessage.setText("<a href='http://10.100.201.234:8080/api/task/verifyEmailforTask" +
                    "?emailCode="+emailCode+"&employeEmail="+sendingEmail+"&id="+id+"'>Tasdiqlang</a>");
            javaMailSender.send(simpleMailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ApiResponse verifyEmailforTask(String emailCode, String employeEmail,long id){

        Optional<Task> optionalTask = taskRepository.findByIdAndCodeForEmailAndUser_Email(id,emailCode,employeEmail);
        if (optionalTask.isPresent()){
            Task task= optionalTask.get();
            task.setDoingTask(true);
            task.setCodeForEmail(null);
            taskRepository.save(task);
            return new ApiResponse("vazifa tasdiqlandi, Va bajarish rejimiga o'tdi vaqt ketdi",true);
        }

        return new ApiResponse("Tasdiqlanmadi",false);




    }


//task bergan odamni emailiga task to'g'rimi tasdiqlash  xabar
    public boolean sendEmailforTaskCheck(String ownerEmail,String emailCode,Long id,String employeEmail){
        try {
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setFrom(employeEmail);
            simpleMailMessage.setTo(ownerEmail);
            simpleMailMessage.setSubject("vazifa to'griligini tasdiqlash");
            simpleMailMessage.setText("<a href='http://10.100.201.234:8080/api/task/verifyEmailforTaskChecked" +
                    "?id="+id+"'>Tasdiqlang</a>");
            javaMailSender.send(simpleMailMessage);
            return true;
        }catch (Exception e){
            return false;
        }
    }




    @PreAuthorize(value = "hasAnyRole('ROLL_HR_MANAGER','ROLL_MANAGER','ROLL_DIRECTOR')")
    public ApiResponse verifyEmailforTaskCheck(long id){

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()){
            Task task= optionalTask.get();
            task.setDoingTask(false);
            task.setCodeForEmail(null);
            task.setCompletedTask(true);
            taskRepository.save(task);
            return new ApiResponse("vazifa muvaffaqiyatli bajarildi tasdiqlandi",true);
        }

        return new ApiResponse("Tasdiqlanmadi",false);




    }
}
