package com.scarecrow.root.orchardapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scarecrow.root.orchardapp.R;

/**
 * Created by root on 17-8-1.
 */

public class FragmentEmpty extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_empty, viewgroup, false);
        return v;

    }
}

