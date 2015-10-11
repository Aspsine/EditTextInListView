package com.aspsine.edittextinlistview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import java.util.LinkedList;
import java.util.List;


public class NotePadActivity extends AppCompatActivity {

    private static final int PAGE_NUM = 20;

    private List<NotePageBaseFragment> mNotePages;

    private PageController mController;

    private ViewPager viewPager;

    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pad);
        initNotePad();
        initPager();
        initSeekBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Select Page");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == Menu.NONE) {
            if (seekBar.getVisibility() == View.VISIBLE){
                seekBar.setVisibility(View.GONE);
            }else {
                seekBar.setVisibility(View.VISIBLE);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initNotePad() {
        mNotePages = new LinkedList<>();
        NoteCoverFragment cover = new NoteCoverFragment();
        mNotePages.add(cover);
        for (int i = 0; i < PAGE_NUM; i++) {
            NotePageFragment page = NotePageFragment.newInstance(i);
            mNotePages.add(page);
        }
        NoteEndCoverFragment endCover = new NoteEndCoverFragment();
        mNotePages.add(endCover);
    }

    private void initPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mController = new PageController(getSupportFragmentManager());
        viewPager.setAdapter(mController);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                seekBar.setProgress(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initSeekBar() {
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setVisibility(View.GONE);
        seekBar.setMax(mController.getCount()-1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            boolean drag = false;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (viewPager.getCurrentItem() != progress && drag) {
                    viewPager.setCurrentItem(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                drag = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                drag = false;
            }
        });
    }

    class PageController extends FragmentStatePagerAdapter {

        public PageController(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mNotePages.get(position);
        }

        @Override
        public int getCount() {
            return mNotePages.size();
        }
    }
}
