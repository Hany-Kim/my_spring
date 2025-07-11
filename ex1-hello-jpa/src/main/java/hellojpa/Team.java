package hellojpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
    /*
    * 객체간 양방향 연관관계를 위한 설정
    * team은 Member클래스의 Team타입의 team변수 명이다.
    *
    * DB 테이블간에는 방향 개념이 없다.
    * FK만 있으면 양방향 조회가 가능하다.
    * 단방향일 때와 차이가 없다.
    * */
    /*
    * 양방향 관계 == 단방향 관계가 2개
    * 객체의 두 관계 중 1개만 연관관계의 주인이 됨
    *
    * 연관관계의 주인 : 외래 키 관리 (등록, 수정)
    * 주인은 mappedBy 속성을 사용 X
    *
    * 주인이 아닌 쪽은 읽기만 가능
    * 주인이 아니면 mappedBy 속성 지정
    * */
    /*
    * 양방향 매핑 정리
    * - 단방향 매핑만으로도 이미 연관관계 매핑은 완료. => 단방향 설정만으로도 설계, 구현은 될 수 있어야 한다. 먼저 단방향으로 설계와 구현을 끝내자.
    * - 양방향 매핑은 반대 방향으로 조회(객체 그래프 탐색)기능이 추가된 것 뿐. => 양방향을 하는 것은 '조회'를 추가하는 것이다.
    * - JPQL에서 역방향으로 탐색할 일이 많다.
    * - 단방향 매핑을 잘 하고 양방향은 필요할 때 추가해도 됨. (테이블에 영향을 주지 않음)
    *
    * 단방향으로 설계, 구현 끝내고,
    * 양방향은 나중에 고민
    * */

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

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void addMember(Member member) {
        /*
        * 연관관계 편의 메서드2
        * */
        member.setTeam(this);
        members.add(member);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", members=" + members + // members에서도 members.toString()이 호출된다.
                '}';
    }
}
