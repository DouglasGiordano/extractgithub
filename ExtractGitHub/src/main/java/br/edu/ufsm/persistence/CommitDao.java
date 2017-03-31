/*
 * To change this license header, choose License Headers in Commit Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Commit;
import java.util.List;
import javax.annotation.PostConstruct;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Dougl
 */
public class CommitDao extends NewPersistence<Commit, Integer> {

    public CommitDao() {
        
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
    
    public List<Commit> getCommits(long idProject){
        Criteria criteria = getCriteria(Commit.class)
        .createAlias("project", "p")
        .add(Restrictions.eq("p.id", idProject));
        return criteria.list();
    }
}
