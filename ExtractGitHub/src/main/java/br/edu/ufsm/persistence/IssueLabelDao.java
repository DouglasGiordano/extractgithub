/*
 * To change this license header, choose License Headers in IssueLabel Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.IssueLabel;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author Dougl
 */
public class IssueLabelDao extends NewPersistence<IssueLabel, Integer> {
    @Override
    @PostConstruct
    public void init() {
        this.object = new IssueLabel();
    }

    @Override
    public IssueLabel getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntityManager() {
        return EntityManagerFactory.entityManager.createEntityManager();
    }
}
