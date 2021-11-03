package controller;

import database.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Cidade;

/**
 *
 * @author jdhein
 */
public class CidadeController {

     public boolean verificaExistencia(Cidade objeto)
    {
        try {
            Connection con = Conexao.getConnection();
            ResultSet rs = null;
            PreparedStatement stmt = null;
           
            String wSQL = " SELECT id FROM cidades WHERE nome = ? AND id_estado = ? ";
            stmt = con.prepareStatement(wSQL);
            stmt.setString(1, objeto.getNome());               
            stmt.setInt(2, objeto.getId_estado());    
     
            rs = stmt.executeQuery();
            
            return rs.next();
              
        } catch (SQLException ex ){
            System.out.println("ERRO de SQL: " + ex.getMessage());
            return false;
        }catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return false;
        }
		
    }
    
    public String incluir(Cidade objeto)
    {
        try {
            Connection con = Conexao.getConnection();
            PreparedStatement stmt = null;
            
            //VALIDAR SE A CIDADE JÁ EXISTE
            if(verificaExistencia(objeto) == true){
                return "Cidade já existe";
            }else{
           
                String wSQL = " INSERT INTO cidades VALUES(DEFAULT, ?, ?) ";
                stmt = con.prepareStatement(wSQL);
                stmt.setString(1, objeto.getNome());    
                stmt.setInt(2, objeto.getId_estado());     

                stmt.executeUpdate();
            }
            return "";
              
        } catch (SQLException ex ){
            System.out.println("ERRO de SQL: " + ex.getMessage());
            return "Erro ao incluir cidade";
        }catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return "Erro ao incluir cidade";
        }
		
    }
    
}
