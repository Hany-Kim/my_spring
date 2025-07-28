package hellojpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Child> childList = new ArrayList<>();
    /*
    * 영속성 전이: CASCADE - 주의
    * - 영속성 전이는 연관관계를 매핑하는 것과 아무 관련이 없음
    * - 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화하는 편리함을 제공할 뿐
    * - 항상 사용해야 한다는 것은 아님 - 하나의 부모가 자식을 관리할 때 사용할 의미가 있다.
    *   ex. 게시판 - 하나의 게시판에서만 활용되는 첨부파일 데이터 -> 사용O
    *   ex. 게시판 - 여러 엔티티가 관리하는 첨부파일 데이터 -> 사용X
    *
    * 종류
    * - ALL : 모두 적용 (라이프 싸이클 다 맞춰야 할때)
    * - PERSIST : 영속 (저장할 때만 라이프 싸이클 맞춰야 할때)
    * - REMOVE : 삭제
    * - MERGE : 병합
    * - REFRESH : REFRESH
    * - DETACH : DETACH
    * */

    public void addChild(Child child) {
        childList.add(child);
        child.setParent(this);
    }

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
