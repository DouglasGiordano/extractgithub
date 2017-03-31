/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Commit;
import br.edu.ufsm.persistence.CommitDao;
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

    public static List<Commit> extract(GitHubClient client, RepositoryId repositoryId, List<Commit> commits) {
        List<Commit> list = new ArrayList<>();
        CommitDao commitDao = new CommitDao();
        //Basic authentication
        CommitService commitService = new CommitService(client);
        for (Commit commitL : commits) {
            if (commitL.getFiles() == null || commitL.getFiles().isEmpty()) {
                try {
                    System.out.println("Baixando commit: " + commitL.getUrl());
                    RepositoryCommit commit = commitService.getCommit(repositoryId, commitL.getSha());
                    Commit commitD = new Commit(commit);
                    list.add(commitD);
                    commitDao.save(commitD);
                } catch (Exception ex) {
                    System.out.println("Impossivel baixar commit: " + commitL.getUrl());
                }
            } else {
                list.add(commitL);
            }

        }
        return list;
    }
}
