package com.example.dell.moviesapplication;

        import android.content.Intent;
        import android.graphics.Color;
        import android.os.Bundle;

        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;

        import com.jjoe64.graphview.DefaultLabelFormatter;
        import com.jjoe64.graphview.GraphView;
        import com.jjoe64.graphview.ValueDependentColor;
        import com.jjoe64.graphview.series.BarGraphSeries;
        import com.jjoe64.graphview.series.DataPoint;
        import com.jjoe64.graphview.series.LineGraphSeries;

        import java.text.NumberFormat;
        import java.util.ArrayList;
        import java.util.List;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        Intent intent = getIntent();

//        List<String> newList = new ArrayList<>();
        int patients = 0;
        for(int i=0;i<intent.getExtras().size();i++){
            if(intent.getExtras()!=null) {
                patients = (int) intent.getExtras().get("SIZE");
            }
        }

        initChart(patients, 1);
    }

    private void initChart(int patients, int doctors){

        GraphView graph = (GraphView) findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, patients),
                new DataPoint(1, doctors)
        });
        graph.addSeries(series);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });


        graph.getViewport().setMinX((int)0);
        graph.getViewport().setMinY((int)0);


        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        series.setSpacing(50);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
    }

}