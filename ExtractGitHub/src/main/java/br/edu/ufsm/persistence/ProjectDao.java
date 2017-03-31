/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import br.edu.ufsm.model.Project;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 *
 * @author Dougl
 */
public class ProjectDao extends NewPersistence<Project, Object> {

    @Override
    @PostConstruct
    public void init() {
        this.object = new Project();
    }

    @Override
    public Project getObject() {
        if (this.object == null) {
            return new Project();
        } else {
            return this.object;
        }
    }
}
