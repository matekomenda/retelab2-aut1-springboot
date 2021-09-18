package hu.bme.aut.retelab2.domain;

import hu.bme.aut.retelab2.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MySchedule {

    @Autowired
    private AdRepository adRepository;

    @Scheduled(fixedDelay = 6000)
    public void removeExpiredAds(){
        LocalDateTime ltdNow = LocalDateTime.now();
        List<Ad> actualAdList = adRepository.findAll();

        for(int i = 0; i < actualAdList.size(); i++ ){
            //if(actualAdList.get(i).getExpirationDate().compareTo(ltdNow) < 0) adRepository.deleteById(actualAdList.get(i).getId());
            if(LocalDateTime.now().isAfter(actualAdList.get(i).getExpirationDate())){
                adRepository.deleteById(actualAdList.get(i).getId());
            }
        }

    }
}
