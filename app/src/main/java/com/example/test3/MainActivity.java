package com.example.test3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        TextView textView = (TextView) findViewById(R.id.textView);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarcodeScannerOptions options =
                        new BarcodeScannerOptions.Builder().
                                setBarcodeFormats(
                                        // 바코드 포맷 설정
                                        Barcode.FORMAT_EAN_13
                                ).build();

                AssetManager assetManager = getResources().getAssets();

                InputStream is;
                Bitmap bitmap = null;
                try {
                    is = assetManager.open("EAN-13_barcode.jpg");
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap != null) {
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 480, 360, true);
                    imageView.setImageBitmap(resizedBitmap);
                    bitmap = resizedBitmap;

                    InputImage image = InputImage.fromBitmap(bitmap, 0);
                    BarcodeScanner scanner = BarcodeScanning.getClient();

                    Task<List<Barcode>> result = scanner.process(image)
                            .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                                @Override
                                public void onSuccess(List<Barcode> barcodes) {
                                    // Task completed successfully
                                    for (Barcode barcode : barcodes) {
                                        Rect bounds = barcode.getBoundingBox();
                                        Point[] corners = barcode.getCornerPoints();

                                        String rawValue = barcode.getRawValue();
                                        int valueType = barcode.getValueType();


                                        textView.setText(rawValue);
                                        }
                                    }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Task failed with an exception
                                    // ...
                                }
                            });
                }
            }
        });
    }
}