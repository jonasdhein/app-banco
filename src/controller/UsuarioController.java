/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.Conexao;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
//import model.Usuario;

/**
 *
 * @author jonas
 */
public class UsuarioController {
    
    //Usuario objUsuario;
    //JTable jtbUsuarios = null;
    
    //public UsuarioDAO(Usuario objUsuario, JTable jtbUsuarios) {
    //    this.objUsuario = objUsuario;
    //    this.jtbUsuarios = jtbUsuarios;
    //}
    
    public boolean login(String user, String pass)
    {
        try {
            //Conexao.abreConexao();
            Connection con = Conexao.getConnection();
            ResultSet rs = null;
            PreparedStatement stmt = null;
           
            String wSQL = " SELECT id, nome FROM usuarios WHERE login = ? AND senha = md5(md5(encode(?::bytea, 'base64'))) ";
            stmt = con.prepareStatement(wSQL);
            stmt.setString(1, user);    
            stmt.setString(2, pass);
    
            rs = stmt.executeQuery();
            
            //objUsuario = new Usuario();
            return rs.next();
              
        } catch (SQLException ex ){
            System.out.println("ERRO de SQL: " + ex.getMessage());
            return false;
        }catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return false;
        }
		
    }
}
