package jparest.practice.rest.controller;


import jparest.practice.rest.dto.GetMostSavedRestResponse;
import jparest.practice.rest.dto.GetNewSavedRestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant/ranking")
public class RankingRestaurantController {

    @GetMapping("/most/save")
    public List<GetMostSavedRestResponse> getMostSavedRest() {
        return null;
    }

    @GetMapping("/new/save")
    public List<GetNewSavedRestResponse> getNewSavedRest() {
        return null;
    }


}
