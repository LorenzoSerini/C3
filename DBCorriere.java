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
import it.unicam.c3.Consegne.Consegna;
import it.unicam.c3.Consegne.StatoConsegna;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBCorriere extends DBConnection implements IDBCorriere{
    private String sql;

    public DBCorriere() throws SQLException {
        super();
    }

    public DBCorriere(String connectionString, String username, String password) throws SQLException {
        super(connectionString,username,password);
    }

    @Override
    public void updateConsegnaInCarico(Consegna consegna) throws SQLException {
        sql = "update Consegne set Stato=? where Id='"+consegna.getId()+"'";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, StatoConsegna.PRESA_IN_CARICO.toString());
        prepStat.executeUpdate();

    }

    @Override
    public void updateCorriere(Consegna consegna)throws SQLException {
        sql = "update Consegne set Corriere=? where Id='" + consegna.getId() + "'";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, consegna.getCorriere().getEmail());
        prepStat.executeUpdate();
    }

    @Override
    public void updateConsegnaEffettuata(Consegna consegna) throws SQLException {
        sql = "update Consegne set Stato=? where Id='"+consegna.getId()+"'";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, StatoConsegna.EFFETTUATA.toString());
        prepStat.executeUpdate();
    }

    @Override
    public void updateConsegnaInAttesa(Consegna consegna) throws SQLException {
        sql = "update Consegne set Stato=? where Id='"+consegna.getId()+"'";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, StatoConsegna.IN_ATTESA.toString());
        prepStat.executeUpdate();
    }

    @Override
    public void updateCorriereNullo(Consegna consegna) throws SQLException {
        sql = "update Consegne set Corriere=? where Id='"+consegna.getId()+"'";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setNull(1,  19);
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
