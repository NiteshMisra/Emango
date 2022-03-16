package task.emango.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import task.emango.R;
import task.emango.databinding.FragmentHomeBinding;
import task.emango.rest.response.CountryListData;
import task.emango.utils.SpinAdapter;
import task.emango.viewmodel.MyViewModel;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyViewModel myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        showProgressBar(null);
        myViewModel.getCountryList(requireContext()).observe(getViewLifecycleOwner(),countryResponse -> {
            hideProgress();
            if (countryResponse == null){
                Toast.makeText(requireContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!countryResponse.getSuccess()){
                if (!TextUtils.isEmpty(countryResponse.getError()))
                    Toast.makeText(requireContext(), countryResponse.getError(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(requireContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayAdapter<CountryListData> dataAdapter = new SpinAdapter(requireContext(), android.R.layout.simple_spinner_item, countryResponse.getData().toArray(new CountryListData[0]));
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinner.setAdapter(dataAdapter);

        });

    }
}