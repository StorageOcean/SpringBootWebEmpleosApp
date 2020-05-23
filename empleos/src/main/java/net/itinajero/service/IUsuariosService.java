package net.itinajero.service;

import java.util.List;

import net.itinajero.model.Usuario;

public interface IUsuariosService {
	List<Usuario> buscarTodas();

	Usuario buscarPorId(Integer idUsuario);

	void guardar(Usuario usuario);

	void eliminar(Integer idUsuario);
	
	Usuario buscarPorUserName (String username);
}
