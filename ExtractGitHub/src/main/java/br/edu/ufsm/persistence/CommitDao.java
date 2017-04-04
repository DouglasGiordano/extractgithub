/*
 * To change this license header, choose License Headers in Commit Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Commit;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.Query;

/**
 *
 * @author Dougl
 */
public class CommitDao extends NewPersistence<Commit, String> {

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

    public List<String> getCommits(long idProject) {
        Query q = getEntity().createNativeQuery("SELECT DISTINCT(sha) "
                + "FROM commit "
                + "INNER JOIN project_commit ON commit.sha = commits_sha  "
                + "LEFT JOIN commit_commit_file ON commit.sha = commit_commit_file.commit_sha where isnull(files_id) and project_id = " + idProject + "");
        return q.getResultList();
    }
}
