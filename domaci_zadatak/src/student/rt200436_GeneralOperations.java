/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import domaci_zadatak.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import rs.etf.sab.operations.*;

public class rt200436_GeneralOperations implements GeneralOperations{

    @Override
    public void eraseAll() {
        Connection conn = DB.getInstance().getConnection();
        String sql = "delete from SastojiSe\n" +
            "delete from Voznja\n" +
            "delete from Ponuda\n" +
            "delete from Paket\n" +
            "delete from Opstina\n" +
            "delete from Grad\n" +
            "delete from Administrator\n" +
            "delete from Kurir\n" +
            "delete from Korisnik\n" +
            "delete from Zahtev\n" +
            "delete from Vozilo";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            int executeUpdate = ps.executeUpdate();
        } catch (SQLException ex) {}
    }
    
}
