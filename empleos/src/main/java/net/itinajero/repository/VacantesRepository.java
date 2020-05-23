package net.itinajero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import net.itinajero.model.Vacante;

public interface VacantesRepository extends JpaRepository<Vacante,Integer>{
	List<Vacante> findByEstatus(String estatus);
	List<Vacante> findByDestacadoAndEstatusOrderByIdDesc(int destacado,String estatus);
	List<Vacante> findBySalarioBetween(double salarioi,double salariof);
	List<Vacante> findByEstatusIn(String[] estatus);
}
