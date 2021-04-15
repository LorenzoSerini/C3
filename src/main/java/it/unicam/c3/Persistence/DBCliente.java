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

import it.unicam.c3.Citta.PuntoRitiro;
import it.unicam.c3.Commercio.Prodotto;
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Consegne.StatoConsegna;
import it.unicam.c3.Ordini.Ordine;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBCliente extends DBConnection implements IDBCliente{
    private String sql;

    public DBCliente() throws SQLException {
        super();
    }

    public DBCliente(String connectionString, String username, String password) throws SQLException {
        super(connectionString,username,password);
    }

    @Override
    public void saveOrdine(Ordine ordine) throws SQLException {
        sql = "insert into Ordini(Id,Cliente, PuntoVendita, Stato) values (?,?,?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, ordine.getId());
        prepStat.setString(2, ordine.getCliente().getEmail());
        prepStat.setString(3, ordine.getPuntoVendita().getId());
        prepStat.setString(4, ordine.getStato().toString());
        prepStat.executeUpdate();
        this.saveProdottiOrdine(ordine);
    }

    private void saveProdottiOrdine(Ordine ordine) throws SQLException {
        sql="insert into ListaProdottiOrdine(IdOrdine,IdProdotto) values (?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        for(Prodotto p:ordine.getProdotti()) {
            prepStat.setString(1, ordine.getId());
            prepStat.setString(2, p.getId());
            prepStat.executeUpdate();
        }
    }

    @Override
    public void updateConsegnaRitirata(Consegna consegna) throws Exception {
        sql = "update Consegne set Stato=? where Id='"+consegna.getId()+"'";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, StatoConsegna.RITIRATA.toString());
        prepStat.executeUpdate();
    }

    @Override
    public void updateDisponibilita(PuntoRitiro pr) throws SQLException {
        sql = "update PuntiRitiro set SlotDisponibili=? where Id='"+pr.getId()+"'";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setInt(1, pr.getSlotDisponibili());
        prepStat.executeUpdate();
    }


}
