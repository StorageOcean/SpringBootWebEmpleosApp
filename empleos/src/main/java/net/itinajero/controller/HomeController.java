package net.itinajero.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jdk.nashorn.internal.objects.annotations.Getter;
import net.itinajero.model.Perfil;
import net.itinajero.model.Usuario;
import net.itinajero.model.Vacante;
import net.itinajero.service.ICategoriasService;
import net.itinajero.service.IUsuariosService;
import net.itinajero.service.IVacantesService;
import net.itinajero.service.VacanteServiceImpl;

@Controller
public class HomeController {

	@Autowired
	private IVacantesService serviceImpl;

	@Autowired
	private IUsuariosService usuarImpl;

	@Autowired
	private ICategoriasService iCategoriaService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/tabla")
	public String mostrarTabla(Model model) {
		List<Vacante> lista = serviceImpl.buscarTodas();// getVacantes();
		model.addAttribute("vacantes", lista);
		return "tabla";
	}

	@GetMapping("/index")
	public String mostrarIndex(Authentication auth,HttpSession session) {
		String username = auth.getName();
		System.out.println("Nombre del usuario: " + username);
		
		for (GrantedAuthority rol : auth.getAuthorities()) {
			System.out.println("Nombre rol: " + rol);
		}
		
		if (session.getAttribute("usuario") == null) {
			Usuario usuario = usuarImpl.buscarPorUserName(username);
			usuario.setPassword(null);
			session.setAttribute("usuario", usuario);
			System.out.println("Nombre usuario: " + usuario);
		}
		
		
		return "redirect:/";
	}

	@GetMapping("/signup")
	public String registrarse(Usuario usuario) {
		return "formRegistro";
	}

	@PostMapping("/signup")
	public String guardarRegistro(Usuario usuario, RedirectAttributes attributes) {
		String pwdPlano = usuario.getPassword();
		String pwdEncriptado = passwordEncoder.encode(pwdPlano);
		usuario.setPassword(pwdEncriptado);
		usuario.setEstatus(1); // Activado por defecto
		usuario.setFechaRegistro(new Date()); // Fecha de Registro, la fecha actual del servidor

		// Creamos el Perfil que le asignaremos al usuario nuevo
		Perfil perfil = new Perfil();
		perfil.setId(13); // Perfil USUARIO
		usuario.agregar(perfil);

		/**
		 * Guardamos el usuario en la base de datos. El Perfil se guarda automaticamente
		 */
		usuarImpl.guardar(usuario);

		attributes.addFlashAttribute("msg", "El registro fue guardado correctamente!");

		return "redirect:/usuarios/index";
	}

	@GetMapping("/detalle")
	public String mostrarDetalle(Model model) {
		Vacante objVacante = new Vacante();
		objVacante.setNombre("Ingeniero de comunicaciones");
		objVacante.setDescripcion("Se solicita ingeniero para dar soporte intranet.");
		objVacante.setFecha(new Date());
		objVacante.setSalario(9700.0);
		model.addAttribute("vacante", objVacante);
		return "detalle";
	}

	@GetMapping("/listado")
	public String mostrarListado(Model model) {
		List<String> lista = new LinkedList<String>();
		lista.add("Ingeniero de Sistemas");
		lista.add("Auxiliar de Contabilidad");
		lista.add("Vendedor");
		lista.add("Arquitecto");
		model.addAttribute("empleos", lista);
		return "listado";
	}

	@GetMapping("/")
	public String mostrarHome(Model model) {
		/*
		 * model.addAttribute("mensaje","Bienvenidos a Empleos App");
		 * model.addAttribute("fecha",new Date());
		 */
		/*
		 * String nombre = "Auxiliar de Contabilidad"; Date fechaPub = new Date();
		 * double salario = 9000.0; boolean vigente = true;
		 * 
		 * 
		 * model.addAttribute("nombre", nombre); model.addAttribute("fecha", fechaPub );
		 * model.addAttribute("salario", salario); model.addAttribute("vigente",
		 * vigente);
		 */

		/*
		 * List<Vacante> lista = serviceImpl.buscarTodas();//getVacantes();
		 * model.addAttribute("vacantes",lista);
		 */

		return "home";
	}

	/**
	 * Metodo que regresa una lista de objetos de tipo vacante.
	 * 
	 * @return
	 */
	private List<Vacante> getVacantes() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		List<Vacante> lista = new LinkedList<Vacante>();
		try {
			// Creamos la oferta de Trabajo 1
			Vacante vacante1 = new Vacante();
			vacante1.setId(1);
			vacante1.setNombre("Ingeniero Civil");
			vacante1.setDescripcion("Solicitamos Ing. Civil para diseñar puente peatonal.");
			vacante1.setFecha(sdf.parse("08-02-2019"));
			vacante1.setSalario(8500.0);
			vacante1.setDestacado(1);
			vacante1.setImagen("empresa1.png");

			// Creamos la oferta de Trabajo 2
			Vacante vacante2 = new Vacante();
			vacante2.setId(2);
			vacante2.setNombre("Contador Publico");
			vacante2.setDescripcion("Empresa importante solicita contador con 5 años de experiencia titulado.");
			vacante2.setFecha(sdf.parse("09-02-2019"));
			vacante2.setSalario(12000.0);
			vacante2.setDestacado(0);
			vacante2.setImagen("empresa2.png");

			// Creamos la oferta de Trabajo 3
			Vacante vacante3 = new Vacante();
			vacante3.setId(3);
			vacante3.setNombre("Ingeniero Electrico");
			vacante3.setDescripcion(
					"Empresa internacional solicita Ingeniero Mecanico para matenimiento de la instalacion electrica.");
			vacante3.setFecha(sdf.parse("10-02-2019"));
			vacante3.setSalario(10500.0);
			vacante3.setDestacado(0);

			// Creamos la oferta de Trabajo 4
			Vacante vacante4 = new Vacante();
			vacante4.setId(4);
			vacante4.setNombre("Ingeniero Electrico");
			vacante4.setDescripcion("Solicitamos diseñador Grafico titulado para diseñar publicidad de la empresa.");
			vacante4.setFecha(sdf.parse("11-02-2019"));
			vacante4.setSalario(7500.0);
			vacante4.setDestacado(1);
			vacante4.setImagen("empresa3.png");

			lista.add(vacante1);
			lista.add(vacante2);
			lista.add(vacante3);
			lista.add(vacante4);

		} catch (ParseException e) {
			System.out.println("Error: " + e.getMessage());
			// TODO: handle exception
		}
		return lista;
	}

	@GetMapping("/search")
	public String buscar(@ModelAttribute("search") Vacante vacante, Model model) {
		System.out.println("Buscando por : " + vacante);
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("descripcion",
				ExampleMatcher.GenericPropertyMatchers.contains());
		Example<Vacante> example = Example.of(vacante, matcher);
		List<Vacante> lista = serviceImpl.buscarByExample(example);
		model.addAttribute("vacantes", lista);
		return "home";
	}
	
	/**
	 * Método que muestra el formulario de login personalizado.
	 * @return
	 */
	@GetMapping("/login")
	public String mostrarLogin() {
		return "formLogin";
	}
	
	/**
	 * Método personalizado para cerrar la sesión del usuario
	 * @param request
	 * @return
	 */
	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}
	
	/**
     * Utileria para encriptar texto con el algorito BCrypt
     * @param texto
     * @return
     */
    @GetMapping("/bcrypt/{texto}")
    @ResponseBody
   	public String encriptar(@PathVariable("texto") String texto) {    	
   		return texto + " Encriptado en Bcrypt: " + passwordEncoder.encode(texto);
   	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@ModelAttribute
	public void setGenericos(Model model) {
		Vacante vacanteSearch = new Vacante();
		vacanteSearch.reset();
		model.addAttribute("vacantes", serviceImpl.buscarDestacadas());
		model.addAttribute("categorias", iCategoriaService.buscarTodas());
		model.addAttribute("search", vacanteSearch);
	}
}
