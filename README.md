# EditTextInListView
Demonstrate how to put EidtText widget in ListView as an item.

## Demo
[Download](https://raw.githubusercontent.com/Aspsine/EditTextInListView/master/art/notepad.apk)

## Code Instruction
[com.aspsine.edittextinlistview.LineAdapter](https://github.com/Aspsine/EditTextInListView/blob/master/app/src/main/java/com/aspsine/edittextinlistview/LineAdapter.java#L42)

```java

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
	if (holder.etLine.getTag() instanceof TextWatcher) {
		holder.etLine.removeTextChangedListener((TextWatcher) (holder.etLine.getTag()));
	}

	holder.etLine.setHint(position + ".");

	if (TextUtils.isEmpty(line.getText())) {
		holder.etLine.setTextKeepState("");
	} else {
		holder.etLine.setTextKeepState(line.getText());
	}

	if (line.isFocus()) {
		holder.etLine.requestFocus();
	} else {
		holder.etLine.clearFocus();
	}

	holder.etLine.setOnTouchListener(new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				check(position);
			}
			return false;
		}
	});

	holder.etLine.addTextChangedListener(watcher);
	holder.etLine.setTag(watcher);

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

```

## ScreenShot

![Cover](https://raw.githubusercontent.com/Aspsine/EditTextInListView/master/art/1.jpg)
![Cover](https://raw.githubusercontent.com/Aspsine/EditTextInListView/master/art/2.jpg)
![Cover](https://raw.githubusercontent.com/Aspsine/EditTextInListView/master/art/4.jpg)
![Cover](https://raw.githubusercontent.com/Aspsine/EditTextInListView/master/art/3.jpg)

