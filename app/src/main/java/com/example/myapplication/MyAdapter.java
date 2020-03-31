package com.example.myapplication;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Vector;

import androidx.recyclerview.widget.RecyclerView;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private Vector<AlarmInfo> alarmInfos;

    public MyAdapter(Vector<AlarmInfo> alarmInfos) {
        this.alarmInfos=alarmInfos;
    }

    @Override
    // retournele nb total de cellule que contiendra la liste
    public int getItemCount() {
        return alarmInfos.size();
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

        AlarmInfo Etab= (AlarmInfo) alarmInfos.get(position);
        System.out.println("Vname =" + Etab.getMessage());
        System.out.println("position=" + position);

        //  System.out.println("position =" + position);
        holder.display(Etab);


    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView message;
        private final TextView time;
        private final ImageView img;

        //  private Pair<String, String> currentPair;
        private AlarmInfo currentAlarm;


        public MyViewHolder(final View itemView) {
            super(itemView);

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            img=  itemView.findViewById(R.id.img);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                    builder.setTitle(currentAlarm.getMessage());
                    builder.setMessage("quest ce que vous voulez ?")
                            .setCancelable(false)
                            .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Toast.makeText(itemView.getContext(),	"supp "+currentAlarm.getMessage(),
                                            Toast.LENGTH_SHORT).show();

                                    /*MainActivity.mydatabase.etabDao().deleteEtabByName(currentEtab.getName());
                                    Intent intent =new Intent(itemView.getContext(),Mylist.class);
                                    itemView.getContext().startActivity(intent);*/
                                }
                            });
                    builder.setNegativeButton("modifier", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            /*Intent intent =new Intent(itemView.getContext(),MapsActivity.class);
                            intent.putExtra("etab",currentEtab);
                            itemView.getContext().startActivity(intent);
                            */
                            Toast.makeText(itemView.getContext(),	"modifier "+currentAlarm.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });
        }

        public void display(AlarmInfo alarm) {
            currentAlarm = alarm;
            message.setText(currentAlarm.getMessage());
            //time.setText(currentAlarm.getDate()+" "+currentAlarm.getTime());
            String currentDate= DateFormat.getDateInstance(DateFormat.LONG).format(currentAlarm.getCalendar().getTime());
            time.setText(currentDate);
            img.setImageResource(R.drawable.alarm);
            img.setContentDescription("alarm");

        }
    }

}