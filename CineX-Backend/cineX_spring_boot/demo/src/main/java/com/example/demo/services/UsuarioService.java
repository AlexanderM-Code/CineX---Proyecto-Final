package com.example.demo.services;

import com.example.demo.entities.Usuario;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario create(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario existingUsuario = findById(id);
        existingUsuario.setNombre(usuario.getNombre());
        existingUsuario.setEmail(usuario.getEmail());
        return usuarioRepository.save(existingUsuario);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
}
