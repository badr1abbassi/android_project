package com.example.myapplication;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class MedicamentAdapter extends RecyclerView.Adapter<MedicamentAdapter.MyViewHolder> {


    private List<MedicamentInfos> medicamentInfos;


    public MedicamentAdapter(List<MedicamentInfos> medicamentInfos) {
        this.medicamentInfos = medicamentInfos;
    }

    @Override
    // retournele nb total de cellule que contiendra la liste
    public int getItemCount() {
        return medicamentInfos.size();
    }

    @Override
    //crée la vu d'une cellule
    // parent pour créer la vu et int pour spécifier le type de la cellule si on a plusieurrs type (orgnaisation differts)
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //pour créer un laouyt depuis un XML
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    //appliquer ne donnée à une vue
    public void onBindViewHolder(MyViewHolder holder, int position) {

        MedicamentInfos Etab= (MedicamentInfos) medicamentInfos.get(position);
        System.out.println("Vname =" + Etab.getMessage());
        System.out.println("position=" + position);

        //  System.out.println("position =" + position);
        try {
            holder.display(Etab);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView message;
        private final TextView time;
        private final ImageView img;
        private Context mContext;
        //  private Pair<String, String> currentPair;
        private MedicamentInfos currentAlarm;


        public MyViewHolder(final View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            img=  itemView.findViewById(R.id.img);
            mContext=itemView.getContext();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle(currentAlarm.getMessage());
                    builder.setMessage("Supprimer "+currentAlarm.getMessage()+" ?")
                            .setCancelable(false)
                            .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    cancelAlarm(currentAlarm.getId());
                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("alarmes").child("alarm"+ currentAlarm.getId())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                dataSnapshot.getRef().removeValue();
                                            Intent intent = new Intent(mContext, ListeMedicamentActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            mContext.startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e("ERROR","onCancelled", databaseError.toException());
                                        }
                                    });
                                }
                            });
                    builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            Toast.makeText(itemView.getContext(),	"modifier "+currentAlarm.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        public void display(MedicamentInfos alarm) throws ParseException {
            currentAlarm = alarm;
            message.setText(currentAlarm.getMessage());
            String currentDate;

            if(currentAlarm.getDate().getYear()!=0) {
                currentDate = (String) android.text.format.DateFormat.format("yyyy-MM-dd hh:mm", currentAlarm.getCalendar());

            }else{
                currentDate = (String) android.text.format.DateFormat.format("hh:mm", currentAlarm.getCalendar());
                currentDate="chaque jour à : "+currentDate;
            }
            time.setText(currentDate);
            //time.setText(currentAlarm.getCalendar().getTime()+"");
             //img.setImageURI(currentAlarm.getImage());
            //img.setImageURI(null);
            Picasso.get().load(currentAlarm.getImageUrl()).centerCrop().fit().into(img);
            Log.d("Adapter url: ",currentAlarm.getImageUrl());

            img.setContentDescription("alarm");
        }
        public void cancelAlarm(int id){
            AlarmManager alarmManager =(AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            Intent intent =new Intent(mContext,AlertReceiver.class);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(mContext,id,intent,0);
            alarmManager.cancel(pendingIntent);
            Toast.makeText(mContext, "Alarme OFF", Toast.LENGTH_LONG).show(); //Generate a toast only if you want
        }

    }

}