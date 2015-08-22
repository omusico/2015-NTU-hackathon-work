package tw.anonheroes.util;

import android.app.Dialog;
import android.content.Context;
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
        Picasso.with(context).load(event.getUrl()).into(image);
        if(event.is110()){
            Picasso.with(context).load(R.drawable.icon110).into(icon110);
        }
        if(event.is119()){
            Picasso.with(context).load(R.drawable.icon119).into(icon119);
        }
        if(event.is113()){
            Picasso.with(context).load(R.drawable.icon113).into(icon113);
        }
        dialog.show();
    }
}
