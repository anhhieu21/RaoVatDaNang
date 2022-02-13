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
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dev.assignment.Api;
import com.dev.assignment.Constants;
import com.dev.assignment.R;
import com.dev.assignment.RealPathUtil;
import com.dev.assignment.activity.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PostFragment extends Fragment {
    private Uri mUri;
    private static final int MY_REQUEST_CODE = 10;
    ImageView imageUpload, imvExit;

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

    Button btnSelectImg, btnPost;
    Spinner spnType1, spnType2;
    String selectedType1;
    ArrayAdapter<CharSequence> adapterType1, adapterType2;
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
                    imageUpload.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    });


    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance(String param1, String param2) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        BottomNavigationView navBar = getActivity().findViewById(R.id.nvgMain);
        navBar.setVisibility(View.GONE);

        initView(view);
//        validator = new Validator(this);
//        validator.setValidationListener(this);


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
                builder.setTitle("Bạn có muốn rời khỏi đăng tin?");
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

        btnPost.setOnClickListener(view1 -> {
            if (mUri != null) {
                postPr();
            }
        });

        return view;
    }

    // Select img from gallery
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
        imageUpload = view.findViewById(R.id.imageUpload);
        imvExit = view.findViewById(R.id.imvExit);
        edtName = view.findViewById(R.id.edtNameProduct);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtDetail = view.findViewById(R.id.edtDetail);
        btnSelectImg = view.findViewById(R.id.btnSelectImg);
        btnPost = view.findViewById(R.id.btnPost);
        spnType1 = view.findViewById(R.id.spnType1);
        spnType2 = view.findViewById(R.id.spnType2);

        adapterType1 = ArrayAdapter.createFromResource(this.getActivity(), R.array.arrayType1, R.layout.spinner_layout);
        adapterType1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnType1.setAdapter(adapterType1);
        spnType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType1 = spnType1.getSelectedItem().toString();
                int parentID = parent.getId();
                if (parentID == R.id.spnType1) {
                    switch (selectedType1) {
                        case "Xe cộ":
                            adapterType2 = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.arrayType2_vehicle, R.layout.spinner_layout);
                            break;
                        case "Đồ điện tử":
                            adapterType2 = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.arrayType2_device, R.layout.spinner_layout);
                            break;
                        case "Nội thất":
                            adapterType2 = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.arrayType2_furniture, R.layout.spinner_layout);
                            break;
                        case "Thú cưng":
                            adapterType2 = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.arrayType2_pet, R.layout.spinner_layout);
                            break;
                        case "Văn phòng phẩm":
                            adapterType2 = ArrayAdapter.createFromResource(parent.getContext(),
                                    R.array.arrayType2_stationery, R.layout.spinner_layout);
                            break;
                        default:
                            break;
                    }
                    adapterType2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnType2.setAdapter(adapterType2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void postPr() {
        String idUser = Constants.KEY_IDUSER;
        Log.e("111", idUser + "");
        String status = "đang bán";
        String lockPr = "0";
        String name = edtName.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String detail = edtDetail.getText().toString().trim();
        String type1 = spnType1.getSelectedItem().toString();
        String type2 = spnType2.getSelectedItem().toString();
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

        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody requestBodyPrice = RequestBody.create(MediaType.parse("multipart/form-data"), price);
        RequestBody requestBodyDetail = RequestBody.create(MediaType.parse("multipart/form-data"), detail);
        RequestBody requestBodyStatus = RequestBody.create(MediaType.parse("multipart/form-data"), status);
        RequestBody requestBodyLock = RequestBody.create(MediaType.parse("multipart/form-data"), lockPr);
        RequestBody requestBodyIdUser = RequestBody.create(MediaType.parse("multipart/form-data"), idUser);
        RequestBody requestBodyType1 = RequestBody.create(MediaType.parse("multipart/form-data"), type1);
        RequestBody requestBodyType2 = RequestBody.create(MediaType.parse("multipart/form-data"), type2);
        String strRealPath = RealPathUtil.getRealPath(getContext(), mUri);
        Log.e("167", strRealPath + "");
        File file = new File(strRealPath);
        RequestBody requestBodyImage_1 = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part mMultipartBodyImage_1 = MultipartBody.Part.createFormData(Constants.KEY_IMAGE, file.getName(), requestBodyImage_1);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_POST_RP).addConverterFactory(GsonConverterFactory.create()).build();
        Api reportAPI = retrofit.create(Api.class);
        reportAPI.create(requestBodyName, requestBodyPrice, requestBodyType1, requestBodyType2, requestBodyStatus, requestBodyLock, requestBodyIdUser, requestBodyDetail, mMultipartBodyImage_1).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
                try {
                    Toast.makeText(getContext(), "Post successful!", Toast.LENGTH_SHORT).show();
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