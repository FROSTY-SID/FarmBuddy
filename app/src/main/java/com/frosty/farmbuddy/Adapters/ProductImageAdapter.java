package com.frosty.farmbuddy.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.frosty.farmbuddy.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

/**
 * Created by siddh on 19-01-2018.
 */

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.ProductImageViewHolder> {

    protected int numItemsToCreate;
    private ArrayList<Uri> imageUris;
    private Context context;
    private FirebaseStorage storage;

    public  ProductImageAdapter(ArrayList<Uri> imageUris,Context context){
        this.imageUris = imageUris;
        numItemsToCreate = imageUris.size();
        this.context = context;
        storage = FirebaseStorage.getInstance();
    }

    public ProductImageAdapter(int num){
        numItemsToCreate = num;
    }
    @Override
    public ProductImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context =  viewGroup.getContext();
        int layoutIdForItemPic = R.layout.item_pic_recycler_view;
        LayoutInflater inflater  = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForItemPic,viewGroup,shouldAttachToParentImmediately);
        ProductImageViewHolder viewHolder = new ProductImageViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ProductImageViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return numItemsToCreate;
    }


    public class ProductImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageViewProduct  ;

        public ProductImageViewHolder(View itemView) {
            super(itemView);
            mImageViewProduct =itemView.findViewById(R.id.image_view_item_pic);
        }

        void bind(int Index){
            if(imageUris!=null){

                byte[] imageData = null;

                try
                {

                    final int THUMBNAIL_SIZE = 264;
                    //File f = new File(imageUris.get(Index).toString());
                    //Log.d("PIC_ADAPTER",f!=null?f.getName():"null");
                    //FileInputStream fis = new FileInputStream(f);
                    //Bitmap imageBitmap = BitmapFactory.decodeFile(imageUris.get(Index).get);
                    Bitmap imageBitmap =MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUris.get(Index));

                    imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    imageData = baos.toByteArray();
                    mImageViewProduct.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
                }
                catch(Exception ex) {
                    Log.d("PIC_ADAPTER_ERROR",ex.toString());
                    ex.printStackTrace();

                }

                /*StorageReference gsReference = storage.getReferenceFromUrl(imageUris.get(Index));
                Glide.with(context)
                        .using(new FirebaseImageLoader())
                        .load(gsReference)
                        .into(mImageViewProduct);*/
            }
        }
    }
}
