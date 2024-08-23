package com.demo.userservice.bean;

import com.demo.userservice.entity.group.Group;
import com.demo.userservice.entity.role.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;


@Data
public class Users {
        private Long id;
        private String username;
        private String password;
        private Set<Role> roles;
        private Set<Group> groups;

}
