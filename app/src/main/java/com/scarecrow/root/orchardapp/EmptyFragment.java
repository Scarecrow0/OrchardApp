package com.scarecrow.root.orchardapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 17-8-1.
 */

public class EmptyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewgroup,
                             Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.empty_fragment, viewgroup, false);
        return v;

    }
}

