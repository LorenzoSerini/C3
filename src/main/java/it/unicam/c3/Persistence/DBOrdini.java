package it.unicam.c3.Persistence;

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Commercio.PuntoVendita;
import it.unicam.c3.Ordini.Ordine;
import it.unicam.c3.Ordini.StatoOrdine;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DBOrdini extends DBConnection implements IDBOrdini{
    private String sql;
    private IDBAccounts accounts;
    private static List<Ordine> ordini;


    public DBOrdini() throws SQLException {
        super();
        this.accounts=new DBAccounts();
        ordini = new LinkedList<>();
    }

    public DBOrdini(String connectionString, String username, String password) throws SQLException {
        super(connectionString,username,password);
        this.accounts=new DBAccounts();
        ordini = new LinkedList<>();
    }


    public DBOrdini(IDBAccounts dbAccounts, String connectionString, String username, String password) throws SQLException {
        super(connectionString,username,password);
        this.accounts=dbAccounts;
        ordini = new LinkedList<>();
    }

    public DBOrdini(IDBAccounts dbAccounts) throws SQLException {
        super();
        this.accounts=dbAccounts;
        ordini = new LinkedList<>();
    }


    @Override
    public List<Ordine> getOrdini() throws Exception {
        if(ordini.isEmpty()) {
            sql = "Select * from Ordini";
            setData(sql);
            while (getData().next()) {
                Cliente cliente = this.getCliente(getData().getString("Cliente"));
                PuntoVendita pv = this.getPV(getData().getString("PuntoVendita"));
                StatoOrdine stato = StatoOrdine.valueOf(getData().getString("Stato"));
                String idOrdine = getData().getString("Id");
                ordini.add(new Ordine(cliente, pv, null, idOrdine, stato));
            }
            for (Ordine o : ordini) putProdotti(o);
        }
        return ordini;
    }


    private Cliente getCliente(String emailCliente) throws Exception {
        for(Cliente c:this.accounts.getClienti()){
            if(c.getEmail().equals(emailCliente)) return c;
        }
        return null;
    }

    private PuntoVendita getPV(String idPV) throws Exception {
       for(Commerciante c:this.accounts.getCommercianti()){
           for(PuntoVendita pv:c.getPuntiVendita()){
               if(pv.getId().equals(idPV)) return pv;
           }
       }
        return null;
    }

    private void putProdotti(Ordine o) throws SQLException {
        sql = "Select * from ListaProdottiOrdine where IdOrdine='"+o.getId()+"'";
        setData(sql);
        while(getData().next()){
            for(Prodotto p:o.getPuntoVendita().getProdotti()){
                if(getData().getString("IdProdotto").equals(p.getId())){
                    o.addProdotto(p);
                }
            }
        }
    }

}
