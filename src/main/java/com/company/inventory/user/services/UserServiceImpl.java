package com.company.inventory.user.services;

import com.company.inventory.exception.UsuarioFoundException;
import com.company.inventory.user.dao.IRolDao;
import com.company.inventory.user.dao.IUserDao;
import com.company.inventory.user.model.User;
import com.company.inventory.user.model.UserRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDao usuarioRepository;

    @Autowired
    private IRolDao rolRepository;

    @Override
    public User guardarUsuario(User usuario, Set<UserRol> usuarioRoles) throws Exception {
        User usuarioLocal = usuarioRepository.findByUsername(usuario.getUsername());
        if(usuarioLocal != null){
            System.out.println("El usuario ya existe");
            throw new UsuarioFoundException("El usuario ya esta presente");
        }
        else{
            for(UserRol usuarioRol:usuarioRoles){
                rolRepository.save(usuarioRol.getRol());
            }
            usuario.getUsuarioRoles().addAll(usuarioRoles);
            usuarioLocal = usuarioRepository.save(usuario);
        }
        return usuarioLocal;
    }

    @Override
    public User obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public void eliminarUsuario(Long usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }

    @Override
    public void save(User usuario) {

    }



    @Override
    public String obtenerPreguntaSecreta(String username) {
        User usuario = usuarioRepository.findByUsername(username);
        return usuario != null ? usuario.getPreguntaSecreta() : null;
    }

    @Override
    public Optional<User> recuperarUsuario(String preguntaSecreta, String respuestaSecreta) {
        return Optional.empty();
    }

    public User obtenerUsuarioPorNombre(String nombreUsuario) {
        return usuarioRepository.findByUsername(nombreUsuario);
    }


    public boolean validarPreguntaRespuesta(String nombreUsuario, String preguntaSecreta, String respuestaSecreta) {
        User usuario = obtenerUsuarioPorNombre(nombreUsuario);

        return usuario != null && preguntaSecreta.equals(usuario.getPreguntaSecreta()) && respuestaSecreta.equals(usuario.getRespuestaSecreta());
    }

    public void cambiarClave(String nombreUsuario, String nuevaClave) {
        User usuario = obtenerUsuarioPorNombre(nombreUsuario);

        if (usuario != null) {
            usuario.setPassword(nuevaClave);
            usuarioRepository.save(usuario);
        }
    }
}
