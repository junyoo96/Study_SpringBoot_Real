package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

//값 타입은 변경 불가능하게 설계해야함
    //@Setter를 제거
    //생성자에서 값을 모두 초기화해서 변경 불가능한 클래스를 만들기
        //JPA 스펙상 엔티티나 임베디드 타입은 생성자를 public이나 protected로 설정하기(protected 권장)
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address(){

    }

    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

}
