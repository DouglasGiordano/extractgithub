/*
 */
package br.edu.ufsm.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;

/**
 *
 * @author Dougl
 */
public class ExtracaoIssue {
    public static void main() {
        try {
            RepositoryService service = new RepositoryService();
            RepositoryId repo = new RepositoryId("rails", "rails");
            Repository repositorio = service.getRepository(repo);
            IssueService serviceIssue = new IssueService();
            HashMap<String, String> parametros = new HashMap<String, String>();
            parametros.put("01-01-2011", "03-25-2017");
            List<Issue> issues = serviceIssue.getIssues(repositorio,
                    parametros);
            int i=0;
            for(Issue issue: issues){
                System.out.println(issue.getTitle()+" - "+issue.getCreatedAt());
                i++;
            }
            System.out.println(i);
        } catch (IOException ex) {
            Logger.getLogger(ExtracaoIssue.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
