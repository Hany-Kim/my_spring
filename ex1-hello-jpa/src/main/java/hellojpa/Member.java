package hellojpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.util.Date;

@Entity
public class Member {

    @Id
    private Long id;

    @Column(name = "name")
    private String username;
    /*
    * unique 제약 조건을 @Column에서는 잘 사용하지 않는다.
    * 제약조건 이름이 랜덤으로 생기기 때문에 예외가 발생할 때,
    * 파악하기 번거롭다.
    *
    * unique 제약조건은 @Table의 uniqueConstraints를 사용해
    * 제약조건 이름을 직접 명명하여 사용한다.
    * */

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description; // VARCHAR보다 더 긴 문자열 사용할 때
    
    @Transient
    private int temp; // DB와 매핑하지 않고 메모리에서만 사용할 떄

    public Member() {
//    JPA는 내부적으로 리플렉션을 쓰기 때문에 동적으로 객체를 생성할 수 있어야 한다.
//    따라서 기본 생성자가 필요하다.
    }

}
