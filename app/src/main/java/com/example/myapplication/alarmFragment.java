package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Vector;

public class alarmFragment extends Fragment {

    View view;
    Context context;
    RecyclerView rv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Read xml file and return View object.
        // inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot)
        view = inflater.inflate(R.layout.activity_alarm_fragment, container, false);
        context=view.getContext();
        //	get	the	reference	of	RecyclerView
        rv	=	(RecyclerView)	view.findViewById(R.id.list);
        //	set	a	LinearLayoutManager	with	default	vertical	orientation
        rv.setLayoutManager(new LinearLayoutManager(context));
        //	call	the	constructor	of	MyAdapter	to	send	the	reference	and	data	to	Adapter
        //rv.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        //rv.setItemAnimator(new MyAnimator());
        return view;
    }

    public void showAlarmes(List<AlarmInfo> listeAlarmes) {
        rv.setAdapter(new MyAdapter(listeAlarmes));
    }
}