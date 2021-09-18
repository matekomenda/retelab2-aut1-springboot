package hu.bme.aut.retelab2.repository;

import hu.bme.aut.retelab2.domain.Ad;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class AdRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Ad save(Ad ad) { return em.merge(ad); }

    public List<Ad> findAll(){ return em.createQuery("SELECT a FROM Ad a", Ad.class).getResultList(); }

    public List<Ad> findPriceMinMax(int value1,int value2){
        List<Ad> query = em.createQuery("SELECT a FROM Ad a WHERE a.price >= :value1 AND a.price <= :value2 ",Ad.class).setParameter("value1",value1).setParameter("value2",value2).getResultList();
        return query;
    }

    public Ad findById(long id){ return em.find(Ad.class, id); }

    public List<Ad> findByTag(String tag){
        Query query = em.createQuery("SELECT DISTINCT a FROM Ad a WHERE :tag MEMBER OF a.tags");
        query.setParameter("tag",tag);
        return query.getResultList();
        //return em.createQuery("SELECT a FROM Ad a WHERE a.tags IN (:tag)",Ad.class).setParameter("tag",tag).getResultList();
    }

    @Transactional
    public void deleteById(long id){
        Ad ad = findById(id);
        em.remove(ad);
    }

}
