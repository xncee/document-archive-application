package services;

import data.DBFacade;
import utils.ErrorHandler;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class ActivityLogger {
    private static final DBFacade dbfacade = DBFacade.getInstance();
    private void commentLog(String documentId, int userId,String comment){
        LocalDateTime localDateTime = LocalDateTime.now();
        if (comment != null && !comment.isEmpty()) {
            dbfacade.addActivity(documentId,userId,"Added a comment: "+comment, localDateTime);
        }
    }
    private void statusLog(String documentId, int userId,String newStatus){
        LocalDateTime localDateTime = LocalDateTime.now();
        dbfacade.addActivity(documentId,userId,"Updated status to "+newStatus, localDateTime);
    }
}