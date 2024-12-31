package controllers;

import data.DBFacade;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ActivityLogger {
    private static final DBFacade dbfacade = DBFacade.getInstance();
    private void commentLog(String documentId, int userId,String comment){
        LocalDateTime localDateTime = LocalDateTime.now();
        if (comment != null && !comment.isEmpty()) {
            try {
                dbfacade.addActivity(documentId,userId,"Added a comment: "+comment, localDateTime);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void statusLog(String documentId, int userId,String newStatus){
        LocalDateTime localDateTime = LocalDateTime.now();
        try {
            dbfacade.addActivity(documentId,userId,"Updated status to "+newStatus, localDateTime);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}