package jparest.practice.rest.controller;


import jparest.practice.rest.dto.GetMostSavedRestRequest;
import jparest.practice.rest.dto.GetNewSavedRestRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant/ranking")
public class RankingRestaurantController {

    @GetMapping("/most/save")
    public List<GetMostSavedRestRequest> getMostSavedRest() {
        return null;
    }

    @GetMapping("/new/save")
    public List<GetNewSavedRestRequest> getNewSavedRest() {
        return null;
    }


}
