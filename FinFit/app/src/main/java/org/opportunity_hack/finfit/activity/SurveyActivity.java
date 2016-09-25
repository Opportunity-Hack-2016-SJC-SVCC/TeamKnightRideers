package org.opportunity_hack.finfit.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.opportunity_hack.finfit.R;
import org.opportunity_hack.finfit.helper.RestApi;

import java.util.ArrayList;

import tyrantgit.explosionfield.ExplosionField;

public class SurveyActivity extends AppCompatActivity {

    static int count = 0;
    static int currentProgress = 10;
    static TextView question;
    static TextView questionNumber;
    static TextView progressDetails;
    static ProgressBar pBar;
    static ArrayList<String> questionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RestApi api = new RestApi();
        api.getConnection("Select+Name,Question__c,Question__r.Min_Answers_to_Select__c,Language__c,Question__r.Max_Answers_to_Select__c,Id+FROM+Language_Question__c+WHERE+Question__r.Include_Question_on_Main_Flow__c=true+ORDER+BY+Question__r.Order__c");


        questionName = RestApi.questionName;

        Button next = (Button) findViewById(R.id.next);
        question = (TextView) findViewById(R.id.question);
        questionNumber = (TextView) findViewById(R.id.questionNumber);
        progressDetails = (TextView) findViewById(R.id.progressDetails);
        pBar = (ProgressBar) findViewById(R.id.horizontal_progress_bar);
        final CardView card = (CardView) findViewById(R.id.card_view);
        final ExplosionField mExplosionField = ExplosionField.attach2Window(this);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mExplosionField.explode(card);
                mExplosionField.clear();
                /*if (count < questionName.size()-1) {
                    count++;
                    currentProgress = currentProgress + 10;
                    pBar.setProgress(currentProgress);
                    question.setText(questionName.get(count));
                    questionNumber.setText("Question " + (count + 1));
                    progressDetails.setText("Progress (" + (count + 1) + "/" + questionName.size() + ")");
                }
                else {
                    Intent i = new Intent(SurveyActivity.this, MainActivity.class);
                    startActivity(i);
                    Toast.makeText(SurveyActivity.this, "Survey Completed!", Toast.LENGTH_SHORT).show();
                    finish();
                    count = 0;
                    currentProgress = 10;
                    questionName.clear();
                }*/

            }
        });
    }

    public static void setData() {
        question.setText(questionName.get(count));
        questionNumber.setText("Question " + (count + 1));
        pBar.setMax(questionName.size()*10);
        pBar.setProgress(currentProgress);
        progressDetails.setText("Progress (" + (count + 1) + "/" + questionName.size() + ")");
    }
}
