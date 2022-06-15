package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A") // 상속관계 매핑 - Item의 @DiscriminatorColumn이 구별할 수 있게 해주는 값 지정
@Getter @Setter
public class Album extends Item{

    private String artist;
    private String etc;

}