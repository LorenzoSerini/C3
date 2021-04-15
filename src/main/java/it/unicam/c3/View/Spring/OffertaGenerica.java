package it.unicam.c3.View.Spring;

import it.unicam.c3.Commercio.IOfferta;
import it.unicam.c3.Commercio.OffertaATempo;
import it.unicam.c3.Commercio.PuntoVendita;

import java.time.LocalDate;

public class OffertaGenerica {
    private String id;
    private String descrizione, importo;
    private LocalDate scadenza;
    private PuntoVendita puntoVendita;

    public OffertaGenerica() {

    }

    public OffertaGenerica(IOfferta offerta) {
        id = offerta.getId();
        descrizione = offerta.getDescrizione();
        importo = offerta.getImporto();
        if (offerta instanceof OffertaATempo)
            scadenza = ((OffertaATempo) offerta).getScadenza();
    }

    public OffertaGenerica(IOfferta offerta, PuntoVendita puntoVendita) {
        this(offerta);
        this.puntoVendita = puntoVendita;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getImporto() {
        return importo;
    }

    public void setImporto(String importo) {
        this.importo = importo;
    }

    public LocalDate getScadenza() {
        return scadenza;
    }

    public void setScadenza(LocalDate scadenza) {
        this.scadenza = scadenza;
    }

    public boolean hasScadenza() {
        return scadenza != null;
    }

    public PuntoVendita getPuntoVendita() {
        return puntoVendita;
    }

    public void setPuntoVendita(PuntoVendita puntoVendita) {
        this.puntoVendita = puntoVendita;
    }
}
