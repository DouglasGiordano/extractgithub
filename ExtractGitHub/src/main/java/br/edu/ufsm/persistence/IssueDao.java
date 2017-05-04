/*
 * To change this license header, choose License Headers in Issue Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Issue;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

/**
 *
 * @author Dougl
 */
public class IssueDao extends NewPersistence<Issue, Integer> {

    //<editor-fold defaultstate="collapsed" desc="INIT">
    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private final EntityManager entityManager;

    public IssueDao() {
        this.entityManager = Persistence.createEntityManagerFactory("ExtractGitHub").createEntityManager();
    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new Issue();
    }

    @Override
    public Issue getObject() {
        return this.object;
    }
}
