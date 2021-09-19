package hu.bme.aut.retelab2.controller;

import hu.bme.aut.retelab2.domain.Ad;
import hu.bme.aut.retelab2.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/ads")
public class AdController {

    @Autowired
    private AdRepository adRepository;

    @GetMapping
    public ResponseEntity<List<Ad>> getAll(@RequestParam(required = false,defaultValue = "0") int min, @RequestParam(required = false,defaultValue = "10000000")int max){
        List<Ad> modifiedResponse = new ArrayList<>();
        if(min == 0 && max == 10000000){
            modifiedResponse = adRepository.findAll();
            for(int i =0; i < modifiedResponse.size(); i++){
                modifiedResponse.get(i).setSecretToken("0");
            }
        } else {
           modifiedResponse = adRepository.findPriceMinMax(min,max);
           for(int i =0; i < modifiedResponse.size(); i++){
               modifiedResponse.get(i).setSecretToken("0");
           }
        }
        return new ResponseEntity<List<Ad>>(modifiedResponse,HttpStatus.OK);
    }


    @GetMapping("{tag}")
    public ResponseEntity<List<Ad>> getByTags(@PathVariable String tag){
        List<Ad> response = adRepository.findByTag(tag);
        for(int i = 0;  i < response.size(); i++){
            response.get(i).setSecretToken("0");
        }
        return new ResponseEntity<List<Ad>>(response,HttpStatus.OK);
        //return new ResponseEntity<List<Ad>>(adRepository.findByTag(tag),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Ad> create(@RequestBody Ad ad){
        ad.setId(null);
        ad.setCreatedAt(new Date());
        ad.setSecretToken(ad.generate());


        return new ResponseEntity<Ad>(adRepository.save(ad),HttpStatus.OK);
    }

    /*@PutMapping
    public ResponseEntity<Ad> update(@RequestBody Ad ad){
        //Ad newAd = new Ad();
        Ad currentAd = adRepository.findById(ad.getId());
        if(currentAd == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if(currentAd.getSecretToken().equals(ad.getSecretToken())){
            currentAd.setCreatedAt(currentAd.getCreatedAt());
            currentAd = adRepository.save(ad);
            return new ResponseEntity<Ad>(currentAd,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
        }
    }*/
    @PutMapping
    public ResponseEntity<Ad> update(@RequestBody Ad ad){
        Ad newAd = new Ad();
        Ad currentAd = adRepository.findById(ad.getId());
        if(currentAd == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if(currentAd.getSecretToken().equals(ad.getSecretToken())){
            newAd.setId(ad.getId());
            newAd.setAddress(ad.getAddress());
            newAd.setDescription(ad.getDescription());
            newAd.setPrice(ad.getPrice());
            newAd.setCreatedAt(currentAd.getCreatedAt());
            newAd.setSecretToken(ad.getSecretToken());
            newAd.setTags(ad.getTags());
            newAd.setExpirationDate(ad.getExpirationDate());
            return new ResponseEntity<>(adRepository.save(newAd),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
        }
    }

}
