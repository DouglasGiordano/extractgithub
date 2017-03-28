/*
 * To change this license header, choose License Headers in Commit Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Commit;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author Dougl
 */
public class CommiDao extends NewPersistence<Commit, Integer> {

    public CommiDao() {
        
    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new Commit();
    }

    @Override
    public Commit getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntityManager() {
        return EntityManagerFactory.entityManager.createEntityManager();
    }
}
