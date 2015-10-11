package com.aspsine.edittextinlistview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotePageFragment extends NotePageBaseFragment {

    private static final String EXTRA_PAGE_NUM = "EXTRA_PAGE_NUM";

    private static final String EXTRA_LINES = "EXTRA_LINES";

    private static final int LINE_NUM = 20;

    private ArrayList<Line> mLines;

    private ListView listView;

    private TextView tvPage;

    private LineAdapter mAdapter;

    private int mPageNum;

    public static final NotePageFragment newInstance(int pageNum) {
        NotePageFragment fragment = new NotePageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_PAGE_NUM, pageNum);
        fragment.setArguments(bundle);
        return fragment;
    }

    public NotePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            mPageNum = getArguments().getInt(EXTRA_PAGE_NUM);
            mLines = createLines();
        } else {
            mPageNum = savedInstanceState.getInt(EXTRA_PAGE_NUM);
            mLines = savedInstanceState.getParcelableArrayList(EXTRA_LINES);
        }

        mAdapter = new LineAdapter(mLines);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notepad, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        tvPage = (TextView) view.findViewById(R.id.tvPage);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setAdapter(mAdapter);
        tvPage.setText("- " + String.valueOf(mPageNum) + " -");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_LINES, mLines);
        outState.putInt(EXTRA_PAGE_NUM, mPageNum);
    }

    private ArrayList<Line> createLines() {
        ArrayList<Line> lines = new ArrayList<>();
        for (int i = 0; i < LINE_NUM; i++) {
            Line line = new Line();
            line.setNum(i);
            lines.add(line);
        }
        return lines;
    }

}
