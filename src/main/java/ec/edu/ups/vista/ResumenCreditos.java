package ec.edu.ups.vista;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import ec.edu.ups.modelos.Credito;
import ec.edu.ups.modelos.Cuenta;
import ec.edu.ups.modelos.Usuario;
import ec.edu.ups.modelos.enums.TipoUsuario;
import ec.edu.ups.negocio.ProcesoGestionLocalON;

@ManagedBean
@ViewScoped
public class ResumenCreditos {
	
	
	@Inject
	private ProcesoGestionLocalON procesoGestion;
	
	private Cuenta cuenta;

	@PostConstruct
	public void init() {
		Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		try {
			cuenta = procesoGestion.listarCuentas()
		               .stream()
		               .filter(c -> c.getPropietario().getCedula().equals(usuario.getPropietario().getCedula()))
		               .findFirst().get();
		} catch (Exception e) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml?faces-redirect=true");
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public DateTimeFormatter getFormatoFecha() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy");
	}
	
	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}
	
	public String estadoCredito(Credito credito) {
		if (LocalDate.now().isBefore(credito.getFechaVencimiento())) {
			if(credito.getMonto() == credito.getSaldo()) {
				return "PAGADO";
			} else {
				return "PENDIENTE";
			}
		}
		return "EN MORA";
	}
	
	public void solicitarCredito() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cuenta", cuenta);
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("solicitudCredito.xhtml?faces-redirect=true");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	*
	*/
	public void verCuotas(Credito credito) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("credito", credito);
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("resumenCuotas.xhtml?faces-redirect=true");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
