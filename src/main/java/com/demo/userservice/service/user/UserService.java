package com.demo.userservice.service.user;

import com.demo.userservice.bean.Users;
import com.demo.userservice.entity.group.Group;
import com.demo.userservice.entity.role.Role;
import com.demo.userservice.entity.user.User;
import com.demo.userservice.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String createUser(Users user) {
        User user1 =new User();
        Long id = getNextUserId();
        user1.setId(id);
        user1.setPassword(user.getPassword());
        user1.setUsername(user.getUsername());


        for (Role role : user.getRoles()) {
            entityManager.merge(role);
        }

       /* for (Group group : user.getGroups()) {
            entityManager.merge(group);
        }*/

        user1.setRoles(user.getRoles());
        entityManager.persist(user1);
        return "successfully created user";
    }

    private Long getNextUserId() {
        return ((Number) entityManager.createNativeQuery("SELECT user_seq.NEXTVAL").getSingleResult()).longValue();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return null;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
