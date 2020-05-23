package net.itinajero.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.itinajero.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
	Usuario findByUsername(String username);
}
