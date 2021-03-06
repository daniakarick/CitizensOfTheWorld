package com.example.danie.comcet325bg46ic;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.danie.comcet325bg46ic.data.Location;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by danie on 21/12/2016.
 */
public class AddLocation extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    EditText locationNameTxt;
    EditText locationTxt;
    EditText descriptionTxt;
    EditText priceTxt;
    ImageView preview;

    RadioButton rdoTakePic;
    RadioButton rdoSelectPic;
    int acitivityCode = 0;
    RadioGroup rdoGroup;
    Bitmap imageToSave = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);

        locationNameTxt = (EditText)findViewById(R.id.locationName);
        locationTxt = (EditText)findViewById(R.id.locationText);
        descriptionTxt = (EditText)findViewById(R.id.descriptionText);
        priceTxt = (EditText)findViewById(R.id.priceTxt);
        preview = (ImageView)findViewById(R.id.imgPreview);

        locationNameTxt.setText("hid");
        locationTxt.setText("uihui");
        descriptionTxt.setText("fhe");
        priceTxt.setText("5.99");
        rdoGroup = (RadioGroup)findViewById(R.id.imageChoices);
        rdoTakePic = (RadioButton)findViewById(R.id.TakePhoto);
        rdoSelectPic = (RadioButton)findViewById(R.id.UploadImage);
        rdoGroup.setOnCheckedChangeListener(this);
    }

    public void GetImage(View v){
        Intent intent = null;

        if(rdoTakePic.isChecked()){
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            acitivityCode = 2;
        }else if(rdoSelectPic.isChecked()){
            intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            acitivityCode = 3;
        }

        if(intent != null){
            if(intent.resolveActivity(getPackageManager()) != null){
                startActivityForResult(intent,acitivityCode);
            }
        }
    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                imageToSave = (Bitmap)extras.get("data");
                preview.setImageBitmap(imageToSave);
            }
        }
        else if(requestCode == 3){
            if(resultCode == RESULT_OK){
                Uri selectedImage = data.getData();

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                imageToSave = BitmapFactory.decodeFile(filePath);
                preview.setImageBitmap(imageToSave);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
    public void AddLocation(View v){
        Location locationToAdd = new Location();
        String filePath = "";

        if(imageToSave == null) {
           // imageToSave = BitmapFactory.decodeResource(getResources(), R.drawable.tokyo);
        }
            filePath = SaveImage(imageToSave);


            locationToAdd.Name = locationNameTxt.getText().toString();
            locationToAdd.Price = Double.parseDouble(priceTxt.getText().toString());
            locationToAdd.Description = descriptionTxt.getText().toString();
            locationToAdd.FileName = filePath;
            SQLDatabase sqlDb = new SQLDatabase(this);
            sqlDb.addLocation(locationToAdd);
            Toast.makeText(getApplicationContext(), "Location was added", Toast.LENGTH_LONG).show();

    }

    public String SaveImage(Bitmap b){
        OutputStream output;
        File filePath = getFilesDir();
        File dir = new File(filePath.getAbsolutePath() + "/CitizensoftheWorld");

        try{
            dir.mkdirs();
        }catch (Exception e){
            e.printStackTrace();
        }

        String fileName = setFileName();
        File file = new File(dir,fileName);

        try{
            output = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 50, output);
            output.flush();
            output.close();
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }

        return fileName;
    }

    public String setFileName(){
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date currentDate = new Date();
        df.format(currentDate);
        String date = currentDate.toString();
        char [] charDate = date.toCharArray();

        for(int i=0;i<charDate.length;i++){
            String s = Character.toString(charDate[i]);

            if(s.equals("") || s.equals(" ")){
                s = "_";
            }

            charDate[i] = s.charAt(0);
        }
        String result = "";
        for(char c : charDate){
            result += c;
        }
        return "image_" + result + ".jpg";
    }
}
