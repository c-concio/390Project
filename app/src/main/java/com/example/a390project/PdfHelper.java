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
import com.example.a390project.Model.EmployeeComment;
import com.example.a390project.Model.Task;
import com.example.a390project.Model.TaskClasses.InspectionTask;
import com.example.a390project.Model.TaskClasses.PackagingTask;
import com.example.a390project.Model.TaskClasses.PaintingTask;
import com.example.a390project.Model.TaskClasses.PrePaintingTask;
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
        TextView receivedTextView = content.findViewById(R.id.countedTextView);
        TextView acceptedTextView = content.findViewById(R.id.acceptedTextView);
        TextView rejectedTextView = content.findViewById(R.id.rejectedTextView);

        // set the text for the views
        receivedTextView.setText(String.valueOf(inspectionTask.getPartCounted()));
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
    private void createPrePaintLayout(){
        // get the layout view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_prepaint_layout, null);

        // find views for the prepaint values
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

        rootRef.child("projects").child(projectPO).child("tasks").addValueEventListener(projectValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get all the keys associated with the project
                final List<String> taskIds = new ArrayList<>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    taskIds.add(postSnapshot.getKey());
                }
                Log.d(TAG, "onDataChange: Task keys checked");

                final List<String> finalTaskIds = taskIds;

                rootRef.child("tasks").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        // check the task types of each task to see which tasks will need to be printed on the pdf
                        Boolean taskTypesInProject[] = {false, false, false, false, false};
                        String sortedTaskIds[] = {null, null, null, null, null};
                        for (String taskId : finalTaskIds){
                            DataSnapshot currentSnapshot = dataSnapshot.child(taskId);
                            String taskType = currentSnapshot.child("taskType").getValue(String.class);

                            Log.d(TAG, "onDataChange: task type " + taskId);
                            // check taskType and set boolean array to true
                            checkTaskType(taskTypesInProject, sortedTaskIds, taskType, taskId);
                        }

                        int pageNumber = 0;


                        // --------------------------------------------- 0. Inspection Task ---------------------------------------------
                        // if inspection task is available, create the inspection page
                        if (taskTypesInProject[0]){
                            InspectionTask inspectionTask = dataSnapshot.child(sortedTaskIds[0]).getValue(InspectionTask.class);
                            if (inspectionTask != null) {
                                startPage(pageNumber);
                                createInspectionLayout(inspectionTask, false);
                                endPage();
                                pageNumber++;

                                // Inspection task comments
                                if (dataSnapshot.child(sortedTaskIds[0]).child("employeeComments").exists()) {
                                    createCommentsLayout(dataSnapshot, sortedTaskIds[0], "Inspection", pageNumber);
                                    endPage();
                                    pageNumber++;
                                }
                            }
                        }

                        // --------------------------------------------- 2. Pre-paint Task ---------------------------------------------
                        if (taskTypesInProject[1]){
                            PrePaintingTask prePaintingTask = dataSnapshot.child(sortedTaskIds[1]).getValue(PrePaintingTask.class);
                            startPage(pageNumber);
                            createPrePaintLayout();
                            endPage();
                            pageNumber++;

                            // Inspection task comments
                            if (dataSnapshot.child(sortedTaskIds[1]).child("employeeComments").exists()) {
                                createCommentsLayout(dataSnapshot, sortedTaskIds[1], "Pre-Painting", pageNumber);
                                endPage();
                                pageNumber++;
                            }
                        }


                        // --------------------------------------------- 3. Paint Task ---------------------------------------------
                        if (taskTypesInProject[2]){
                            PaintingTask paintingTask = dataSnapshot.child(sortedTaskIds[2]).getValue(PaintingTask.class);
                            startPage(pageNumber);
                            createPaintLayout();
                            endPage();
                            pageNumber++;

                            // Inspection task comments
                            if (dataSnapshot.child(sortedTaskIds[2]).child("employeeComments").exists()) {
                                createCommentsLayout(dataSnapshot, sortedTaskIds[2], "Painting", pageNumber);
                                endPage();
                                pageNumber++;
                            }

                            // --------------------------------------------- 4. Baking Task ---------------------------------------------

                        }

                        // --------------------------------------------- 5. Packaging Task ---------------------------------------------
                        if (taskTypesInProject[3]){
                            //PackagingTask packagingTask = dataSnapshot.child(sortedTaskIds[3]).getValue(PackagingTask.class);
                            startPage(pageNumber);
                            createPackagingLayout();
                            endPage();
                            pageNumber++;

                            // Inspection task comments
                            if (dataSnapshot.child(sortedTaskIds[3]).child("employeeComments").exists()) {
                                createCommentsLayout(dataSnapshot, sortedTaskIds[3], "Packaging", pageNumber);
                                endPage();
                                pageNumber++;
                            }
                        }

                        // --------------------------------------------- 6. Final Inspection ---------------------------------------------
                        if (taskTypesInProject[4]){
                            InspectionTask inspectionTask = dataSnapshot.child(sortedTaskIds[4]).getValue(InspectionTask.class);
                            startPage(pageNumber);
                            createInspectionLayout(inspectionTask, true);
                            endPage();
                            pageNumber++;

                            // Inspection task comments
                            if (dataSnapshot.child(sortedTaskIds[4]).child("employeeComments").exists()) {
                                createCommentsLayout(dataSnapshot, sortedTaskIds[4], "Final Inspection", pageNumber);
                                endPage();
                                pageNumber++;
                            }
                        }

                        saveFile(projectPO, this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void measureLayout(View content){
        // measure the layout
        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);
        content.measure(measureWidth, measuredHeight);
        content.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());
    }

    // function checks what type of task is given
    private void checkTaskType(Boolean[] tasksTypesInProject, String[] sortedTaskIds, String taskType, String taskId){
        switch(taskType){
            case ("Inspection"):
                tasksTypesInProject[0] = true;
                sortedTaskIds[0] = taskId;
                break;
            case ("Pre-Painting"):
                tasksTypesInProject[1] = true;
                sortedTaskIds[1] = taskId;
                break;
            case ("Painting"):
                tasksTypesInProject[2] = true;
                sortedTaskIds[2] = taskId;
                break;
            case ("Packaging"):
                tasksTypesInProject[3] = true;
                sortedTaskIds[3] = taskId;
                break;
            case ("Final-Inspection"):
                tasksTypesInProject[4] = true;
                sortedTaskIds[4] = taskId;
                break;
        }
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
        rootRef.child("projects").child(projectPO).child("tasks").removeEventListener(projectValueEventListener);
        rootRef.child("tasks").removeEventListener(taskValueEventListener);
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
