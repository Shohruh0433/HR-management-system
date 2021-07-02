package uz.developer.hrmanagementsystem.myconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/register/manager","/register/hrManager/*","/api/user/**").hasRole("ROLL_DIRECTOR")
                .antMatchers("/api/auth/register/employee","/api/salary/**","/api/task/**","/api/turnicet/byUserId/*").hasAnyRole("ROLL_HR_MANAGER","ROLL_DIRECTOR")

                .antMatchers("/api/task/myTasks","/api/task/myTasks/**","/api/turnicet/in/*","/api/turnicet/out/*").hasRole("ROLL_EMPLOYEE")


                .antMatchers("/api/auth/verifyEmail","/api/auth/login","/api/task/verifyEmailforTask").permitAll()

                .anyRequest().authenticated();


    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("*************************");//kiritiladi
        javaMailSender.setPassword("***************************");//kiritiladi
        Properties properties=javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol","smtp");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.debug","true");
        return javaMailSender;
    }

    //bu mening parol va loginlarimni solishtirish uchun
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
