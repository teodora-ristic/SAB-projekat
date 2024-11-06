/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import domaci_zadatak.DB;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.*;

public class rt200436_CourierOperations implements CourierOperations{

    @Override
    public boolean insertCourier(String korisnickoIme, String regBroj) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "INSERT INTO Kurir(KorisnickoImeKurir,RegBroj,BrojIsporucenihPaketa,Profit,Status) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, korisnickoIme);
            ps.setString(2, regBroj);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean deleteCourier(String korisnickoIme) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "DELETE FROM Kurir WHERE KorisnickoImeKurir = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, korisnickoIme);
            int executeUpdate = ps.executeUpdate();
            if(executeUpdate>0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public List<String> getCouriersWithStatus(int status) {
        List<String> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT KorisnickoImeKurir from Kurir WHERE Status = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString(1));
            }
            return lista;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public List<String> getAllCouriers() {
        List<String> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT KorisnickoImeKurir from Kurir ORDER BY Profit DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getString(1));
            }
            return lista;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public BigDecimal getAverageCourierProfit(int brojIsporucenihPaketa) {
        double profit = 0;
        int cnt = 0;
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT Profit from Kurir WHERE BrojIsporucenihPaketa>?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, brojIsporucenihPaketa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {                
                profit+=rs.getDouble(1);
                cnt++;
            }
            BigDecimal prosek = new BigDecimal(profit/cnt);
            return prosek;
        } catch (SQLException ex) {
            return null;
        }

    }
    
}
