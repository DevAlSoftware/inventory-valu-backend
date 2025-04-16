package com.company.inventory.user.dao;

import com.company.inventory.user.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolDao extends CrudRepository<Rol,Long> {
}
