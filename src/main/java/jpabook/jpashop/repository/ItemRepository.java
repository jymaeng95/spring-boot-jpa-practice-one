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

    // 상품 저장
    public void save(Item item) {
        // 신규로 등록
        if(item.getId() == null) {
            em.persist(item);
        }
        // DB에 등록된 경우이므로 update
        else {
            em.merge(item);
        }
    }

    public Long saveOne(Item item) {
        // 신규로 등록
        if(item.getId() == null) {
            em.persist(item);
        }
        // DB에 등록된 경우이므로 update
        else {
            em.merge(item);
        }
        return item.getId();
    }


    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
