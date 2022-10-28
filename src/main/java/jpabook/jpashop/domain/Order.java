package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // DB에서 보통 order by와 같은 예약어가 있으므로 class는 order그대로 두고 테이블명은 예약어를 피해 orders로 사용하기 위함
@Getter @Setter
//protected 생성자 자동으로 생성해줌 : 외부에서 맘대로 객체 생성하지 못하게 하기 위해 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    //CascadeType.ALL일 경우, Order가 persist될 때 OrderItem도 자동으로 persist됨
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // 테이블간의 관계에서 연관관계 주인이 아닌 쪽에 사용, OrderItem 클래스의 order 변수와 매핑
    private List<OrderItem> orderItems = new ArrayList<>();

    //CascadeType.ALL일 경우, Delivery가 persist될 때 OrderItem도 자동으로 persist됨
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
    public void setMember(Member member){
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

    /**
     * 생성 메소드
    **/
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /**
     * 비즈니스 로직
     * 주문 취소
    **/
    public void cancel() {
        //만약 배달상태가 '완료'인 경우
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        // 변경감지 기능 덕분에 DB에 쿼리를 날리는 코드 없이 트랜잭션 커밋 시점에 변경사항에 대해 자동으로 쿼리를 만들어서 DB를 업데이트 함
        // 배달상태 '취소'로 변경
        this.setStatus(OrderStatus.CANCEL);
        // 모든 OrderItem에 대해서도 '취소' 처리
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    /**
     * 조회 로직
     * 전체 주문 가격 조회
    **/
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
