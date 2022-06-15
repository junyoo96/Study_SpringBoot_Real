package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // DB에서 보통 order by와 같은 예약어가 있으므로 class는 order그대로 두고 테이블명은 예약어를 피해 orders로 사용하기 위함
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    //@ManyToOne : 여러개의 테이블이 하나의 테이블과 연관관계 맺을 때 사용(연관관계 주인인쪽에 사용)
    //@JoinColmn : 테이블간의 연관관계를 매핑할 때, 연관관계의 주인인(다른 테이블의 fk를 갖고있는) 쪽에 사용
        // name : 연관관계를 맺을 해당 객체의 컬럼명
            //즉, orders 테이블의 member pk를 저장하는 컬럼명이 member_id가 됨
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") // 테이블간의 관계에서 연관관계 주인인 쪽에 사용, member 클래스의 컬럼명이 member_id이라고 정의된 변수와 매칭
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 테이블간의 관계에서 연관관계 주인이 아닌 쪽에 사용, OrderItem 클래스의 order 변수와 매핑
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //@JoinColmn : 테이블간의 연관관계를 매핑할 때, 연관관계의 주인인(다른 테이블의 fk를 갖고있는) 쪽에 사용
        // name : 연관관계를 맺을 해당 객체의 컬럼값
            //즉, orders 테이블의 delivery pk를 저장하는 컬럼명이 delivery_id가 됨
    //Order와 Delivery 테이블 관계 정의시, Order테이블 쪽에 Delivery테이블의 fk가 있기 때문에
    //즉, delivery 클래스에 컬럼명이 delivery_id라고 정의된 변수와 매칭
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    //==연관관계 편의 메서드==//
    public void setMemeber(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
