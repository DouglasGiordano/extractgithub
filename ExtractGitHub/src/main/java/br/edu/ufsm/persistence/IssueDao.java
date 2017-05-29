/*
 * To change this license header, choose License Headers in Issue Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Issue;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

/**
 *
 * @author Douglas Giordano
 */
@Stateful
@LocalBean
public class IssueDao extends NewPersistence<Issue, Integer> {
    @PersistenceContext(unitName = "ExtractGitHub", name = "ExtractGitHub", type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;
    public IssueDao() {

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

    @Override
    public EntityManager getEntity() {
        return this.entityManager;
    }
    
    public List<BigInteger> getIssues(long idProject) {
        Query q = getEntity().createNativeQuery("SELECT DISTINCT(issue.id) "
                + "FROM issue "
                + "INNER JOIN project_issue ON issue.id = issue_id  "
                + "where project_id = " + idProject + "");
        return q.getResultList();
    }
    
        public List<String> getRede(long idProject) {
        Query q = getEntity().createNativeQuery("SELECT GROUP_CONCAT(concat(issue_comment.user_id,'(',user.login,')') SEPARATOR '##'), issue.id\n" +
"                FROM extract_github.issue_issue_comment\n" +
"                INNER JOIN issue ON issue.id = issue_issue_comment.issue_id\n" +
"                INNER JOIN issue_comment ON issue_comment.id = issue_issue_comment.issueComments_id\n" +
"				INNER JOIN project_issue ON issue.id = project_issue.issue_id\n" +
"                INNER JOIN extract_github.user ON user.id = issue_comment.user_id\n" +
"                WHERE project_issue.project_id = " + idProject + " "+
"                GROUP BY issue_issue_comment.issue_id");
        return q.getResultList();
    }
}
