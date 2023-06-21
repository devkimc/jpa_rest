package jparest.practice.rest.domain;

import jparest.practice.common.util.TimeBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rest extends TimeBaseEntity implements Persistable<String> {

    @Id
    @Column(name = "rest_id", nullable = false, length = 16)
    private String id;

    @Column(nullable = false, length = 30)
    private String restName;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private int totalFavorite;

    @Builder.Default
    @OneToMany(mappedBy = "rest")
    private List<GroupRest> groupRests = new ArrayList<GroupRest>();

    public void setTotalFavorite(int totalFavorite) {
        this.totalFavorite = totalFavorite;
    }

    public void increaseTotalFavorite() {
        setTotalFavorite(this.totalFavorite + 1);
    }

    public void decreaseTotalFavorite() {
        setTotalFavorite(this.totalFavorite - 1);
    }

    @Override
    public boolean isNew() {
        return super.getCreatedAt() == null;
    }
}
