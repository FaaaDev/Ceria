package com.faadev.ceria.screen.ui.my_post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.faadev.ceria.adapter.TabAdapter;
import com.faadev.ceria.databinding.FragmentMyPostBinding;
import com.faadev.ceria.http.ApiService;
import com.faadev.ceria.http.response.MyPostResponse;
import com.faadev.ceria.model.MyPost;
import com.faadev.ceria.screen.fragment.ListPostFragment;
import com.faadev.ceria.utils.ShowDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPostFragment extends Fragment {

    private FragmentMyPostBinding binding;
    private TabAdapter adapter;
    private ApiService apiService;
    private List<MyPost> listApprove;
    private List<MyPost> listWaiting;
    private List<MyPost> listReject;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyPostBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = new ApiService(getContext());

        listApprove = new ArrayList<>();
        listWaiting = new ArrayList<>();
        listReject = new ArrayList<>();

        adapter = new TabAdapter(getChildFragmentManager());
        Bundle approveBundle = new Bundle();
        approveBundle.putSerializable("data", (Serializable) listApprove);
        ListPostFragment approve = new ListPostFragment();
        approve.setArguments(approveBundle);

        Bundle waitingApprove = new Bundle();
        waitingApprove.putSerializable("data", (Serializable) listWaiting);
        ListPostFragment waiting = new ListPostFragment();
        waiting.setArguments(waitingApprove);

        Bundle rejectBundle = new Bundle();
        rejectBundle.putSerializable("data", (Serializable) listReject);
        ListPostFragment reject = new ListPostFragment();
        reject.setArguments(rejectBundle);

        adapter.addFragment(approve, "Disetujui");
        adapter.addFragment(waiting, "Menunggu");
        adapter.addFragment(reject, "Ditolak");
        binding.vp.setAdapter(adapter);
        binding.tab.setupWithViewPager(binding.vp);
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyPost();
    }

    private void getMyPost() {
        listApprove = new ArrayList<>();
        listWaiting = new ArrayList<>();
        listReject = new ArrayList<>();
        apiService.getMyPost(new Callback<MyPostResponse>() {
            @Override
            public void onResponse(Call<MyPostResponse> call, Response<MyPostResponse> response) {
                System.out.println(response.body().getCode());
                if (response.body().getCode() == 200) {
                    for (MyPost x : response.body().getData()) {
                        if (x.getApprove() == 1) {
                            listApprove.add(x);
                        } else if (x.getApprove() == 0) {
                            listWaiting.add(x);
                        } else {
                            listReject.add(x);
                        }
                    }

                    adapter = new TabAdapter(getChildFragmentManager());
                    Bundle approveBundle = new Bundle();
                    approveBundle.putSerializable("data", (Serializable) listApprove);
                    ListPostFragment approve = new ListPostFragment();
                    approve.setArguments(approveBundle);

                    Bundle waitingApprove = new Bundle();
                    waitingApprove.putSerializable("data", (Serializable) listWaiting);
                    ListPostFragment waiting = new ListPostFragment();
                    waiting.setArguments(waitingApprove);

                    Bundle rejectBundle = new Bundle();
                    rejectBundle.putSerializable("data", (Serializable) listReject);
                    ListPostFragment reject = new ListPostFragment();
                    reject.setArguments(rejectBundle);

                    adapter.addFragment(approve, "Disetujui");
                    adapter.addFragment(waiting, "Menunggu");
                    adapter.addFragment(reject, "Ditolak");
                    binding.vp.setAdapter(adapter);
                    binding.tab.setupWithViewPager(binding.vp);
                } else {
                    ShowDialog.showError(getChildFragmentManager(), response.body().getCode(), "Error " + response.code() + "-Gagal medapatkan data");
                }
            }

            @Override
            public void onFailure(Call<MyPostResponse> call, Throwable t) {
                System.out.println(t.getMessage());
                ShowDialog.showError(getChildFragmentManager(), 500, "Server lagi bermasalah nih, coba lagi nanti yaa..");
            }
        });
    }
}