package com.demo.userservice.entity.role;

import com.demo.userservice.entity.group.Group;
import com.demo.userservice.entity.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "\"ROLE\"")
public class Role {
    @Id
    /*@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)*/
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    @ManyToMany
    @JoinTable(
        name = "groups_roles",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Group> groups;

    @ElementCollection
    @CollectionTable(
            name = "schema_role_associations",
            joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "schema_name")
    private Set<String> schemas;

}
