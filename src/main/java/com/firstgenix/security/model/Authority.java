package com.firstgenix.security.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "AUTHORITY")
public class Authority implements Serializable {

    @Id
    @Column(name = "ID")
    private String id=UUID.randomUUID().toString().toUpperCase();

    @Column(name = "NAME", length = 50)
    @NotNull
    private String name;

    @ManyToMany(mappedBy = "authorities",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;
    

	public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    

}