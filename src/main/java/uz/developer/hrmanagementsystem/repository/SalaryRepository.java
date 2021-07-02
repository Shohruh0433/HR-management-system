package uz.developer.hrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.developer.hrmanagementsystem.entity.Salary;

import java.util.List;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<Salary,Long> {


    List<Salary> findAllByForMonthAndForYear(int forMonth, int forYear);

    List<Salary> findAllByUserId(UUID userId);

}
