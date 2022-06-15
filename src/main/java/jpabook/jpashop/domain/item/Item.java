package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
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

}
