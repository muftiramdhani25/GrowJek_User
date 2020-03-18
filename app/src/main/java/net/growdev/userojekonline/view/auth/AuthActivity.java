package net.growdev.userojekonline.view.auth;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import net.growdev.userojekonline.MainActivity;
import net.growdev.userojekonline.R;
import net.growdev.userojekonline.helper.HeroHelper;
import net.growdev.userojekonline.helper.SessionManager;
import net.growdev.userojekonline.model.modelauth.ResponseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity implements AuthContract.View {

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;

    private AuthPresenter presenter;
    private ProgressDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        requestPermission();

        presenter = new AuthPresenter(this);
        loadingDialog = new ProgressDialog(this);
    }

    private void requestPermission(){
        String [] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION};
        Permissions.check(this, permissions,null, null, new PermissionHandler() {
            @Override
            public void onGranted() {
                Toast.makeText(AuthActivity.this, "Permisson UUID telah aktif", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                showDialogLogin();
                break;
            case R.id.btn_register:
                showDialogRegister();
                break;
        }
    }

    private void showDialogRegister() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_form_register);
        dialog.setCancelable(true);

        EditText edtName = dialog.findViewById(R.id.edt_reg_name);
        EditText edtEmail = dialog.findViewById(R.id.edt_reg_email);
        EditText edtPhone = dialog.findViewById(R.id.edt_reg_phone);
        EditText edtPassword = dialog.findViewById(R.id.edt_reg_password);
        Button btnDialogRegister = dialog.findViewById(R.id.btn_dialog_register);

        btnDialogRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = edtName.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String phone = edtPhone.getText().toString();

                if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ||TextUtils.isEmpty(password)){
                    Toast.makeText(AuthActivity.this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    presenter.actionRegister(nama, email, password, phone);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void showDialogLogin(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_form_login);
        dialog.setCancelable(true);

        EditText edtEmail = dialog.findViewById(R.id.edt_email);
        EditText edtPassword = dialog.findViewById(R.id.edt_password);
        Button btnDialogLogin = dialog.findViewById(R.id.btn_dialog_login);

        btnDialogLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(AuthActivity.this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    String uuid = HeroHelper.getDeviceUUID(AuthActivity.this);
                    presenter.actionLogin(uuid, email, password);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        loadingDialog.setMessage("Loading");
        loadingDialog.show();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }

    @Override
    public void showError(String localizedMessage) {
        Toast.makeText(this, localizedMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pindahActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void dataUser(ResponseAuth dataUser) {
        // masukan data ke session
        SessionManager sesi = new SessionManager(this);
        sesi.createLoginSession(dataUser.getToken());
        sesi.setIduser(dataUser.getIdUser());
    }

    // default MVP
    @Override
    public void onAttachView() {
        presenter.onAttach(this);
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onAttachView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDetachView();
    }
}
