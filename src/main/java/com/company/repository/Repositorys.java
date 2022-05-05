package com.company.repository;

import com.company.TelegramBot;
import com.company.entity.ElonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.*;

@Component
public class Repositorys {
    @Autowired
    private TelegramBot telegramBot;

    //Create
    public void create_card(ElonDTO elonDTO,int id,String firsName,String username) {

        Connection connection = getConnection();
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1=null;
        
        String sql = "insert into elon_table(title, content,photo )" +
                "values(?,?,?)";
        
        String sql1= """
                insert into elon_profile (profile_id,name,username) 
                values (?,?,?);
                    """;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, elonDTO.getTitile());
            preparedStatement.setString(2, elonDTO.getContent());
            preparedStatement.setString(3, elonDTO.getPhoto());

            preparedStatement.executeUpdate();

            preparedStatement1= connection.prepareStatement(sql1);
            preparedStatement1.setInt(1,id);
            preparedStatement1.setString(2,firsName);
            preparedStatement1.setString(3,username);

            preparedStatement1.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement1 != null) {
                try {
                    preparedStatement1.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //read
    public void card_list(Message message) {
        Connection connection = getConnection();
        Statement statement = null;

        try {
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from elon_table;");


            while (resultSet.next()) {
                String title=resultSet.getString("title");
                String content=resultSet.getString("content");
                String photo=resultSet.getString("photo");
                SendPhoto sendPhoto=new SendPhoto();
                sendPhoto.setChatId(String.valueOf(message.getChatId()));
                sendPhoto.setPhoto(new InputFile(photo));
                sendPhoto.setCaption("Title: "+title+"\n"+"Content: "+content);
                telegramBot.sendMsg(sendPhoto);

            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/java_db", "jb_java", "root");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
