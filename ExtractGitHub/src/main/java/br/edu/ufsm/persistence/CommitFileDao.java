/*
 * To change this license header, choose License Headers in CommitFile Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.CommitFile;
import java.util.List;
import javax.annotation.PostConstruct;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Dougl
 */
public class CommitFileDao extends NewPersistence<CommitFile, Integer> {

    public CommitFileDao() {
        
    }

    @Override
    @PostConstruct
    public void init() {
        this.object = new CommitFile();
    }

    @Override
    public CommitFile getObject() {
        return this.object;
    }
    
    public List<CommitFile> getCommitFiles(String shaCommit){
        Criteria criteria = getCriteria(CommitFile.class)
        .createAlias("commit", "c")
        .add(Restrictions.eq("c.sha", shaCommit));
        return criteria.list();
    }
}
