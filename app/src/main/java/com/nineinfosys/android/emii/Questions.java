package com.nineinfosys.android.emii;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Questions extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);


        expListView = (ExpandableListView) findViewById(R.id.expandableListView);

        // preparing list data
        prepareListData();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });




        // Listview Group collasped listener


    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("What is EMI ?");
        listDataHeader.add("How to Calculate EMI");




        // Adding child data
        List<String> EMI= new ArrayList<String>();
        EMI.add("An equated monthly installment (EMI) " );
                EMI.add( "is a fixed payment ");
        EMI.add("amount made by a borrower " );
        EMI.add( "to a lender at a specified ") ;
        EMI.add("date each calendar month." );
        EMI.add(  " Equated monthly installments") ;
        EMI.add(" are used to pay off both " );
                EMI.add(   "interest and principal ");
        EMI.add( "each month so that over " );
                EMI.add("a specified number of years") ;
        EMI.add(" the loan is paid off in full.most common " );
        EMI.add( "types of loans, such as " );
        EMI.add("real estate mortgages, " );
        EMI.add(  "the borrower makes fixed periodic " );
                EMI.add("payments to the lender over " );
                EMI.add(  "the course of several years with" );
        EMI.add(" the goal of retiring the loan.\n" );



        List<String> Calculate = new ArrayList<String>();
        Calculate.add("EMI = [P x I x (1+I)^N]/[(1+I)^N-1]");
        Calculate.add("P =loan amount or Principal");
        Calculate.add("I = Interest rate per month");
        Calculate.add("[To calculate rate per month: ");
                Calculate.add( "if the interest rate per annum is 14%,");
        Calculate.add( "the per month rate " );
                Calculate.add("14/(12 x 100)]");
        Calculate.add( "N = the number of installments\n");





        listDataChild.put(listDataHeader.get(0), EMI); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Calculate);

    }

}