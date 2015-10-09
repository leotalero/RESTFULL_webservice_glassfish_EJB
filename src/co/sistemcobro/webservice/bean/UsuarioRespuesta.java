package co.sistemcobro.webservice.bean;
import java.util.List;

import co.sistemcobro.hermes.bean.UsuarioAplicacionBean;
import co.sistemcobro.hermes.bean.UsuarioBean;

public class UsuarioRespuesta {

	public UsuarioBean usauriobean;
	public Errorwebservice error;
	public String sessioninfo;
	public List<UsuarioAplicacionBean> aplicaciones;
	public UsuarioBean getUsauriobean() {
		return usauriobean;
	}
	public void setUsauriobean(UsuarioBean usauriobean) {
		this.usauriobean = usauriobean;
	}
	public Errorwebservice getError() {
		return error;
	}
	public void setError(Errorwebservice error) {
		this.error = error;
	}
	public String getSessioninfo() {
		return sessioninfo;
	}
	public void setSessioninfo(String sessioninfo) {
		this.sessioninfo = sessioninfo;
	}
	public List<UsuarioAplicacionBean> getAplicaciones() {
		return aplicaciones;
	}
	public void setAplicaciones(List<UsuarioAplicacionBean> aplicaciones) {
		this.aplicaciones = aplicaciones;
	}

	
	
	
	
	
	
}
