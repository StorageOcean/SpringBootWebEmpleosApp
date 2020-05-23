package net.itinajero.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.itinajero.model.Categoria;

import net.itinajero.service.ICategoriasService;

@Controller
@RequestMapping(value = "/categorias")
public class CategoriasController {
	
	@Autowired
	//@Qualifier("categoriasServiceJpa")
   	private ICategoriasService serviceImpl;

	
	//GetMapping("/index")
	@RequestMapping(value = "/indexPaginate",method =RequestMethod.GET)
	public String mostrarIndex(Model model,Pageable page) {
		Page<Categoria>  list= serviceImpl.buscarTodas(page);
		System.out.println("Cantidad : " + list.getSize());
		model.addAttribute("categorias",list );
		return "categorias/listCategorias";
	}
	
	@RequestMapping(value = "/index",method =RequestMethod.GET)
	public String mostrarIndexPaginado(Model model) {
		List<Categoria>  list= serviceImpl.buscarTodas();
		model.addAttribute("categorias",list );
		return "categorias/listCategorias";
	}
	
	//GetMapping("/create")
	@RequestMapping(value = "/create",method =RequestMethod.GET)
	public String crear(Categoria  categoria) {
		return "categorias/formCategoria";
	}
	
	//@PostMapping("/save")
	@RequestMapping(value="/save",method=RequestMethod.POST)
	public String guardar(Categoria categoria,BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				System.out.println("Error : " + error.getDefaultMessage());
			}
			return "categorias/formCategoria";
		}
		attributes.addFlashAttribute("msg", "Registro Guardado.");
		System.out.println("Categoria : " + categoria);
		serviceImpl.guardar(categoria);
		return "redirect:/categorias/index";
	}
	
	@RequestMapping(value="/edit/{id}",method = RequestMethod.GET)
	public String editar(@PathVariable("id") int idCategoria, Model model) {
		Categoria categoria = serviceImpl.buscarPorId(idCategoria);
		model.addAttribute("categoria",categoria);
		return "categorias/formCategoria";		
	}
	
	@RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
	public String eliminar(@PathVariable("id") int idCategoria, Model model,RedirectAttributes attributes) {
		System.out.println("Borrando categoria con id: " + idCategoria);
		serviceImpl.eliminar(idCategoria);
		attributes.addFlashAttribute("msg","La categoria fue eliminada.");		
		return "redirect:/categorias/index";		
	}
	
}
