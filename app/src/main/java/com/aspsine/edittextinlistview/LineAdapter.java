package com.aspsine.edittextinlistview;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.List;

/**
 * Created by aspsine on 15/10/11.
 */
public class LineAdapter extends BaseAdapter {

    private List<Line> lines;

    public LineAdapter(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public int getCount() {
        return lines.size();
    }

    @Override
    public Line getItem(int position) {
        return lines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_line, parent, false);
            holder.etLine = (EditText) convertView.findViewById(R.id.etLine);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Line line = lines.get(position);

        if (holder.etLine.getTag() instanceof TextWatcher) {
            holder.etLine.removeTextChangedListener((TextWatcher) (holder.etLine.getTag()));
        }

        holder.etLine.setHint(position + ".");

        if (TextUtils.isEmpty(line.getText())) {
            holder.etLine.setText("");
        } else {
            holder.etLine.setText(line.getText());
        }

        if (line.isFocus()) {
            if (!holder.etLine.isFocused()) {
                holder.etLine.requestFocus();
            }
            CharSequence text = line.getText();
            holder.etLine.setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
            // 使光标闪烁
            holder.etLine.setCursorVisible(true);
        } else {
            if (holder.etLine.isFocused()) {
                holder.etLine.clearFocus();
            }
            holder.etLine.setCursorVisible(false);
        }

        holder.etLine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final boolean focus = line.isFocus();
                    check(position);
                    if (!focus && !holder.etLine.isFocused()) {
                        holder.etLine.requestFocus();
                        holder.etLine.onWindowFocusChanged(true);
                    }
                }
                return false;
            }
        });

        final TextWatcher watcher = new SimpeTextWather() {

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    line.setText(null);
                } else {
                    line.setText(String.valueOf(s));
                }
            }
        };
        holder.etLine.addTextChangedListener(watcher);
        holder.etLine.setTag(watcher);

        // 第6步：设置OnFocusChangeListener当EditText获取焦点后重新设置光标visible为true。
        //
        // 通过以上5步虽然可以解决焦点混乱的问题，但是在EditText的Text内容为空时
        // 当前获取到焦点的EditText不一定能显示光标。
        // 确保只有获取到焦点的view的CursorVisible为true。
        holder.etLine.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    holder.etLine.setCursorVisible(true);
                } else {
                    holder.etLine.setCursorVisible(false);
                }
            }
        });
        return convertView;
    }

    private void check(int position) {
        for (Line l : lines) {
            l.setFocus(false);
        }
        lines.get(position).setFocus(true);
    }

    static class ViewHolder {
        EditText etLine;
    }
}
