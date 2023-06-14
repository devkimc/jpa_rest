package jparest.practice.rest.service;

import jparest.practice.rest.domain.Rest;
import jparest.practice.rest.dto.GetMostSavedRestResponse;
import jparest.practice.rest.dto.GetNewSavedRestResponse;
import jparest.practice.rest.repository.RestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Override
    public List<GetNewSavedRestResponse> getNewSavedRest() {
        List<Rest> rankRestaurants = restRepository.findTop5ByOrderByCreatedAtDesc();

        // TODO: LocalDateTime 형식의 필드를 기준으로 정렬하는 쿼리메소드를 작성 하였으나 적용이 안됨 -> 원인 찾은 후에 로직 삭제
        List<Rest> sortedRest = rankRestaurants.stream()
                .sorted(Comparator.comparing(Rest::getCreatedAt)
                        .reversed())
                .toList();

        List<GetNewSavedRestResponse> resultList = new ArrayList<>();

        for (int i = 0; i < rankRestaurants.size(); i++) {
            Rest r = sortedRest.get(i);

            GetNewSavedRestResponse result = GetNewSavedRestResponse.builder()
                    .rank(i + 1)
                    .restId(r.getId())
                    .restName(r.getRestName())
                    .savedAt(r.getCreatedAt())
                    .build();

            resultList.add(result);
        }

        return resultList;
    }
}
