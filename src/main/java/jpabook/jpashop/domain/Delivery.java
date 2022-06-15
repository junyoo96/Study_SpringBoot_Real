package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    //@OneToOne(mappedBy = ) : 테이블간의 연관관계를 매핑할 때, 연관관계의 주인이 아닌쪽 에 사용, 연관된 다른 테이블에서 매핑된 변수명을 정의
    //즉, order 클래스의 delivery라고 정의된 변수와 매칭
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    // @Enumerated(EnumType.ORDINAL) 사용하면안됨
        // ORDINAL 사용하면 숫자형태로 1,2,3.. 변환되어 저장되기 때문에 나중에 다른 상태가 추가될 경우 매칭된 숫자가 달라짐
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
}
