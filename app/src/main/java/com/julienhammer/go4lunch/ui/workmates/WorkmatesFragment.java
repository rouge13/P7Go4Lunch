package com.julienhammer.go4lunch.ui.workmates;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.FragmentWorkmatesBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.viewmodel.ListViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkmatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkmatesFragment extends Fragment {
    FragmentWorkmatesBinding binding;


    private void configureListViewModel() {
        ViewModelFactory listViewModelFactory = ViewModelFactory.getInstance();
        ListViewModel listViewModel =
                new ViewModelProvider(this, listViewModelFactory).get(ListViewModel.class);

        this.listViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance()
        ).get(ListViewModel.class);
//        listViewModel.refresh();
    }


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WorkmatesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkmatesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkmatesFragment newInstance(String param1, String param2) {
        WorkmatesFragment fragment = new WorkmatesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    }
}