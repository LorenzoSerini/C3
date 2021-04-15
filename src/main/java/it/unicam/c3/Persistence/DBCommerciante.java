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
import it.unicam.c3.Commercio.*;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Ordini.Ordine;
import it.unicam.c3.Ordini.StatoOrdine;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class DBCommerciante extends DBConnection implements IDBCommerciante {
    private Commerciante commerciante;
    private String sql;

    public DBCommerciante(Commerciante commerciante) throws SQLException {
        super();
        this.commerciante = commerciante;
    }

    public DBCommerciante(Commerciante commerciante, String connectionString, String username, String password) throws SQLException {
        super(connectionString,username,password);
        this.commerciante = commerciante;
    }

    @Override
    public void saveProdotto(PuntoVendita pv, Prodotto prodotto) throws SQLException {
        sql = "insert into Prodotti(Id,Descrizione, Prezzo, Disponibilita, PuntoVendita) values (?,?,?,?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, prodotto.getId());
        prepStat.setString(2, prodotto.getDescrizione());
        prepStat.setDouble(3, prodotto.getPrezzo());
        prepStat.setBoolean(4, prodotto.getDisponibilita());
        prepStat.setString(5, pv.getId());
        prepStat.executeUpdate();
    }

    @Override
    public void updateDisponibilitaProdotto(Prodotto prodotto) throws SQLException {
        sql = "update Prodotti set Disponibilita=? where Id='"+prodotto.getId()+"'";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setBoolean(1, prodotto.getDisponibilita());
        prepStat.executeUpdate();
    }

    @Override
    public void saveOfferta(PuntoVendita pv, IOfferta offerta) throws SQLException {
        sql = "insert into Offerte(Id,Descrizione, Importo, PuntoVendita, Scadenza) values (?,?,?,?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, offerta.getId());
        prepStat.setString(2, offerta.getDescrizione());
        prepStat.setString(3, offerta.getImporto());
        prepStat.setString(4, pv.getId());
        prepStat.setNull(5,  19);
        prepStat.executeUpdate();
    }

    @Override
    public void saveOfferta(PuntoVendita pv, IOfferta offerta, LocalDate date) throws SQLException {
        sql = "insert into Offerte(Id,Descrizione, Importo, PuntoVendita, Scadenza) values (?,?,?,?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, offerta.getId());
        prepStat.setString(2, offerta.getDescrizione());
        prepStat.setString(3, offerta.getImporto());
        prepStat.setString(4, pv.getId());
        prepStat.setDate(5, Date.valueOf(date));
        prepStat.executeUpdate();
    }

    @Override
    public void savePuntoVendita(PuntoVendita puntoVendita) throws SQLException {
        sql = "insert into PuntiVendita(Id, Nome, Posizione, Commerciante) values (?,?,?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, puntoVendita.getId());
        prepStat.setString(2, puntoVendita.getNome());
        prepStat.setString(3, puntoVendita.getPosizione());
        prepStat.setString(4, this.commerciante.getEmail());
        prepStat.executeUpdate();
    }

    @Override
    public void removePuntoVendita(PuntoVendita pv) throws SQLException {
        for(Prodotto p:pv.getProdotti()) removeProdotto(p);
        for(IOfferta o:pv.getOfferte()) removeOfferta(o);
        String sql = "delete from PuntiVendita where Id=?";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, pv.getId());
        prepStat.executeUpdate();
    }

    @Override
    public void removeProdotto(Prodotto prodotto) throws SQLException {
        String sql = "delete from Prodotti where Id=?";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, prodotto.getId());
        prepStat.executeUpdate();
    }

    @Override
    public void removeOfferta(IOfferta offerta) throws SQLException {
        String sql = "delete from Offerte where Id=?";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, offerta.getId());
        prepStat.executeUpdate();
    }

    @Override
    public void updateStatoOrdine(Ordine ordine, StatoOrdine stato) throws SQLException {
        String sql = "update Ordini set Stato=? where Id='"+ordine.getId()+"'";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, stato.toString());
        prepStat.executeUpdate();
    }

    @Override
    public void saveConsegna(Consegna consegna) throws SQLException {
        sql = "insert into Consegne(Id, Ordine, PuntoRitiro, Stato, Ritirabile) values (?,?,?,?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, consegna.getId());
        prepStat.setString(2, consegna.getOrdine().getId());
        prepStat.setString(3, consegna.getPuntoRitiro().getId());
        prepStat.setString(4, consegna.getStato().toString());
        prepStat.setBoolean(5, consegna.isRitirabile());
        prepStat.executeUpdate();
    }

    @Override
    public void updateConsegnaRitirabile(Consegna consegna) throws SQLException {
        sql = "update Consegne set Ritirabile=? where Id='" + consegna.getId() + "'";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setBoolean(1, consegna.isRitirabile());
        prepStat.executeUpdate();
    }



}
