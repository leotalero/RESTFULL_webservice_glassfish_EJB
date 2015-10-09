package co.sistemcobro.webservice.services;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.log4j.Logger;

import co.sistemcobro.all.constante.EstadoEnum;
import co.sistemcobro.hermes.bean.AccesoHistorial;
import co.sistemcobro.hermes.bean.Sessionserial;
import co.sistemcobro.hermes.bean.UsuarioBean;
import co.sistemcobro.hermes.constante.AccesoTipoEnum;
import co.sistemcobro.hermes.ejb.UsuarioEJB;
import javaxt.utils.Base64;



@Stateless
public class AuthenticationService extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@EJB
	private UsuarioEJB usuarioEJB;
	
	public AuthenticationService() {
		super();
	}
	
	
	private Logger logger = Logger.getLogger(AuthenticationService.class);
	
	public boolean authenticate(String authCredentials) {
		//HttpServletRequest req = (HttpServletRequest) request;
		//HttpServletResponse resp = (HttpServletResponse) response;
		//resp.setHeader("Pragma", "No-cache");
		//resp.setHeader("Cache-Control", "no-cache");
		//resp.setDateHeader("Expires", 1); 
		//HttpSession session = req.getSession();
		if (null == authCredentials)
			return false;
		// header value format will be "Basic encodedstring" for Basic
		// authentication. Example "Basic YWRtaW46YWRtaW4="
		final String encodedUserPassword = authCredentials.replaceFirst("Basic"
				+ " ", "");
		String usernameAndPassword = null;
		try {
			byte[] decodedBytes = Base64.decode(
					encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		final StringTokenizer tokenizer = new StringTokenizer(
				usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();

		// we have fixed the userid and password as admin
		// call some UserService/LDAP here
		//boolean authenticationStatus = "admin".equals(username)
		//		&& "admin".equals(password);
		boolean authenticationStatus=false;
		try {
			String usuario=username ;
			String pass=password ;
			UsuarioBean usuariobean = null;
			//List<EmpleadoBean> empleados=new ArrayList<EmpleadoBean>();
			if(usuario!=null && pass!=null){
				InitialContext ic = new InitialContext();  
				usuarioEJB = (UsuarioEJB) ic.lookup("java:global/WebServiceGlassfish/UsuarioEJB"); 
				
				//UsuarioEJB usuarioejb=new UsuarioEJB();
				UsuarioBean activo = usuarioEJB.isUsuario(usuario, pass);
				if(activo!=null){
					usuariobean=activo;
									
					 authenticationStatus=true;
				}else{
					return false;
				}
				
			}else {
				return false;	
			}
			
			
			//session.setAttribute("usuariologueado", usuariobean);
			
			//request.setAttribute("empleado", empleado);
			///List<Result> results = someDAO.list();
			String json = new Gson().toJson(usuariobean);
		
		
			} catch (Exception e) {
			logger.error(e.toString(), e.fillInStackTrace());
			
		}
		
		
		return authenticationStatus;
	}
}