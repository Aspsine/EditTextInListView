# EditTextInListView
Demonstrate how to put EidtText widget in ListView as an item.

## Demo
[Download](https://raw.githubusercontent.com/Aspsine/EditTextInListView/master/art/notepad.apk)

## Code Instruction

[com.aspsine.edittextinlistview.LineAdapter#getView()](https://github.com/Aspsine/EditTextInListView/blob/master/app/src/main/java/com/aspsine/edittextinlistview/LineAdapter.java#L42)

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
	
	// step 1: remove android.text.TextWatcher added in step 5 to make sure android.text.TextWatcher 
	//         don't trigger in step 2;
	// why?
	// 
	// note: When an object of a type is attached to an Editable, 
	//       TextWatcher's methods will be called when the EidtText's text is changed.
	//       
	//       EditText use a ArrayList<TextWatcher> type object to store the listener, so we must
	//       make sure there's only one TextWatcher object in this list;
	// 
	// Avoid triggering TextWatcher's method in step 2 we remove it at first time.
	// 
	if (holder.etLine.getTag() instanceof TextWatcher) {
		holder.etLine.removeTextChangedListener((TextWatcher) (holder.etLine.getTag()));
	}
	
	// step 2: set text and focus after remove android.text.TextWatcher(step 1);
	holder.etLine.setHint(position + ".");
	
	// set text
	if (TextUtils.isEmpty(line.getText())) {
		holder.etLine.setTextKeepState("");
	} else {
		holder.etLine.setTextKeepState(line.getText());
	}
	
	// set focus status
	// why?
	//
	// note: ListView has a very elegant recycle algorithm. So views in ListView is not reliable.
	//       Especially in this case, EditText is an item of ListView. Software input window may cause
	//       ListView's relayout lead adapter's getView() invoke many times.
	//       Above all if we change EditText's focus state directly in EditText level(not in Adapter). 
	//       The focus state may be messed up when the particularly view reused in other position. 
	//       
	//       So using data source control View's state is the core to deal with this problem.	
	if (line.isFocus()) {
		holder.etLine.requestFocus();
	} else {
		holder.etLine.clearFocus();
	}
	
	// step 3: set an OnTouchListener to EditText to update focus status indicator in data source
	// why?
	// 
	// in step 2, we know we must control view state through data source. We use OnTouchListener
	// to watch the state change and update the data source when user move up fingers(ACTION_UP).
	// We don't want to consume the touch event, simply return false in method onTouch().
	holder.etLine.setOnTouchListener(new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				check(position);
			}
			return false;
		}
	});
	
	// step 4: set TextWatcher to EditText to listen text changes in EditText to updating the text in data source
	// why?
	// 
	// again, use data source to control view state.
	// When user edit the text in one EditText item and scroll the ListView. The particularly EditText item will be
	// reuse in adapter's getView(), this may lead text messed up in ListView.
	// How to deal with this problem?
	// Easy! We update the text in data source at the same time when user is editing. TextWatcher is the best way to
	// do this.
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
	
	// step 5: Set watcher as a tag of EditText.
	// so we can remove the same object which was setted to EditText in step 4;
	// Make sure only one callback is attached to EditText
	holder.etLine.setTag(watcher);

	return convertView;
}

/**
 * change focus status in data source
 */
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

