package net.itinajero.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.itinajero.model.Categoria;
import net.itinajero.model.Vacante;
import net.itinajero.service.ICategoriasService;
import net.itinajero.service.IVacantesService;
 
import net.itinajero.util.Utileria;
 

@Controller
@RequestMapping(value = "/vacantes")
public class VacanteController {
	
	@Value("${empleosapp.ruta.images}")
	private String ruta;
	
	@Autowired
	private IVacantesService vacServiceImpl;
	
	@Autowired
	private ICategoriasService catServiceImpl;

	@GetMapping(value = "/index")
	public String mostrarIndex(Model model) {
		List<Vacante> list = vacServiceImpl.buscarTodas();
		model.addAttribute("lista", list);
		return "vacantes/listVacantes";
	}
	
	@GetMapping(value = "/indexPaginate")
	public String mostrarIndexPaginado(Model model, Pageable page) {
		Page<Vacante>lista = vacServiceImpl.buscarTodas(page);
		System.out.println("Cantidad : " + lista.getSize());
		model.addAttribute("lista", lista);
		return "vacantes/listVacantes";
	}

	@GetMapping(value = "/create")
	public String crear(Vacante vacante,Model model) {
		//List<Categoria> categorias = catServiceImpl.buscarTodas();
		//model.addAttribute("categorias", categorias);
		return "vacantes/formVacante";
	}

	@PostMapping(value = "/save")
	public String guardar(Vacante vacante, BindingResult result,RedirectAttributes attributes,
			@RequestParam("archivoImagen") MultipartFile multiPart) {
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Error : " + error.getDefaultMessage());
			}
			return "vacantes/formVacante";
		}
		
		if (!multiPart.isEmpty()) {			
			//String ruta = "c:/empleos/img-vacantes/"; // Windows
			
			String nombreImagen = Utileria.guardarArchivo(multiPart, ruta);
			if (nombreImagen != null){ // La imagen si se subio
				// Procesamos la variable nombreImagen
				vacante.setImagen(nombreImagen);
			}
		}
		
		attributes.addFlashAttribute("msg", "Registro Guardado.");
		System.out.println("Vacante : " + vacante);
		vacServiceImpl.guardar(vacante);
		return "redirect:/vacantes/index";
	}

	/*
	 * @PostMapping(value = "/save") public String
	 * guardar(@RequestParam("nombre")String
	 * nombre,@RequestParam("descripcion")String descripcion,
	 * 
	 * @RequestParam("categoria")String categoria, @RequestParam("estatus")String
	 * estatus,
	 * 
	 * @RequestParam("fecha")String fecha, @RequestParam("destacado")Integer
	 * destacado,
	 * 
	 * @RequestParam("salario")Double salario, @RequestParam("detalles")String
	 * detalles) {
	 * 
	 * System.out.println("nombre "+nombre);
	 * System.out.println("descripcion "+descripcion);
	 * System.out.println("categoria "+categoria);
	 * System.out.println("estatus "+estatus); System.out.println("fecha "+fecha);
	 * System.out.println("destacado "+destacado);
	 * System.out.println("salario "+salario);
	 * System.out.println("detalles "+detalles); return "vacantes/listVacantes"; }
	 */

	@GetMapping(value = "/delete/{id}")
	public String eliminar(@PathVariable("id") int idVacante,RedirectAttributes attributes) {
		System.out.println("Borrando vacante con id: " + idVacante);
		//model.addAttribute("id", idVacante);
		vacServiceImpl.eliminar(idVacante);
		attributes.addFlashAttribute("msg","La vacante fue eliminada.");		
		return "redirect:/vacantes/index";
	}

	@GetMapping(value = "/view/{id}")
	public String verDetalle(@PathVariable("id") int idVacante, Model model) {
		Vacante vacante = vacServiceImpl.buscarPorId(idVacante);
		System.out.println("IdVacante:" + idVacante);
		System.out.println("Vacante:" + vacante.toString());
		model.addAttribute("idVacante", idVacante);
		model.addAttribute("vacante", vacante);
		// return "vacantes/detalle";
		return "detalle";
	}
	
	@GetMapping(value = "/edit/{id}")
	public String editar(@PathVariable("id") int idVacante, Model model) {
		Vacante vacante = vacServiceImpl.buscarPorId(idVacante);
		//List<Categoria> categorias = catServiceImpl.buscarTodas();
		model.addAttribute("vacante",vacante);
		//model.addAttribute("categorias", categorias);
		return "vacantes/formVacante";
	}
	
	@ModelAttribute
	public void setGenericos(Model model) {
		List<Categoria> categorias = catServiceImpl.buscarTodas();
		model.addAttribute("categorias",categorias);
	}

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

}
