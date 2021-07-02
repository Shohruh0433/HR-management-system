package uz.developer.hrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.developer.hrmanagementsystem.entity.User;

import javax.validation.constraints.Email;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID> {
    boolean existsByEmail(@Email String email);


    Optional<User> findByEmailAndEmailCode(@Email String email, String emailCode);


    Optional<User> findByEmail(@Email String email);
}
