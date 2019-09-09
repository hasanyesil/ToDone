package com.greenluck.todone.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.greenluck.todone.R;
import com.greenluck.todone.data.database.DatabaseHelper;
import com.greenluck.todone.model.TaskList;


public class DeleteListDialogFragment extends DialogFragment {

    private TextView mDeleteListTextview;
    private DatabaseHelper mHelper;
    private TaskList mList;
    private OnListDeletedListener mOnListDeletedListener;

    public interface OnListDeletedListener{
        void onListDeleted(TaskList list);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnListDeletedListener = (OnListDeletedListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        mHelper = DatabaseHelper.getInstance(getContext());
        mList = getArguments().getParcelable("list");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_delete_list,null);

        mDeleteListTextview = (TextView) view.findViewById(R.id.delete_list_textview);
        mDeleteListTextview.setText(getString(R.string.delete_list,mList.getName()));

        builder.setView(view)
                .setTitle("Are you sure?")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mOnListDeletedListener.onListDeleted(mList);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black87));
        return alertDialog;
    }
}
