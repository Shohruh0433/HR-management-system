package uz.developer.hrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import uz.developer.hrmanagementsystem.entity.User;
import uz.developer.hrmanagementsystem.entity.enums.RoleEnum;
import uz.developer.hrmanagementsystem.payload.LoginDto;
import uz.developer.hrmanagementsystem.payload.RegisterDto;
import uz.developer.hrmanagementsystem.repository.RoleRepository;
import uz.developer.hrmanagementsystem.repository.UserRepository;
import uz.developer.hrmanagementsystem.responce.ApiResponse;
import uz.developer.hrmanagementsystem.security.JwtFilter;
import uz.developer.hrmanagementsystem.security.JwtProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtFilter jwtFilter;





    public ApiResponse registerManager(RegisterDto registerDto,HttpServletRequest httpServletRequest){
        String directorEmail = jwtFilter.getEmail(httpServletRequest);
        User user=new User();
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) return new ApiResponse("Bu email avval ro'yxatdan o'tgan",false);

        user.setEmail(registerDto.getEmail());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findAllByName(RoleEnum.ROLL_MANAGER)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);
        boolean sendEmail = emailService.sendEmail(registerDto.getEmail(), user.getEmailCode(),directorEmail);
        if (sendEmail)
        return new ApiResponse("Muvaffaqiyatli ro'yxatdan o'tdingiz. Akkountingizni aktivlashtirish uchun emailingizni tasdiqlang",true);
        else
            return new ApiResponse("Emailga xabar yuborishda xatolik",false);

    }

    public ApiResponse registerEmployee(RegisterDto registerDto, HttpServletRequest httpServletRequest
                                        ){

        String managerEmail = jwtFilter.getEmail(httpServletRequest);
        User user=new User();
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) return new ApiResponse("Bu email avval ro'yxatdan o'ttgan",false);

        user.setEmail(registerDto.getEmail());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findAllByName(RoleEnum.ROLL_EMPLOYEE)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);
        boolean sendEmail = emailService.sendEmail(registerDto.getEmail(), user.getEmailCode(),managerEmail);
        if (sendEmail)
            return new ApiResponse("Muvaffaqiyatli ro'yxatdan o'tdingiz. Akkountingizni aktivlashtirish uchun emailingizni tasdiqlang",true);
        else
            return new ApiResponse("Emailga xabar yuborishda xatolik",false);

    }

public ApiResponse hrManager(UUID id){
    Optional<User> userOptional = userRepository.findById(id);
    if (!userOptional.isPresent())
        return new ApiResponse("Bunday xodim mavjud emas",false);
    User user= userOptional.get();
    user.setRoles(Collections.singleton(roleRepository.findAllByName(RoleEnum.ROLL_HR_MANAGER)));
    userRepository.save(user);
    return new ApiResponse("Muvaffaqiyatli Hr manager tayinlandi",true);

}


    public ApiResponse login(LoginDto loginDto){
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()

            ));

            User user=(User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(user.getEmail(), user.getRoles());
            return new ApiResponse(token,true);

        }catch (BadCredentialsException e){
            return new ApiResponse("parol yoki login xato",false);
        }
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isPresent())
            return optionalUser.get();
        throw new UsernameNotFoundException(username+" topilmadi");
    }
}
