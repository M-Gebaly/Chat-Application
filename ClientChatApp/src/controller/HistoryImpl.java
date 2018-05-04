/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import XML.jaxb1.FontFamily;
import XML.jaxb1.HistoryType;
import XML.jaxb1.MessageType;
import XML.jaxb1.ObjectFactory;
import java.io.InputStream;
import java.math.BigInteger;
import model.Message;

/**
 *
 * @author Asmaa
 */
public class HistoryImpl implements HistoryInterface{

    @Override
    public boolean saveHistory(ArrayList<Message> messages, File file,long ownerID) {
         try {
            JAXBContext context = JAXBContext.newInstance("XML.jaxb1");
            ObjectFactory factory = new ObjectFactory();
            HistoryType history = factory.createHistoryType();
            history.setOwner(BigInteger.valueOf(ownerID));
            List<MessageType> messagesXML = history.getMessage();
            //messagesXML.clear();
            //parse object message to xml
            for(Message message : messages) {
                MessageType messageType = factory.createMessageType();
                messageType.setFromID(BigInteger.valueOf(message.getFromID()));
                messageType.setFrom(message.getFrom());
                messageType.setTo(message.getTo());
                messageType.setContent(message.getContent());
                messageType.setColor(message.getFontColor());
                messageType.setFamily(FontFamily.fromValue(message.getFontFamily()));
                messageType.setSize(message.getFontSize());
                messageType.setDate(message.getDate());
                messagesXML.add(messageType);
                
            }
            JAXBElement<HistoryType> createHistory = factory.createHistory(history);
            Marshaller marsh = context.createMarshaller();
            marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //put Schcema
            marsh.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "history.xsd");
            //put xslt header
            marsh.setProperty("com.sun.xml.internal.bind.xmlHeaders","<?xml-stylesheet type='text/xsl' href='history.xsl'?>");
            
            // put xml into file
             
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            marsh.marshal(createHistory,fileOutputStream );
            fileOutputStream.close();
            
            //copy xsd to be at the same place with xml
             String dest[]=file.getPath().split(file.getName());
             
             String path=dest[0]+"history.xsd";
             File outFile=new File(path);            
             copyFile(outFile, "/XML/history.xsd");
             
             //copy xslt to be at the same place with xml
             path=dest[0]+"history.xsl";
             outFile=new File(path);
             copyFile(outFile, "/XML/history.xsl");
             
             //copy logo     
             path=dest[0]+"a.png";
             outFile=new File(path);             
             copyFile(outFile, "/resources/a.png");
             fileOutputStream.close();
        } catch (JAXBException | IOException ex) {
            Logger.getLogger(HistoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
       
         return true;
    }
    public void copyFile(File file,String path){
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            InputStream inputStream=getClass().getResource(path).openStream();
             int readByte ; 
             while((readByte=inputStream.read())!= -1)
                {
                    fileOutputStream.write(readByte);
                }
             fileOutputStream.close();
             inputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HistoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HistoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
             
    }
    
}
