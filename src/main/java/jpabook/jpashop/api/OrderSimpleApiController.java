package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문 관련 Controller
 * OneToOne(주문 - 배송), ManyToOne(주문 - 회원) 관계 예제
**/
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * 주문 조회
     * V2 - 엔티티를 DTO로 변환
     * 쿼리 (1 + N)번에 조회
     * 단점 : 1 + N 문제 발생
    **/
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * 주문 조회
     * V3 - 엔티티를 DTO로 변환, fetch join 최적화
     * 쿼리 1번에 조회
     * fetch join 진행할 때는 lazy 상관없이 조회할때 연관관계 있는 모든 객체들 나중이 아니라 바로 조회함
     * 전반적인 경우에는 V3를 사용하되 트랙픽이 많은 기능인 경우에만 V4 사용
     * 장점 : 재사용성은 좋음
     * 단점 : 엔티티로 조회하고 DTO로 변환하므로, 엔티티를 조회할 시 DTO에서 사용하지 않는 데이터도 가져와야함
    **/
    @GetMapping("api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * 주문 조회
     * V4 - 엔티티를 DTO로 변환, fetch join 최적화, JPA에서 엔티티가 아닌 DTO로 바로 조회
     * 원하는 데이터만 조회해서 바로 DTO에 데이터를 넣을 수 있음
     * 전반적인 경우에는 V3를 사용하되 트랙픽이 많은 기능인 경우에만 V4 사용
     * 장점 : V3보다는 성능최적화됨(생각보다 미비)
     * 단점 : 원하는 데이터만 가져와서 사용하는 형태이기 때문에 재사용성이 떨어짐, API 스펙에 맞춘 코드가 repository에 들어감(화면 스펙에 영향을 받음)
    **/
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    /**
     * 주문 조회 DTO
    **/
    @Data
    static class SimpleOrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}