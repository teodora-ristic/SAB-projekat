/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import domaci_zadatak.DB;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import rs.etf.sab.operations.*;

public class rt200436_PackageOperations implements PackageOperations{
    
    public class Par<A,B> implements Pair<A,B>{
        public A first;
        public B second;

        public Par(A first, B second) {
            this.first = first;
            this.second = second;
        }

	@Override
	public A getFirstParam() {
		return first;
	}

	@Override
	public B getSecondParam() {
		return second;
	}

        
    }

    @Override
    public int insertPackage(int opstinaOd, int opstinaDo, String korisnickoIme, int tipPaketa, BigDecimal tezinaPaketa) {
        Connection conn = DB.getInstance().getConnection();
        
        if (tipPaketa<0 || tipPaketa>2) return -1;
        
        String sqlp = "SELECT * from Korisnik where KorisnickoIme= ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlp);) {
            ps.setString(1, korisnickoIme);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
        
        String sqlp1 = "SELECT * from Opstina where IdOpstine= ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlp1);) {
            ps.setInt(1, opstinaOd);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
        
        String sqlp2 = "SELECT * from Opstina where IdOpstine= ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlp2);) {
            ps.setInt(1, opstinaDo);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
        
        String sql = "INSERT INTO Paket(Status,Cena,TipPaketa,TezinaPaketa,IdOpstineOd,IdOpstineDo,KorisnickoIme) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setInt(1, 0);
            ps.setInt(2, 0);
            ps.setInt(3, tipPaketa);
            ps.setBigDecimal(4, tezinaPaketa);
            ps.setInt(5, opstinaOd);
            ps.setInt(6, opstinaDo);
            ps.setString(7, korisnickoIme);
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
    public int insertTransportOffer(String korisnickoImeKurir, int idPaketa, BigDecimal procenat) {
        Connection conn = DB.getInstance().getConnection();
        
        String sql = "SELECT Status from Kurir WHERE KorisnickoImeKurir = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1, korisnickoImeKurir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 1) {
                    return -1; //kurir u stanju vozi
                }
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
        
        String sqlp = "SELECT * from Paket where IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlp);) {
            ps.setInt(1, idPaketa);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return -1;
            }
        } catch (SQLException ex) {
            return -1;
        }
        
        
        
        String sql1 = "INSERT INTO Ponuda(KorisnickoImeKurir,IdPaketa,ProcenatCeneIsporuke) VALUES(?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql1,PreparedStatement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, korisnickoImeKurir);
            ps.setInt(2, idPaketa);
            if(procenat==null) {
                BigDecimal procenatRand = new BigDecimal("-10 + (int)(Math.random() * ((10 - (-10)) + 1))");
                
                ps.setBigDecimal(3, procenatRand);
            } else {
                ps.setBigDecimal(3, procenat);
            }
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
    public boolean acceptAnOffer(int idPonude) { 
        int[] OSNOVNA_CENA = {10,25,75};
        int[] TEZINSKI_FAKTOR = {0,1,2};
        int[] CENA_PO_KG = {0,100,300};
        
        int idPaketa;
        String korisnickoImeKurir;
        int procenat;
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdPaketa,KorisnickoImeKurir,ProcenatCeneIsporuke from Ponuda WHERE IdPonude = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, idPonude);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {                
                idPaketa = rs.getInt(1);
                korisnickoImeKurir = rs.getString(2);
                procenat = rs.getInt(3);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        String sql1 = "UPDATE Paket SET Status=? WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql1);) {
            ps.setInt(1, 1);
            ps.setInt(2, idPaketa);
            int executeUpdate = ps.executeUpdate();
            if(executeUpdate>0) {
                
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        String sql2 = "UPDATE Paket SET KorisnickoImeKurir=? WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql2);) {
            ps.setString(1, korisnickoImeKurir);
            ps.setInt(2, idPaketa);
            int executeUpdate = ps.executeUpdate();
            if(executeUpdate>0) {
                
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        int tip;
        double tezina;
        int opstinaOd;
        int opstinaDo;
        String sql3 = "SELECT TipPaketa,TezinaPaketa,IdOpstineOd,IdOpstineDo from Paket WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql3);) {
            ps.setInt(1, idPaketa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {                
                tip = rs.getInt(1);
                tezina = rs.getDouble(2);
                opstinaOd = rs.getInt(3);
                opstinaDo = rs.getInt(4);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        int xOd;
        int yOd;
        String sql4 = "SELECT X,Y from Opstina WHERE IdOpstine = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql4);) {
            ps.setInt(1, opstinaOd);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {                
                xOd = rs.getInt(1);
                yOd = rs.getInt(2);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        int xDo;
        int yDo;
        String sql5 = "SELECT X,Y from Opstina WHERE IdOpstine = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql5);) {
            ps.setInt(1, opstinaDo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {                
                xDo = rs.getInt(1);
                yDo = rs.getInt(2);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        String sqlT = "UPDATE Paket SET VremePrihvatanja=CURRENT_TIMESTAMP WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlT);) {
            ps.setInt(1, idPaketa);
            int executeUpdate = ps.executeUpdate();
            if(executeUpdate>0) {
                
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        String sql6 = "UPDATE Paket SET Cena=? WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql6);) {
            double cena= (OSNOVNA_CENA[tip] + (TEZINSKI_FAKTOR[tip] * tezina) * CENA_PO_KG[tip])
                    *Math.sqrt((xDo-xOd)*(xDo-xOd) + (yDo-yOd)*(yDo-yOd))*(1+procenat/100.0D);
            ps.setDouble(1, cena);
            ps.setInt(2, idPaketa);
            int executeUpdate = ps.executeUpdate();
            if(executeUpdate>0) {
                
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        
        int idVoznje = 0;
        String sqlv = "SELECT IdVoznje FROM Voznja where KorisnickoImeKurir = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlv);) {
            ps.setString(1, korisnickoImeKurir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idVoznje = rs.getInt(1);
            } else {
            }
        } catch (SQLException ex) {
            return false;
        }
        
        if(idVoznje==0) {
            String sql7 = "INSERT INTO Voznja(KorisnickoImeKurir) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(sql7);) {
                ps.setString(1, korisnickoImeKurir);
                ps.executeUpdate();
            } catch (SQLException ex) {
                return false;
            }
        
        }
        
        String sql8 = "SELECT IdVoznje FROM Voznja where KorisnickoImeKurir = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql8);) {
            ps.setString(1, korisnickoImeKurir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idVoznje = rs.getInt(1);
            } else {
                return false;
            }
        } catch (SQLException ex) {
            return false;
        }
        
        
        String sql9 = "INSERT INTO SastojiSe(IdVoznje,IdPaketa) VALUES (?,?) ";
        try (PreparedStatement ps = conn.prepareStatement(sql9);) {
            ps.setInt(1, idVoznje);
            ps.setInt(2, idPaketa);
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
    public List<Integer> getAllOffers() {
        List<Integer> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdPonude from Ponuda";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
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
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int idPaketa) {
        
        List<Pair<Integer, BigDecimal>> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();

        String sql = "SELECT IdPonude,ProcenatCeneIsporuke from Ponuda where IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, idPaketa);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {  
                Pair<Integer, BigDecimal> pair= new Par<>(rs.getInt(1),rs.getBigDecimal(2));
                lista.add(pair);
            }
            return lista;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public boolean deletePackage(int idPaketa) {
        Connection conn = DB.getInstance().getConnection();
        
        String sql1 = "DELETE FROM Ponuda WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql1);) {
            ps.setInt(1, idPaketa);
            int executeUpdate = ps.executeUpdate();
        } catch (SQLException ex) {
            return false;
        }
        
        String sql = "DELETE FROM Paket WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, idPaketa);
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
    public boolean changeWeight(int idPaketa, BigDecimal tezina) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "UPDATE Paket SET Tezina = ? WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setBigDecimal(1, tezina);
            ps.setInt(2, idPaketa);
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
    public boolean changeType(int idPaketa, int tip) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "UPDATE Paket SET Tip = ? WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, tip);
            ps.setInt(2, idPaketa);
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
    public Integer getDeliveryStatus(int idPaketa) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT Status FROM Paket WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, idPaketa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public BigDecimal getPriceOfDelivery(int idPaketa) { 
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT Cena FROM Paket WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, idPaketa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getDouble(1) == 0) {
                    return null;
                }
                return new BigDecimal(rs.getDouble(1));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public Date getAcceptanceTime(int idPaketa) {
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT VremePrihvatanja FROM Paket WHERE IdPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setInt(1, idPaketa);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDate(1);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int tip) {
        List<Integer> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdPaketa from Paket WHERE TipPaketa = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, tip);
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
    public List<Integer> getAllPackages() {
        List<Integer> lista = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        String sql = "SELECT IdPaketa from Paket";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
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
    public List<Integer> getDrive(String korisnickoImeKurir) { 
        Connection conn = DB.getInstance().getConnection();
        
        int idVoznje = 0;
        String sql = "SELECT IdVoznje FROM Voznja where KorisnickoImeKurir = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setString(1, korisnickoImeKurir);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idVoznje = rs.getInt(1);
            } else {
                return null;
            }
        } catch (SQLException ex) {
            return null;
        }
        
        List<Integer> lista = new ArrayList<>();
        String sql1= "SELECT IdPaketa from SastojiSe WHERE IdVoznje = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql1);){
            ps.setInt(1, idVoznje);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (getDeliveryStatus(rs.getInt(1)) != 3){
                    lista.add(rs.getInt(1));
                }
            }
            return lista;
        } catch (SQLException ex) {
            return null;
        }
    }

    @Override
    public int driveNextPackage(String korisnickoImeKurir) { 
        Connection conn = DB.getInstance().getConnection();
        
        int brojIsporucenihPaketa = 0;
        String sqlb = "SELECT BrojIsporucenihPaketa FROM Kurir WHERE KorisnickoImeKurir = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlb);) {
            ps.setString(1, korisnickoImeKurir);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                brojIsporucenihPaketa = rs.getInt(1);
            } else {
                return -2;
            }
        } catch (SQLException ex) {
            return -2;
        }
        
        String sqlu = "UPDATE Kurir SET BrojIsporucenihPaketa=? WHERE KorisnickoImeKurir = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlu);) {
            ps.setInt(1, brojIsporucenihPaketa+1);
            ps.setString(2, korisnickoImeKurir);
            int executeUpdate = ps.executeUpdate();
            if(executeUpdate>0) {
            } else {
                return -2;
            }
        } catch (SQLException ex) {
            return -2;
        }
        
        String sqlS = "UPDATE Kurir SET Status=? WHERE KorisnickoImeKurir = ?";
        try (PreparedStatement ps = conn.prepareStatement(sqlS);) {
            ps.setInt(1, 1);
            ps.setString(2, korisnickoImeKurir);
            int executeUpdate = ps.executeUpdate();
            if(executeUpdate>0) {
            } else {
                return -2;
            }
        } catch (SQLException ex) {
            return -2;
        }
        
        
        List<Integer> drive = getDrive(korisnickoImeKurir);
        if(drive.size()>0) {
            Integer idPaketa = drive.get(0);
            String sql = "UPDATE Paket SET Status=? WHERE IdPaketa = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql);) {
                ps.setInt(1, 3);
                ps.setInt(2, idPaketa);
                int executeUpdate = ps.executeUpdate();
                if(executeUpdate>0) {
                } else {
                    return -2;
                }
            } catch (SQLException ex) {
                return -2;
            }
            if (drive.size()>1) {
                Integer idPaketaSl = drive.get(1);
                String sql1 = "UPDATE Paket SET Status=? WHERE IdPaketa = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql1);) {
                    ps.setInt(1, 2);
                    ps.setInt(2, idPaketaSl);
                    int executeUpdate = ps.executeUpdate();
                    if(executeUpdate>0) {
                        return idPaketa;
                    } else {
                        return -2;
                    }
                } catch (SQLException ex) {
                    return -2;
                }
            } 
            
            
        } else {
            return -1;
        }
        
        if (drive.size() == 1){
            Integer idPaketa = drive.get(0);
            int idVoznje = 0;
            String sql = "SELECT IdVoznje FROM Voznja where KorisnickoImeKurir = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql);) {
                ps.setString(1, korisnickoImeKurir);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    idVoznje=rs.getInt(1);
                } 
            } catch (SQLException ex) {
                return -2;
            }

            List<Integer> lista = new ArrayList<>();
            String sql1= "SELECT IdPaketa from SastojiSe WHERE IdVoznje = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql1);){
                ps.setInt(1, idVoznje);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    lista.add(rs.getInt(1));
                    
                }
            } catch (SQLException ex) {
                return -2;
            }
            
            double cena = 0;
            String sql2 = "SELECT Cena from Paket WHERE IdPaketa = ?";
            for (Integer paket : lista) {
                try (PreparedStatement ps = conn.prepareStatement(sql2);){
                    ps.setInt(1, paket);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {                
                        cena += rs.getDouble(1);
                    } else {
                        return -2;
                    }
                } catch (SQLException ex) {
                    return -2;
                }

            }
            
            String regBr;
            String sql3 = "SELECT RegBroj from Kurir WHERE KorisnickoImeKurir = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql3);) {
                ps.setString(1, korisnickoImeKurir);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    regBr = rs.getString(1);
                } else {
                    return -2;
                }
            } catch (SQLException ex) {
                return -2;
            }
            
            int tipGoriva;
            double potrosnja;
            String sql4 = "SELECT TipGoriva,Potrosnja from Vozilo WHERE RegBroj = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql4);) {
                ps.setString(1, regBr);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    tipGoriva = rs.getInt(1);
                    potrosnja = rs.getDouble(2);
                } else {
                    return -2;
                }
            } catch (SQLException ex) {
                return -2;
            }
            
            double distanca = 0;
            List<Integer> xOd = new ArrayList<>();
            List<Integer> xDo = new ArrayList<>();
            List<Integer> yOd = new ArrayList<>();
            List<Integer> yDo = new ArrayList<>();
            int opstinaOd;
            int opstinaDo;
            
            
            for (Integer id : lista) {
                String sql5 = "SELECT IdOpstineOd,IdOpstineDo from Paket WHERE IdPaketa = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql5);) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {               
                        opstinaOd = rs.getInt(1);
                        opstinaDo = rs.getInt(2);
                    } else {
                        return -2;
                    }
                } catch (SQLException ex) {
                    return -2;
                }
                
                String sql6 = "SELECT X,Y from Opstina WHERE IdOpstine = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql6);) {
                    ps.setInt(1, opstinaOd);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {   
                        xOd.add(rs.getInt(1));
                        yOd.add(rs.getInt(2));
                    } else {
                        return -2;
                    }
                } catch (SQLException ex) {
                    return -2;
                }
                
                String sql7 = "SELECT X,Y from Opstina WHERE IdOpstine = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql7);) {
                    ps.setInt(1, opstinaDo);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {                
                        xDo.add(rs.getInt(1));
                        yDo.add(rs.getInt(2));
                    } else {
                        return -2;
                    }
                } catch (SQLException ex) {
                    return -2;
                }
                
            }
            for (int i = 0; i < xOd.size(); i++) {
                distanca+=Math.sqrt((xDo.get(i)-xOd.get(i))*(xDo.get(i)-xOd.get(i)) + (yDo.get(i)-yOd.get(i))*(yDo.get(i)-yOd.get(i)));
                if(i!=0) {
                    distanca+=Math.sqrt((xDo.get(i-1)-xOd.get(i))*(xDo.get(i-1)-xOd.get(i)) + (yDo.get(i-1)-yOd.get(i))*(yDo.get(i-1)-yOd.get(i)));
                    
                }
                    
            }
            
            int[] CENA_GORIVA = {15,36,32};
            double gorivo = distanca*CENA_GORIVA[tipGoriva]*potrosnja;
            double profit = cena - gorivo;
            
            double stariProfit = 0;
            String sqlSP = "SELECT Profit from Kurir where KorisnickoImeKurir = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlSP);){
                ps.setString(1, korisnickoImeKurir);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    stariProfit = rs.getInt(1);
                } else {
                    return -2;
                }
            } catch (SQLException ex) {
                return -2;
            }
            
            String sql8 = "UPDATE Kurir SET Profit = ? where KorisnickoImeKurir = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql8);){
                ps.setDouble(1, stariProfit + profit);
                ps.setString(2, korisnickoImeKurir);
                int executeUpdate = ps.executeUpdate();
                if (executeUpdate > 0) {
                    
                } else {
                    return -2;
                }
            } catch (SQLException ex) {
                return -2;
            }
            
            String sqlS2 = "UPDATE Kurir SET Status=? WHERE KorisnickoImeKurir = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlS2);) {
                ps.setInt(1, 0);
                ps.setString(2, korisnickoImeKurir);
                int executeUpdate = ps.executeUpdate();
                if(executeUpdate>0) {
                } else {
                    return -2;
                }
            } catch (SQLException ex) {
                return -2;
            }
            
            String sql9 = "DELETE FROM SastojiSe where IdVoznje = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql9);){
                ps.setInt(1, idVoznje);
                int executeUpdate = ps.executeUpdate();
                if (executeUpdate > 0) {
                    
                } else {
                    return -2;
                }
            } catch (SQLException ex) {
                return -2;
            }
            
            String sql10 = "DELETE FROM Voznja where IdVoznje = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql10);){
                ps.setInt(1, idVoznje);
                int executeUpdate = ps.executeUpdate();
                if (executeUpdate > 0) {
                    return idPaketa;
                } else {
                    return -2;
                }
            } catch (SQLException ex) {
                return -2;
            }
            
            
        }
        return -2;
    }
        
    
}
