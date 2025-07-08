package jpabook.jpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/*
* 테이블 명이 ORDER일 경우, 예약어인 Order By 때문에
* ORDER라는 테이블명을 사용할 수 없을 때가 있음.
* */

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @Column(name = "MEMBER_ID")
    private Long memberId;
    /*
    * 데이터 중심 설계의 문제점
    * - 현재 방식은 객체 설계를 테이블 설계에 맞춘 방식
    * - 테이블의 외래키를 객체에 그대로 가져옴
    * - 객체 그래프 탐색이 불가능
    * - 참조가 없으므로 UML도 잘못됨
    *
    * private Member member; -> 객체 중심 설계
    * */

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
