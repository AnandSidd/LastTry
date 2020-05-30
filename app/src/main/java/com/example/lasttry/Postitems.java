package com.example.lasttry;

import com.google.firebase.firestore.PropertyName;
import com.google.j2objc.annotations.Property;

public class Postitems {
        private String PostUrl;
        private String Email;
        private String Caption;


        public String getPostUrl(){
            return PostUrl;
        }

        public String getEmail(){
            return Email;
        }
        public String getCaption(){
            return Caption;
        }

        public void setPostUrl(String PostUrl){
            this.PostUrl = PostUrl;
        }
        public void setEmail(String Email){
            this.Email = Email;
        }
        public void setCaption(String Caption){
            this.Caption = Caption;
        }

        public Postitems(){}

        public Postitems(String PostUrl, String Email, String Caption){
            this.PostUrl = PostUrl;
            this.Email = Email;
            this.Caption = Caption;
        }
}
