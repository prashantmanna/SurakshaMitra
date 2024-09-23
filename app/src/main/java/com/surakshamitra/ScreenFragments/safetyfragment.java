// SafetyFragment.java
package com.surakshamitra.ScreenFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surakshamitra.R;
import com.surakshamitra.videoAdaptor;
import com.surakshamitra.youtubeVideo;

import java.util.ArrayList;
import java.util.List;

public class safetyfragment extends Fragment {

    private RecyclerView recyclerView;
    private List<youtubeVideo> youtubeVideos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_safetyfragment, container, false);

        recyclerView = view.findViewById(R.id.safetyRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        youtubeVideos.add(new youtubeVideo("jv_gSMi9M2c"));
        youtubeVideos.add(new youtubeVideo("SY7RhBszp0k"));
        youtubeVideos.add(new youtubeVideo("TmxCvx2D2B8"));
        youtubeVideos.add(new youtubeVideo("tJsGGsPNakw"));

        videoAdaptor adapter = new videoAdaptor(youtubeVideos);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
