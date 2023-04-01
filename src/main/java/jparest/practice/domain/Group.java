package jparest.practice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Group {

    private Long id;
    private Member member;
}
