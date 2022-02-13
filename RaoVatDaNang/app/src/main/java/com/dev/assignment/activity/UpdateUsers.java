package com.dev.assignment.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dev.assignment.Constants;
import com.dev.assignment.R;
import com.dev.assignment.apiRegister.RegisterEmail;
import com.dev.assignment.apiRegister.RegisterReponse;
import com.dev.assignment.apiSetupUser.ApiClienUsers;
import com.dev.assignment.apiSetupUser.ApiSevice;
import com.dev.assignment.apiSetupUser.RealPathUtil;
import com.dev.assignment.itemFB.ApiSeviceJoinFB;
import com.dev.assignment.itemFB.BackJson;
import com.dev.assignment.itemFB.FBemail;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUsers extends AppCompatActivity {
    public static final String TAG = UpdateUsers.class.getName();
    private static final int MY_REQUEST_CODE = 10;
    public EditText cnName, cnPhone;
    public Spinner cnAddress;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrAddress;
    public ImageView cnImage, setupUser;
    public TextView cnEmail, tvTitleName;
    String email1, avt;
    private Uri mUri;
    private ProgressDialog mProgressDialog;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            cnImage.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_users);
        mapView();
        arrAddress = new ArrayList<>();
        arrAddress.addAll(Arrays.asList("Quận Hải Châu", "Quận Liên Chiểu", "Quận Cẩm Lệ",
                "Quận Thanh Khê", "Quận Ngũ Hành Sơn", "Quận Sơn Trà", "Huyện Hòa Vang", "Huyện Hoàng Sa"));
        adapter = new ArrayAdapter<String>(UpdateUsers.this, android.R.layout.simple_spinner_item, arrAddress);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cnAddress.setAdapter(adapter);
        onCLick();
        if (checkLoginNumberupdate() > 0) {
            cnEmail.setText(email1); //lam get api o doan ni neok lam di t thoat day
            FBemail fBemail = new FBemail(email1);
            ApiSeviceJoinFB.apiSeviceJoinFb.getDataFB(fBemail).enqueue(new Callback<BackJson>() {
                @Override
                public void onResponse(Call<BackJson> call, Response<BackJson> response) {
                    BackJson backJson = response.body();
                    cnName.setText(backJson.getUser().getName());
                    cnPhone.setText(backJson.getUser().getPhone());
                    cnAddress.setSelection(arrAddress.indexOf(backJson.getUser().getAddress()));
                    avt = backJson.getUser().getAvatar();
                    Glide.with(UpdateUsers.this).load(avt).into(cnImage);
                }

                @Override
                public void onFailure(Call<BackJson> call, Throwable t) {
                }
            });
        }
    }

    private void mapView() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Vui lòng chờ...");
        cnEmail = (TextView) findViewById(R.id.CN_email);
        cnEmail.setText(Constants.EMAIL_YOUR);
        cnName = (EditText) findViewById(R.id.CN_name);
        cnAddress = (Spinner) findViewById(R.id.Sp_addressUpdate);
        cnPhone = (EditText) findViewById(R.id.CN_phone);
        cnImage = (ImageView) findViewById(R.id.CN_images);
        setupUser = (ImageView) findViewById(R.id.CN_Users);
        cnName.setText(Constants.NAME_YOUR);
        Picasso.get().load(Constants.URL_AVATAR_YOUR).into(cnImage);
    }

    private void onCLick() {
        setupUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strname = cnName.getText().toString();
//                String stremail = edEmail.getText().toString();
                String strphone = cnPhone.getText().toString();
                if (TextUtils.isEmpty(strname)
                        && TextUtils.isEmpty(strphone)) {
                    cnName.setError("Vui lòng nhập Name");
//                   edEmail.setError("Vui lòng nhập Email");
                    cnPhone.setError("Vui lòng nhập Phone");
//                   spAddress.setError("Vui lòng nhập Address");

                    Toast.makeText(UpdateUsers.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                } else if (cnName.getText().toString().length() < 5) {
                    cnName.setError("Name trên 5 ký tự");
                    Toast.makeText(UpdateUsers.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                }
                //k chưa ký tự dac biet name
                else if (!cnName.getText().toString().matches("[a-z,A-Z,0-9]*")) {
                    cnName.setError("Name không chứa kí tự đặc biệt");
                    Toast.makeText(UpdateUsers.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                } else if (!isValidPhoneNumber(strphone)) {
                    cnPhone.setError("Vui lòng nhập đúng SDT của bạn");
                    Toast.makeText(UpdateUsers.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                } else {
                    callApiUpdateAcount();
                }

            }
        });
        //roi ak chay thu co chi noi t nghe ok
        cnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequetImage();
            }
        });
    }

    public void onClickRequetImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] peremision = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(peremision, MY_REQUEST_CODE);
        }
    }

    public final static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || target.length() < 6 || target.length() > 13) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
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
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Mở ảnh"));
    }

    private void callApiUpdateAcount() {
        mProgressDialog.show();

        String email = Constants.EMAIL_YOUR;
        String name = cnName.getText().toString().trim();
        String phone = cnPhone.getText().toString().trim();
        String address = cnAddress.getSelectedItem().toString();

        RequestBody requestBodyEmail = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody requestBodyPhone = RequestBody.create(MediaType.parse("multipart/form-data"), phone);
        RequestBody requestBodyAddress = RequestBody.create(MediaType.parse("multipart/form-data"), address);
        if (mUri == null) {
            RequestBody requestBodyAvatar = null;
            MultipartBody.Part multipartBodyAvatar = null;
            ApiSevice.apiSevice.uploarUserApi(requestBodyEmail, requestBodyName, requestBodyPhone, requestBodyAddress, multipartBodyAvatar).enqueue(new Callback<RegisterReponse>() {
                @Override
                public void onResponse(Call<RegisterReponse> call, Response<RegisterReponse> response) {
                    mProgressDialog.dismiss();
                    Toast.makeText(UpdateUsers.this, "thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<RegisterReponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(UpdateUsers.this, "Call thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            String strRealPath = RealPathUtil.getRealPath(this, mUri);
            Log.e("DoAn", strRealPath);
            File file = new File(strRealPath);
            RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part multipartBodyAvatar = MultipartBody.Part.createFormData(ApiClienUsers.KEY_AVATAR, file.getName(), requestBodyAvatar);
            ApiSevice.apiSevice.uploarUserApi(requestBodyEmail, requestBodyName, requestBodyPhone, requestBodyAddress, multipartBodyAvatar).enqueue(new Callback<RegisterReponse>() {
                @Override
                public void onResponse(Call<RegisterReponse> call, Response<RegisterReponse> response) {
                    mProgressDialog.dismiss();
                    Toast.makeText(UpdateUsers.this, "thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<RegisterReponse> call, Throwable t) {
                    mProgressDialog.dismiss();
                    Toast.makeText(UpdateUsers.this, "Call thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public int checkLoginNumberupdate() {
        SharedPreferences sharedPreferences = getSharedPreferences("USERFBFILE", MODE_PRIVATE);
        String ckmail = sharedPreferences.getString("EMAIL", "");
        if (ckmail != "") {
            email1 = Constants.EMAIL_YOUR;
            return 1;
        }
        return -1;
    }
}