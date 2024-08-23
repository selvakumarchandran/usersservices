package com.demo.userservice.entity.group;

import com.demo.userservice.entity.role.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "\"GROUP\"")
public class Group {
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "groups")
    private Set<Role> roles;
}
