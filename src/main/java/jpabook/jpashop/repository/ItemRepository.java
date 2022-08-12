package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        // id가 없으면, 신규등록으로 보고 persist 실행
        if(item.getId() == null) {
            em.persist(item);
        }
        // id가 있으면, 이미 데이터베이스에 저장된 엔티티를 수정한다고 보고, merge를 실행(업데이트와 비슷한 느낌)
        else{
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

}
