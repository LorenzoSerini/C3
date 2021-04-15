/*******************************************************************************
 * MIT License

 * Copyright (c) 2021 Lorenzo Serini and Alessandro Pecugi
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
/**
 *
 */

package it.unicam.c3.Persistence;

import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Citta.PuntoRitiro;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Consegne.StatoConsegna;
import it.unicam.c3.Ordini.Ordine;


import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DBConsegne extends DBConnection implements IDBConsegne{
    private String sql;
    private static List<Consegna> consegne;
    private IDBOrdini dbOrdini;
    private IDBPuntiRitiro dbPuntiRitiro;

    public DBConsegne() throws SQLException {
        super();
        this.dbOrdini=new DBOrdini();
        this.dbPuntiRitiro=new DBPuntiRitiro();
        consegne = new LinkedList<>();
    }

    public DBConsegne(String connectionString, String username, String password) throws SQLException {
        super(connectionString,username,password);
        this.dbOrdini=new DBOrdini();
        this.dbPuntiRitiro=new DBPuntiRitiro();
        consegne = new LinkedList<>();
    }


    public DBConsegne(IDBOrdini dbOrdini, IDBPuntiRitiro dbPuntiRitiro, String connectionString, String username, String password) throws SQLException {
        super(connectionString,username,password);
        this.dbOrdini=dbOrdini;
        this.dbPuntiRitiro=dbPuntiRitiro;
        consegne = new LinkedList<>();
    }

    public DBConsegne(IDBOrdini dbOrdini, IDBPuntiRitiro dbPuntiRitiro) throws SQLException {
        super();
        this.dbOrdini=dbOrdini;
        this.dbPuntiRitiro=dbPuntiRitiro;
        consegne = new LinkedList<>();
    }

    @Override
    public List<Consegna> getConsegne() throws Exception {
        if(consegne.isEmpty()) {
            sql = "Select * from Consegne";
            setData(sql);
            while (getData().next()) {
                Commerciante commerciante = this.getCommerciante(getData().getString("Ordine"));
                Ordine ordine = this.getOrdine(getData().getString("Ordine"));
                PuntoRitiro pr = this.getPuntoRitiro(getData().getString("PuntoRitiro"));
                StatoConsegna stato = StatoConsegna.valueOf(getData().getString("Stato"));
                String idConsegna = getData().getString("Id");
                boolean ritirabile = getData().getBoolean("Ritirabile");
                consegne.add(new Consegna(commerciante,ordine,pr,idConsegna));
                consegne.get(consegne.size()-1).setStato(stato);
                consegne.get(consegne.size()-1).setRitirabile(ritirabile);
            }
        }
        return consegne;
    }


    private Commerciante getCommerciante(String idOrdine) throws Exception {
        for(Ordine o:this.dbOrdini.getOrdini()){
            if(o.getId().equals(idOrdine)){
                return o.getPuntoVendita().getCommerciante();
            }
        }
        return null;
    }

    private Ordine getOrdine(String id) throws Exception {
        for(Ordine o:this.dbOrdini.getOrdini()){
            if(o.getId().equals(id)){
                return o;
            }
        }
        return null;
    }

    private PuntoRitiro getPuntoRitiro(String idPuntoRitiro) throws Exception {
        for(PuntoRitiro pr:this.dbPuntiRitiro.getPuntiRitiro()){
            if(pr.getId().equals(idPuntoRitiro)){
                return pr;
            }
        }
        return null;
    }


}
