/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Issue;
import br.edu.ufsm.persistence.IssueDao;
import com.jcabi.github.Coordinates;
import com.jcabi.github.Github;
import com.jcabi.github.RtGithub;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
            Map<String, String> filtros = new HashMap<>();
            filtros.put("state", "all");
            filtros.put("filter", "all");
            List<org.eclipse.egit.github.core.Issue> issues = serviceIssue.getIssues(repositoryId,
                    filtros);
            for (org.eclipse.egit.github.core.Issue issue : issues) {
                list.add(new Issue(issue));
            }
        } catch (IOException ex) {
            Logger.getLogger(ExtracaoIssue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static List<Issue> extract2(String nome, String senha, String usuario, String repositorio) {
        List<Issue> list = new ArrayList<>();
        try {
            Github github = new RtGithub(nome, senha);
            Map<String, String> filtros = new HashMap<>();
            filtros.put("2008-01-01", "2018-01-01");
            Iterable<com.jcabi.github.Issue> issues = github.repos().get(new Coordinates.Simple(usuario, repositorio)).issues().iterate(filtros);
            Iterator<com.jcabi.github.Issue> it = issues.iterator();
            while (it.hasNext()) {
                com.jcabi.github.Issue element = it.next();
                System.out.println("Issue: "+element.number());
                System.out.println("IssueB: "+element.hashCode());
            }
        } catch (Exception ex) {
            Logger.getLogger(ExtracaoIssue.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public static void extract(GitHubClient client, RepositoryId repositoryId, List<BigInteger> issues, IssueDao issueDao) {
        try {
            IssueService serviceIssue = new IssueService(client);
            for (BigInteger issueId : issues) {
                Issue issue = issueDao.getByIdO(issueId.longValueExact(), Issue.class);
                if (issue.getComments() != 0) {
                    List<Comment> issueComents = serviceIssue.getComments(repositoryId, issue.getNumber());
                    issue.setComments(issueComents);
                    issueDao.save(issue);
                    System.out.println("Extraido comentarios issue: " + issue.getNumber());
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ExtracaoIssue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
