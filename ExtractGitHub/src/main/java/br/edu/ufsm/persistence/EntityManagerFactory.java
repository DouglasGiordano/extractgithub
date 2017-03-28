/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.persistence;

import javax.persistence.Persistence;

/**
 *
 * @author Dougl
 */
public class EntityManagerFactory {
    public static javax.persistence.EntityManagerFactory entityManager;
    static{
        entityManager = Persistence.createEntityManagerFactory("ExtractGitHub");
    }
}
