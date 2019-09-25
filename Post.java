/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blogutil;


/**
 *Author : Josh Lucas, Yatharth Soni
 *Sheridan ID :____________ ,991503772
 *Created : 2018/08/06 
 *Instructor : Haya Ghalayini
 *Project : Final Assignment
 * Blog Util
 * File Name : Post.java
 */

public class Post {
    final private String authorName;
    final private String title;
    final private String subject;
    final private String province;
    final private String date;
    final private String post;
    
    /*Post Constructor- generates variables and assigns all the parameters sent 
     *while creating the object to the variables
     */
public Post(String author, String title, String subject, 
            String province, String date, String post) {
        this.authorName = author;
        this.title = title;
        this.subject = subject;
        this.province = province;
        this.date = date;
        this.post = post;
        
    }
    
    //getter methods for Author, Title, Subject, Province, Date, Post as well as RawStrings
    public String getAuthor() {
        return authorName;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public String getProvince() {
        return province;
    }
    
    public String getDate() {
        return date;
    }
    
    public String getPost() {
        return post;
    }
    
    public String getRawStrings() {
        return (
            this.authorName +
            this.title +
            this.subject +
            this.province +
            this.date +
            this.post).toLowerCase();
        
    }
}
