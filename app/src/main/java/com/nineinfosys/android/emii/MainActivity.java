package com.nineinfosys.android.emii;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements  OnChartValueSelectedListener{

    Button emiCalcBtn, Reset, Save;
    private PieChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private Typeface mTfRegular;
    private Typeface mTfLight;
    float st1,st2,TA,strAmount;

    EditText Principal,Intrest,Year,Total_Interest,result,Total_Amount;
    double loanamount=0;
 //   double totalPayment, totalInterest, PrincipalAmount, ToatalInterest, TotalPayment, interestRate, loanPeriod, MonthlyPayment, AnnualPayment, monthlyRate;

    private String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
    protected float[] mParties;

   // private double[] mParties;
   // WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

         Principal = (EditText) findViewById(R.id.principal);
        Intrest = (EditText) findViewById(R.id.interest);
        Year = (EditText) findViewById(R.id.years);



         Total_Interest = (EditText) findViewById(R.id.total_interest);
        result = (EditText) findViewById(R.id.emi);
        Total_Amount = (EditText) findViewById(R.id.total_amount);

        emiCalcBtn = (Button) findViewById(R.id.btn_calculate);
       Reset = (Button) findViewById(R.id.btn_reset);
      //  Save = (Button) findViewById(R.id.btn_save);
        emiCalcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                st1 =Float.parseFloat (Principal.getText().toString());
                st2 = Float.parseFloat (Intrest.getText().toString());
                Toast.makeText(MainActivity.this, " PrincipalAmount" + st1+"   "+st2, Toast.LENGTH_SHORT).show();

                String st3 = Year.getText().toString();

             /*   if (TextUtils.isEmpty(st1)) {
                    Principal.setError("Enter Prncipal Amount");
                    Principal.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(st2)) {
                    Intrest.setError("Enter Interest Rate");
                    Intrest.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(st3)) {
                    Year.setError("Enter Years");
                    Year.requestFocus();
                    return;
                }*/
                Toast.makeText(MainActivity.this, "loanamount"+loanamount, Toast.LENGTH_SHORT).show();
                float p = Float.parseFloat(String.valueOf(st1));
                float i = Float.parseFloat(String.valueOf(st2));
                float y = Float.parseFloat(st3);

                float Principal = calPric(p);

                float Rate = calInt(i);

                float Months = calMonth(y);

                float Dvdnt = calDvdnt(Rate, Months);

                float FD = calFinalDvdnt(Principal, Rate, Dvdnt);

                float D = calDivider(Dvdnt);

                float emi = calEmi(FD, D);

                 TA = calTa(emi, Months);

                float ti = calTotalInt(TA, Principal);


                result.setText(String.valueOf(emi));

                Total_Interest.setText(String.valueOf(ti));
                Total_Amount.setText(String.valueOf(TA));
            }
        });
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Principal.setText("");
                Intrest.setText("");
                Year.setText("");
                result.setText("");
                Total_Interest.setText("");

                Total_Amount.setText("");
            }
        });

      /*  Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Principal.getText().toString();
                Intrest.getText().toString();
                Year.getText().toString();
                result.getText().toString();

                TI.getText().toString();
                Total_Amount.getText().toString();


            }
        });*/

        //for piechart
       // mParties = new double[]{st1, st2};

        //  mParties=strPrincipal;
/*
        tvX = (TextView) findViewById(R.id.tvXMax);
        tvY = (TextView) findViewById(R.id.tvYMax);*/



    }

    private void displayPiechart(float st1, float st2)
    {
       // Toast.makeText(MainActivity.this, " ANNNNNN" + st1+"   "+st2, Toast.LENGTH_SHORT).show();
        strAmount=(float) TA;
        mParties = new float[]{st1, st2};
        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);
       mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(55f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" â‚¬");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        setData(2, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_question) {
            startActivity(new Intent(MainActivity.this, Questions.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public float calPric(float p) {

        return (float) (p);

    }

    public float calInt(float i) {

        return (float) (i / 12 / 100);

    }

    public float calMonth(float y) {

        return (float) (y * 12);

    }

    public float calDvdnt(float Rate, float Months) {

        return (float) (Math.pow(1 + Rate, Months));

    }

    public float calFinalDvdnt(float Principal, float Rate, float Dvdnt) {

        return (float) (Principal * Rate * Dvdnt);

    }

    public float calDivider(float Dvdnt) {

        return (float) (Dvdnt - 1);

    }

    public float calEmi(float FD, Float D) {

        return (float) (FD / D);

    }

    public float calTa(float emi, Float Months) {

        return (float) (emi * Months);

    }

    public float calTotalInt(float TA, float Principal) {

        return (float) (TA - Principal);

    }




    private void setData(int count, float range) {

        float mult = range;

        //ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float) st1, "Principal-" + st1));
        entries.add(new PieEntry((float) st2, "Interest-" + st2));
       // Toast.makeText(this, "" + (float) st1, Toast.LENGTH_LONG).show();


        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
      /*  for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mParties[i % mParties.length]));
        }*/

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();


}
    private SpannableString generateCenterSpannableText() {


        Toast.makeText(this, "otjhofgkjlfkjhfkljh" + (float) TA, Toast.LENGTH_LONG).show();
        SpannableString s = new SpannableString("Total Amount\n"+strAmount);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 18, 0);

     //s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
      //s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
      //  s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
     // s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }



    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}



