/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import domaci_zadatak.DB;
import java.math.BigDecimal;
import java.util.List;
import rs.etf.sab.operations.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class rt200436_VehicleOperations implements VehicleOperations{

    @Override
    public boolean insertVehicle(String regBroj, int tipGoriva, BigDecimal potrosnja) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "INSERT INTO Vozilo(RegBroj,TipGoriva,Potrosnja) VALUES(?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, regBroj);
            ps.setInt(2, tipGoriva);
            ps.setBigDecimal(3, potrosnja);
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
    public int deleteVehicles(String... strings) {
        int count = 0;
        Connection conn = DB.getInstance().getConnection();
        String sql = "DELETE FROM Vozilo WHERE RegBroj = ?";
        for (String string : strings) {
            try (PreparedStatement ps = conn.prepareStatement(sql);) {
                ps.setString(1, string);
                int cnt = ps.executeUpdate();
                count+=cnt;
            } catch (SQLException ex) {}
        }
        return count;
    }

    @Override
    public List<String> getAllVehichles() {
        List<String> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT RegBroj from Vozilo";
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
    public boolean changeFuelType(String regBroj, int tipGoriva) {
        if (tipGoriva<0 || tipGoriva>2) return false;
        Connection conn = DB.getInstance().getConnection();
        String sql = "UPDATE Vozilo SET TipGoriva = ? WHERE RegBroj = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, tipGoriva);
            ps.setString(2, regBroj);
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
    public boolean changeConsumption(String regBroj, BigDecimal potrosnja) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "UPDATE Vozilo SET Potrosnja = ? WHERE RegBroj = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setBigDecimal(1, potrosnja);
            ps.setString(2, regBroj);
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
    
}
