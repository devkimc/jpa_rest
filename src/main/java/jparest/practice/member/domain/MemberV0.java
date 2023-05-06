//package jparest.practice.member.domain;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Getter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "users")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @Column(length = 100, nullable = false)
//    private String loginId;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column(length = 200, nullable = false)
//    private String email;
//
//    @Column(nullable = false, length = 50)
//    private String name;
//}
