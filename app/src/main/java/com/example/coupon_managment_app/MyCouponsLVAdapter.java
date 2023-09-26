package com.example.coupon_managment_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyCouponsLVAdapter extends ArrayAdapter<Coupon> {
    private Context context;
    private ArrayList<Coupon> couponsArrayList;
    int line;
    private static final int width=70;
    private static final int height=70;

    public MyCouponsLVAdapter(@NonNull Context context, int line, @NonNull ArrayList<Coupon> coupons) {
        super(context, line, coupons);
        this.context = context;
        this.couponsArrayList = coupons;
        this.line = line;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        /**Creating Custom View*/
        LayoutInflater inflater = LayoutInflater.from(context);
        View myView = inflater.inflate(line, parent, false);

        Coupon coupon = couponsArrayList.get(position);
        TextView tvCouponId, tvCompanyId, tvTitle, tvCategory,tvPrice, tvStartDate,tvEndDate,tvDes;
        ImageView ivCouponImage;
        tvCouponId = myView.findViewById(R.id.myCoupons_IdTV);
        tvCompanyId = myView.findViewById(R.id.myCoupons_companyIdTV);
        tvTitle = myView.findViewById(R.id.myCoupons_titleTV);
        tvCategory = myView.findViewById(R.id.myCoupons_categoryTV);
        ivCouponImage = myView.findViewById(R.id.myCoupons_ivImage);
        tvPrice = myView.findViewById(R.id.myCoupons_priceTV);
        tvStartDate = myView.findViewById(R.id.myCoupons_startDateTV);
        tvEndDate = myView.findViewById(R.id.myCoupons_endDateTV);
        tvDes = myView.findViewById(R.id.myCoupons_descTV);

        //loading image
        Bitmap imageBitmap = loadImageFromInternalStorage(coupon.getImage());
        if (imageBitmap != null) {
            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
            ivCouponImage.setImageBitmap(imageBitmap);
        }

        // date format fix
        Date startDate = coupon.getStartDate();
        Date endDate = coupon.getEndDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedStartDate = dateFormat.format(startDate);
        String formattedEndDate = dateFormat.format(endDate);

        tvCouponId.setText("Coup. ID: " + coupon.getId());
        tvCompanyId.setText("Comp. ID: " + coupon.getCompanyId());
        tvTitle.setText("Title: " + coupon.getTitle());
        tvCategory.setText("Cat.: " + coupon.getCategory().name());

        tvPrice.setText("PP: "+ coupon.getPrice()); // i.e. Purchase Price
        tvStartDate.setText("Start Date: " + formattedStartDate);
        tvEndDate.setText("End Date: " + formattedEndDate);
        tvDes.setText("Descr.: " + coupon.getDescription());


        return myView;
    }


    public void refreshAllMyCoupons(ArrayList<Coupon> coupons) {
        if (coupons==null){
            clear();
            return;}
        clear();
        addAll(coupons);
        notifyDataSetChanged();
    }

    public void refreshMyCouponAdded(Coupon coupon) {
        add(coupon);
        notifyDataSetChanged();
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
}
