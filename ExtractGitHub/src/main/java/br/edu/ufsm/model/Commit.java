/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.model;

import br.edu.ufsm.persistence.EntityBD;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Douglas Giordano
 */
@Entity
@Table(name = "commit")
public class Commit implements Serializable, EntityBD {

    @Id
    private String sha;
    private int commentCount;
    @Lob
    @Column(length = 10000)
    private String message;
    @Embedded
    private CommitStats stats;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CommitFile> files;
    private String url;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User author;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User committer;

    public Commit() {
    }

    public Commit(org.eclipse.egit.github.core.RepositoryCommit commit) {
        this.commentCount = commit.getCommit().getCommentCount();
        this.setMessage(commit.getCommit().getMessage());
        this.stats = new CommitStats(commit.getStats());
        if (commit.getFiles() != null) {
            for (org.eclipse.egit.github.core.CommitFile file : commit.getFiles()) {
                files.add(new CommitFile(file));
            }
        }
        this.sha = commit.getSha();
        this.url = commit.getUrl();
        this.author = new User(commit.getAuthor());
        this.committer = new User(commit.getCommitter());
        if (author.getId() == 0 && committer.getId() == 0) {
            this.author = null;
            this.committer = null;
        }
    }

    /**
     * @return the commentCount
     */
    public int getCommentCount() {
        return commentCount;
    }

    /**
     * @param commentCount the commentCount to set
     */
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = convertToUTF8(message);
    }

    /**
     * @return the stats
     */
    public CommitStats getStats() {
        return stats;
    }

    /**
     * @param stats the stats to set
     */
    public void setStats(CommitStats stats) {
        this.stats = stats;
    }

    /**
     * @return the files
     */
    public List<CommitFile> getFiles() {
        return files;
    }
        /**
     * @return the id
     */
    public Object getPk() {
        return sha;
    }
    /**
     * @param files the files to set
     */
    public void setFiles(List<CommitFile> files) {
        this.files = files;
    }

    /**
     * @return the sha
     */
    public String getSha() {
        return sha;
    }

    /**
     * @param sha the sha to set
     */
    public void setSha(String sha) {
        this.sha = sha;
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

    /**
     * @return the author
     */
    public User getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(User author) {
        this.author = author;
    }

    /**
     * @return the committer
     */
    public User getCommitter() {
        return committer;
    }

    /**
     * @param committer the committer to set
     */
    public void setCommitter(User committer) {
        this.committer = committer;
    }

    private String convertToUTF8(String str) {
         Charset UTF_8 = Charset.forName("UTF-8");
        byte[] byteArray = str.getBytes(UTF_8);
        return new String(byteArray, UTF_8);
    }
}
