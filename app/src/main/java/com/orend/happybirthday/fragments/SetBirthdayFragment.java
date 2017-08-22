package com.orend.happybirthday.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.orend.happybirthday.R;
import com.orend.happybirthday.model.Child;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Activities that contain this fragment must implement the
 * {@link SetBirthdayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SetBirthdayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetBirthdayFragment extends Fragment {

    /*********************/
    //     CONST VARS
    /*********************/
    private static final String DATE_FORMAT = "dd-MM-yyyy";
    private static final String BABY_NAME = "baby_name";
    private static final String BABY_DATE = "baby_date";
    private static final String BABY_IMAGE_URI = "baby_image";


    /*********************/
    // PRIVATE VARS
    /********************/
    private OnFragmentInteractionListener mListener;
    private EditText mEditDate;
    private EditText mEditName;
    private Calendar mCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);
    private ImageView mImageView;
    private Uri mImageUri;
    private Button mBtnShowBirthdayScreen;
    private SharedPreferences mSharedPref;

    public SetBirthdayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment MainFragment.
     */
    public static SetBirthdayFragment newInstance() {
        return new SetBirthdayFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mEditDate = (EditText) rootView.findViewById(R.id.editDate);
        mEditName = (EditText) rootView.findViewById(R.id.kid_name);

        //listener on change text if bd and name are full the bd field should be enable
        mEditName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mBtnShowBirthdayScreen.setEnabled(!editable.toString().isEmpty() && !mEditDate.getText().toString().isEmpty());
                if(!editable.toString().isEmpty()) {
                    mSharedPref.edit().putString(BABY_NAME, editable.toString()).apply();
                }
            }
        });

        // set calendar date and update mEditDate
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if(validateBirthday()) {
                    updateDate();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.err_birthday_date), Toast.LENGTH_LONG).show();
                }
            }

        };

        // onclick - popup datepicker
        mEditDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), mDateSetListener, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImageView = (ImageView)view.findViewById(R.id.img_main);
        mBtnShowBirthdayScreen = (Button)view.findViewById(R.id.btn_show_bd_scr);
        mBtnShowBirthdayScreen.setEnabled(false);
        mBtnShowBirthdayScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Child child = new Child.BuilderChild(mEditName.getText().toString(),
                        mEditDate.getText().toString(), mImageUri).build();
                if (mListener != null) {
                    mListener.onFragmentInteraction(child);
                }
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(SetBirthdayFragment.this)
                        .returnAfterFirst(true) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                        .folderMode(true) // set folder mode (false by default)
                        .single()
                        .showCamera(true)
                        .folderTitle(getString(R.string.folder_title)) // folder selection title
                        .imageTitle(getString(R.string.image_title))
                        .start(0); // image selection title
            }
        });

        //init with persistent data
        mSharedPref = getActivity().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if(!mSharedPref.getAll().isEmpty()) {
            mEditName.setText(mSharedPref.getString(BABY_NAME, ""));
            long date = mSharedPref.getLong(BABY_DATE, -1);
            if(date != -1) {
                mCalendar.setTimeInMillis(date);
                mEditDate.setText(mSimpleDateFormat.format(mCalendar.getTime()));
            }
            String imgPath = mSharedPref.getString(BABY_IMAGE_URI, null);
            if(imgPath != null) {
                mImageUri = Uri.parse(imgPath);
                mImageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Image> images = ImagePicker.getImages(data);
        if (images != null && !images.isEmpty()) {
            String imgPath = images.get(0).getPath();
            mImageView.setImageBitmap(BitmapFactory.decodeFile(images.get(0).getPath()));
            mImageUri = Uri.parse(images.get(0).getPath());
            mSharedPref.edit().putString(BABY_IMAGE_URI, imgPath).apply();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /***************************/
    //     PRIVATE METHODS
    /***************************/

    private boolean validateBirthday() {
        Calendar tmpCalendar = Calendar.getInstance();
        tmpCalendar.setTimeInMillis(System.currentTimeMillis());
        return tmpCalendar.after(mCalendar);
    }

    private void updateDate() {
        Date date = mCalendar.getTime();
        mEditDate.setText(mSimpleDateFormat.format(date));
        mBtnShowBirthdayScreen.setEnabled(!mEditName.getText().toString().isEmpty());
        mSharedPref.edit().putLong(BABY_DATE, date.getTime()).apply();
    }

    /***************************/
    //      Interfaces
    /***************************/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Child child);
    }
}
