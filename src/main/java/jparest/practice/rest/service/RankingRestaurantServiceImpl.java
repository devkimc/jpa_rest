package jparest.practice.rest.service;

import jparest.practice.rest.domain.Rest;
import jparest.practice.rest.dto.GetMostSavedRestResponse;
import jparest.practice.rest.repository.RestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RankingRestaurantServiceImpl implements RankingRestaurantService  {

    private final RestRepository restRepository;

    @Override
    public List<GetMostSavedRestResponse> getMostSavedRest() {
       List<Rest> rankRestaurants = restRepository.findTop5ByOrderByTotalFavoriteDesc();

        List<GetMostSavedRestResponse> resultList = new ArrayList<>();

        for (int i = 0; i < rankRestaurants.size(); i++) {
            Rest r = rankRestaurants.get(i);

            System.out.println("r.getTotalFavorite() = " + r.getTotalFavorite());

            GetMostSavedRestResponse result = GetMostSavedRestResponse.builder()
                    .rank(i + 1)
                    .restId(r.getId())
                    .restName(r.getRestName())
                    .totalFavorite(r.getTotalFavorite())
                    .build();

            resultList.add(result);
        }

        return resultList;
    }

//    @Override
//    public List<GetNewSavedRestResponse> getNewSavedRest() {
//        List<Rest> rankRestaurants = restRepository.findTop5OrderByCreatedAtDesc().get();
//
//        System.out.println("rankRests = " + rankRestaurants);
//
//        List<GetNewSavedRestResponse> resultList = new ArrayList<>();
//
//        for (int i = 0; i < rankRestaurants.size(); i++) {
//            Rest r = rankRestaurants.get(i);
//
//            GetNewSavedRestResponse result = GetNewSavedRestResponse.builder()
//                    .rank(i)
//                    .restId(r.getId())
//                    .restName(r.getRestName())
//                    .savedAt(r.getCreatedAt())
//                    .build();
//
//            resultList.add(result);
//        }
//
//        return resultList;
//    }
}
