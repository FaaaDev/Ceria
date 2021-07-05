package com.faadev.ceria.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.faadev.ceria.R;
import com.faadev.ceria.adapter.CardAdapter;

public class TerbaruFragment extends Fragment {

    private RecyclerView rv_content1;
    private CardAdapter cra1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_terbaru, container, false);

        _init(root);
        _prep();

        return root;
    }

    private void _init(View root){
        rv_content1 = root.findViewById(R.id.rv_content1);

    }

    private void _prep(){
        cra1 = new CardAdapter(getContext());
        rv_content1.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        rv_content1.setAdapter(cra1);
    }
}