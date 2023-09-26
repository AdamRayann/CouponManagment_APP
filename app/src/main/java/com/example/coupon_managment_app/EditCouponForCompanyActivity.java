package com.example.coupon_managment_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
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

public class EditCouponForCompanyActivity extends AppCompatActivity {
    private EditText etPrice, etAmount, etDes, etTitle;
    private ImageButton saveBtn, cancelBtn, imageBtn;
    private ImageView selectedImageIM;
    private Coupon selectedCoupon;
    private TextView etStartDate, etEndDate;
    private Date startDate=null;
    private Date endDate=null;
    private String imagePath="";
    private Spinner categoriesSpinner;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_coupon_for_company);

        saveBtn = findViewById(R.id.editCoupon_btnSave);
        cancelBtn = findViewById(R.id.editCoupon_btnCancel);
        imageBtn = findViewById(R.id.editCoupon_btnPickImage);
        etPrice = findViewById(R.id.editCoupon_etPrice);
        etAmount = findViewById(R.id.editCoupon_etAmount);
        categoriesSpinner = findViewById(R.id.editCoupon_spCategories);
        etDes = findViewById(R.id.editCoupon_etDescription);
        etStartDate = findViewById(R.id.editCoupon_tcStartDate);
        etTitle = findViewById(R.id.editCoupon_etTitle);
        etEndDate = findViewById(R.id.editCoupon_tvEndDate);
        selectedImageIM = findViewById(R.id.editCoupon_ivImage);

        Intent intent = getIntent();
        selectedCoupon = (Coupon) intent.getSerializableExtra("coupon");
        if(selectedCoupon == null) //nothing to be done
            finish();
        startDate = selectedCoupon.getStartDate();
        endDate = selectedCoupon.getEndDate();
        //date fix
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);
        etTitle.setText(selectedCoupon.getTitle());
        etAmount.setText(selectedCoupon.getAmount()+"");
        etPrice.setText( selectedCoupon.getPrice()+"");
        etStartDate.setText(formattedStartDate);
        etEndDate.setText(formattedEndDate);
        etDes.setText(selectedCoupon.getDescription());

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

        selectedImageIM.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // This code will be executed after the ImageView has been laid out and has valid dimensions.
                int width = selectedImageIM.getWidth();
                int height = selectedImageIM.getHeight();

                // Remove the listener to avoid multiple calls
                selectedImageIM.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                // Now you can use the width and height
                if (width != 0 && height != 0) {
                    Bitmap imageBitmap = loadImageFromInternalStorage(selectedCoupon.getImage());
                    imagePath = selectedCoupon.getImage();
                    if (imageBitmap != null) {
                        imageBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
                        selectedImageIM.setImageBitmap(imageBitmap);
                    }
                }
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

    class ButtonsClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(v.getId() == saveBtn.getId()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditCouponForCompanyActivity.this);
                builder.setTitle("Update Confirmation");
                builder.setMessage("Are you sure you want to Update the selected coupoun ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                if (etStartDate.getText().toString().isEmpty()||etEndDate.getText().toString().isEmpty()||etPrice.getText().toString().isEmpty() || etAmount.getText().toString().isEmpty()
                        || etDes.getText().toString().isEmpty() || etStartDate.getText().toString().isEmpty() || etTitle.getText().toString().isEmpty()
                        || etEndDate.getText().toString().isEmpty()) {
                    if (etTitle.getText().toString().isEmpty()) {
                        etTitle.setError("Enter title");
                    }
                    if (etAmount.getText().toString().isEmpty()) {
                        etAmount.setError("Enter amount");
                    }
                    if (etPrice.getText().toString().isEmpty()) {
                        etPrice.setError("Enter price");
                    }
                    if (etDes.getText().toString().isEmpty()) {
                        etDes.setError("Enter description");
                    }
                    if (etStartDate.getText().toString().isEmpty()) {
                        etStartDate.setError("Select start date");
                    }
                    if (etEndDate.getText().toString().isEmpty()) {
                        etEndDate.setError("Select end date");
                    }
                    return;
                }
                if (startDate == null || endDate == null || startDate.after(endDate)) {
                    etEndDate.setError("End date must be after start date");
                    return;
                }
                if(imagePath=="")
                {
                    Toast.makeText(EditCouponForCompanyActivity.this, "Please choose an image!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(imagePath.isEmpty())
                {
                    Toast.makeText(EditCouponForCompanyActivity.this, "Please choose an image!", Toast.LENGTH_SHORT).show();
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
                int couponId = selectedCoupon.getId();
                int companyid = (int) intent.getSerializableExtra("companyId");

                //check title if exists
                AdminFacade admin=new AdminFacade(EditCouponForCompanyActivity.this);
                admin= (AdminFacade) LoginManager.getInstance(EditCouponForCompanyActivity.this).login("admin@admin.com","admin",ClientType.Administrator);
                try {
                    Company company=admin.companiesDAO.getOneCompany(companyid);
                    ArrayList<Coupon> allcoupons=admin.couponsDAO.getAllCoupons();
                    for(Coupon coupon:allcoupons)
                    {
                        if(coupon.getTitle().equals(etTitle.getText().toString()))
                        {
                            if(coupon.getCompanyId()==companyid) {
                                if(coupon.getId()!=couponId) {
                                    etTitle.setError("Title already exists!");
                                    return;
                                }
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
                Coupon coupon = new Coupon(couponId,companyid, category, title, description, startDate, endDate, amount, price, imagePath);
                intent.putExtra("coupon",coupon);
                intent.putExtra("requestCode",6);
                setResult(RESULT_OK, intent);
                finish();
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

            if(v.getId() == cancelBtn.getId()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditCouponForCompanyActivity.this);
                builder.setTitle("Cancel Confirmation");
                builder.setMessage("Are you sure you want to Cancel your current editing Process ?");
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
            if(v.getId() == imageBtn.getId())
            {
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
    private Bitmap loadImageFromInternalStorage(String path) {
        try {
            File file = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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