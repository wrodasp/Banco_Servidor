package ec.edu.ups.vista;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuota;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.EstadoCuota;

@ManagedBean
@ViewScoped
public class ResumenCuotas {
	
	private Credito credito;
	
	public ResumenCuotas() {
	}
	
	@PostConstruct
	public void init() {
		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		credito = (Credito) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("credito");
		try {
			if (usuario == null) {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Credito getCredito() {
		return credito;
	}
	
	public EstadoCuota[] getEstadosCuota() {
		return EstadoCuota.values();
	}
	
	public String verDetalleCuota(Cuota cuota) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cuota", cuota);
		return "resumenPago.xhtml?faces-redirect=true";
	}
}
