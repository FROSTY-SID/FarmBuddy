package com.frosty.farmbuddy.Adapters;



import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.frosty.farmbuddy.Fragments.MyShopFragment;
import com.frosty.farmbuddy.Fragments.ProductUpdateFragment;
import com.frosty.farmbuddy.Objects.Product;
import com.frosty.farmbuddy.R;
import com.frosty.farmbuddy.Utility.FarmBuddyValues;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

/**
 * Created by siddh on 23-01-2018.
 */

public class FireBaseMyShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

    private View mView;
    private Context context;
    private FirebaseStorage storage;
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;
    private ImageView mImageViewProduct;
    private TextView  mTextViewCat;
    private TextView mTextViewVariety;
    private TextView mTextViewName;
    private TextView mTextViewRate;
    private ImageView mImageViewEdit;
    private FragmentTransaction fragmentTransaction;

    public FireBaseMyShopViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        context = itemView.getContext();
        storage = FirebaseStorage.getInstance();
        itemView.setOnClickListener(this);

    }

    public void bindProducts(final Product product, final FragmentManager fragmentManager ){
        mImageViewProduct = mView.findViewById(R.id.my_shop_product_imageView);
        mTextViewCat= mView.findViewById(R.id.my_shop_product_cat);
       mTextViewVariety = mView.findViewById(R.id.my_shop_product_variety);
       mTextViewName = mView.findViewById(R.id.my_shop_product_name);
       mTextViewRate = mView.findViewById(R.id.my_shop_product_rate);
       mImageViewEdit = mView.findViewById(R.id.imageView_edit_product);
       fragmentTransaction =  fragmentManager.beginTransaction();

       mImageViewEdit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              // Log.d("ViewHolder",product.id);
               fragmentTransaction.replace(R.id.empty_activity_layout_for_fragment, ProductUpdateFragment.getInstance(product.id), FarmBuddyValues.FRAGMENT_PRODUCT_UPDATE_TAG);
               fragmentTransaction.addToBackStack(FarmBuddyValues.FRAGMENT_PRODUCT_UPDATE_TAG);
               fragmentTransaction.commit();
           }
       });
       if(product.productPicUrls!=null) {
           StorageReference gsReference = storage.getReferenceFromUrl(product.productPicUrls.get(0));
       /* Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(gsReference)
                .into(mImageViewProduct);*/
           gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
               @Override
               public void onSuccess(Uri uri) {
                   Picasso.with(context)
                           .load(uri.toString())
                           .resize(MAX_WIDTH, MAX_HEIGHT)
                           .centerCrop()
                           .into(mImageViewProduct);
               }
           });
       }
      /* if(product.productPicUrls!=null) {
           Picasso.with(context)
                   .load(storage.getReferenceFromUrl(product.productPicUrls.get(0)).toString())
                   .resize(MAX_WIDTH, MAX_HEIGHT)
                   .centerCrop()
                   .into(mImageViewProduct);
       }*/

       // Log.d("VIEWHOLDER","ImageView "+product!=null?storage.getReferenceFromUrl(product.productPicUrls.get(0)).toString():"NUll");
        mTextViewCat.setText(product.cat);
        mTextViewName.setText(product.name);
        mTextViewRate.setText("₹ "+product.rate + " /"+product.unit );
        mTextViewVariety.setText(product.variety);



    }

    @Override
    public void onClick(View v) {

    }
}
