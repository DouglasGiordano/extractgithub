/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.extractgithub_web;

import br.edu.ufsm.controller.ExtracaoCommit;
import br.edu.ufsm.controller.ExtracaoRepository;
import br.edu.ufsm.model.Commit;
import br.edu.ufsm.model.Project;
import br.edu.ufsm.persistence.CommitBD;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.RepositoryService;

/**
 *
 * @author Dougl
 */
@Named(value = "extrairMBean")
@ViewScoped
public class ExtrairMBean implements Serializable{
    private Project project;
    private List<Project> projects;
    private String usuario;
    private String senha;
    private String proprietario;
    private String repositorio;
    /**
     * Creates a new instance of ExtrairMBean
     */
    @PostConstruct
    public void init() {
        projects = CommitBD.list();
    }
    
    
    public void extrairCommit(){
        try{
            System.out.println("Iniciando");
        GitHubClient client = new GitHubClient();
        client.setCredentials(usuario, senha);
        RepositoryService service = new RepositoryService();
        RepositoryId repo = new RepositoryId(proprietario, repositorio);
        Project projeto = ExtracaoRepository.extractRepository(client, repo);
        List<Commit> commits = ExtracaoCommit.extract(client, repo);
        projeto.setCommits(commits);
        CommitBD.save(projeto);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dados do repositório foram extraidos com sucesso.", ""));
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro ao extrair os dados: "+ex.getMessage() , ""));
         
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
}
