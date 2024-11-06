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
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.*;

public class rt200436_UserOperations implements UserOperations{

    @Override
    public boolean insertUser(String userName, String firstName, String lastName, String password) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "INSERT INTO Korisnik(KorisnickoIme,Ime,Prezime,Sifra,BrojPoslatihPaketa) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            char f = firstName.charAt(0);
            char l = lastName.charAt(0); 
            if(Character.isUpperCase(f) && Character.isUpperCase(l) && password.length()>=8 && password.matches(".*[0-9].*") && password.matches(".*[a-zA-Z].*")) {
                ps.setString(1, userName);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, password);
                ps.setInt(5, 0);
                ps.executeUpdate();
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    @Override
    public int declareAdmin(String userName) {
        Connection conn = DB.getInstance().getConnection();
        String ime = "";
        String prezime = "";
        String sifra = "";
        
        String sql1 = "SELECT * FROM Admin WHERE KorisnickoImeAdmin = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql1);) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return 1;
            }
        } catch (SQLException ex) {}
        
        String sql2 = "SELECT * FROM Korisnik WHERE KorisnickoIme = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql2);) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()) {
                return 2;
            }
        } catch (SQLException ex) {
            return 2;
        }
        
        String sql3 = "INSERT INTO Administrator(KorisnickoImeAdmin) VALUES(?)";
        try (PreparedStatement ps = conn.prepareStatement(sql3);) {
            ps.setString(1, userName);
            ps.executeUpdate();
            return 0;
        } catch (SQLException ex) {
            return 1;
        }
        
    }

    @Override
    public Integer getSentPackages(String... strings) {
        int count = 0;
        Connection conn = DB.getInstance().getConnection();
        
        String sql = "SELECT * FROM Korisnik WHERE KorisnickoIme = ?";
        for (String string : strings) {
            try (PreparedStatement ps = conn.prepareStatement(sql);) {
                ps.setString(1, string);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return null;
                }
            } catch (SQLException ex) {
                return null;
            }
        }
        
        String sql1 = "SELECT BrojPoslatihPaketa FROM Korisnik WHERE KorisnickoIme = ?";
        for (String string : strings) {
            try (PreparedStatement ps = conn.prepareStatement(sql1);) {
                ps.setString(1, string);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {                    
                    count+=rs.getInt(1);
                }
            } catch (SQLException ex) {
                return null;
            }
        }
        return count;
    }

    @Override
    public int deleteUsers(String... strings) {
        int count = 0;
        Connection conn = DB.getInstance().getConnection();
        String sql = "DELETE FROM Korisnik WHERE KorisnickoIme = ?";
        for (String string : strings) {
            try (PreparedStatement ps = conn.prepareStatement(sql);) {
                ps.setString(1, string);
                int cnt = ps.executeUpdate();
                count+=cnt;
            } catch (SQLException ex) {
                return count;
            }
        }
        return count;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT KorisnickoIme from Korisnik";
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
    
}
