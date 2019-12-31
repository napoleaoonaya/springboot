package curso.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.InterfacePessoaRepository;

@Controller
public class PessoaController {
	
	@Autowired
	private InterfacePessoaRepository interfacePessoaRepository;
	
	@RequestMapping(method = RequestMethod.GET, value="/cadastropessoa")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;
	}
	
	@RequestMapping(method = RequestMethod.POST, value="**/salvarpessoa")
	public ModelAndView salvar(Pessoa pessoa) {
		interfacePessoaRepository.save(pessoa);
		
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		
		Iterable<Pessoa> pessoaIterable = interfacePessoaRepository.findAll();
		
		andView.addObject("pessoas", pessoaIterable);
		andView.addObject("pessoaobj", new Pessoa());
		
		return andView;
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/listapessoas")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		
		Iterable<Pessoa> pessoaIterable = interfacePessoaRepository.findAll();
		
		andView.addObject("pessoas", pessoaIterable);
		andView.addObject("pessoaobj", new Pessoa());
		
		return andView;
	}
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {
		
		Optional<Pessoa> pessoa = interfacePessoaRepository.findById(idpessoa);
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj",pessoa.get());
		
		return modelAndView;
		
	}
	
	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {
		
		interfacePessoaRepository.deleteById(idpessoa);
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas",interfacePessoaRepository.findAll());
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		return modelAndView;
		
	}
	
	@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", interfacePessoaRepository.findPessoaByName(nomepesquisa));
		modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;
		
	}
	
}
