package negocio;

import modelo.Usuario;
import persistencia.UsuarioDAO;

public class UsuarioService {
    private Usuario[] usuarios;
    private int totalUsuarios;
    private UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.usuarios = new Usuario[UsuarioDAO.MAX_USUARIOS];
        this.totalUsuarios = usuarioDAO.cargarTodos(usuarios);
    }

    public Usuario autenticar(String username, String password) throws ProyectoException {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new ProyectoException("Debe ingresar usuario y contraseña.");
        }
        for (int i = 0; i < totalUsuarios; i++) {
            if (usuarios[i] != null
                    && usuarios[i].getUsername().equalsIgnoreCase(username.trim())
                    && usuarios[i].getPassword().equals(password)) {
                return usuarios[i];
            }
        }
        throw new ProyectoException("Usuario o contraseña incorrectos.");
    }
}