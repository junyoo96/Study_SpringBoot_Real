package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 상속관계 매핑 - 정규화 하지 않고 하나의 테이블에 모두 넣고 사용하는 경우
@DiscriminatorColumn(name = "dtype") // 상속관계 매핑 - Album, Book, Movie를 구별할 수 있게 하기 위해
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    //Cateogry 테이블과 Item 테이블간의 연관관계를 나타내기 위함
        // mappedBy = 연관된 테이블에 해당하는 객체에서 item을 나타내는 것의 변수명
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직 ==//
    /**
     * stock 증가
     * 재고가 증가하거나 상품 주문을 취소해서 재고를 다시 늘려야할 때 사용
    **/
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     * 파라미터로 넘어온 수만큼 재고 줄임
     * 만약 재고 부족하면 예외 발생
     * 주로 상품 주문 시 사용
    **/
    public void removeStock(int quantity){
        System.out.println(this.stockQuantity);
        System.out.println(quantity);
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
        System.out.println(stockQuantity);
    }



}
