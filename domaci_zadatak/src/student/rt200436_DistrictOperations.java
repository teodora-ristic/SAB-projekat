/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import java.sql.Connection;
import domaci_zadatak.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.*;

public class rt200436_DistrictOperations implements DistrictOperations{

    @Override
    public int insertDistrict(String naziv, int idGrada, int x, int y) {
        
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdOpstine from Opstina where Naziv=?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1, naziv);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
        
        
        String sql1 = "INSERT INTO Opstina(Naziv,IdGrada,X,Y) VALUES(?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql1,PreparedStatement.RETURN_GENERATED_KEYS);){
            ps.setString(1, naziv);
            ps.setInt(2, idGrada);
            ps.setInt(3, x);
            ps.setInt(4, y);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
    }

    @Override
    public int deleteDistricts(String... strings) {
        int count = 0;
        Connection conn = DB.getInstance().getConnection();
        String sql = "DELETE FROM Opstina WHERE Naziv = ?";
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
    public boolean deleteDistrict(int idOpstine) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "DELETE FROM Opstina WHERE IdOpstine = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, idOpstine);
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
    public int deleteAllDistrictsFromCity(String nazivGrada) {
        int id = 0;
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdGrada from Grad where Naziv=?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1, nazivGrada);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id=rs.getInt(1);
            }
        } catch (SQLException ex) {}
        
        int count = 0;
        String sql1 = "DELETE from Opstina where IdGrada = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql1);){
            ps.setInt(1, id);
            int cnt = ps.executeUpdate();
            count+=cnt;
        } catch (SQLException ex) {}
        
        return count;
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int idGrada) {
        List<Integer> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdOpstine from Opstina where IdGrada = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, idGrada);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(rs.getInt(1));
            }
            return lista;
        } catch (SQLException ex) {
            return null;
        }
        
        
    }

    @Override
    public List<Integer> getAllDistricts() {
        List<Integer> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdOpstine from Opstina";
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
