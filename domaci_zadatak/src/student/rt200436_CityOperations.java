/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import domaci_zadatak.DB;
import java.util.List;
import rs.etf.sab.operations.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class rt200436_CityOperations implements CityOperations{

    @Override
    public int insertCity(String naziv, String postanskiBroj) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdGrada from Grad where Naziv=?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1, naziv);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
        
        String sql1 = "SELECT IdGrada from Grad where PostanskiBroj=?";
        try (PreparedStatement ps = conn.prepareStatement(sql1);){
            ps.setString(1, postanskiBroj);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
        
        
        String sql2 = "INSERT INTO Grad(Naziv,PostanskiBroj) VALUES (?,?) ";
        try (PreparedStatement ps = conn.prepareStatement(sql2,PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, naziv);
            ps.setString(2, postanskiBroj);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
    }

    @Override
    public int deleteCity(String... strings) {
        int count = 0;
        Connection conn = DB.getInstance().getConnection();
        String sql = "DELETE FROM Grad WHERE Naziv = ?";
        for (String string : strings) {
            try (PreparedStatement ps = conn.prepareStatement(sql);) {
                ps.setString(1, string);
                int cnt = ps.executeUpdate();
                count+=cnt;
            } catch (SQLException ex) {
                
            }
        }
        return count;
    }

    @Override
    public boolean deleteCity(int idGrada) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "DELETE FROM Grad WHERE IdGrada = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, idGrada);
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
    public List<Integer> getAllCities() {
        List<Integer> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdGrada from Grad";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt(1));
            }
            return lista;
        } catch (SQLException ex) {
            return null;
        }
        
    }
    
}
