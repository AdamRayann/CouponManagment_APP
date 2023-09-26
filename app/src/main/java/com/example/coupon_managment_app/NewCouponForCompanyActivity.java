package com.example.coupon_managment_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.net.Uri;


public class NewCouponForCompanyActivity extends AppCompatActivity {
    private TextView  etStartDate, etEndDate;
    private EditText etPrice, etAmount, etDes, etTitle;
    private Date startDate=null;
    private Date endDate=null;
    private ImageButton saveBtn, cancelBtn,imageBtn;
    private ImageView selectedImageIM;
    private Spinner categoriesSpinner;
    private String imagePath="";
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_coupon_for_company);

        saveBtn = findViewById(R.id.CompanyForCoupon_IbtnSave);
        cancelBtn = findViewById(R.id.CompanyForCoupon_ImCancel);
        etPrice = findViewById(R.id.CompanyForCoupon_tvPrice);
        etAmount = findViewById(R.id.CompanyForCoupon_tvAmount);
        etDes = findViewById(R.id.CompanyForCoupon_tvDes);
        etStartDate = findViewById(R.id.CompanyForCoupon_tvStartDate);
        etTitle = findViewById(R.id.CompanyForCoupon_tvTitle);
        etEndDate = findViewById(R.id.CompanyForCoupon_tvEndDate);
        selectedImageIM = findViewById(R.id.CompanyForCoupon_IVimage);
        categoriesSpinner = findViewById(R.id.CompanyForCoupon_spCategory);
        imageBtn = findViewById(R.id.CompanyForCoupon_btnChooseImage);


        ButtonsClick buttonsClick = new ButtonsClick();
        saveBtn.setOnClickListener(buttonsClick);
        cancelBtn.setOnClickListener(buttonsClick);
        imageBtn.setOnClickListener(buttonsClick);


        /** fill spinner with categories**/
        // SHOULD FILL USING
        Category[] categories = Category.values();
        String[] categoryNames = new String[categories.length];

        for (int i = 0; i < categories.length; i++) {
            categoryNames[i] = categories[i].toString();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesSpinner.setAdapter(adapter);

        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etStartDate);
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etEndDate);
            }
        });
    }

    private void showDatePickerDialog(final TextView editText) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Handle the date selection
                // Update the EditText field with the selected date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String formattedDate = dateFormat.format(calendar.getTime());
                try {
                    if (editText.getId() == etStartDate.getId()) {
                        startDate = dateFormat.parse(formattedDate);
                    } else {
                        endDate = dateFormat.parse(formattedDate);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                editText.setText(formattedDate);
            }
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Locks dates before the current day
        datePickerDialog.show();
    }

    class ButtonsClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == saveBtn.getId()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(NewCouponForCompanyActivity.this);
                builder.setTitle("Save Confirmation");
                builder.setMessage("Are you sure you want to save the new coupon?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (etStartDate.getText().toString().isEmpty()||etEndDate.getText().toString().isEmpty()||etPrice.getText().toString().isEmpty() || etAmount.getText().toString().isEmpty()
                        || etDes.getText().toString().isEmpty() || etStartDate.getText().toString().isEmpty()
                        || etTitle.getText().toString().isEmpty() || etEndDate.getText().toString().isEmpty()) {
                    if (etStartDate.getText().toString().isEmpty())
                    {
                        etStartDate.setError("select start date");
                    }
                    if (etEndDate.getText().toString().isEmpty())
                    {
                        etEndDate.setError("select end date");
                    }
                    if (etPrice.getText().toString().isEmpty()) {
                        etPrice.setError("enter price");
                    }
                    if (etAmount.getText().toString().isEmpty()) {
                        etAmount.setError("enter amount");
                    }
                    if (etDes.getText().toString().isEmpty()) {
                        etDes.setError("enter description");
                    }
                    if (etTitle.getText().toString().isEmpty()) {
                        etTitle.setError("enter title");
                    }
                    return;
                }
                if (startDate == null || endDate == null || startDate.after(endDate)) {
                    etEndDate.setError("end date must be after start date");
                    return;
                }
                if(imagePath.isEmpty())
                {
                    Toast.makeText(NewCouponForCompanyActivity.this, "Please choose an image!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(imagePath=="")
                {
                    Toast.makeText(NewCouponForCompanyActivity.this, "Please choose an image!", Toast.LENGTH_LONG).show();
                    return;
                }
                int amount = Integer.parseInt(etAmount.getText().toString());
                double price = Double.parseDouble(etPrice.getText().toString());
                if(amount<1)
                {
                    etAmount.setError("Amount must be positive!");
                    return;
                }
                if(price<0)
                {
                    etPrice.setError("Price must be positive!");
                    return;
                }

                Intent intent = getIntent();
                int companyid = (int) intent.getSerializableExtra("companyId");

                //check title if exists
                AdminFacade admin=new AdminFacade(NewCouponForCompanyActivity.this);
                admin= (AdminFacade) LoginManager.getInstance(NewCouponForCompanyActivity.this).login("admin@admin.com","admin",ClientType.Administrator);
                try {
                    Company company=admin.companiesDAO.getOneCompany(companyid);
                    ArrayList<Coupon> allcoupons=admin.couponsDAO.getAllCoupons();
                    for(Coupon coupon:allcoupons)
                    {
                        if(coupon.getTitle().equals(etTitle.getText().toString()))
                        {
                            if(coupon.getCompanyId()==companyid) {
                                etTitle.setError("Title already exists!");
                                return;
                            }
                        }
                    }
                } catch (CompanyException e) {
                    throw new RuntimeException(e);
                } catch (CouponException e) {
                    throw new RuntimeException(e);
                }
                //
                String title = etTitle.getText().toString();
                String description = etDes.getText().toString();
                Category category = Category.valueOf(categoriesSpinner.getSelectedItem().toString());
                Coupon coupon = new Coupon(companyid, category, title, description, startDate, endDate, amount, price, imagePath);
                intent.putExtra("coupon", coupon);
                intent.putExtra("requestCode", 5);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked "No," so do nothing
                dialog.dismiss(); // Dismiss the dialog
            }
        });

        AlertDialog dialog = builder.create();
                dialog.show();
            }
            if (v.getId() == cancelBtn.getId()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewCouponForCompanyActivity.this);
                builder.setTitle("Cancel Confirmation");
                builder.setMessage("Are you sure you want to Cancel your current process ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "No," so do nothing
                        dialog.dismiss(); // Dismiss the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            if (v.getId() == imageBtn.getId()) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the selected image URI
            Uri selectedImageUri = data.getData();
            Bitmap selectedImageBitmap = null;
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imagePath = saveImageToInternalStorage(selectedImageBitmap, this);
            int width = selectedImageIM.getWidth();
            int height = selectedImageIM.getHeight();

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(selectedImageBitmap, width, height, false);
            selectedImageIM.setImageBitmap(resizedBitmap);
        }
    }

    private String saveImageToInternalStorage(Bitmap bitmap, Context context) {
        try {
            // Use the app's private directory.
            File directory = context.getDir("imageDir", Context.MODE_PRIVATE);
            // Name the file.
            File myImageFile = new File(directory, "selectedImage.jpg");

            FileOutputStream fos = new FileOutputStream(myImageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            return myImageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}




