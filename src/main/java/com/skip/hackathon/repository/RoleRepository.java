package com.skip.hackathon.repository;

import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.skip.hackathon.model.Role;

@Transactional(propagation = Propagation.MANDATORY)
public interface RoleRepository extends Repository<Role, Integer>, IBaseRepository<Role, Integer> {

	Role findByRole(String role);

}
