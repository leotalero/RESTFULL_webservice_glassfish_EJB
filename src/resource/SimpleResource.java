package resource;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.SerializationUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import co.sistemcobro.all.constante.EstadoEnum;
import co.sistemcobro.all.exception.LogicaException;
import co.sistemcobro.all.util.Base64TC;
import co.sistemcobro.hermes.bean.AccesoHistorial;
import co.sistemcobro.hermes.bean.Sessionserial;
import co.sistemcobro.hermes.bean.UsuarioAplicacionBean;
import co.sistemcobro.hermes.bean.UsuarioBean;
import co.sistemcobro.hermes.constante.AccesoTipoEnum;
import co.sistemcobro.hermes.ejb.UsuarioEJB;
import co.sistemcobro.webservice.bean.Errorwebservice;
import co.sistemcobro.webservice.bean.UsuarioRespuesta;
import co.sistemcobro.webservice.constante.ErrorTipoEnum;

//import co.sistemcobro.hermes.bean.UsuarioBean;

//import com.google.gson.Gson;



@Path("simple") 
@Stateless
public class SimpleResource {
	@EJB
	private UsuarioEJB usuarioEJB;
	
    @SuppressWarnings("unused")
    @Context
    private UriInfo context;

    /**
     * Default constructor. 
     */
    public SimpleResource() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retrieves representation of an instance of SimpleResource
     * @return an instance of String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        // TODO return proper representation object
       return "<greeting>Hello...</greeting>";
    }

    /**
     * PUT method for updating or creating an instance of SimpleResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
    
    
  /*  @Path("login")
    @GET
	@Produces("application/json")
    public Response convertFtoCfromInput(@PathParam("f") float f) throws JSONException {
    	 
		JSONObject jsonObject = new JSONObject();
		float celsius;
		celsius =  (f - 32)*5/9; 
		jsonObject.put("F Value", f); 
		jsonObject.put("C Value", celsius);
 
		String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n" + jsonObject;
		return Response.status(200).entity(result).build();
    	
	  }*/
    
    
    @Path("/login")
    @POST  
    @Produces("application/json")
    public Response doSayHelloWithFormParam(@FormParam("nombredeusuario") String usuario,@FormParam("password") String password ,@FormParam("device_info") String deviceinfo ,@Context HttpServletRequest request) {
    	
    	UsuarioBean usuariobean = new UsuarioBean();
    	UsuarioBean usuariobeanfinal = new UsuarioBean();
    	JSONObject jsonObject = new JSONObject();
    	
    	Errorwebservice errorclase =new Errorwebservice();
    	UsuarioBean activo;
    	UsuarioRespuesta usuariorespuesta = new UsuarioRespuesta();
    	String json = "";
    	try {
			activo = usuarioEJB.getUsuarioPorUsuario(usuario);
		
			if(activo!=null){
				
				usuariobean=activo;
					///////inserta registro de acceso en accesohistorico
								AccesoHistorial accesohistorial = new AccesoHistorial();
								accesohistorial.setIdusuario(activo.getIdusuario());
								accesohistorial.setIdaplicacion(72);//aplicacion movil
								accesohistorial.setIdaccesotipo(AccesoTipoEnum.ACCCESO_CONCEDIDO.getIndex());
								accesohistorial.setIdusuariocrea(usuariobean.getCodusuario());
								accesohistorial.setEstado(EstadoEnum.ACTIVO.getIndex());
								usuariobean.setClave(password);
								 Sessionserial sessionserial = new Sessionserial();
								sessionserial.setUsuario(usuariobean);
								//sessionserial.setExiste(existe);
								// int a = session.getMaxInactiveInterval();
								String ip = request.getRemoteAddr();
								sessionserial.setIp(ip);
								String useragent = request.getHeader("User-Agent");
								sessionserial.setUseragent(useragent);
								sessionserial.setDeviceinfo(deviceinfo);
								byte[] data = SerializationUtils.serialize(sessionserial);
								accesohistorial.setSessionserializada(data);
								accesohistorial.setEstadosession(EstadoEnum.ACTIVO.getIndex());
								usuarioEJB.insertaAccesoHistorial(accesohistorial);
								accesohistorial.setIdusuariomod(usuariobean.getIdusuario());
								usuarioEJB.actualizarAccesoHistorial(accesohistorial);
								Date fecha = new Date();
								String idaccesohistoricocod = accesohistorial.getIdaccesohistorial()+"_"+fecha.getTime();
								Base64TC base64=new Base64TC();
								String cod = base64.codificar(idaccesohistoricocod);
								
								/////
				
								usuariobeanfinal.setIdusuario(activo.getCodusuario());
								usuariobeanfinal.setNombre(activo.getNombre());
								usuariobeanfinal.setUsuario(activo.getUsuario());
								usuariobeanfinal.setEstado(activo.getEstado());
								usuariobeanfinal.setCodigoidentificacion(usuariobean.getCodigoidentificacion());
								usuariobeanfinal.setNumidentificacion(usuariobean.getNumidentificacion());
								usuariobeanfinal.setIdentificaciontipo(usuariobean.getIdentificaciontipo());
								List<UsuarioAplicacionBean> usuarioaplicaciones = usuarioEJB.getUsuarioAplicacionPorIdusuarioconLink(usuariobean.getIdusuario());
								
								
								
								
				errorclase.setCodigo(ErrorTipoEnum.CORRECTO.getIndex());
				errorclase.setError(ErrorTipoEnum.CORRECTO.toString());
				usuariorespuesta.setUsauriobean(usuariobeanfinal);
				usuariorespuesta.setAplicaciones(usuarioaplicaciones);
				usuariorespuesta.setSessioninfo(cod);
				usuariorespuesta.setError(errorclase);
				
			}else{
				//return false;
				
				errorclase.setCodigo(ErrorTipoEnum.ERROR_USUARIO_NO_ENCONTRADO.getIndex());//1.error usuario o contrasena
				errorclase.setError(ErrorTipoEnum.ERROR_USUARIO_NO_ENCONTRADO.toString());//1.error usuario o contrasena
				usuariorespuesta.setError(errorclase);
			
			}
			 json = new Gson().toJson(usuariorespuesta);
		} catch (LogicaException e) {
			// TODO Auto-generated catch block
			errorclase.setCodigo(ErrorTipoEnum.ERROR_VALIDANDO_USUARIO.getIndex());//1.error usuario o contrasena
			errorclase.setError(ErrorTipoEnum.ERROR_VALIDANDO_USUARIO.toString());//1.error usuario o contrasena
			
			usuariorespuesta.setError(errorclase);
			json = new Gson().toJson(usuariorespuesta);
			e.printStackTrace();
		}
		
    	
    	// json = new Gson().toJson(usuariobean);
		return Response.status(200).entity(json).build();
    }

}