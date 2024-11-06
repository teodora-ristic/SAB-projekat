/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import domaci_zadatak.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.*;

public class rt200436_CourierRequestOperation implements CourierRequestOperation{

    @Override
    public boolean insertCourierRequest(String korisnickoIme, String regBroj) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String sqlp = "SELECT * from Korisnik where KorisnickoIme= ? ";
        try (PreparedStatement ps = conn.prepareStatement(sqlp);) {
            ps.setString(1, korisnickoIme);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        String sqlp2 = "SELECT * from Vozilo where RegBroj = ? ";
        try (PreparedStatement ps = conn.prepareStatement(sqlp2);) {
            ps.setString(1, regBroj);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        String sql = "INSERT INTO Zahtev(KorisnickoIme,RegBroj) VALUES (?,?) ";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, korisnickoIme);
            ps.setString(2, regBroj);
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public boolean deleteCourierRequest(String korisnickoIme) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "DELETE FROM Zahtev WHERE KorisnickoIme = ?";
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
    public boolean changeVehicleInCourierRequest(String korisnickoIme, String regBroj) {
        Connection conn = DB.getInstance().getConnection();
        
        String sqlp = "SELECT * from Vozilo where RegBroj = ? ";
        try (PreparedStatement ps = conn.prepareStatement(sqlp);) {
            ps.setString(1, regBroj);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        String sql = "UPDATE Zahtev SET RegBroj = ? WHERE KorisnickoIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, regBroj);
            ps.setString(2, korisnickoIme);
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
    public List<String> getAllCourierRequests() {
        List<String> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT KorisnickoIme from Zahtev";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
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
    public boolean grantRequest(String korisnickoIme) {
        Connection conn = DB.getInstance().getConnection();
        String query = "{ call SP_AcceptRequest_ (?) }";
        try ( CallableStatement cs = conn.prepareCall(query)) {
            cs.setString(1, korisnickoIme);
            cs.execute();
            return true;
        } catch (SQLException ex) {
            return false;
        }

        /*
        Connection conn = DB.getInstance().getConnection();
        String regBroj;
        
        String sql = "SELECT RegBroj FROM Zahtev WHERE KorisnickoIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, korisnickoIme);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                regBroj = rs.getString(1);
            } else {
                return false;
            }
            
        } catch (SQLException ex) {
            return false;
        }
        
        String sql1 = "INSERT INTO Kurir(KorisnickoImeKurir,RegBroj,BrojIsporucenihPaketa,Profit,Status) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql1);) {
            ps.setString(1, korisnickoIme);
            ps.setString(2, regBroj);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.executeUpdate();
        } catch (SQLException ex) {
            return false;
        }
        
        String sql2 = "DELETE FROM Zahtev WHERE KorisnickoIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql2);) {
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
        */
        
    }
    
}
