package tw.anonheroes.util;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import tw.anonheroes.R;
import tw.anonheroes.event.GcmReceiveEvent;

/**
 * Created by ivan on 8/23/15.
 */
public class DialogUtil {

    public static void showCustomDialog(Context context, GcmReceiveEvent event){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.photo_dialog);

        ImageView image = (ImageView) dialog.findViewById(R.id.location_photo);
        TextView resultText = (TextView) dialog.findViewById(R.id.result);
        ImageView icon110 = (ImageView) dialog.findViewById(R.id.icon110);
        ImageView icon119 = (ImageView) dialog.findViewById(R.id.icon119);
        ImageView icon113 = (ImageView) dialog.findViewById(R.id.icon113);

        resultText.setText(event.getResult());
        Log.d("ivan", "result: " + event.getResult() + ", url: " + event.getUrl());
        Picasso.with(context).load(event.getUrl()).fit().centerCrop().into(image);
        if(event.is110()){
            icon110.setVisibility(View.VISIBLE);
            Picasso.with(context).load(R.drawable.icon110).into(icon110);
        }
        if(event.is119()){
            icon119.setVisibility(View.VISIBLE);
            Picasso.with(context).load(R.drawable.icon119).into(icon119);
        }
        if(event.is113()){
            icon113.setVisibility(View.VISIBLE);
            Picasso.with(context).load(R.drawable.icon113).into(icon113);
        }
        dialog.show();
    }
}
