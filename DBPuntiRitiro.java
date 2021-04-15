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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DBPuntiRitiro extends DBConnection implements IDBPuntiRitiro{
    private String sql;
    private static List<PuntoRitiro> puntiRitiro;


    public DBPuntiRitiro() throws SQLException {
        super();
        puntiRitiro=new LinkedList<>();
    }

    public DBPuntiRitiro(String connectionString, String username, String password) throws SQLException{
        super(connectionString,username,password);
        puntiRitiro=new LinkedList<>();
    }

    @Override
    public void savePuntoRitiro(PuntoRitiro pr) throws SQLException {
        sql = "insert into PuntiRitiro(Id, Indirizzo, Capienza, SlotDisponibili) values (?,?,?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, pr.getId());
        prepStat.setString(2, pr.getIndirizzo());
        prepStat.setInt(3, pr.getCapienza());
        prepStat.setInt(4, pr.getSlotDisponibili());
        prepStat.executeUpdate();
    }

    @Override
    public void removePuntoRitiro(PuntoRitiro pr) throws SQLException {
        String sql = "delete from PuntiRitiro where Id=?";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, pr.getId());
        prepStat.executeUpdate();
    }

    @Override
    public List<PuntoRitiro> getPuntiRitiro() throws SQLException {
        if(puntiRitiro.isEmpty()) {
            sql = "Select * from PuntiRitiro";
            setData(sql);
            while (getData().next()) {
                PuntoRitiro pr = new PuntoRitiro(getData().getString("Indirizzo"), getData().getInt("Capienza"));
                pr.setId(getData().getString("Id"));
                pr.incrementOccupati(pr.getCapienza() - getData().getInt("SlotDisponibili"));
                puntiRitiro.add(pr);
            }
        }
        return puntiRitiro;
    }

}
