package br.edu.ufsm.persistence;



import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import br.edu.ufsm.persistence.EntityBD;

/**
 * Classe resolve os métodos básicos de cadastro (CRUD) com API da
 * <code>JPA</code>.
 *
 * @author YaW Tecnologia
 */
public abstract class NewPersistence<T extends EntityBD, PK extends Object> {

//    @PersistenceContext
//    private EntityManager entityManager;
    protected T object;

    public abstract void init();

    public T findByCriteria(String column, Object value) {
        Session session = (Session) getEntityManager().getDelegate();
        try {
            return (T) session.createCriteria(this.getObject().getClass())
                    .add(Restrictions.eq(column, value))
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List listByCriteria(String column, Object value, Class myClass, String order) {
        HashMap<String, Object> parametros = new HashMap();
        parametros.put(column, value);

        Criteria criteria = getCriteria(myClass);
        for (Map.Entry<String, Object> entry : parametros.entrySet()) {
            String campo = entry.getKey();
            Object valor = entry.getValue();
            criteria.add(Restrictions.eq(campo, valor));
        }
        return criteria.addOrder(Order.asc(order)).list();
    }

    public Criteria getCriteria(Class classe) {
        Session session = (Session) this.getEntityManager().getDelegate();
        Object objeto = null;
        Transaction tx = null;
        Criteria criteria = null;
        try {
            tx = session.getTransaction();//cria uma transação para o hibernate conectar no banco
            criteria = session.createCriteria(classe);
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            //session.close();
        }
        return criteria;
    }

    public List getObjects(Criteria criteria) {
        Session session = (Session) this.getEntityManager().getDelegate();
        List objects = (List) criteria.list();
//        session.close();
        return objects;
    }

    public Object getObject(HashMap<String, Object> filtros, Class classe) {
        Object objeto = null;
        Criteria criteria = getCriteria(classe);
        for (Map.Entry<String, Object> entry : filtros.entrySet()) {
            String campo = entry.getKey();
            Object valor = entry.getValue();
            criteria.add(Restrictions.eq(campo, valor));
        }
        criteria.setMaxResults(1);//no maximo 1 resultado
        return getObject(criteria);
    }

    public Object getObject(Criteria criteria) {
        Session session = (Session) this.getEntityManager().getDelegate();
        Object object = criteria.uniqueResult();
        //session.close();
        return object;
    }

    public List getObjects(HashMap<String, Object> filtros, Class classe) {
        Object objeto = null;
        Criteria criteria = getCriteria(classe);
        for (Map.Entry<String, Object> entry : filtros.entrySet()) {
            String campo = entry.getKey();
            Object valor = entry.getValue();
            criteria.add(Restrictions.eq(campo, valor));
        }
        return getObjects(criteria);
    }

    public EntityBD getByCriteria(String column, Object value, Class myClass) {
        Session session = (Session) this.getEntityManager().getDelegate();
        try {
            if (myClass != null) {
                EntityBD obj = (EntityBD) session.createCriteria(myClass).add(Restrictions.eq(column, value)).uniqueResult();
                if (obj == null) {
                    return null;
                } else {
                    return obj;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public T save(T myEntity) {
        this.object = myEntity;
        if (this.getObject().getPk() != null) {
            T entity = getEntityManager().merge(this.getObject());
            return entity;
        } else {
            getEntityManager().persist(this.getObject());
            T entity = (getById((PK) this.getObject().getPk()));
            return entity;
        }
    }

    public boolean remove(T myEntity) {
        this.object = myEntity;
        getEntityManager().remove(getEntityManager().merge(this.getObject()));
        if (getById((PK) this.getObject().getPk()) == null) {
            init();
            return true;
        } else {
            init();
            return false;
        }
    }

    public T getById(PK id) {
        return (T) getEntityManager().find(this.getObject().getClass(), id);
    }

    public List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(this.getObject().getClass()));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List findAll(Class classe) {
        Object objeto = null;
        Criteria criteria = getCriteria(classe);
        return getObjects(criteria);
    }

    public List<T> findRange(int[] range) {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(this.getObject().getClass()));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        Root<T> rt = cq.from(this.getObject().getClass());
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    //<editor-fold defaultstate="collapsed" desc="GET/SET">
    public abstract EntityManager getEntityManager();

    public abstract T getObject();

    public void setObject(T object) {
        this.object = object;
    }

}
//</editor-fold>
