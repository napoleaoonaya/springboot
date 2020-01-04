package curso.springboot;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encoder {
	
	public static void main(String[] args) {
	
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String resultado = encoder.encode("123");
		System.out.println(resultado);
		
		
	}


}
