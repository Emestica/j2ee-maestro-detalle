package com.ae.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;

@ApplicationScoped
public class JpaConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(JpaConfiguration.class);
    private EntityManagerFactory entityManagerFactory;

    public JpaConfiguration(){
        /* Empty Constructor */
    }

    @PostConstruct
    public void postConstructJpaConfiguration(){
        LOGGER.info("postConstructJpaConfiguration() => Starting");
        if(entityManagerFactory==null){
            LOGGER.info("postConstructJpaConfiguration() => Initializing EntityManagerFactory");
            this.entityManagerFactory = Persistence.createEntityManagerFactory("oracle-pu");
        }
        LOGGER.info("postConstructJpaConfiguration() => Completed");
    }

    @PreDestroy
    public void preDestroyOnJpaConfiguration(){
        LOGGER.info("preDestroyOnJpaConfiguration() => Starting");
        if(entityManagerFactory!=null){
            LOGGER.info("preDestroyOnJpaConfiguration() => Closing EntityManagerFactory");
            entityManagerFactory.close();
        }
        LOGGER.info("preDestroyOnJpaConfiguration() => Completed");
    }

    public EntityManager getEntityManager(){
        return entityManagerFactory.createEntityManager();
    }

    public void closeEntityManager(EntityManager entityManager){
        if(entityManager!=null){
            entityManager.close();
        }
    }

    public void executeInTransaction(Consumer<EntityManager> action){
        LOGGER.info("executeInTransaction() => Starting");
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            action.accept(entityManager);
            entityManager.getTransaction().commit();
            LOGGER.info("executeInTransaction() => Entity Transaction Its Done!");
        } catch (Exception e) {
            LOGGER.error("executeInTransaction() => Exception\n", e);
            if(entityManager.getTransaction().isActive()){
                LOGGER.info("executeInTransaction() => Applied Rollback");
                entityManager.getTransaction().rollback();
            }
        } finally {
            closeEntityManager(entityManager);
        }
        LOGGER.info("executeInTransaction() => Completed");
    }

}
