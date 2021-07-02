package uz.developer.hrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uz.developer.hrmanagementsystem.entity.Salary;
import uz.developer.hrmanagementsystem.payload.SalaryDto;
import uz.developer.hrmanagementsystem.repository.SalaryRepository;
import uz.developer.hrmanagementsystem.repository.UserRepository;
import uz.developer.hrmanagementsystem.responce.ApiResponse;

import java.util.UUID;

@Service
public class SalaryService {

    @Autowired
    SalaryRepository salaryRepository;

    @Autowired
    UserRepository userRepository;

    @PreAuthorize(value = "hasAnyRole('ROLL_DIRECTOR','ROLL_HR_MANAGER')")
    public ApiResponse give(SalaryDto salaryDto){
        boolean b = userRepository.existsById(salaryDto.getUserId());
        if (!b) return  new ApiResponse("Bunday xodim topilmadi", false);

        Salary salary=new Salary();

        salary.setUserId(salaryDto.getUserId());
        salary.setAmount(salaryDto.getAmount());
        salary.setForMonth(salaryDto.getForMonth());
        salary.setForYear(salary.getForYear());
        salaryRepository.save(salary);
        return  new ApiResponse("xodimga oylik muvaaffaqiyatli belgilandi", true);

    }
    @PreAuthorize(value = "hasAnyRole('ROLL_DIRECTOR','ROLL_HR_MANAGER')")
    public ApiResponse getByMonthAndYear(int month, int year){
        return new ApiResponse("malumot ",true,salaryRepository.findAllByForMonthAndForYear(month, year));
    }

    @PreAuthorize(value = "hasAnyRole('ROLL_DIRECTOR','ROLL_HR_MANAGER')")
    public ApiResponse getByUserId(UUID id){
        return new ApiResponse("malumot ",true,salaryRepository.findAllByUserId(id));

    }


}
