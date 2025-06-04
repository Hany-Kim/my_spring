package hello.hellospring.domain;

import javax.persistence.*;

/*
@Entity : JPA가 관리하는 Entity
 */

@Entity
public class Member {

    /*
    GenerationType.IDENTITY : DB가 알아서 ID 생성
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
