/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.extractgithub_web;

import br.edu.ufsm.controller.ExtracaoCommit;
import br.edu.ufsm.controller.ExtracaoIssue;
import br.edu.ufsm.controller.ExtracaoRepository;
import br.edu.ufsm.model.Commit;
import br.edu.ufsm.model.GeradorCombinacoes;
import br.edu.ufsm.model.Issue;
import br.edu.ufsm.model.Project;
import br.edu.ufsm.persistence.CommitDao;
import br.edu.ufsm.persistence.CommitFileDao;
import br.edu.ufsm.persistence.IssueDao;
import br.edu.ufsm.persistence.ProjectDao;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.RepositoryService;

/**
 *
 * @author Dougl
 */
@Named(value = "extrairMBean")
@ViewScoped
public class ExtrairMBean implements Serializable {

    private Project project;
    private List<Project> projects;
    private String usuario;
    private String senha;
    private String proprietario;
    private String repositorio;
    private GitHubClient client;
    private RepositoryService service;

    private String redeGeradaBanco;

    @EJB
    private ProjectDao projectDao;

    @EJB
    private IssueDao issueDao;

    @EJB
    private CommitDao commitDao;

    @EJB
    private CommitFileDao commitFileDao;

    /**
     * Creates a new instance of ExtrairMBean
     */
    @PostConstruct
    public void init() {
        client = new GitHubClient();
        service = new RepositoryService();
        setProjects(projectDao.findAll());
    }

    public void extrairCommit() {
        System.out.println("Iniciando Extração de Commits");
        client.setCredentials(usuario, senha);
        RepositoryId repo = new RepositoryId(proprietario, repositorio);
        Project projeto = ExtracaoRepository.extractRepository(client, repo);
        List<Commit> commits = ExtracaoCommit.extract(client, repo);
//        List<Commit> estruturados = new ArrayList<>();
//        for (Commit commit : commits) {
//            Commit commitBuscado = commitDao.getById(commit.getSha(), Commit.class);
//            if (commitBuscado != null) {
//                commitBuscado.setAuthor(commit.getAuthor());
//                commitBuscado.setCommitter(commit.getCommitter());
//                commitBuscado.setAuthorCommit(commit.getAuthorCommit());
//                commitBuscado.setCommiterCommit(commit.getCommiterCommit());
//                commitDao.save(commitBuscado);
//            }
//        }
        projeto.setCommits(commits);
        projectDao.save(projeto);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dados do repositório foram extraidos com sucesso.", ""));

        System.out.println("Extração de Commits Finalizada");
    }

    public void extrairFilesCommit() {
        System.out.println("Iniciando Extração de Commits (Arquivos)");
        RepositoryId repo = new RepositoryId(proprietario, repositorio);
        client.setCredentials(usuario, senha);
        Project projeto = ExtracaoRepository.extractRepository(client, repo);
        List<String> commits = commitDao.getCommits(projeto.getId());
        CommitService commitService = new CommitService(client);
        Commit commitD;
        RepositoryCommit commit;
        for (String commitL : commits) {
            try {
                System.out.println("Extraindo commit: " + commitL);
                commit = commitService.getCommit(repo, commitL);
                commitD = new Commit(commit);
                commitDao.save(commitD);

            } catch (Exception ex) {
                System.out.println("Impossivel baixar commit: " + commitL + "\n\n" + ex.getMessage());
            }
        }
        System.out.println("Extração de Commits(Arquivos) Finalizada");
    }

    public void extrairIssue() {
        try {
            System.out.println("Iniciando");
            GitHubClient client = new GitHubClient();
            client.setCredentials(usuario, senha);
            RepositoryService service = new RepositoryService();
            RepositoryId repo = new RepositoryId(proprietario, repositorio);
            Project projeto = ExtracaoRepository.extractRepository(client, repo);
            List<Issue> issues = ExtracaoIssue.extract(client, repo);
            projeto.setIssue(issues);
            projectDao.save(projeto);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dados do repositório foram extraidos com sucesso.", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao extrair os dados: " + ex.getMessage(), ""));
            ex.printStackTrace();
        }
        System.out.println("Finalizando");
    }
    public void extrairIssueComment() {
        try {
            System.out.println("Iniciando");
            GitHubClient client = new GitHubClient();
            client.setCredentials(usuario, senha);
            RepositoryService service = new RepositoryService();
            RepositoryId repo = new RepositoryId(proprietario, repositorio);
            Project projeto = ExtracaoRepository.extractRepository(client, repo);
            List<BigInteger> issues = issueDao.getIssues(projeto.getId());
            IssueService serviceIssue = new IssueService(client);
            for (BigInteger issueId : issues) {
                Issue issue = issueDao.getByIdO(issueId.longValueExact(), Issue.class);
                if (issue.getComments() != 0) {
                    List<Comment> issueComents = serviceIssue.getComments(repo, issue.getNumber());
                    issue.setComments(issueComents);
                    issueDao.save(issue);
                    System.out.println("Extraido comentarios issue: " + issue.getNumber());
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dados do repositório foram extraidos com sucesso.", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao extrair os dados: " + ex.getMessage(), ""));
            ex.printStackTrace();
        }
        System.out.println("Finalizando");
    }

    public void gerarNetwork() {
        try {
            GitHubClient client = new GitHubClient();
            client.setCredentials(usuario, senha);
            RepositoryService service = new RepositoryService();
            RepositoryId repo = new RepositoryId(proprietario, repositorio);
            Project projeto = ExtracaoRepository.extractRepository(client, repo);
            List<String> lista = commitFileDao.getUsersFile(projeto.getId());
            new GeradorCombinacoes(projeto.getName(), "#31948c", projeto.getId()).execute(lista);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rede temporal gerada com sucesso.", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao gerar a rede temporal: " + ex.getMessage(), ""));
            ex.printStackTrace();
        }
        System.out.println("Finalizando");
    }
    public void gerarNetworkIssue() {
        try {
            GitHubClient client = new GitHubClient();
            client.setCredentials(usuario, senha);
            RepositoryService service = new RepositoryService();
            RepositoryId repo = new RepositoryId(proprietario, repositorio);
            Project projeto = ExtracaoRepository.extractRepository(client, repo);
            List<String> lista = issueDao.getRede(projeto.getId());
            for(String t: lista){
                System.out.println(t);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Rede temporal gerada com sucesso.", ""));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao gerar a rede temporal: " + ex.getMessage(), ""));
            ex.printStackTrace();
        }
        System.out.println("Finalizando");
    }

    /**
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * @return the proprietario
     */
    public String getProprietario() {
        return proprietario;
    }

    /**
     * @param proprietario the proprietario to set
     */
    public void setProprietario(String proprietario) {
        this.proprietario = proprietario;
    }

    /**
     * @return the repositorio
     */
    public String getRepositorio() {
        return repositorio;
    }

    /**
     * @param repositorio the repositorio to set
     */
    public void setRepositorio(String repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * @return the projects
     */
    public List<Project> getProjects() {
        return projects;
    }

    /**
     * @param projects the projects to set
     */
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    /**
     * @return the redeGeradaBanco
     */
    public String getRedeGeradaBanco() {
        return redeGeradaBanco;
    }

    /**
     * @param redeGeradaBanco the redeGeradaBanco to set
     */
    public void setRedeGeradaBanco(String redeGeradaBanco) {
        this.redeGeradaBanco = redeGeradaBanco;
    }
}
