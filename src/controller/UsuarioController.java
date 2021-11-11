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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import model.Usuario;
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
    public boolean login(String user, String pass) {
        try {
            //Conexao.abreConexao();
            Connection con = Conexao.getConnection();
            ResultSet rs = null;
            PreparedStatement stmt = null;

            String wSQL = " SELECT id, nome FROM usuarios WHERE COALESCE(excluido,false) = false AND login = ? AND senha = md5(md5(encode(?::bytea, 'base64'))) ";
            stmt = con.prepareStatement(wSQL);
            stmt.setString(1, user);
            stmt.setString(2, pass);

            rs = stmt.executeQuery();

            //objUsuario = new Usuario();
            return rs.next();

        } catch (SQLException ex) {
            System.out.println("ERRO de SQL: " + ex.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return false;
        }

    }

    public Usuario buscar(String codigo) {
        Usuario objUsuario = null;
        try {
            Connection con = Conexao.getConnection();
            ResultSet rs = null;
            PreparedStatement stmt = null;

            String wSQL = " SELECT * FROM usuarios WHERE id = ? ";
            stmt = con.prepareStatement(wSQL);
            stmt.setInt(1, Integer.parseInt(codigo));

            rs = stmt.executeQuery();

            if (rs.next()) {
                objUsuario = new Usuario();

                objUsuario.setId(rs.getInt("id"));
                objUsuario.setNome(rs.getString("nome"));
                objUsuario.setLogin(rs.getString("login"));
                objUsuario.setSenha(rs.getString("senha"));
                objUsuario.setExcluido(rs.getBoolean("excluido"));
                objUsuario.setId_bairro(rs.getInt("id_bairro"));
                objUsuario.setData_nascimento(rs.getString("data_nascimento"));
            }

        } catch (SQLException ex) {
            System.out.println("ERRO de SQL: " + ex.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return null;
        }

        return objUsuario;

    }

    public boolean verificaExistencia(Usuario objeto) {
        try {
            //Conexao.abreConexao();
            Connection con = Conexao.getConnection();
            ResultSet rs = null;
            PreparedStatement stmt = null;

            String wSQL = " SELECT id FROM usuarios WHERE login = ? ";
            stmt = con.prepareStatement(wSQL);
            stmt.setString(1, objeto.getLogin());

            rs = stmt.executeQuery();

            if (rs.next()) {
                return true; //se existir, retorna TRUE
            } else {
                return false;
            }

        } catch (SQLException ex) {
            System.out.println("ERRO de SQL: " + ex.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return false;
        }

    }

    public String incluir(Usuario objeto) {
        try {
            Connection con = Conexao.getConnection();
            PreparedStatement stmt = null;

            //VALIDAR SE O LOGIN EXISTE
            if (verificaExistencia(objeto) == true) {
                return "Usuário já existe";
            } else {

                String wSQL = " INSERT INTO usuarios VALUES(DEFAULT, ?, ?, md5(md5(encode(?::bytea, 'base64'))), false, ?, ?) ";
                stmt = con.prepareStatement(wSQL);
                stmt.setString(1, objeto.getNome());
                stmt.setString(2, objeto.getLogin());
                stmt.setString(3, objeto.getSenha());
                stmt.setInt(4, objeto.getId_bairro());

                java.sql.Date date = java.sql.Date.valueOf(objeto.getData_nascimento());
                stmt.setDate(5, date);

                stmt.executeUpdate();
            }
            return "";

        } catch (SQLException ex) {
            System.out.println("ERRO de SQL: " + ex.getMessage());
            return "Erro ao incluir";
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return "Erro ao incluir";
        }

    }

    public boolean alterar(Usuario objeto) {
        try {
            Connection con = Conexao.getConnection();
            PreparedStatement stmt = null;

            //VALIDAR SE O LOGIN EXISTE
            if (verificaExistencia(objeto) == true) {
                String wSQL = " UPDATE usuarios ";
                wSQL += " SET nome = ? ";
                if (!objeto.getSenha().equals("")) { //senha está preenchida?
                    wSQL += " ,senha = md5(md5(encode(?::bytea, 'base64'))) ";
                }
                wSQL += " ,id_bairro = ? ";
                wSQL += " WHERE id = ? ";

                stmt = con.prepareStatement(wSQL);
                stmt.setString(1, objeto.getNome());
                if (!objeto.getSenha().equals("")) {//senha está preenchida?
                    stmt.setString(2, objeto.getSenha());
                    stmt.setInt(3, objeto.getId_bairro());
                    stmt.setInt(4, objeto.getId());
                } else {
                    stmt.setInt(2, objeto.getId_bairro());
                    stmt.setInt(3, objeto.getId());
                }

                stmt.executeUpdate();
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            System.out.println("ERRO de SQL: " + ex.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return false;
        }

    }

    public boolean excluir(String codigo) {
        try {
            Connection con = Conexao.getConnection();
            PreparedStatement stmt = null;

            String wSQL = " UPDATE usuarios SET excluido = true WHERE id = ? ";
            stmt = con.prepareStatement(wSQL);
            stmt.setInt(1, Integer.parseInt(codigo));

            stmt.executeUpdate();

            return true;

        } catch (SQLException ex) {
            System.out.println("ERRO de SQL: " + ex.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
            return false;
        }
    }

    public void preencher(JTable jtbUsuarios, String texto) {

        Conexao.abreConexao();

        Vector<String> cabecalhos = new Vector<String>();
        Vector dadosTabela = new Vector(); //receber os dados do banco

        Icon iconeEditar
                = new ImageIcon("H:\\2021B\\JAVA\\AppBanco\\src\\images\\edit.png");
        Icon iconeLixeira
                = new ImageIcon("H:\\2021B\\JAVA\\AppBanco\\src\\images\\delete.png");

        cabecalhos.add("Id");
        cabecalhos.add("Nome");
        cabecalhos.add("Nascimento");
        cabecalhos.add("Editar");
        cabecalhos.add("Excluir");

        ResultSet result = null;

        try {
            StringBuilder sql = new StringBuilder();
            sql.append(" SELECT id, nome, TO_CHAR(data_nascimento, 'dd/MM/yyyy') as data_nascimento ");
            sql.append(" FROM usuarios WHERE COALESCE(excluido,false) = false ");
            sql.append(" AND LOWER(nome) like LOWER('%");
            sql.append(texto);
            sql.append("%') ");
            sql.append("ORDER BY nome ");

            result = Conexao.stmt.executeQuery(sql.toString());

            Vector<Object> linha;
            while (result.next()) {
                linha = new Vector<Object>();

                linha.add(result.getInt(1));
                linha.add(result.getString(2));
                linha.add(result.getString(3));

                linha.add(iconeEditar);
                linha.add(iconeLixeira);

                dadosTabela.add(linha);
            }

        } catch (Exception e) {
            System.out.println("problemas para popular tabela...");
            System.out.println(e);
        }

        jtbUsuarios.setModel(new DefaultTableModel(dadosTabela, cabecalhos) {

            @Override
            public Class getColumnClass(int column) {
                return getValueAt(1, column).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            // permite seleção de apenas uma linha da tabela
        });

        // permite seleção de apenas uma linha da tabela
        jtbUsuarios.setSelectionMode(0);

        // redimensiona as colunas de uma tabela
        TableColumn column = null;
        for (int i = 0; i <= 4; i++) {
            column
                    = jtbUsuarios.getColumnModel().getColumn(i);
            switch (i) {
                case 0:
                    column.setPreferredWidth(60);//id 
                    break;
                case 1:
                    column.setPreferredWidth(200);//nome
                    break;
                case 2:
                    column.setPreferredWidth(100);//data
                    break;
                case 3:
                    column.setPreferredWidth(10);//editar
                    break;
                case 4:
                    column.setPreferredWidth(10);//excluir
                    break;
            }
        }

        //função para deixar a tabela zebrada
        jtbUsuarios.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
                if (row % 2 == 0) {
                    //setBackground(Color.LIGHT_GRAY);
                } else {
                    //setBackground(Color.WHITE);
                }

                return this;
            }
        });
        //return (true);
    }

 }
