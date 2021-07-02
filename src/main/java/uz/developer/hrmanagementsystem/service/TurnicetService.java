package uz.developer.hrmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.developer.hrmanagementsystem.entity.Turnicet;
import uz.developer.hrmanagementsystem.repository.TurnicetRepository;
import uz.developer.hrmanagementsystem.repository.UserRepository;
import uz.developer.hrmanagementsystem.responce.ApiResponse;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Service
public class TurnicetService {

    @Autowired
    TurnicetRepository turnicetRepository;
    @Autowired
    UserRepository userRepository;

    public ApiResponse input(UUID id){
        boolean exists = userRepository.existsById(id);
        if (!exists) return new ApiResponse("Bunday user topilmadi. Korxonaga kirolmaysiz!",false);

        Turnicet turnicet=new Turnicet();
        turnicet.setArrival(true);
        Date date = new Date();
        Timestamp ts=new Timestamp(date.getTime());
        turnicet.setDate( ts);
        turnicet.setUserId(id);
        turnicetRepository.save(turnicet);
        return new ApiResponse("Xush kelibsiz Kirishingiz mumkin!",true);

    }
    public ApiResponse output(UUID id){
        boolean exists = userRepository.existsById(id);
        if (!exists) return new ApiResponse("Bunday user topilmadi! Siz noqonuniy kiribsiz",false);

        Turnicet turnicet=new Turnicet();
        turnicet.setArrival(false);
        Date date = new Date();
        Timestamp ts=new Timestamp(date.getTime());
        turnicet.setDate( ts);
        turnicet.setUserId(id);
        turnicetRepository.save(turnicet);
        return new ApiResponse("Xayr salomat bo'ling!",true);

    }


    public ApiResponse getTurnicetById(UUID id){
     return new ApiResponse("malumotlar ",true,turnicetRepository.findAllByUserId(id));

    }


}
