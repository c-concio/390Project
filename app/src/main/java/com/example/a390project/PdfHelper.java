package com.example.a390project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a390project.ListViewAdapters.EmployeeCommentListViewAdapter;
import com.example.a390project.ListViewAdapters.PrepaintTaskListViewAdapter;
import com.example.a390project.Model.EmployeeComment;
import com.example.a390project.Model.Task;
import com.example.a390project.Model.TaskClasses.InspectionTask;
import com.example.a390project.Model.TaskClasses.Inventory;
import com.example.a390project.Model.TaskClasses.PackagingTask;
import com.example.a390project.Model.TaskClasses.PaintingTask;
import com.example.a390project.Model.TaskClasses.PrePaintingTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

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

    private OutputStream outputStream;
    private Context context;

    FirebaseHelper firebaseHelper;

    private static final String TAG = "PdfHelper";


    // value event listeners
    private ValueEventListener projectValueEventListener;
    final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

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
    private void startPage(int pageNumber){
        pageInfo = new PdfDocument.PageInfo.Builder(canvasWidth, canvasHeight, pageNumber).create();
        page = document.startPage(pageInfo);
    }

    private void startPage(int pageNumber, int height){
        pageInfo = new PdfDocument.PageInfo.Builder(canvasWidth, height, pageNumber).create();
        page = document.startPage(pageInfo);
    }

    // header (outputs info of project and customer

    // ----------------------------------- Inspection -----------------------------------
    // receive the parts received, parts accepted, and parts rejected and create the inspection layout
    private void createInspectionLayout(InspectionTask inspectionTask, Boolean isFinal){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_inspection_layout, null);

        // find the views for the inspection values
        TextView countedTextView = content.findViewById(R.id.countedTextView);
        TextView acceptedTextView = content.findViewById(R.id.acceptedTextView);
        TextView rejectedTextView = content.findViewById(R.id.rejectedTextView);

        // set the text for the views
        countedTextView.setText(String.valueOf(inspectionTask.getPartCounted()));
        acceptedTextView.setText(String.valueOf(inspectionTask.getPartAccepted()));
        rejectedTextView.setText(String.valueOf(inspectionTask.getPartRejected()));

        if (isFinal){
            TextView inspectionTitleTextView = content.findViewById(R.id.inspectionTitleTextView);
            inspectionTitleTextView.setText("Final-Inspection");
        }

        measureLayout(content);

        content.draw(page.getCanvas());
    }

    // ----------------------------------- Prepaint -----------------------------------
    private void createPrePaintLayout(PrePaintingTask prePaintingTask){
        // get the layout view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_prepaint_layout, null);

        // find views for the prepaint values
        TextView sandBlastingTextView = content.findViewById(R.id.sandBlastingTextView);
        TextView sandingTextView = content.findViewById(R.id.sandingTextView);
        TextView manualSolventCleaningTextView = content.findViewById(R.id.manualSolventCleaningTextView);
        TextView iriditeTextView = content.findViewById(R.id.iriditeTextView);
        TextView maskingTextView = content.findViewById(R.id.maskingTextView);

        //int minuteConversion = 60000;
        int minuteConversion = 1000;

        sandBlastingTextView.setText(String.valueOf(prePaintingTask.getSandblastingHours()/minuteConversion) + "s");
        sandingTextView.setText(String.valueOf(prePaintingTask.getSandingHours()/minuteConversion) + "s");
        manualSolventCleaningTextView.setText(String.valueOf(prePaintingTask.getCleaningHours()/minuteConversion) + "s");
        iriditeTextView.setText(String.valueOf(prePaintingTask.getIriditeHours()/minuteConversion) + "s");
        maskingTextView.setText(String.valueOf(prePaintingTask.getMaskingHours()/minuteConversion) + "s");

        measureLayout(content);

        content.draw(page.getCanvas());
    }


    // ----------------------------------- Painting -----------------------------------
    private void createPaintLayout(){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_paint_layout, null);

        measureLayout(content);

        content.draw(page.getCanvas());

    }

    // ----------------------------------- Baking -----------------------------------
    public void createBakingLayout(){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_baking_layout, null);

        measureLayout(content);

        content.draw(page.getCanvas());

    }

    // ----------------------------------- Packaging -----------------------------------
    private void createPackagingLayout(){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_packaging_layout, null);

        measureLayout(content);

        content.draw(page.getCanvas());
    }

    // ----------------------------------- Comments -----------------------------------
    private void createCommentsLayout(DataSnapshot dataSnapshot, String taskId, String taskType, int pageNumber){
        // create a view for the employee layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_comments_layout, null);

        ListView employeeCommentsListView = content.findViewById(R.id.employeeCommentsListView);
        List<EmployeeComment> employeeComments = new ArrayList<>();

        TextView commentsTitleTextView = content.findViewById(R.id.commentsTitleTextView);
        String commentTitle = taskType + " Comments";
        commentsTitleTextView.setText(commentTitle);

        // get all the comments for the given taskIds
        for(DataSnapshot postSnapshot : dataSnapshot.child(taskId).child("employeeComments").getChildren()){
            EmployeeComment comment;
            comment = postSnapshot.getValue(EmployeeComment.class);
            employeeComments.add(comment);
        }

        // set the adapter of the employeeCommentsListView
        EmployeeCommentListViewAdapter adapter = new EmployeeCommentListViewAdapter(content.getContext(), employeeComments);
        employeeCommentsListView.setAdapter(adapter);

        // ---------------------------------------------------

        // measure the height of the listView of comments
        int totalHeight = 0;

        for (int i = 0; i < adapter.getCount(); i++) {
            View mView = adapter.getView(i, null, employeeCommentsListView);

            mView.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),

                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = employeeCommentsListView.getLayoutParams();
        params.height = totalHeight
                + (employeeCommentsListView.getDividerHeight() * (adapter.getCount() - 1));
        employeeCommentsListView.setLayoutParams(params);
        employeeCommentsListView.requestLayout();
        totalHeight = totalHeight + 150;


        Log.d(TAG, "createCommentsLayout: comment height " + totalHeight);

        if (totalHeight > canvasHeight)
            startPage(pageNumber, totalHeight);
        else
           startPage(pageNumber);

        // ---------------------------------------------------

        // measure the layout
        measureLayout(content);

        content.draw(page.getCanvas());

    }


    private void endPage(){
        document.finishPage(page);
    }

    // generate pdf
    void generatePdf(final String projectPO){
        List<Boolean> taskTypes = new ArrayList<>();

        // main listener
        rootRef.addValueEventListener((projectValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // get all the project's tasks
                // list of taskIds:
                List<String> taskIds = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.child("projects").child(projectPO).child("tasks").getChildren()) {
                    taskIds.add(postSnapshot.getKey());
                }

                // -------------------------------------------------- Set ID --------------------------------------------------
                // go through all the tasks and compute
                // variables for the inspection IDs
                String inspectionID = null;
                List<String> prePaintIDs = new ArrayList<>();
                List<String> paintingIDs = new ArrayList<>();
                List<String> packagingIDs = new ArrayList<>();
                String finalInspectionID = null;
                int pageNumber = 0;

                for (String currentTaskID : taskIds) {
                    // get the taskType of current taskID
                    String taskType = dataSnapshot.child("tasks").child(currentTaskID).child("taskType").getValue(String.class);

                    // check taskType and assign to respective taskID
                    switch (taskType) {
                        case ("Inspection"):
                            inspectionID = currentTaskID;
                            break;
                        case ("Pre-Painting"):
                            prePaintIDs.add(currentTaskID);
                            break;
                        case ("Painting"):
                            paintingIDs.add(currentTaskID);
                            break;
                        case ("Packaging"):
                            packagingIDs.add(currentTaskID);
                            break;
                        case ("Final-Inspection"):
                            finalInspectionID = currentTaskID;
                            break;
                    }
                }

                // task dataSnapshot
                DataSnapshot taskSnapshot = dataSnapshot.child("tasks");

                // --------------------------------------------- 1. Inspection Task ---------------------------------------------
                // if inspection task is available, create the inspection page
                if (inspectionID != null) {
                    InspectionTask inspectionTask = taskSnapshot.child(inspectionID).getValue(InspectionTask.class);
                    if (inspectionTask != null) {
                        startPage(pageNumber);
                        createInspectionLayout(inspectionTask, false);
                        endPage();
                        pageNumber++;

                        // Inspection task comments
                        if (taskSnapshot.child(inspectionID).child("employeeComments").exists()) {
                            createCommentsLayout(taskSnapshot, inspectionID, "Inspection", pageNumber);
                            endPage();
                            pageNumber++;
                        }
                    }
                }

                // --------------------------------------------- 2. Pre-paint Task ---------------------------------------------

                DataSnapshot workSnapshot = dataSnapshot.child("workHistory").child("workingTasks");

                if (!prePaintIDs.isEmpty()) {
                    // go through all the prepaint tasks and create and output the layout
                    long sandBlastingHours = 0;
                    long sandingHours = 0;
                    long cleaningHours = 0;
                    long iriditeHours = 0;
                    long maskingHours = 0;

                    for (String prePaintID : prePaintIDs) {
                        startPage(pageNumber);

                        // go through all the working blocks for the task and output the hours
                        DataSnapshot workBlockSnapshot = workSnapshot.child(prePaintID).child("workBlocks");
                        for (DataSnapshot postSnapshot : workBlockSnapshot.getChildren()){
                            String title = postSnapshot.child("title").getValue(String.class);
                            long workingTime = postSnapshot.child("workingTime").getValue(Long.class);

                            switch(title){
                                case "SandBlasting":
                                    sandBlastingHours += workingTime;
                                    break;
                                case "Sanding":
                                    sandingHours += workingTime;
                                    break;
                                case "Masking":
                                    maskingHours += workingTime;
                                    break;
                                case "ManualSolventCleaning":
                                    cleaningHours += workingTime;
                                    break;
                                case "Iridite":
                                    iriditeHours += workingTime;
                                    break;
                            }
                        }

                        PrePaintingTask prePaintingTask = new PrePaintingTask(sandBlastingHours, sandingHours, cleaningHours, iriditeHours, maskingHours);


                        createPrePaintLayout(prePaintingTask);

                        endPage();
                        pageNumber++;

                        // Inspection task comments
                        if (taskSnapshot.child(prePaintID).child("employeeComments").exists()) {
                            createCommentsLayout(taskSnapshot, prePaintID, "Pre-Painting", pageNumber);
                            endPage();
                            pageNumber++;
                        }
                    }
                }

                DataSnapshot inventorySnapshot = dataSnapshot.child("inventory");
                // --------------------------------------------- 3. Paint Task ---------------------------------------------
                if (!paintingIDs.isEmpty()) {
                    for (String currentTaskID : paintingIDs) {

                        startPage(pageNumber);
                        createPaintLayout();
                        endPage();
                        pageNumber++;

                        // get the paint used information
                        PaintingTask paintingTask = taskSnapshot.child(currentTaskID).getValue(PaintingTask.class);
                        String paintType = paintingTask.getPaintType();

                        // get the baking information
                        Inventory inventoryItem = inventorySnapshot.child(paintType).child(paintingTask.getPaintCode()).getValue(Inventory.class);

                        // if liquid get liquid info
                        if (paintType.equals("powder")){
                            
                        }

                        // if powder get powder info



                        // Inspection task comments
                        if (taskSnapshot.child(currentTaskID).child("employeeComments").exists()) {
                            createCommentsLayout(taskSnapshot, currentTaskID, "Painting", pageNumber);
                            endPage();
                            pageNumber++;
                        }

                    }

                    // --------------------------------------------- 4. Baking Task ---------------------------------------------

                }

                /*// --------------------------------------------- 5. Packaging Task ---------------------------------------------
                if (!packagingIDs.isEmpty()) {
                    //PackagingTask packagingTask = taskSnapshot.child(sortedTaskIds[3]).getValue(PackagingTask.class);
                    startPage(pageNumber);
                    createPackagingLayout();
                    endPage();
                    pageNumber++;

                    // Inspection task comments
                    if (taskSnapshot.child(sortedTaskIds[3]).child("employeeComments").exists()) {
                        createCommentsLayout(taskSnapshot, sortedTaskIds[3], "Packaging", pageNumber);
                        endPage();
                        pageNumber++;
                    }
                }*/

                // --------------------------------------------- 6. Final Inspection ---------------------------------------------
                if (finalInspectionID != null) {
                    InspectionTask inspectionTask = taskSnapshot.child(finalInspectionID).getValue(InspectionTask.class);
                    startPage(pageNumber);
                    createInspectionLayout(inspectionTask, true);
                    endPage();
                    pageNumber++;

                    // Inspection task comments
                    if (dataSnapshot.child(finalInspectionID).child("employeeComments").exists()) {
                        createCommentsLayout(taskSnapshot, finalInspectionID, "Final Inspection", pageNumber);
                        endPage();
                        pageNumber++;
                    }
                }

                saveFile(projectPO, this);

                // ------------------------------------------------------------------------------------------------------------
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));
    }

    private void measureLayout(View content){
        // measure the layout
        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);
        content.measure(measureWidth, measuredHeight);
        content.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());
    }

    private void saveFile(String projectPO, ValueEventListener taskValueEventListener){
        // get the directory file of the folder the pdfs will be saved in. If the folder is not present, it is created in the External memory
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Pdf Reports/";
        File filePath = new File(directory_path);
        if (!filePath.exists()){
            filePath.mkdirs();
        }
        // --------------------------------------------- 7. saveFile ---------------------------------------------
        try {
            // save the generated file with the given name
            File file = new File(directory_path,  projectPO + "Report.pdf");

            Log.d(TAG, "onStart: location here: " + file.getAbsolutePath());

            outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);
            document.close();
            outputStream.close();

            //also shares pdf
            Uri contentUri = FileProvider.getUriForFile(context, "com.example.fileprovider", file);
            shareDocument(contentUri);

            Toast.makeText(context, "Pdf Generation Successful", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }

        // detatch listeners
        rootRef.removeEventListener(projectValueEventListener);
    }

    private void shareDocument(Uri uri) {
        Intent mShareIntent = new Intent();
        mShareIntent.setAction(Intent.ACTION_SEND);
        mShareIntent.setType("application/pdf");
        // Assuming it may go via eMail:
        mShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Here is a PDF from PdfSend");
        // Attach the PDf as a Uri, since Android can't take it as bytes yet.
        mShareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(mShareIntent);
        return;
    }
}
