/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Project;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.PersistenceUnit;

/**
 *
 * @author Dougl
 */
@Stateless
@LocalBean
public class ProjectDao extends NewPersistence<Project, Integer> {

    public static List<Project> list() {
        EntityManager factory
                = Persistence.createEntityManagerFactory("ExtractGitHub").createEntityManager();

        javax.persistence.Query query;
        query = factory.createQuery("from Project p");
        return query.getResultList();
    }
    //<editor-fold defaultstate="collapsed" desc="INIT">
    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    
    private EntityManager entityManager;

    @Override
    @PostConstruct
    public void init() {
        this.object = new Project();
    }

    @Override
    public Project getObject() {
        return this.object;
    }

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}
