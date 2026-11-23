package cl.duoc.gasvolcan.auth.config;

import cl.duoc.gasvolcan.auth.model.Usuario;
import cl.duoc.gasvolcan.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatosIniciales {

	@Value("${app.seed.admin-password:Admin123!}")
	private String adminPassword;

	@Value("${app.seed.vendedor-password:Vendedor123!}")
	private String vendedorPassword;

	@Value("${app.seed.cliente-password:Cliente123!}")
	private String clientePassword;

	@Bean
	CommandLineRunner usuarioInicial(UsuarioRepository repo, PasswordEncoder encoder) {
		return args -> {
			crearSiNoExiste(repo, encoder, "admin@gaselvolcan.cl", adminPassword, "ADMIN");
			crearSiNoExiste(repo, encoder, "vendedor@gaselvolcan.cl", vendedorPassword, "VENDEDOR");
			crearSiNoExiste(repo, encoder, "cliente@gaselvolcan.cl", clientePassword, "CLIENTE");
		};
	}

	private void crearSiNoExiste(UsuarioRepository repo, PasswordEncoder encoder,
			String correo, String password, String rol) {
		if (repo.findByCorreoIgnoreCase(correo).isEmpty()) {
			Usuario usuario = new Usuario();
			usuario.setCorreo(correo);
			usuario.setPassword(encoder.encode(password));
			usuario.setRol(rol);
			usuario.setHabilitado(true);
			repo.save(usuario);
		}
	}
}
