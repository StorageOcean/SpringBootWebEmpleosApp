package net.itinajero.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.itinajero.model.Categoria;

@Service
public class CategoriaServiceImpl implements ICategoriasService {

	private List<Categoria> lista = null;

	public CategoriaServiceImpl() {
		lista = new LinkedList<Categoria>();
		lista.add(new Categoria(1, "Ventas", "Descripcion Ventas"));
		lista.add(new Categoria(2, "Contabilidad", "Descripcion Contabilidad"));
		lista.add(new Categoria(3, "Transporte", "Descripcion Transporte"));
		lista.add(new Categoria(4, "Informatica", "Descripcion Informatica"));
		lista.add(new Categoria(5, "Construccion", "Descripcion Construccion"));
		lista.add(new Categoria(6, "Sistema & Software", "Descripcion Sistemas"));
	}

	@Override
	public List<Categoria> buscarTodas() {
		// TODO Auto-generated method stub
		return lista;
	}

	@Override
	public Categoria buscarPorId(Integer idCategoria) {
		// TODO Auto-generated method stub
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getId() == idCategoria) {
				return lista.get(i);
			}
		}
		return null;
	}

	@Override
	public void guardar(Categoria categoria) {
		// TODO Auto-generated method stub
		lista.add(categoria);

	}

	@Override
	public void eliminar(Integer idCategoria) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<Categoria> buscarTodas(Pageable page) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
