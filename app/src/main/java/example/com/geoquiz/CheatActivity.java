package example.com.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "example.com.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "example.com.geoquiz.answer_shown";
    private boolean answerIsTrue;
    private TextView tv_Answer;
    private Button bt_Show_Answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        tv_Answer = findViewById(R.id.tv_answer);
        bt_Show_Answer = findViewById(R.id.bt_show_answer);
        answerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        bt_Show_Answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (answerIsTrue)
                    tv_Answer.setText(R.string.true_button);
                else
                    tv_Answer.setText(R.string.false_button);
                setAnswerShownResult(true);
                //定义动画效果
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = bt_Show_Answer.getWidth() / 2;
                    int cy = bt_Show_Answer.getHeight() / 2;
                    float radius = bt_Show_Answer.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(bt_Show_Answer, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            bt_Show_Answer.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    bt_Show_Answer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
