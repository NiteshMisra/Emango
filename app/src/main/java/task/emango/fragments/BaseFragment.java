package task.emango.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.lang.ref.WeakReference;
import java.util.Objects;

import task.emango.R;

public class BaseFragment extends Fragment {

    private ProgressDialog pDialog;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgressBar(String message) {
        if (pDialog == null) {
            pDialog = new ProgressDialog(requireActivity());
        }
        if (!pDialog.isShowing() && !requireActivity().isFinishing()) {
            pDialog.setTitle(null);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            if (message == null){
                Objects.requireNonNull(pDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pDialog.show();
                pDialog.setContentView(R.layout.progress_bar_layout);
            }else if (message.isEmpty()){
                Objects.requireNonNull(pDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                pDialog.show();
                pDialog.setContentView(R.layout.progress_bar_layout);
            }else {
                pDialog.setMessage(message);
                pDialog.show();
            }
        }
    }

    public void hideProgress() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }
}
