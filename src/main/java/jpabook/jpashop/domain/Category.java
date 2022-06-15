package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    //@ManyToMany : 다대다 관계 설정
    //@JoinTable : Category 테이블과 Item 테이블 간의 중간 테이블에 대한 정보를 표현하기 위해 사용
        //name : 중간 테이블 이름
        //joinColumns : 중간 테이블에서 Category 테이블과 연결하는 fk(Category 테이블의 pk)
        //inverseJoinColumns : 중간 테이블에서 Category 테이블과 연결하는 fk(Item 테이블의 pk)
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    //자기자신을 양방향으로 연관짓기 (Category와 하위 Cateogry들(Album, Book, Movie)을 연관짓기)
    @ManyToOne(fetch = FetchType.LAZY)
    //Category테이블의 parent_id 컬럼
    @JoinColumn(name = "parent_id")
    private Category parent;

    //parent_id를 컬럼에 해당하는 parent 변수에 매핑
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    //==연관관계 편의 메서드==//
    public void addChildCategory(Category child){
         this.child.add(child);
         child.setParent(this);
    }
}
