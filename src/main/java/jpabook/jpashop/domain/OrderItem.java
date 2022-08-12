package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
//protected 생성자 자동으로 생성해줌 : 외부에서 맘대로 객체 생성하지 못하게 하기 위해 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") // 테이블간의 관계에서 연관관계 주인인 쪽에 사용, item의 클래스의 컬럼명 item_id 와 매핑
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // 테이블간의 관계에서 연관관계 주인인 쪽에 사용, order 클래스의 컬럼명 order_id 와 매핑
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

//    //@NoArgsConstructor(access = AccessLevel.PROTECTED) 사용했기 때문에 생략 가능
//    protected OrderItem(){
//
//    }

    /**
     * 생성 메소드
     * 생성 로직의 유지보수를 위해 함수로 만들어 사용
    **/
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        //주문한 item 지정
        orderItem.setItem(item);
        //주문한 상품 가격 지정
        orderItem.setOrderPrice(orderPrice);
        //주문한 상품 개수 지정
        orderItem.setCount(count);
        //주문한 수량만큼 상품의 재고 줄임
        item.removeStock(count);

        return orderItem;
    }

    /**
     * 비즈니스 로직
     * 주문 취소
    **/
    public void cancel(){
        //주문 취소했으니 해당 item 재고 수량 증가
        getItem().addStock(count);
    }

    /**
     * 조회 로직
     * 주문상품 전체 가격 조회
    **/
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }

}
