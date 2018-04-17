package it.dsgroup.provafinale.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import it.dsgroup.provafinale.LoginActivity;
import it.dsgroup.provafinale.R;
import it.dsgroup.provafinale.models.Pacco;
import it.dsgroup.provafinale.utilities.JasonParser;

/**
 * Created by utente9.academy on 15/12/2017.
 */

public class PushNotification extends Service {

    private DatabaseReference refInsert;
    private DatabaseReference refStato;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ChildEventListener child1;
    private SharedPreferences preferences;
    private Pacco paccoInConsegna;
    private ChildEventListener child2;
    private String utenteAttivo;
    private String tipoUtenteAttivo;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        tipoUtenteAttivo = preferences.getString("tipoUtenteAttivo", "");



        utenteAttivo = preferences.getString("utenteAttivo", "");


        child1 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    //Pacco pacco = userSnapshot.getValue(Pacco.class);
                    //if (pacco.getStato()!=null && pacco.getStato().equals("in consegna")) {
                        //pushValidation(utenteAttivo + " : pacco da consegnare con id " + pacco.getIdPacco());
                    //}
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    //Pacco pacco = userSnapshot.getValue(Pacco.class);
                    //if (pacco.getStato()!=null && pacco.getStato().equals("in consegna")) {
                      //  pushValidation(utenteAttivo + " : pacco da consegnare con id " + pacco.getIdPacco());
                    //}
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        child2 = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String utente = preferences.getString("corriereCommissionato","");
                if (utente.equals(utenteAttivo)){

                    pushValidation("pacco " + dataSnapshot.getValue() + " commissionato");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                pushValidation("pacco " + dataSnapshot.getValue() + " in consegna");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        refInsert = database.getReferenceFromUrl("https://provafinale-733e5.firebaseio.com/users/clienti");
        refStato = database.getReferenceFromUrl("https://provafinale-733e5.firebaseio.com/users/corrieri");
        refInsert.addChildEventListener(child1);
        refStato.addChildEventListener(child2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void pushValidation(String chiave) {
        Intent i = new Intent(this, LoginActivity.class);
        sendNotificaton(i, "", chiave);
    }


    private void sendNotificaton(Intent intent, String s, String chiave) {
        //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher_round);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent activity = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT); // serve per settare l'activity che deve partire al click della notifica

        Uri dsUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this); // creazione oggetto notifica
        builder.setContentTitle(s);
        builder.setContentText(chiave);
        builder.setAutoCancel(true);
        builder.setSound(dsUri);
        builder.setSmallIcon(R.mipmap.ic_launcher);// se non metto questa immagine potrebbe non funzionare la notifica
        //builder.setLargeIcon(bitmap);
        builder.setShowWhen(true);
        builder.setContentIntent(activity);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // l'oggetto che rende visibile l'oggetto notifica
        manager.notify(0, builder.build());
    }
}
