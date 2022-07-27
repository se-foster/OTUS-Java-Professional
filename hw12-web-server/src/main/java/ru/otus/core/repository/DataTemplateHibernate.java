package ru.otus.core.repository;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class DataTemplateHibernate<T> implements DataTemplate<T> {

    private final Class<T> clazz;
    private final EntityGraphUtil<T> entityGraph;

    public DataTemplateHibernate(Class<T> clazz, EntityGraphUtil<T> entityGraph) {
        this.clazz = clazz;
        this.entityGraph = entityGraph;
    }

    @Override
    public Optional<T> findById(Session session, long id) {
        var query = session.createQuery(String.format("from %s where id = " + id, clazz.getSimpleName()), clazz);
        entityGraph.apply(session, query);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<T> findByEntityField(Session session, String entityFieldName, Object entityFieldValue) {
        var criteriaBuilder = session.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(clazz);
        var root = criteriaQuery.from(clazz);
        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(root.get(entityFieldName), entityFieldValue));

        var query = session.createQuery(criteriaQuery);
        entityGraph.apply(session, query);
        return query.getResultList();
    }

    @Override
    public List<T> findAll(Session session) {
        var query = session.createQuery(String.format("from %s", clazz.getSimpleName()), clazz);
        entityGraph.apply(session, query);
        return query.getResultList();
    }

    @Override
    public void insert(Session session, T object) {
        session.persist(object);
    }

    @Override
    public void update(Session session, T object) {
        session.merge(object);
    }
}
