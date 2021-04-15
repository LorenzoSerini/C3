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

import it.unicam.c3.Anagrafica.Cliente;
import it.unicam.c3.Anagrafica.Commerciante;
import it.unicam.c3.Anagrafica.Corriere;
import it.unicam.c3.Commercio.PuntoVendita;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class DBAccounts extends DBConnection implements IDBAccounts{
    private String sql;
    private static List<Commerciante> commercianti;
    private static List<Cliente> clienti;
    private static List<Corriere> corrieri;

    public DBAccounts() throws SQLException {
        super();
        commercianti = new LinkedList<>();
        clienti = new LinkedList<>();
        corrieri = new LinkedList<>();
    }

    public DBAccounts(String connectionString, String username, String password) throws SQLException {
        super(connectionString,username,password);
        commercianti = new LinkedList<>();
        clienti = new LinkedList<>();
        corrieri = new LinkedList<>();
    }


    @Override
    public List<Commerciante> getCommercianti() throws SQLException {
        if(commercianti.isEmpty()) {
            String sql = "Select * from Commercianti";
            setData(sql);
            while (getData().next()) {
                commercianti.add(new Commerciante(getData().getString("Nome"), getData().getString("Cognome"), getData().getString("Email"), getData().getString("Password")));
            }
            returnAll(commercianti);
        }
        return commercianti;
    }


    @Override
    public List<Cliente> getClienti() throws SQLException {
        if(clienti.isEmpty()) {
            sql = "Select * from Clienti";
            setData(sql);
            while (getData().next()) {
                clienti.add(new Cliente(getData().getString("Nome"), getData().getString("Cognome"), getData().getString("Email"), getData().getString("Password")));
            }
        }
        return clienti;
    }

    @Override
    public List<Corriere> getCorrieri() throws SQLException {
       if(corrieri.isEmpty()) {
           String sql = "Select * from Corrieri";
           setData(sql);
           while (getData().next()) {
               corrieri.add(new Corriere(getData().getString("Nome"), getData().getString("Cognome"), getData().getString("Email"), getData().getString("Password")));
           }
       }
        return corrieri;
    }

    @Override
    public void registerCommerciante(Commerciante commerciante) throws SQLException {
        sql = "insert into Commercianti(Email, Nome, Cognome, Password) values (?,?,?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, commerciante.getEmail());
        prepStat.setString(2, commerciante.getNome());
        prepStat.setString(3, commerciante.getCognome());
        prepStat.setString(4, commerciante.getPassword());
        prepStat.executeUpdate();
    }

    @Override
    public void registerCliente(Cliente cliente) throws SQLException {
        sql = "insert into Clienti(Email, Nome, Cognome, Password) values (?,?,?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, cliente.getEmail());
        prepStat.setString(2, cliente.getNome());
        prepStat.setString(3, cliente.getCognome());
        prepStat.setString(4, cliente.getPassword());
        prepStat.executeUpdate();
    }

    @Override
    public void registerCorriere(Corriere corriere) throws SQLException {
        sql = "insert into Corrieri(Email, Nome, Cognome, Password) values (?,?,?,?)";
        PreparedStatement prepStat = getConnection().prepareStatement(sql);
        prepStat.setString(1, corriere.getEmail());
        prepStat.setString(2, corriere.getNome());
        prepStat.setString(3, corriere.getCognome());
        prepStat.setString(4, corriere.getPassword());
        prepStat.executeUpdate();
    }

    private void returnAll(List<Commerciante> commList) throws SQLException {
        for(Commerciante c:commList){
            this.putPuntiVendita(c);
            for(PuntoVendita pv:c.getPuntiVendita()){
                this.putProdotti(pv);
                this.putOfferte(pv);
            }
        }
    }

    private void  putPuntiVendita(Commerciante commerciante) throws SQLException {
        String sql = "Select * from PuntiVendita where Commerciante='"+commerciante.getEmail()+"'";
        setData(sql);
        while (getData().next()) {
            commerciante.addPuntoVendita(getData().getString("Id"), getData().getString("Nome"), getData().getString("Posizione"));
        }
    }


    private void putProdotti(PuntoVendita pv) throws SQLException {
        String sql = "Select * from Prodotti where PuntoVendita='"+pv.getId()+"'";
        setData(sql);
        while (getData().next()) {
            pv.addProdotto(getData().getString("Id"), getData().getString("Descrizione"), getData().getDouble("Prezzo"));
        }
    }

    private void putOfferte(PuntoVendita pv) throws SQLException {
        String sql = "Select * from Offerte where PuntoVendita='"+pv.getId()+"'";
        setData(sql);
        while (getData().next()) {
            if(getData().getDate("Scadenza")!=null){
                if(getData().getDate("Scadenza").compareTo(Date.valueOf(LocalDate.now()))>=0) {
                    pv.addOfferta(getData().getString("Id"), getData().getString("Descrizione"), getData().getString("Importo"), getData().getDate("Scadenza").toLocalDate());
                }
            } else pv.addOfferta(getData().getString("Id"), getData().getString("Descrizione"), getData().getString("Importo"));
        }
    }

}
