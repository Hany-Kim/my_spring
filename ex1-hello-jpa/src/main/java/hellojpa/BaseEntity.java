package hellojpa;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    /*
    * @MappedSuperclass
    * - 상속관계 매핑이 아님
    * - 엔티티 아님 -> 테이블과 매핑 안됨
    * - 부모 클래스를 상속 받는 자식 클래스의 매핑 정보만 제공
    * - 조회, 검색 불가(em.find(BaseEntity) 불가
    * - 직접 생성해서 사용할 일 없으므로 추상 클래스 권장
    * - 테이블과 관계없고, 단순히 엔티티가 공통으로 사용하는 매핑 정보를 모으는 역할(공통 속성)
    * - 주로 등록일, 수정일, 등록자, 수정자 같은 전체 엔티티에서 공통으로 적용하는 정보를 모을 때 사용
    * - 참고: @Entity 클래스는 엔티티(@Entity)나 @MappedSuperclass 클래스로 지정한 클래스만 상속 가능
    * */

    private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
