package ru.nomad.weather;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ErrorDialogFragment extends DialogFragment {

    private final String textError;

    public ErrorDialogFragment(String textError) {
        this.textError = textError;
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_error, container, false);
        ((TextView) view.findViewById(R.id.text_error)).setText(textError);
        view.findViewById(R.id.close_dialog).setOnClickListener(v -> dismiss());
        return view;
    }*/

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title_dialog)
                .setPositiveButton(R.string.close_dialog, (dialog, which) -> dismiss())
                .setMessage(textError);
        return builder.create();
    }
}