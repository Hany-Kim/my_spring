package hellojpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.util.Date;

@Entity
@SequenceGenerator(
        name = "member_seq_generator",
        sequenceName = "member_seq")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
    private Long id;
    /*
     * @Id : 직접 id 지정
     * @GeneratedValue(strategy = GenerationType.AUTO) : DB방언에 따라 자동으로 Id 할당
     * @GeneratedValue(strategy = GenerationType.IDENTITY) : AUTO_INCREMENT, em.persist()시점에 즉시 INSERT SQL실행하고 식별자 ID 조회. 그전 까지 ID 모름
     * @GeneratedValue(strategy = GenerationType.SEQUENCE) : 주로 Oracle에서 사용. Sequence 오브젝트를 생성한 뒤 Sequence 오브젝트에서 ID값을 조회해 INSERT SQL 실행. Sequence 오브젝트에서 ID 관리
     * */

    @Column(name = "name")
    private String username;

    public Member() {
//    JPA는 내부적으로 리플렉션을 쓰기 때문에 동적으로 객체를 생성할 수 있어야 한다.
//    따라서 기본 생성자가 필요하다.
    }

}
