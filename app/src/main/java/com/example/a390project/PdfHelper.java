package com.example.a390project;

import android.app.Activity;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class PdfHelper {

    // variables
    private PdfDocument document;
    private int canvasHeight;
    private int canvasWidth;
    private PdfDocument.PageInfo pageInfo;
    private PdfDocument.Page page;

    private OutputStream os;
    private Context context;

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
    }

    // start a page with the specified page number
    void startPage(int pageNumber){
        pageInfo = new PdfDocument.PageInfo.Builder(canvasWidth, canvasHeight, pageNumber).create();
        page = document.startPage(pageInfo);
    }

    // header (outputs info of project and customer

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

        // measure the layout
        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);
        content.measure(measureWidth, measuredHeight);
        content.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());

        content.draw(page.getCanvas());
        document.finishPage(page);
    }

    // prepaint task


    // paint task
    public void createPaintLayout(){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_paint_layout, null);

        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);
        content.measure(measureWidth, measuredHeight);
        content.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());

        content.draw(page.getCanvas());
        document.finishPage(page);

    }

    // baking
    public void createBakingLayout(){

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.pdf_baking_layout, null);

        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);
        content.measure(measureWidth, measuredHeight);
        content.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());

        content.draw(page.getCanvas());
        document.finishPage(page);

    }

    // comment takes in the taskId


    // generate pdf
    public void generatePdf(){
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";

        Log.d(TAG, "onStart: location: " + directory_path);
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


}
