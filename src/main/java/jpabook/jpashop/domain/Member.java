package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    //@Embedded : 임베디드 객체에 사용
    @Embedded
    private Address address;

    //@OneToMany : 하나의 테이블이 여러개의 테이블과 연관관계를 맺을 때 사용
            // 테이블간의 연관관계를 매핑할 때, 연관관계의 주인이 아닌쪽에 사용
        //mappedBy : Order 객체의 어떤 필드와 매칭되는지를 지정
            //즉,  Order 클래스의 member 변수와 연결하기 위함
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

}
