package curso.springboot.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;

@Entity
public class Pessoa implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "Nome não pode ser vazio")
	@NotNull(message = "Nome não pode ser nulo")
	private String nome;

	@NotEmpty(message = "Sobrenome não pode ser vazio")
	@NotNull(message = "Sobrenome não pode ser nulo")
	private String sobrenome;
	
	@Min(value = 18 , message = "idade invalida")
	private int idade;
	
	//Remove as pessoas e o telefone junto orphanRemoval = true, cascade = CascadeType.ALL
	@OneToMany(mappedBy = "pessoa", orphanRemoval = false, cascade = CascadeType.ALL)
	private List<Telefone> telefones;
	
	public List<Telefone> getTelefones() {
		return telefones;
	}
	
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	
	public int getIdade() {
		return idade;
	}
	
	public void setIdade(int idade) {
		this.idade = idade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
}
