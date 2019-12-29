package curso.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.InterfacePessoaRepository;

@Controller
public class PessoaController {
	
	@Autowired
	private InterfacePessoaRepository interfacePessoaRepository;
	
	@RequestMapping(method = RequestMethod.GET, value="/cadastropessoa")
	public String inicio() {
		return "cadastro/cadastropessoa";
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/salvarpessoa")
	public String salvar(Pessoa pessoa) {
		interfacePessoaRepository.save(pessoa);
		return "cadastro/cadastropessoa";
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/listapessoas")
	public ModelAndView pessoas() {
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		
		Iterable<Pessoa> pessoaIterable = interfacePessoaRepository.findAll();
		
		andView.addObject("pessoas", pessoaIterable);
		
		return andView;
	}
}
