package com.demo.userservice.controller.role;

import com.demo.userservice.entity.role.Role;
import com.demo.userservice.service.role.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role.getName());
        return ResponseEntity.ok(createdRole);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        Role role = roleService.getRole(id);
        if (role == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(role);
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRole() {
        List<Role> roles = roleService.getAllRole();
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        Role updatedRole = roleService.updateRole(id, role);
        if (updatedRole == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return null;
                // ResponseEntity.noContent().build();
    }


    @PostMapping("/{roleId}/schemas")
    public void addSchemasToRole(@PathVariable Long roleId, @RequestBody List<String> schemas) {
        roleService.addSchemasToRole(roleId, schemas);
    }


    @GetMapping("/by-username")
    public Map<String, List<Map<String, String>>> getRolesAndSchemasByUsername(@RequestParam String username) {
        return roleService.getRolesAndSchemasByUsername(username);
    }
}
