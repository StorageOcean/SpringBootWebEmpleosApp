package net.itinajero.service.db;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.itinajero.model.Vacante;
import net.itinajero.repository.VacantesRepository;
import net.itinajero.service.IVacantesService;


@Service
@Primary
public class VacantesServiceJpa implements IVacantesService {

	@Autowired
	private VacantesRepository vacanteRepository;
	
	@Override
	public List<Vacante> buscarTodas() {
		// TODO Auto-generated method stub
		List<Vacante> lista = vacanteRepository.findAll();
		return lista;
	}

	@Override
	public Vacante buscarPorId(Integer idVacante) {
		// TODO Auto-generated method stub
		Optional<Vacante> optional = vacanteRepository.findById(idVacante);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public void guardar(Vacante vacante) {
		// TODO Auto-generated method stub
		vacanteRepository.save(vacante);
		
	}

	@Override
	public List<Vacante> buscarDestacadas() {
		// TODO Auto-generated method stub
		return vacanteRepository.findByDestacadoAndEstatusOrderByIdDesc(1,"Aprobada");
	}

	@Override
	public void eliminar(Integer idVacante) {
		// TODO Auto-generated method stub
		 vacanteRepository.deleteById(idVacante);
	}

	@Override
	public List<Vacante> buscarByExample(Example<Vacante> example) {
		// TODO Auto-generated method stub
		return vacanteRepository.findAll(example);
	}

	@Override
	public Page<Vacante> buscarTodas(Pageable page) {		
		return vacanteRepository.findAll(page);
	}

	

}
