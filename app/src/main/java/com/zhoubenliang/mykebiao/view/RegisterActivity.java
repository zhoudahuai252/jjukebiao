package com.zhoubenliang.mykebiao.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding.view.RxView;
import com.zhoubenliang.mykebiao.R;
import com.zhoubenliang.mykebiao.contonle.CommonUtils;
import com.zhoubenliang.mykebiao.mode.MyUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RegisterActivity extends AppCompatActivity {

    private static final Integer FACE_NO = 2;
    private static final Integer FACE_ERROR = 3;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.bt_register_jiance)
    Button btRegisterJiance;
    @Bind(R.id.pwd)
    EditText pwd;
    @Bind(R.id.pwdqr)
    EditText pwdqr;
    @Bind(R.id.bitmapaddress)
    EditText bitmapaddress;
    @Bind(R.id.photobtn)
    Button photobtn;
    @Bind(R.id.btn_img)
    Button btnImg;
    @Bind(R.id.btn_register)
    Button btnRegister;
    private String ImageName = "";// 图片名称
    private String ImageUrl = "";// 图片地址
    FileOutputStream OutPut = null;// 输出流
    private String TAG = "RegisterActivity";
    private String face_id;
    private final static Integer FACE_EXIST = 1;
    private boolean isFaceAvaiable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, CommonUtils.BMOB_APPID);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        initToolbar();
//        intiFaceset();
        initBtn();

    }

    private void intiFaceset() {
        new Thread() {
            @Override
            public void run() {
                HttpRequests requests = new HttpRequests(CommonUtils.FACE_API_KEY, CommonUtils.FACE_API_SECRET);
                try {
                    PostParameters params = new PostParameters();
                    params.setFacesetName(CommonUtils.FACE_SET_NAME);
                    JSONObject object = requests.facesetCreate(params);
                    Log.d("RegisterActivity", "object:" + object);
                } catch (FaceppParseException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initBtn() {
        RxView.clicks(btnImg).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent it = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                it.setType("image/*");
                startActivityForResult(it, 2);
            }
        });
        RxView.clicks(btnRegister)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.d("RegisterActivity", Thread.currentThread().getName());
                        String userString = username.getText().toString().trim();
                        String pwdString = username.getText().toString().trim();
                        if (!TextUtils.isEmpty(userString)) {
                            if (isUsername(userString)) {
                                if (!TextUtils.isEmpty(pwdString)) {
                                    if (isPwd(pwdString)) {
                                        if (isFaceAvaiable) {
                                            toRegister(userString, pwdString)
                                                    .subscribeOn(Schedulers.newThread())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Subscriber<Boolean>() {
                                                        @Override
                                                        public void onCompleted() {
                                                            //清空
                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {

                                                        }

                                                        @Override
                                                        public void onNext(Boolean aBoolean) {
                                                            if (aBoolean) {
                                                                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                                                //跳转登陆界面
                                                            } else {
                                                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "没有人脸", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "密码必须是6-16位字母数字", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "密码不能空", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(RegisterActivity.this, "用户名必须5-16位字母开头", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(RegisterActivity.this, "用户名为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        RxView.clicks(photobtn).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                btnPhotoClick();
            }
        });
        RxView.clicks(btRegisterJiance)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.newThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        String userString = username.getText().toString().trim();
                        if (!TextUtils.isEmpty(userString)) {
                            checkUser(userString);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "用户名为空", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });


    }

    @NonNull
    private Observable<Boolean> toRegister(final String userString, final String pwdString) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                Log.d("Thread", Thread.currentThread().getName());
                MyUser user = new MyUser();
                user.setUsername(userString);
                user.setPassword(pwdString);
                user.setFace_id(face_id);
                Log.d("RegisterActivity", "------------>" + face_id);
                user.signUp(RegisterActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        subscriber.onNext(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //还要把faceid加入集合
                                Log.d("Thread", Thread.currentThread().getName());
                                HttpRequests requests = new HttpRequests(CommonUtils.FACE_API_KEY, CommonUtils.FACE_API_SECRET);
                                try {
                                    PostParameters pp = new PostParameters();
                                    pp.setFacesetName(CommonUtils.FACE_SET_NAME);
                                    pp.setFaceId(face_id);
                                    JSONObject jsonObject = requests.facesetAddFace(pp);
                                    Log.d("RegisterActivity", "--->" + jsonObject.toString());
                                } catch (FaceppParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("RegisterActivity", "错误日志" + s);
                        subscriber.onNext(false);
                        subscriber.onCompleted();
                    }
                });
            }
        });
    }

    ;

    /**
     * \
     * 检测用户名是否可用
     *
     * @param userString
     */
    private void checkUser(final String userString) {
        if (!isUsername(userString)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, "用户名必须是5-16位字母开头", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("username", userString);
        query.findObjects(this, new FindListener<BmobUser>() {
            @Override
            public void onSuccess(final List<BmobUser> object) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (object.size() > 0) {
                            Toast.makeText(RegisterActivity.this, "用户名已经存在", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, "用户名可用", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onError(int code, String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void btnPhotoClick() {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Log.d("RegisterActivity", "SD卡不存在");
            Toast.makeText(RegisterActivity.this, "SD卡不存在",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (checkpwd()) {
            Toast.makeText(RegisterActivity.this, "两次密码不一致请重新输入",
                    Toast.LENGTH_LONG).show();
            return;
        } else {
            ImageName = new DateFormat().format("yyyyMMdd_hhmmss",
                    Calendar.getInstance(Locale.CHINA)) + ".jpg";
            Log.i(TAG, "图片名称" + ImageName);
            ImageUrl = Environment.getExternalStorageDirectory()
                    .toString()
                    + File.separator
                    + "JJukebiao"
                    + File.separator + ImageName;
            Log.i(TAG, "图片地址ImageURL为" + ImageUrl);
            File file = new File(Environment
                    .getExternalStorageDirectory().toString()
                    + File.separator + "CloudAlbum" + File.separator);
            if (!file.exists()) {
                file.mkdirs();
                Log.i(TAG, "文件夹已经创建");
            }
            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            File f = new File(ImageUrl);
            intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && requestCode == RESULT_OK) {
            Log.i(TAG, "照相成功！");
            todoPic();
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(selectedImage,
                    filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            ImageUrl = picturePath;
            todoPic();
        }
    }

    private void todoPic() {
        bitmapaddress.setText(ImageUrl);
        final ProgressDialog dialog = CommonUtils.getProcessDialog(this, "正在检测");
        dialog.show();
        getData().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RegisterActivity", "e:" + e);
                        Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(Object integer) {
                        Bundle bundle = (Bundle) integer;
                        int result = bundle.getInt("num");
                        switch (result) {
                            case 1:
                                //重复人脸
                                Toast.makeText(RegisterActivity.this, "人脸已经注册过了", Toast.LENGTH_SHORT).show();
                                isFaceAvaiable = false;
                                break;
                            case 2:
                                //有人脸,并且不重复
                                String faceid = bundle.getString("faceid");
                                face_id = faceid;
                                Log.d("RegisterActivity", "---A---->" + face_id);
                                Toast.makeText(RegisterActivity.this, "人脸可用", Toast.LENGTH_SHORT).show();
                                isFaceAvaiable = true;
                                break;
                            case 3:
                                //没有检测到人脸
                                Toast.makeText(RegisterActivity.this, "没有检测到人脸,请重新拍照", Toast.LENGTH_SHORT).show();
                                isFaceAvaiable = false;
                                break;
                        }
                    }
                });
    }

    @NonNull
    private Observable<Object> getData() {
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                HttpRequests httpRequests = new HttpRequests(CommonUtils.FACE_API_KEY, CommonUtils.FACE_API_SECRET);
                try {
                    JSONObject result = httpRequests.detectionDetect(new PostParameters()
                            .setImg(CommonUtils.ImageProcess(ImageUrl)));
                    JSONArray ffid = result.getJSONArray("face");
                    if (ffid.length() > 0) {
                        //1.如果人脸存在,在从FACE)SET里检测是否已经存在该人脸
                        String faceid = result.getJSONArray("face").getJSONObject(0).getString("face_id");
                        Log.d("RegisterActivity", "--->" + faceid);
                        //在search之前需要训练
                        httpRequests.trainSearch(new PostParameters().setFacesetName(CommonUtils.FACE_SET_NAME));
                        PostParameters parameters = new PostParameters();
                        parameters.setFacesetName(CommonUtils.FACE_SET_NAME);
                        parameters.setKeyFaceId(faceid);
                        JSONObject js = httpRequests
                                .recognitionSearch(parameters);
                        Log.d("RegisterActivity", js.toString());
                        JSONObject jsObject;
                        JSONArray candidate = js.getJSONArray("candidate");
                        if (candidate.length() < 1) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("num", FACE_NO);
                            bundle.putString("faceid", faceid);
                            subscriber.onNext(bundle);
                            subscriber.onCompleted();
                            return;
                        }
                        //遍历返回的结果
                        for (int m = 0; m < candidate.length(); m++) {
                            jsObject = candidate.optJSONObject(m);
                            if (jsObject.getDouble("similarity") > 80.00) {
                                //说明已经存在该人脸
                                Log.i(TAG, "相似度为" + jsObject.getDouble("similarity"));
                                Bundle bundle = new Bundle();
                                bundle.putInt("num", FACE_EXIST);
                                subscriber.onNext(bundle);
                                subscriber.onCompleted();
                                break;
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putInt("num", FACE_NO);
                                bundle.putString("faceid", faceid);

                                //没有重复人脸
                                subscriber.onNext(bundle);
                                subscriber.onCompleted();
                                break;
                            }

                        }
                    } else {
                        //没有检测到人脸
                        Bundle bundle = new Bundle();
                        bundle.putInt("num", FACE_ERROR);
                        subscriber.onNext(bundle);
                        subscriber.onCompleted();
                    }
                } catch (FaceppParseException e) {
                    subscriber.onError(e);
                    e.printStackTrace();
                } catch (JSONException e) {
                    subscriber.onError(e);
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 检查2个EditText是否一致
     *
     * @return
     */
    private Boolean checkpwd() {

        String s1, s2;
        s1 = pwd.getText().toString();
        s2 = pwdqr.getText().toString();
        if (!(s1).equals(s2)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPwd(String pwd) {
        String str = "^[a-zA-Z]\\w{5,17}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(pwd);
        return m.matches();
    }

    /**
     * 判断用户名是否合法
     *
     * @param name name用户名
     * @return
     */
    private boolean isUsername(String name) {
        String str = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.navigation_back_selector);
        toolbar.setTitle("用户注册");
        RxToolbar.navigationClicks(toolbar).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onToolbarNavigationClicked();
            }
        });
    }

    // 浏览点击
    private void onToolbarNavigationClicked() {
        finish();
    }

}