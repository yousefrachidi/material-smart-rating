package com.vimalcvs.materialrating;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vimalcvs.materialrating.adapter.FeedbackAdapter;
import com.vimalcvs.materialrating.adapter.ImageAdapter;
import com.vimalcvs.materialrating.data.FeedBack;
import com.vimalcvs.materialrating.utilis.RealPathUtil;
import com.vimalcvs.materialrating.utilis.permissions.PermissionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by VimalCvs on 02/11/2020.
 */

public class MaterialFeedback extends DialogFragment {

    private View theDialogView;
    private TextInputLayout text_input;
    private static final String RATING_KEY = "rating";
    public float rating;
    private String deviceInfo;
    public String email;

    String absolutePath = "";

    RecyclerView recyclerView, recyclerViewImages;
    FeedbackAdapter adapter;
    ImageAdapter imageAdapter;

    public MaterialFeedback(String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireActivity());
        theDialogView = onCreateView(LayoutInflater.from(requireActivity()), null, savedInstanceState);
        builder.setCancelable(false);
        builder.setView(theDialogView);

        recyclerView = theDialogView.findViewById(R.id.recyclerAnonymous);
        recyclerViewImages = theDialogView.findViewById(R.id.recyclerImg);

        initRecyclerview();
        imageAdapter = new ImageAdapter(requireActivity(), new ImageAdapter.ItemListener() {
            @Override
            public void onItemDeleteClick(File file) {

            }

            @Override
            public void onItemSelectedClick() {
                pickPicture();
            }
        });
        recyclerViewImages.setHasFixedSize(false);
        recyclerViewImages.setAdapter(imageAdapter);

        return builder.create();
    }

    private void pickPicture() {
        PermissionManager permissionManager = new PermissionManager(requireActivity());
        if (permissionManager.checkStoragePermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (permissionManager.checkManagerStorage(requireActivity())) {
                    onStartUploading();
                } else {
                    Toast.makeText(requireActivity(), "permission_required", Toast.LENGTH_SHORT).show();
                }
            } else {
                onStartUploading();
            }
        } else {
            permissionManager.requestStoragePermissions((AppCompatActivity) requireActivity());
            // actions.postValue(Collections.singletonList(F0303NewDoc.ACTION_REQUEST_STORAGE_PERMISSIONS));
        }

    }

    private void initRecyclerview() {

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(requireActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FeedbackAdapter(requireActivity(), feedData(), (feedback) -> {
            Toast.makeText(requireActivity(), feedback.getBody(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);


    }

    private List<FeedBack> feedData() {
        return Arrays.asList(
                new FeedBack("I can't hidden some media"),
                new FeedBack("Bugs"),
                new FeedBack("Too many ads"),
                new FeedBack("Suggestions"),
                new FeedBack("Others")
        );
    }

    // Create requireActivity() as a variable in your Fragment class
    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intentData = result.getData();
                if (intentData == null)
                    return;


                File fi = new File(intentData.getData().getPath());

                // Do your code from onActivityResult
                feedDoc(requireActivity(), intentData.getData());

            });

    private void onStartUploading() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        //galleryIntent.setType("application/pdf");
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        //String[] mimeType = {"application/pdf","image/*"};
        //galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES , mimeType );
        galleryIntent.createChooser(galleryIntent, "Select Image");
        mLauncher.launch(galleryIntent);

    }

    private void feedDoc(Context context, Uri uri) {
        String path = RealPathUtil.getRealPath(context, uri);

        if (path != null) {
            File uploadFile = new File(path);
            String nameFileLive = (RealPathUtil.getFilePath(context, uri));
            absolutePath = (uploadFile.getAbsolutePath());

            Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
            //  btnImg.setImageBitmap(bitmap);
            imageAdapter.addImage(new File(absolutePath));

        } else {
            Toast.makeText(context, "file not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View getView() {
        return theDialogView;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lib_material_fragment_feedback, container);

        if (savedInstanceState != null) {
            rating = savedInstanceState.getFloat(RATING_KEY);
        }

        Button bt_maybeLater = view.findViewById(R.id.bt_maybeLater);
        bt_maybeLater.setOnClickListener(cancelButton -> dismiss());


        deviceInfo = "Device Info:\n";
        deviceInfo += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
        deviceInfo += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
        deviceInfo += "\n Device: " + android.os.Build.DEVICE;
        deviceInfo += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";


        try {
            PackageInfo pInfo = requireActivity().getPackageManager().getPackageInfo(requireActivity().getPackageName(), 0);
            deviceInfo += String.format("\n Version app: %s(%s)", pInfo.versionCode, pInfo.versionName);
            // Use the version and versionCode as needed
        } catch (Exception e) {
        }


        TextInputEditText etFeedback = view.findViewById(R.id.et_feedback);
        Editable feedback = etFeedback.getText();

        Button bt_feedbackSend = view.findViewById(R.id.bt_feedbackSend);
        bt_feedbackSend.setOnClickListener(send -> {
            if (send.getId() == R.id.bt_feedbackSend && (feedback != null ? feedback.length() : 0) > 0) {
                StringBuilder body = new StringBuilder();
                body.append( feedback).append(";\n");
                for (FeedBack item : adapter.getItems()) {
                    if (item.isCheck()) {
                        body.append(item.getBody()).append(";\n");
                    }
                }

                List<Uri> uris = new ArrayList<>();
                for (File file : imageAdapter.getItems()) {
                    if (file.exists()) {
                        uris.add(Uri.fromFile(file));
                    }
                }


                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " App Feedback...!");
                intent.putExtra(Intent.EXTRA_TEXT, "Stars: " + rating + "\n\nFeedback: " + body + "\n\n" + deviceInfo);
                intent.putExtra(Intent.EXTRA_STREAM, uris.toArray() );

                if (requireActivity().getPackageManager().resolveActivity(intent, 0) != null) {
                }
                startActivity(intent);


            } else if (send.getId() == R.id.bt_feedbackSend) {
                text_input = view.findViewById(R.id.text_input);
                text_input.setError("Please enter at least 10 characters.");
                return;
            }
            dismiss();
        });

        return view;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
