package uz.developer.hrmanagementsystem.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.developer.hrmanagementsystem.entity.Task;

import javax.validation.constraints.Email;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {

    Page<Task> findAllByUser_Email(@Email String user_email, Pageable pageable);


    Optional<Task> findByUser_EmailAndId(@Email String user_email, long id);

    Optional<Task> findByIdAndCodeForEmailAndUser_Email(long id, String codeForEmail, @Email String user_email);
}
