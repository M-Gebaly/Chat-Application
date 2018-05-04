/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.util.ArrayList;
import model.Message;

/**
 *
 * @author Asmaa
 */
public interface HistoryInterface {
    public boolean saveHistory(ArrayList<Message> messages,File file,long ownerID);
    
}
