package com.company.inventory.user.dao;

import com.company.inventory.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUserDao extends CrudRepository<User,Long> {

    public User findByUsername(String username);

    Optional<User> findByPreguntaSecretaAndRespuestaSecreta(String preguntaSecreta, String respuestaSecreta);



}
