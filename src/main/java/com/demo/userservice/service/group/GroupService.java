package com.demo.userservice.service.group;

import com.demo.userservice.entity.group.Group;
import com.demo.userservice.repository.group.GroupRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }


    @Transactional
    public Group createGroup(String groupName) {
        Long id = getNextGroupId(); // Retrieve the next sequence value for the Group entity
        Group group = new Group();
        group.setId(id);
        group.setName(groupName);
        entityManager.persist(group);
        return group;
    }

    private Long getNextGroupId() {
        // Execute the SQL query to get the next value from the sequence
        Query query = entityManager.createNativeQuery("SELECT group_seq.NEXTVAL");
        return ((Number) query.getSingleResult()).longValue();
    }

    public Group getGroup(Long id) {
        return groupRepository.findById(id).orElse(null);
    }

    public Group updateGroup(Long id, Group group) {
        if (groupRepository.existsById(id)) {
            group.setId(id);
            return groupRepository.save(group);
        }
        return null;
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}
