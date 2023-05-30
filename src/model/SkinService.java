package model;

import java.sql.*;
import java.util.ArrayList;

public class SkinService {
    private static String connectionUrl = "jdbc:ucanaccess://src//resources//BunkerRun.accdb";
    public static ArrayList<Skin> getSkins() {
        ArrayList<Skin> skins = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("Select * from Skins");
            while(rs.next()){
                Skin skin = new Skin(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getBoolean(6), rs.getBoolean(7));
                skins.add(skin);
            }
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return skins;
    }
    public static void buySkin(int id) {
        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            String sql = "Update Skins set is_bought=true where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public static void pickSkin(int id) {
        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            String sql1 = "Update Skins set is_picked=true where id=?";
            String sql2 = "Update Skins set is_picked=false where id!=?";
            PreparedStatement ps1 = connection.prepareStatement(sql1);
            PreparedStatement ps2 = connection.prepareStatement(sql2);
            ps1.setInt(1, id);
            ps2.setInt(1, id);
            ps1.executeUpdate();
            ps2.executeUpdate();
            ps1.close();
            ps2.close();
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getPickedSkinSprite() {
        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("Select sprite from Skins where is_picked=true");
            if(rs.next()){
                return rs.getString(1);
            }
            connection.close();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
