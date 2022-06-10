package com.example.tbm_task.service;

import com.example.tbm_task.model.InfoBts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class InfoBtsService {


    public List<InfoBts> getInfoBts() {
        List<InfoBts> allInfoBts = new ArrayList<>();
        try{
            Class.forName("org.postgresql.Driver");
            Connection con= DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres","postgres","root123");
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery("select * from tbs_data");
            while(rs.next()) {
                InfoBts infoBts = new InfoBts();
                infoBts.setId(rs.getString(1));
                infoBts.setLatitude(rs.getString(2));
                infoBts.setLongitude(rs.getString(3));
                allInfoBts.add(infoBts);
            }
            con.close();
        }catch(Exception e){ System.out.println(e);}

        return allInfoBts;
    }

}
