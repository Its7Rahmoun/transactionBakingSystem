package org.sid.ebankingbackend.ClinetsOpenFeign;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.sid.ebankingbackend.dtos.requestpredected;
import org.sid.ebankingbackend.dtos.saveUserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("MLSERVICE")
public interface MlClient {


    @RequestMapping(method = RequestMethod.POST, value = "/informationVerfier", consumes = "application/json")
    @CircuitBreaker(name = "machineleraningservice",fallbackMethod = "test")
    requestpredected update(@RequestBody saveUserRequest user);

    default requestpredected test( saveUserRequest user,Exception e){
        return requestpredected.builder().predected("not availabele").build();
    }
}
