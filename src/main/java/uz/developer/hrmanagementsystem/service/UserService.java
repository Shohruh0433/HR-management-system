package uz.developer.hrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uz.developer.hrmanagementsystem.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @PreAuthorize(value = "hasAnyRole('ROLL_HR_MANAGER','ROLL_MANAGER','ROLL_DIRECTOR')")
    public ResponseEntity<?> getAll(int page){

        Pageable pageable= PageRequest.of(page,10);
      return ResponseEntity.ok( userRepository.findAll(pageable));
    }

}
