/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Issue;
import br.edu.ufsm.model.PullRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;

/**
 *
 * @author Dougl
 */
public class ExtracaoPullRequest {

    public static List<PullRequest> extract(GitHubClient client, RepositoryId repositoryId) {
        List<PullRequest> list = new ArrayList<>();
        try {
            PullRequestService serviceIssue = new PullRequestService(client);
            List<org.eclipse.egit.github.core.PullRequest> pullRequests = serviceIssue.getPullRequests(repositoryId, "");
            for (org.eclipse.egit.github.core.PullRequest pull : pullRequests) {
                list.add(new PullRequest(pull));
            }
        } catch (IOException ex) {
            Logger.getLogger(ExtracaoPullRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}
