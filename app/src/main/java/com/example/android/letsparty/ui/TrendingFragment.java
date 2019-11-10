package com.example.android.letsparty.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.letsparty.R;
import com.example.android.letsparty.adapter.EventListAdapter;
import com.example.android.letsparty.model.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TrendingFragment extends Fragment implements EventListAdapter.OnEventItemClickedListener{

    private RecyclerView recyclerView;
    private EventListAdapter mAdapter;
    private ArrayList<Event> resultEvents;
    private ArrayList<String> eventKeys;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, null);
        recyclerView = view.findViewById(R.id.trending_event_list);
        resultEvents = new ArrayList<>();
        eventKeys = new ArrayList<>();
        mAdapter = new EventListAdapter(resultEvents, eventKeys, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("events").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    resultEvents.clear();
                    eventKeys.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Event event = document.toObject(Event.class);
                        String key = document.getId();
                        resultEvents.add(event);
                        eventKeys.add(key);
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.e("aaa", "task failed" + task.getResult());
                }
            }
        });

        return view;
    }



    private void openEventDetailActivity(String key){
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra("key", key);
        startActivity(intent);

    }

    @Override
    public void OnEventItemClicked(String key){
        openEventDetailActivity(key);
    }
}