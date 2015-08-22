package tw.anonheroes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tw.anonheroes.api.ApiService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ImageView helpImageButton;

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        Context context = this.getActivity();

        helpImageButton = (ImageView) rootView.findViewById(R.id.help);
        Picasso.with(context).load(R.drawable.help).fit().centerInside().into(helpImageButton);

        helpImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVoiceRecognitionActivity();
            }
        });

        return  rootView;
    }

    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            String result = "";
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for(int i = 0; i < matches.size(); i ++) {
                Log.d("ivan", matches.get(i));
                result += matches.get(i);
            }
            ((MainActivity)getActivity()).getHelp(result);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
