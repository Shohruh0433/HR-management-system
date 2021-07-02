package uz.developer.hrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uz.developer.hrmanagementsystem.entity.Role;
import uz.developer.hrmanagementsystem.entity.enums.RoleEnum;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Role findAllByName(RoleEnum name);
}
