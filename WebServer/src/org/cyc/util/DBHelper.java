/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBHelper {

    public static Connection connect() {
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/edu");
            Connection con = ds.getConnection();
            return con;
        } catch (Exception ex) {
        	System.out.println("“Ï≥£"+ex.toString());
            return null;
        }
    }

    public static void closeResult(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException e) {
        }
    }

    public static void closePreparedStatement(PreparedStatement ps) {
        try {
            ps.close();
        } catch (SQLException e) {
        }
    }

    public static void closeConneciton(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
        }
    }
}
