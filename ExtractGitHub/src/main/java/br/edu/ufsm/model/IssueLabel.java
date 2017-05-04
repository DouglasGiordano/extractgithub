/*
 */
package br.edu.ufsm.model;

import br.edu.ufsm.persistence.EntityBD;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Douglas Montanha Giordano
 */
@Entity
@Table(name = "issue_label", uniqueConstraints =
@UniqueConstraint(columnNames = "name"))
public class IssueLabel implements Serializable, EntityBD {
    private String color;
    @Id
    @Column(name = "name", unique = true, nullable = false, length = 50)
    private String name;
    private String url;

    public IssueLabel(){
        
    }
    
    public IssueLabel(org.eclipse.egit.github.core.Label label){
        this.color = label.getColor();
        this.name = label.getName();
        this.url = label.getUrl();
    }
    
    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Object getPk(){
        return this.name;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IssueLabel other = (IssueLabel) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
