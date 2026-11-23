package cl.duoc.gasvolcan.auth.service;

import cl.duoc.gasvolcan.auth.dto.LoginRequest;
import cl.duoc.gasvolcan.auth.dto.LoginResponse;
import cl.duoc.gasvolcan.auth.model.Usuario;
import cl.duoc.gasvolcan.auth.repository.UsuarioRepository;
import cl.duoc.gasvolcan.auth.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UsuarioRepository usuarios;
	private final PasswordEncoder encoder;
	private final JwtService jwt;

	public AuthService(UsuarioRepository usuarios, PasswordEncoder encoder, JwtService jwt) {
		this.usuarios = usuarios;
		this.encoder = encoder;
		this.jwt = jwt;
	}

	public LoginResponse login(LoginRequest request) {
		Usuario usuario = usuarios.findByCorreoIgnoreCase(request.correo())
				.orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));
		if (!usuario.isHabilitado()) {
			throw new BadCredentialsException("Credenciales invalidas");
		}
		if (!encoder.matches(request.password(), usuario.getPassword())) {
			throw new BadCredentialsException("Credenciales invalidas");
		}
		return new LoginResponse(jwt.generar(usuario), usuario.getCorreo(), usuario.getRol());
	}
}
