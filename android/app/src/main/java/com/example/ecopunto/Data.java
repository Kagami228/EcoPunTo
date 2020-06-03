package com.example.ecopunto;

import android.annotation.SuppressLint;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import io.opencensus.common.Function;

@SuppressLint("Registered")
public class Data {

    public class Trash{
        String box_type;
        String event_type;
        String id;
        double lat;
        double Long;
    }
    public Vector<Trash> trashes;

    private static final String TAG = "DocSnippets";

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4,
            60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private final FirebaseFirestore db;

    Data(FirebaseFirestore db) {

        this.db = db;
        trashes = new  Vector<Trash>();
    }

    public void getAllTrash(final Function<Trash,Boolean> addTrash  ) throws InterruptedException {
        // [START get_all_users]
        db.collection("state")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Trash t =new Trash();// document.toObject(Trash.class);
                                t.box_type= document.getData().get("box_type").toString();
                                t.event_type= document.getData().get("event_type").toString();
                                t.id= document.getData().get("id").toString();
                                t.lat = Double.parseDouble(document.getData().get("lat").toString());
                                t.Long =Double.parseDouble(document.getData().get("long").toString());
                                addTrash.apply(t);
                            }

                        }
                    }
                });
    }


}