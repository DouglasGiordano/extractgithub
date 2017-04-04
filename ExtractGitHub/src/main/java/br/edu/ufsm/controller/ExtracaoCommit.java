/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Commit;
import br.edu.ufsm.model.CommitFile;
import br.edu.ufsm.persistence.CommitDao;
import br.edu.ufsm.persistence.CommitFileDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;

/**
 *
 * @author Douglas Montanha Giordano
 */
public class ExtracaoCommit {

    public static List<Commit> extract(GitHubClient client, RepositoryId repositoryId) {
        List<Commit> list = new ArrayList<>();
        try {
            //Basic authentication
            CommitService commitService = new CommitService(client);
            List<RepositoryCommit> commits = commitService.getCommits(repositoryId);
            for (RepositoryCommit commit : commits) {
                list.add(new Commit(commit));
            }
        } catch (IOException ex) {
            Logger.getLogger(ExtracaoIssue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void extract(GitHubClient client, RepositoryId repositoryId, List<String> commits) {
        CommitDao commitDao = new CommitDao();
        //Basic authentication
        CommitService commitService = new CommitService(client);
        Commit commitD;
        RepositoryCommit commit;
        for (String commitL : commits) {
            try {
                commit = commitService.getCommit(repositoryId, commitL);
                commitD = new Commit(commit);
                commitDao.save(commitD);
            } catch (Exception ex) {
                System.out.println("Impossivel baixar commit: " + commitL + "\n\n" + ex.getMessage());
            } catch (OutOfMemoryError ex) {
                System.out.println("Estouro de Memória: " + commitL + "\n\n" + ex.getMessage());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex2) {
                    System.out.println("Puxa, estava dormindo! Você me acordou"+ex2);
                }
            }
        }
    }
}
