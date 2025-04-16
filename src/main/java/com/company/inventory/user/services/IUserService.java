package com.company.inventory.user.services;

import com.company.inventory.user.model.User;
import com.company.inventory.user.model.UserRol;

import java.util.Optional;
import java.util.Set;

public interface IUserService  {

    public User guardarUsuario(User usuario, Set<UserRol> usuarioRoles) throws Exception;

    public User obtenerUsuario(String username);

    public void eliminarUsuario(Long usuarioId);

    void save(User usuario);

    public User obtenerUsuarioPorNombre(String nombreUsuario);

    public String obtenerPreguntaSecreta(String username);

    public void cambiarClave(String nombreUsuario, String nuevaClave);

    public boolean validarPreguntaRespuesta(String nombreUsuario, String preguntaSecreta, String respuestaSecreta);

    public Optional<User> recuperarUsuario(String preguntaSecreta, String respuestaSecreta);
}
