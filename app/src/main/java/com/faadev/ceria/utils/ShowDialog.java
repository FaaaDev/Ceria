package com.faadev.ceria.utils;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import com.faadev.ceria.screen.fragment.CancelConfirmationFragment;
import com.faadev.ceria.screen.fragment.DeleteConfirmationFragment;
import com.faadev.ceria.screen.fragment.ErrorFragment;
import com.faadev.ceria.screen.fragment.ImagePickerFragment;

public class ShowDialog {

    public static void showError(FragmentManager fragmentManager, int code, String message) {
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putString("code", String.valueOf(code));
        ErrorFragment errorFragment = new ErrorFragment();
        errorFragment.setArguments(bundle);
        errorFragment.show(fragmentManager, errorFragment.getTag());
    }

    public static void showPicker(FragmentManager fragmentManager) {
        ImagePickerFragment imagePickerFragment = new ImagePickerFragment();
        imagePickerFragment.show(fragmentManager, imagePickerFragment.getTag());
    }

    public static void showConfirmDelete(FragmentManager fragmentManager) {
        DeleteConfirmationFragment delete = new DeleteConfirmationFragment();
        delete.show(fragmentManager, delete.getTag());
    }

    public static void showConfirmCancel(FragmentManager fragmentManager) {
        CancelConfirmationFragment cancel = new CancelConfirmationFragment();
        cancel.show(fragmentManager, cancel.getTag());
    }

    public static void showConfirmCancel(FragmentManager fragmentManager, String messages) {
        CancelConfirmationFragment cancel = new CancelConfirmationFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg", messages);
        cancel.setArguments(bundle);
        cancel.show(fragmentManager, cancel.getTag());
    }
}
