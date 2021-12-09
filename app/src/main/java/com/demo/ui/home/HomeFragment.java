package com.demo.ui.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.databinding.FragmentHomeBinding;
import com.demo.model.Device;
import com.demo.ui.BaseActivity;
import com.demo.ui.BaseFragment;
import com.demo.ui.liveview.LiveViewFragment;
import com.demo.utils.Utils;
import com.sdk.NetDEVSDK;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private FragmentHomeBinding binding;

    private ProgressDialog dialog;

    public HomeFragment() {
    }

//    public HomeFragment(BaseActivity activity) {
//        super(activity);
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View mRootView = binding.getRoot();
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialog = new ProgressDialog(getActivity());

        binding.btnDevList.setOnClickListener(v -> {
            getDevList();
        });

        binding.btnDevLogin.setOnClickListener(v -> {
            devLogin();
        });
    }

    private void getDevList(){
        Device device = new Device();
        List<Device> devices = device.getDevices();

        if (devices == null) {
            Utils.showAlert(getActivity(), "", "Query cloud device list failed.");
        } else {
            String[] szDevListArr = new String[devices.size()];
            for(int i=0; i<devices.size(); i++){
                szDevListArr[i] = devices.get(i).getUsername();
            }

            AlertDialog.Builder oBuilder = new AlertDialog.Builder(getActivity());
            oBuilder.setTitle("Device List");
            oBuilder.setSingleChoiceItems(szDevListArr, 0, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int witch) {
                    String str = szDevListArr[witch];
                    binding.tvDevName.setText(str);
                    NetDEVSDK.strDevName = str;
                }
            });
            oBuilder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            oBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            oBuilder.show();
        }
    }

    private void devLogin(){
        Device device = new Device();
        device.setName(binding.tvDevName.getText().toString());
        device.login(new Device.OnLoginListener() {
            @Override
            public void onPreExecute() {
                dialog.setMessage("device logging, please wait.");
                dialog.show();
            }

            @Override
            public void onPostExecute(long id) {
                dialog.dismiss();
                if (id == 0) {
                    Utils.showAlert(getActivity(), "","Login was failed!");
                } else {
//                    Bundle bundle = new Bundle();
//
//                    bundle.putLong("cloudLpUserID", NetDEVSDK.lpUserID);
//                    bundle.putString("szDevName",NetDEVSDK.strDevName);
//
//                    LiveViewFragment f = new LiveViewFragment(getActivity());
//                    f.setArguments(bundle);
//                    (mActivity).showFragment(f, true);
                }
            }
        });
    }
}
