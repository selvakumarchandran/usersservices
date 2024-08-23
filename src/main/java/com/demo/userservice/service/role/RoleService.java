package com.demo.userservice.service.role;

import com.demo.userservice.entity.role.Role;
import com.demo.userservice.repository.role.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Transactional
    public Role createRole(String roleName) {
        Long id = getNextRoleId(); // Implement this method to get the next sequence value from Snowflake
        Role role = new Role();
        role.setId(id);
        role.setName(roleName);
        entityManager.persist(role);
        return role;
    }

    private Long getNextRoleId() {
        Query query = entityManager.createNativeQuery("SELECT role_seq.NEXTVAL");
        return ((Number) query.getSingleResult()).longValue();
    }

    public Role getRole(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role updateRole(Long id, Role role) {
        if (roleRepository.existsById(id)) {
            role.setId(id);
            return roleRepository.save(role);
        }
        return null;
    }

    @Transactional
    public void deleteRole(Long roleId) {
        Role role = entityManager.find(Role.class, roleId);
        if (role != null) {
            entityManager.remove(role);
        }
    }
    @Transactional
    public void addSchemasToRole(Long roleId, List<String> schemas) {
        // Insert schema-role associations
        for (String schema : schemas) {
            entityManager.createNativeQuery(
                            "INSERT INTO schema_role_associations (role_id, schema_name) VALUES (?, ?)")
                    .setParameter(1, roleId)
                    .setParameter(2, schema)
                    .executeUpdate();
        }
    }


    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Transactional
    public Map<String, List<Map<String, String>>> getRolesAndSchemasByUsername(String username) {
        // Get user ID based on username
        Long userId = (Long) entityManager.createNativeQuery(
                        "SELECT id FROM \"user\" WHERE username = ?")
                .setParameter(1, username)
                .getSingleResult();

        // Get roles associated with the user
        List<Object[]> roles = entityManager.createNativeQuery(
                        "SELECT r.id, r.name FROM \"role\" r " +
                                "JOIN users_roles ur ON r.id = ur.role_id " +
                                "WHERE ur.user_id = ?")
                .setParameter(1, userId)
                .getResultList();

        // Get schema names for each role
        Map<Long, List<String>> roleSchemas = roles.stream()
                .collect(Collectors.toMap(
                        row -> ((Number) row[0]).longValue(),  // role_id
                        row -> entityManager.createNativeQuery(
                                        "SELECT schema_name FROM schema_role_associations WHERE role_id = ?")
                                .setParameter(1, ((Number) row[0]).longValue())
                                .getResultList()
                ));

        // Prepare the response structure
        return roles.stream().collect(Collectors.toMap(
                row -> (String) row[1],  // role name
                row -> roleSchemas.get(((Number) row[0]).longValue()).stream()
                        .map(schema -> Map.of("schemaName", schema))
                        .collect(Collectors.toList())
        ));
    }

}
