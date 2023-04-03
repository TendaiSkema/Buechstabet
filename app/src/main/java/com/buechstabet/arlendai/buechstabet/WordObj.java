package com.buechstabet.arlendai.buechstabet;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WordObj {
    Date createdat;
    DocumentReference createdby;
    String dicription;
    String example;
    String type;
    String word;

     public WordObj(){
         this.createdat = new Date();
         this.dicription = "";
         this.word = "";
         this.type = "";
         this.example = "";
     }

    public Map<String, Object> toMap(){
        HashMap<String, Object> retMap = new HashMap<>();
        retMap.put("word", this.word);
        retMap.put("createdAt", this.createdat);
        retMap.put("createdBy", this.createdby);
        retMap.put("example", this.example);
        retMap.put("disc", this.dicription);
        retMap.put("type", this.type);
        return retMap;
    }

    public static WordObj WordFromQueryDocumentSnapshot(QueryDocumentSnapshot document){
         WordObj doc = new WordObj();
         doc.word = document.getString("word");
         doc.createdby = (DocumentReference) document.get("createdBy");
         doc.createdat = document.getDate("createdAt");
         doc.dicription = document.getString("disc");
         doc.example = document.getString("example");
         doc.type = document.getString("type");

         return doc;
    }
}
