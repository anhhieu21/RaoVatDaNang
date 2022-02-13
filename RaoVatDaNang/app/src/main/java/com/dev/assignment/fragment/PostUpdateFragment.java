package com.dev.assignment.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.assignment.Constants;
import com.dev.assignment.R;
import com.dev.assignment.RealPathUtil;
import com.dev.assignment.RetrofitClient;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostUpdateFragment extends Fragment {
    private Uri mUri;
    private static final int MY_REQUEST_CODE = 10;
    String productId = Constants.PR_ID_YOUR;
    TextView tvId, tvType1, tvType2;
    //    EditText edtName, edtPrice, edtDetail;
    @NotEmpty
    @Length(min = 5)
    EditText edtName;

    @Min(1000)
    EditText edtPrice;

    @NotEmpty
    @Length(min = 10)
    EditText edtDetail;
    Validator validator;
    ImageView imvUpload, imvExit;
    Button btnSelectImg, btnUpdate;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data == null) {
                    return;
                }
                Uri uri = data.getData();
                mUri = uri;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                    imvUpload.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public PostUpdateFragment() {
        // Required empty public constructor
    }

    public static PostUpdateFragment newInstance() {
        PostUpdateFragment fragment = new PostUpdateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_update, container, false);
        initView(view);
//        validator = new Validator(this);
//        validator.setValidationListener(this);
        Log.e("345",productId+"");
        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();
            }
        });

        imvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Bạn có muốn rời khỏi sửa tin?");
                builder.setCancelable(false);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.flyMain, new HomeFragment())
                                .addToBackStack(null).commit();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        btnUpdate.setOnClickListener(view1 -> {

            if (mUri != null) {
                postUpdate();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            tvType1.setText(bundle.getString("type1"));
            tvType2.setText(bundle.getString("type2"));
            edtName.setText(bundle.getString("name"));
            edtPrice.setText(bundle.getString("price"));
            edtDetail.setText(bundle.getString("detail"));
        }

        return view;

    }

    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }


    private void initView(View view) {
        tvId = view.findViewById(R.id.tvId);
        tvType1 = view.findViewById(R.id.tvType1);
        tvType2 = view.findViewById(R.id.tvType2);
        edtName = view.findViewById(R.id.edtNameProduct);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtDetail = view.findViewById(R.id.edtDetail);
        imvUpload = view.findViewById(R.id.imageUpload);
        imvExit = view.findViewById(R.id.imvExit);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnSelectImg = view.findViewById(R.id.btnSelectImg);

    }

    public void postUpdate() {
        String type1 = tvType1.getText().toString();
        String type2 = tvType2.getText().toString();
        String name = edtName.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String detail = edtDetail.getText().toString().trim();
        if (name.isEmpty()) {
            edtName.setError("Vui lòng nhập đầy đủ");
            edtName.requestFocus();
            return;
        }
        if (price.isEmpty()) {
            edtPrice.setError("Vui lòng nhập đầy đủ");
            edtPrice.requestFocus();
            return;
        }
        if (detail.isEmpty()) {
            edtDetail.setError("Vui lòng nhập đầy đủ");
            edtDetail.requestFocus();
            return;
        }

        RequestBody requestBodyproductId = RequestBody.create(MediaType.parse("multipart/form-data"), productId);
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody requestBodyPrice = RequestBody.create(MediaType.parse("multipart/form-data"), price);
        RequestBody requestBodyType1 = RequestBody.create(MediaType.parse("multipart/form-data"), type1);
        RequestBody requestBodyType2 = RequestBody.create(MediaType.parse("multipart/form-data"), type2);
        RequestBody requestBodyDetail = RequestBody.create(MediaType.parse("multipart/form-data"), detail);
        String strRealPath = RealPathUtil.getRealPath(getContext(), mUri);
        Log.e("168", strRealPath + "");
        File file = new File(strRealPath);
        RequestBody requestBodyImage_1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part mMultipartBodyImage_1 = MultipartBody.Part.createFormData(Constants.KEY_IMAGE, file.getName(), requestBodyImage_1);

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi()
                .update(requestBodyproductId, requestBodyName, requestBodyPrice, requestBodyType1, requestBodyType2, requestBodyDetail, mMultipartBodyImage_1);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
                try {
                    Toast.makeText(getContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Post Failed!", Toast.LENGTH_SHORT).show();
                }
                getActivity().onBackPressed();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Call api fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

}