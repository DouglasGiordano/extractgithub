/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Issue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

/**
 *
 * @author Dougl
 */
public class ExtracaoIssue {

    public static List<Issue> extract(GitHubClient client, RepositoryId repositoryId) {
        List<Issue> list = new ArrayList<>();
        try {
            IssueService serviceIssue = new IssueService(client);
            List<org.eclipse.egit.github.core.Issue> issues = serviceIssue.getIssues(repositoryId,
                    null);
            for (org.eclipse.egit.github.core.Issue issue : issues) {
                list.add(new Issue(issue));
            }
        } catch (IOException ex) {
            Logger.getLogger(ExtracaoIssue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    public static List<Issue> extract(GitHubClient client, RepositoryId repositoryId, List<Issue> issues) {
        List<Issue> list = new ArrayList<>();
        try {
            IssueService serviceIssue = new IssueService(client);
            for (Issue issue : issues) {
                if(issue.getComments() != 0){
                    List<Comment> issueComents = serviceIssue.getComments(repositoryId, issue.getNumber());
                    issue.setComments(issueComents);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ExtracaoIssue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
