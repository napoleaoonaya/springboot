package curso.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.IntefaceTelefoneRepository;
import curso.springboot.repository.InterfacePessoaRepository;

@Controller
public class PessoaController {

	@Autowired
	private InterfacePessoaRepository interfacePessoaRepository;

	@Autowired
	private IntefaceTelefoneRepository intefaceTelefoneRepository;

	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

		Iterable<Pessoa> pessoaIterable = interfacePessoaRepository.findAll();

		modelAndView.addObject("pessoas", pessoaIterable);
		
		modelAndView.addObject("pessoaobj", new Pessoa());

		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa")
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {
		
		pessoa.setTelefones(intefaceTelefoneRepository.getTelefones(pessoa.getId()));
		
		if(bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			
			Iterable<Pessoa> pessoaIterable = interfacePessoaRepository.findAll();

			modelAndView.addObject("pessoas", pessoaIterable);
			
			modelAndView.addObject("pessoaobj", pessoa);
			
			List<String> msg = new ArrayList<String>();
			for(ObjectError objectError : bindingResult.getAllErrors()){
				msg.add(objectError.getDefaultMessage());//vem das anatoções @NotEmpty NotNull
			}
			
			modelAndView.addObject("msg", msg);
			return modelAndView;
		}
		
		interfacePessoaRepository.save(pessoa);

		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");

		Iterable<Pessoa> pessoaIterable = interfacePessoaRepository.findAll();

		andView.addObject("pessoas", pessoaIterable);
		
		andView.addObject("pessoaobj", new Pessoa());

		return andView;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
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
		
		modelAndView.addObject("pessoaobj", pessoa.get());
		
		modelAndView.addObject("telefones",intefaceTelefoneRepository.getTelefones(idpessoa) );

		return modelAndView;

	}

	@GetMapping("/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {

		Pessoa pessoa = interfacePessoaRepository.findById(idpessoa).get();

		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		
		modelAndView.addObject("pessoaobj", pessoa);
		
		modelAndView.addObject("telefones",intefaceTelefoneRepository.getTelefones(idpessoa) );

		return modelAndView;

	}

	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {

		interfacePessoaRepository.deleteById(idpessoa);

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		modelAndView.addObject("pessoas", interfacePessoaRepository.findAll());
		
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

	@PostMapping("**/addfonePessoa/{pessoaid}")
	public ModelAndView addFonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {

		Pessoa pessoa = interfacePessoaRepository.findById(pessoaid).get();
		
		if(telefone != null && telefone.getNumero().isEmpty() || telefone.getTipo().isEmpty() ){
			
			ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
			modelAndView.addObject("pessoaobj", pessoa);
			modelAndView.addObject("telefones",intefaceTelefoneRepository.getTelefones(pessoaid) );
			
			List<String> msg = new ArrayList<String>();
			
			if(telefone.getNumero().isEmpty()) {
				msg.add("Numero deve ser informado");
			}
			
			if(telefone.getTipo().isEmpty()) {
				msg.add("Numero do tipo deve ser informado");
			}
			
			modelAndView.addObject("msg",msg);
			
			return modelAndView;
		}
		
		telefone.setPessoa(pessoa);
		intefaceTelefoneRepository.save(telefone);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones",intefaceTelefoneRepository.getTelefones(pessoaid) );
		return modelAndView;
	}
	
	@GetMapping("/removertelefone/{idtelefone}")
	public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone) {

		//Carregou o objeto telefone e pessoa
		Pessoa pessoa = intefaceTelefoneRepository.findById(idtelefone).get().getPessoa();
		//Deletei o telefone
		intefaceTelefoneRepository.deleteById(idtelefone);
		//Carreguei para mesma tela 
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		//Passa o objeto pai para mostrar na tela 
		modelAndView.addObject("pessoaobj", pessoa);
		//Carrega os telefones
		modelAndView.addObject("telefones",intefaceTelefoneRepository.getTelefones(pessoa.getId()) );
		
		return modelAndView;

	}

}
