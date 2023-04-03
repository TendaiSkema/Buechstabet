package com.buechstabet.arlendai.buechstabet;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class DbManager {
    FirebaseFirestore db;
    DbManager(){
        db = FirebaseFirestore.getInstance();
    }

    private void onSuccess(DocumentReference documentReference) {
        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
    }

    private void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Error adding document", e);
    }

    public void AddUser(String loc, String uid, Map<String, Object> data){
        // Add a new document with a generated ID
        db.collection(loc)
                .add(data)
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(this::onFailure);
    }

    public void AddWord(String loc, Map<String, Object> data, OnCompleteListener onCompleteListener){
        // Add a new document with a generated ID
        db.collection(loc)
                .add(data)
                .addOnSuccessListener(this::onSuccess)
                .addOnFailureListener(this::onFailure);
    }

    public void readData(String col, OnCompleteListener onCompleteListener){
        ArrayList<DocumentSnapshot> qDsList;
        db.collection(col)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }
}
