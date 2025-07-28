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

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
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
    /*
    * orphanRemoval = true
    * 고아객체 제거: 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제
    *
    * 고아 객체 - 주의
    * - 참조가 제거된 엔티티는 다른 곳에서 참조하지 않는 고아객체로 보고 삭제하는 기능
    * - 참조하는 곳이 하나일 때 사용해야함!
    * - 특정 엔티티가 개인 소유할 때 사용
    * - @OneToOne, @OneToMany만 가능
    * - 참고: 개념적으로 부모를 제거하면 자식은 고아가 된다. 따라서 고아 객체 제거 기능을 활성화하면,
    *   부모를 제거할 때 자식도 함께 제거된다. 이것은 CasecadeType.REMOVE처럼 작동한다.
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

    public List<Child> getChildList() {
        return childList;
    }

    public void setChildList(List<Child> childList) {
        this.childList = childList;
    }
}
