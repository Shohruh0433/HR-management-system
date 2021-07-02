package uz.developer.hrmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.developer.hrmanagementsystem.entity.Turnicet;

import java.util.List;
import java.util.UUID;

public interface TurnicetRepository extends JpaRepository<Turnicet,Long> {


    List<Turnicet> findAllByUserId(UUID userId);
}
