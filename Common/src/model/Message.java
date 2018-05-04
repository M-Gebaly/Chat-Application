/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Asmaa
 */
public class Message implements Serializable{
    private long fromID;
    private String from;
    private String to;
    private XMLGregorianCalendar date;
    private String content;
    private String fontColor;
    private String fontFamily;
    private int fontSize;
    private String fromEmail;
    private String toEmail;
    private String gender;
    private String status;
    public Message() {
    }

    public Message(long fromID,String from, String to, String content, XMLGregorianCalendar date, String fontColor, String fontFamily, int fontSize) {
        this.fromID=fromID;
        this.from = from;
        this.to = to;
        this.content = content;
        this.date = date;
        this.fontColor = fontColor;
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
    }

    public String getFrom() {
        return from;
    }
    
    
    
    public String getContent() {
        return content;
    }

    public XMLGregorianCalendar getDate() {
        return date;
    }

    public String getFontColor() {
        return fontColor;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public String getTo() {
        return to;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(XMLGregorianCalendar date) {
        this.date = date;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public void setFontFamily(String fontStyle) {
        this.fontFamily = fontStyle;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public long getFromID() {
        return fromID;
    }

    public void setFromID(long fromID) {
        this.fromID = fromID;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    

   
}
