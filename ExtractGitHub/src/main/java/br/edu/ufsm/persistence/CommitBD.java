/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Project;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Dougl
 */
public class CommitBD {

    public static void save(Project project) {
        EntityManagerFactory factory
                = Persistence.createEntityManagerFactory("ExtractGitHub");
        EntityManager em = factory.createEntityManager();
        em.getTransaction().begin();

        em.merge(project);

        em.getTransaction().commit();
    }

    public static List<Project> list() {
        EntityManager factory
                = Persistence.createEntityManagerFactory("ExtractGitHub").createEntityManager();

    	javax.persistence.Query query;   
        query = factory.createQuery("from Project p");
    	 return query.getResultList();  
    }
}
