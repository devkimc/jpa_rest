package jparest.practice.rest.repository;

import jparest.practice.rest.domain.Rest;
import jparest.practice.rest.dto.GetFavRestListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestRepository extends JpaRepository<Rest, String> {

    @Query("SELECT " +
            "NEW jparest.practice.rest.dto.GetFavRestListResponse( " +
            "r.id, r.restName, r.latitude, r.longitude ) " +
            "FROM Rest r JOIN r.groupRests gr WHERE gr.group.id = :groupId")
    Page<GetFavRestListResponse> findAllByGroupId(@Param("groupId") Long groupId, Pageable pageable);

    List<Rest> findTop5ByOrderByCreatedAtDesc();

    List<Rest> findTop5ByOrderByTotalFavoriteDesc();
}
