package com.greenluck.todone.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.greenluck.todone.R;
import com.greenluck.todone.model.Task;
import com.greenluck.todone.model.TaskList;


import java.util.ArrayList;

public class AddListDialogFragment extends DialogFragment implements View.OnClickListener {

    private EditText mListNameEdittext;

    private Button mPositiveButton;
    private Button mNegativeButton;

    private ArrayList<Button> colorButtons;
    private int selectedButtonOrder;
    private int selectedColor;
    //Sizes
    private int _25dp;
    private int _20dp;

    private OnAddListDialogResultListener mListDialogResult;

    public interface OnAddListDialogResultListener {
        void getCreatedList(TaskList list);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListDialogResult = (OnAddListDialogResultListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        selectedButtonOrder = 0;
        selectedColor = R.color.grade_gray;

        _20dp = Math.round(convertDpToPixel(20,getContext()));
        _25dp = Math.round(convertDpToPixel(25,getContext()));

        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_list,null);
        mListNameEdittext = (EditText) view.findViewById(R.id.list_name_edit_text);

        initializeButtons(view);

        builder.setView(view)
                .setTitle("New List")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Create List", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String listName = mListNameEdittext.getText().toString();
                        if (!listName.equals("")){
                            java.util.List<Task> tasks = new ArrayList<>();
                            TaskList list = new TaskList(listName,selectedColor);
                            mListDialogResult.getCreatedList(list);
                        }
                    }
                });

        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();


        mPositiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        mPositiveButton.setTextColor(getResources().getColor(R.color.grade_gray));
        mNegativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        mNegativeButton.setTextColor(getResources().getColor(R.color.grade_gray));

        return alertDialog;
    }

    @Override
    public void onClick(View view) {

        LinearLayout.LayoutParams paramUnselectedButton = new LinearLayout.LayoutParams(_20dp,_20dp);
        paramUnselectedButton.setMargins(_20dp,_20dp,0,_20dp);

        LinearLayout.LayoutParams paramsSelectedButton = new LinearLayout.LayoutParams(_25dp,_25dp);
        paramsSelectedButton.setMargins(_20dp,_20dp,0,_20dp);
        switch (view.getId()){
            case R.id.grade_gray_button:
                colorButtons.get(selectedButtonOrder).setLayoutParams(paramUnselectedButton);
                selectedButtonOrder = 0;
                selectedColor = R.color.grade_gray;
                colorizeAlertButtons(getResources().getColor(R.color.grade_gray));
                view.setLayoutParams(paramsSelectedButton);
                break;
            case R.id.deep_orange_button:
                colorButtons.get(selectedButtonOrder).setLayoutParams(paramUnselectedButton);
                selectedButtonOrder = 1;
                selectedColor = R.color.deep_orange;
                colorizeAlertButtons(getResources().getColor(R.color.deep_orange));
                view.setLayoutParams(paramsSelectedButton);
                break;
            case R.id.jade_button:
                colorButtons.get(selectedButtonOrder).setLayoutParams(paramUnselectedButton);
                selectedButtonOrder = 2;
                selectedColor = R.color.jade;
                colorizeAlertButtons(getResources().getColor(R.color.jade));
                view.setLayoutParams(paramsSelectedButton);
                break;
            case R.id.evening_sunshine_button:
                colorButtons.get(selectedButtonOrder).setLayoutParams(paramUnselectedButton);
                selectedButtonOrder = 3;
                selectedColor = R.color.evening_sunshine;
                colorizeAlertButtons(getResources().getColor(R.color.evening_sunshine));
                view.setLayoutParams(paramsSelectedButton);
                break;
            case R.id.pinky_pink_button:
                colorButtons.get(selectedButtonOrder).setLayoutParams(paramUnselectedButton);
                selectedButtonOrder = 4;
                selectedColor = R.color.pinky_pink;
                colorizeAlertButtons(getResources().getColor(R.color.pinky_pink));
                view.setLayoutParams(paramsSelectedButton);
                break;

        }
    }


    private float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    private void colorizeAlertButtons(@ColorInt int color){
        mPositiveButton.setTextColor(color);
        mNegativeButton.setTextColor(color);

        ColorStateList colorStateList = ColorStateList.valueOf(color);
        mListNameEdittext.setBackgroundTintList(colorStateList);
    }

    private void initializeButtons(View view){
        colorButtons = new ArrayList<>();

        colorButtons.add((Button) view.findViewById(R.id.grade_gray_button));
        colorButtons.add((Button) view.findViewById(R.id.deep_orange_button));
        colorButtons.add((Button) view.findViewById(R.id.jade_button));
        colorButtons.add((Button) view.findViewById(R.id.evening_sunshine_button));
        colorButtons.add((Button) view.findViewById(R.id.pinky_pink_button));

        for (Button btn : colorButtons){
            btn.setOnClickListener(this);
        }
    }

}
