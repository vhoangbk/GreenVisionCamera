package com.demo.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.databinding.FragmentOtpConfirmBinding;
import com.demo.ui.BaseActivity;
import com.demo.ui.BaseFragment;
import com.demo.ui.MainActivity;
import com.demo.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class OTPConfirmFragment extends BaseFragment {

    private ProgressDialog dialog;
    private OTPConfirmViewModel viewModel;
    private FragmentOtpConfirmBinding binding;

    public OTPConfirmFragment(BaseActivity activity) {
        super(activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(mActivity).get(OTPConfirmViewModel.class);
        binding = FragmentOtpConfirmBinding.inflate(inflater, container, false);
        View mRootView = binding.getRoot();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        binding.btnConfirmOtp.setOnClickListener(v -> {
            viewModel.cloudLogin(new OTPConfirmViewModel.OnCloudLoginListener() {
                @Override
                public void onPreExecute() {
                    dialog.setMessage("Logging, please wait.");
                    dialog.show();
                }

                @Override
                public void onPostExecute(long id) {
                    dialog.dismiss();
                    if (id != 0) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else {
                        Utils.showAlert(getActivity(),"Fail", "Login failed, please check if the input is correct.");
                    }
                }
            });
        });
    }

}
