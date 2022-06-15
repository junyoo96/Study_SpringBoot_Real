package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter @Setter
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


}
