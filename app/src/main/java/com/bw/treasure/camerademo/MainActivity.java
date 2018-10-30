package com.bw.treasure.camerademo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button camera;
    private Button camera1;
    private Button photo;
    private ImageView cameraImage;
    private Button clipping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    //第一步:找到所有的按钮和图片控件 ,按钮设置点击事件
    private void initView() {
        //初始化控件
        camera1 = findViewById(R.id.camera);
        photo = findViewById(R.id.photo);
        clipping = findViewById(R.id.clipping);
        cameraImage = findViewById(R.id.cameraImage);
        //点击事件
        camera1.setOnClickListener(this);
        photo.setOnClickListener(this);
        clipping.setOnClickListener(this);
        cameraImage.setOnClickListener(this);

    }

    //第二步:设置权限AndroidManifest.xml

    //第三步:判断点击了那个按钮
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.camera:
                //第四步[1]获取相机
                //[1] 相机的隐式回传
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //[2]添加意图(android.intent.category.DEFAULT)
                intent1.addCategory(Intent.CATEGORY_DEFAULT);
                //[3]回传 (注意:请求码要和判断的一样)
                startActivityForResult(intent1, 0);
                break;
            case R.id.photo:
                //第六步:获取相册的图片
                //[1]设置相册的意图（权限）
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                //[2]设置显式MIME数据类型
                intent2.setType("image/*");
                //[3]跳转回传
                startActivityForResult(intent2, 1);
                break;
            case R.id.clipping:
                //第七步:获取相机图片进行裁剪
                //获取相机
                // [1]设置相册的意图（权限）
                Intent intent3 = new Intent(Intent.ACTION_PICK);
                // [2]设置显式MIME数据类型
                intent3.setType("image/*");
                //[3]跳转回传
                startActivityForResult(intent3, 2);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //第五步:获取回传值
        switch (requestCode) {
            case 0:
                //得到拍摄图片
                Bitmap bitmap = data.getParcelableExtra("data");
                //设置给imageView（这个时候就完成了 获取拍照图片）
                cameraImage.setImageBitmap(bitmap);
                break;
            case 1:
                //[1]得到图片路径
                Uri uri1 = data.getData();
                //[2]设置图片（相册获取图片完毕）
                cameraImage.setImageURI(uri1);
                break;
            case 2:
                //获取相测 图片裁剪
                //[1]从返回值中直接获取路径
                Uri uri2 = data.getData();
                //[2]调用裁剪的方法
                Intent crp=	crop(uri2);
                //[3]再次回传
                startActivityForResult(crp, 3);
                break;
            case 3:
                //[1]获取bitmap
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                //[2](设置图片)
                cameraImage.setImageBitmap(bmp);
                break;
        }
    }

    private Intent crop(Uri uri2) {
        /*
         * 裁剪需要的东西
         * 1.图片
         * 2.裁剪框的大小 裁剪完后图片大小（我要裁成什么样子的？其实就是 裁剪完的大小）
         * 3.图片格式
         * 4.得到裁剪完的图片
         * */
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        //设置裁剪的数据源和数据类型
        intent.setDataAndType(uri2, "image/*");
        //可裁剪
        intent.putExtra("crop", "true");
        //裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪后输出图片的尺寸大小（图片展示到imageView的大小，不要太大了，不然会出错）
        //参数（1.输出的大小，大小）
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        // 图片格式（参数：输出格式，格式）
        intent.putExtra("outputFormat", "JPEG");
        // 必须加,否则返回值中找不到返回的值
        intent.putExtra("return-data", true);// 若为true则表示返回数据（图片）
        return intent;
    }
}
