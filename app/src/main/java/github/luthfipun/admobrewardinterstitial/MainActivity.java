package github.luthfipun.admobrewardinterstitial;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity implements OnUserEarnedRewardListener {

    private static final String REWARD_INTERS_AD_UNIT = "ca-app-pub-3940256099942544/5354046379";
    private final String TAG = "MainActivity";
    private RewardedInterstitialAd rewardedInterstitialAd;

    private Button buttonSubmit;
    private TextView txtRewarded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAds();
        initView();
    }

    private void initView() {
        buttonSubmit = findViewById(R.id.btnSubmit);
        txtRewarded = findViewById(R.id.txtRewarded);

        buttonSubmit.setOnClickListener(v -> {
            // click to show ads if the ads is loaded
            rewardedInterstitialAd.show(this, this);
        });
    }

    private void initAds() {
        MobileAds.initialize(this, initializationStatus -> loadAds());
    }

    private void loadAds() {
        AdRequest builder = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(this, REWARD_INTERS_AD_UNIT, builder, new RewardedInterstitialAdLoadCallback(){
            @Override
            public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                MainActivity.this.rewardedInterstitialAd = rewardedInterstitialAd;

                // Ready to show ads and button enable to click
                buttonSubmit.setEnabled(true);

                MainActivity.this.rewardedInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.e(TAG, "Ads is show fullscreen");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.e(TAG, "Ads failed show fullscreen => " + adError.getMessage());
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        Log.e(TAG, "Ads fullscreen dismissed");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                MainActivity.this.notify(loadAdError.getMessage());
            }
        });
    }

    private void notify(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
        // if ads successfully get rewarded
        notify("You are get reward");
        txtRewarded.setText("COIN: 100");
    }
}