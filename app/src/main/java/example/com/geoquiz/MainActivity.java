package example.com.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private Button bt_True;
    private Button bt_Flase;
    private ImageButton ib_Next;
    private ImageButton ib_prev;
    private TextView tv_Question;
    private Button bt_Cheat;
    private Question[] questionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int currentIndex = 0;
    private boolean isCheater;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, currentIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        bt_True = findViewById(R.id.bt_true);
        bt_Flase = findViewById(R.id.bt_false);
        ib_Next = findViewById(R.id.ib_next);
        ib_prev = findViewById(R.id.ib_prev);
        tv_Question = findViewById(R.id.tv_question);
        bt_Cheat = findViewById(R.id.bt_cheat);
        bt_True.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast toast = Toast.makeText(MainActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();*/
                checkAnswer(true);
            }
        });
        bt_Flase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(MainActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();*/
                checkAnswer(false);
            }
        });

        ib_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex = (currentIndex + 1) % questionBank.length;
                isCheater = false;
                updateQuestion();
            }
        });
        ib_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex != 0)
                    currentIndex = currentIndex - 1;
                else
                    currentIndex = 0;
                updateQuestion();
            }
        });
        bt_Cheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = questionBank[currentIndex].isAnswerTrue();
                //startActivity(CheatActivity.newIntent(MainActivity.this, answerIsTrue));
                startActivityForResult(CheatActivity.newIntent(MainActivity.this, answerIsTrue),REQUEST_CODE_CHEAT);
            }
        });
    }

    private void updateQuestion() {
        int question = questionBank[currentIndex].getTextResId();
        tv_Question.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = questionBank[currentIndex].isAnswerTrue();
        int messageResId = 0;
        if (isCheater){
            messageResId = R.string.judgment_toast;
        }else{
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            if (data != null){
                isCheater = CheatActivity.wasAnswerShown(data);
            }
        }
    }
}
