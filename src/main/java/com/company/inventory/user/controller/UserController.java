package com.company.inventory.user.controller;

import com.company.inventory.security.RecuperarClaveRequest;
import com.company.inventory.user.dao.IUserDao;
import com.company.inventory.user.model.Rol;
import com.company.inventory.user.model.User;
import com.company.inventory.user.model.UserRol;
import com.company.inventory.user.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = {"http://localhost:4200", "http://69.62.67.49:4200"})
public class UserController {

    @Autowired
    private IUserService usuarioService;

    @Autowired
    private IUserDao usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/usuarios")
    public User guardarUsuario(@RequestBody User usuario) throws Exception{
        usuario.setPerfil("default.png");

        usuario.setPassword(this.bCryptPasswordEncoder.encode(usuario.getPassword()));

        Set<UserRol> usuarioRoles = new HashSet<>();

        Rol rol = new Rol();
        if (!usuario.isAdmin()) {
            rol.setRolId(2L);
            rol.setRolNombre("NORMAL");
        } else {
            rol.setRolId(1L);
            rol.setRolNombre("ADMIN");
        }
        UserRol usuarioRol = new UserRol();
        usuarioRol.setUsuario(usuario);
        usuarioRol.setRol(rol);

        usuarioRoles.add(usuarioRol);
        return usuarioService.guardarUsuario(usuario,usuarioRoles);
    }


    @GetMapping("/usuarios/{username}")
    public User obtenerUsuario(@PathVariable("username") String username){
        return usuarioService.obtenerUsuario(username);
    }

    @DeleteMapping("/usuarios/{usuarioId}")
    public void eliminarUsuario(@PathVariable("usuarioId") Long usuarioId){
        usuarioService.eliminarUsuario(usuarioId);
    }

    @PostMapping("/usuarios/recuperar-usuario")
    public String recuperarClave(@RequestBody RecuperarClaveRequest request) {
        String nombreUsuario = request.getNombreUsuario();
        String preguntaSecreta = request.getPreguntaSecreta();
        String respuestaSecreta = request.getRespuestaSecreta();
        String nuevaClave = request.getNuevaClave();

        if (usuarioService.validarPreguntaRespuesta(nombreUsuario, preguntaSecreta, respuestaSecreta)) {
            usuarioService.cambiarClave(nombreUsuario, nuevaClave);
            return "Clave recuperada exitosamente.";
        } else {
            return "Validación de pregunta y respuesta fallida.";
        }
    }
}