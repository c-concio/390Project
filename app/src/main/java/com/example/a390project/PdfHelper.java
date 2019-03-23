package com.example.a390project;

import android.app.Activity;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a390project.ListViewAdapters.EmployeeCommentListViewAdapter;
import com.example.a390project.Model.EmployeeComment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

class PdfHelper {

    // variables
    private PdfDocument document;
    private int canvasHeight;
    private int canvasWidth;
    private PdfDocument.PageInfo pageInfo;
    private PdfDocument.Page page;

    private OutputStream os;
    private Context context;

    FirebaseHelper firebaseHelper;

    private static final String TAG = "PdfHelper";

    // instructions on how to use:
    // start a PdfHelper class to start the file
    // start


    PdfHelper(int canvasHeight, int canvasWidth, Context context){
        // initialize PdfDocument
        document = new PdfDocument();
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;

        this.context = context;

        firebaseHelper = new FirebaseHelper();
    }

    // start a page with the specified page number
    void startPage(int pageNumber){
        pageInfo = new PdfDocument.PageInfo.Builder(canvasWidth, canvasHeight, pageNumber).create();
        page = document.startPage(pageInfo);
    }

    // header (outputs info of project and customer

    // ----------------------------------- Inspection -----------------------------------
    // receive the parts received, parts accepted, and parts rejected and create the inspection layout
    public void createInspectionLayout(int partsReceived, int partsAccepted, int partsRejected){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_inspection_layout, null);

        // find the views for the inspection values
        TextView receivedTextView = content.findViewById(R.id.receivedTextView);
        TextView acceptedTextView = content.findViewById(R.id.acceptedTextView);
        TextView rejectedTextView = content.findViewById(R.id.rejectedTextView);

        // set the text for the views
        receivedTextView.setText(String.valueOf(partsReceived));
        acceptedTextView.setText(String.valueOf(partsAccepted));
        rejectedTextView.setText(String.valueOf(partsRejected));

        measureLayout(content);

        content.draw(page.getCanvas());
    }

    // ----------------------------------- Prepaint -----------------------------------
    public void createPrePaintLayout(){
        // get the layout view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_prepaint_layout, null);

        // find views for the prepaint values
        measureLayout(content);

        content.draw(page.getCanvas());
        document.finishPage(page);
    }


    // ----------------------------------- Painting -----------------------------------
    public void createPaintLayout(){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_paint_layout, null);

        measureLayout(content);

        content.draw(page.getCanvas());
        document.finishPage(page);

    }

    // ----------------------------------- Baking -----------------------------------
    public void createBakingLayout(){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_baking_layout, null);

        measureLayout(content);

        content.draw(page.getCanvas());
        document.finishPage(page);

    }

    // ----------------------------------- Comments -----------------------------------
    public void createCommentsLayout(String taskID){
        // create a view for the employee layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_comments_layout, null);
        Activity activity = (Activity) content.getContext();




        // call the getEmployees function in FirebaseHelper to get all the employees
        Log.d(TAG, "createCommentsLayout: --------------------------------------------------------------------- calling get Employee comments");
        //firebaseHelper.getEmployeeComments(activity, content, taskID);

        ListView employeeCommentsListView = content.findViewById(R.id.employeeCommentsListView);
        Log.d(TAG, "createCommentsLayout: listView is " + employeeCommentsListView);

        // measure the layout
        measureLayout(content);

        content.draw(page.getCanvas());

    }

    public void endPage(){
        document.finishPage(page);
    }


    // generate pdf
    void generatePdf(){
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";

        try {
            File pdfDirPath = new File(context.getFilesDir(), "pdfs");
            pdfDirPath.mkdirs();



            // --------------------------------- pdf directory change ---------------------------------
            File file = new File(pdfDirPath, "pdfsend.pdf");

            Log.d(TAG, "onStart: location here: " + file.getAbsolutePath());

            os = new FileOutputStream(file);
            document.writeTo(os);
            document.close();
            os.close();

        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }
    }

    void measureLayout(View content){
        // measure the layout
        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);
        content.measure(measureWidth, measuredHeight);
        content.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());
    }

    void getComments(String taskID){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("tasks").child(taskID).child("employeeComments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                startPage(1);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View content = inflater.inflate(R.layout.pdf_comments_layout, null);
                Activity activity = (Activity) content.getContext();

                // call the getEmployees function in FirebaseHelper to get all the employees
                Log.d(TAG, "createCommentsLayout: --------------------------------------------------------------------- calling get Employee comments");
                //firebaseHelper.getEmployeeComments(activity, content, taskID);

                ListView employeeCommentsListView = content.findViewById(R.id.employeeCommentsListView);
                Log.d(TAG, "createCommentsLayout: listView is " + employeeCommentsListView);

                List<EmployeeComment> comments = new ArrayList<>();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    EmployeeComment currentComment = postSnapshot.getValue(EmployeeComment.class);
                    Log.d(TAG, "onDataChange: Called snapshot");

                    Log.d(TAG, "onDataChange: got comment");
                    comments.add(currentComment);
                }

                // -------------------------- testing
                employeeCommentsListView.setAdapter(new EmployeeCommentListViewAdapter(content.getContext(), comments));


                // measure the layout
                measureLayout(content);

                content.draw(page.getCanvas());

                endPage();

                generatePdf();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
