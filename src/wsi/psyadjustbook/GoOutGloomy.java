package wsi.psyadjustbook;

import java.lang.reflect.Field;

import wsi.psy.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;

public class GoOutGloomy extends Activity {

	ExpandableListView expandableList;
	public String[] group = Listitem.parent;
	public String[][] children = Listitem.chiled;
	private OnChildClickListener onChildClick;
	public int getgroupPosition;
	public int getchildPosition;
	Button continuebutton;
	Button restartbutton;
	parameter textparameter = new parameter();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		getgroupPosition = textparameter.groupPosition;
		getchildPosition = textparameter.childPosition;
		
		continuebutton = (Button) findViewById(R.id.continuebutton);
		restartbutton = (Button) findViewById(R.id.restart);
		expandableList = (ExpandableListView) GoOutGloomy.this.findViewById(R.id.expandableListView);
		
		expandableList.setAdapter(new TreeViewAdapter(this));
		registerForContextMenu(expandableList);

		expandableList.setOnChildClickListener(onChildClick);

		expandableList.setOnGroupExpandListener(new OnGroupExpandListener() {

			public void onGroupExpand(int arg0) {
				ExpandableListView expandlistview = (ExpandableListView) findViewById(R.id.expandableListView);
				for (int i = 0; i < group.length; i++) {
					if ((i != arg0) && (expandlistview.isGroupExpanded(i))) {
						expandlistview.collapseGroup(i);
					}
				}
			}
		});

		restartbutton.setOnClickListener(restartbuttonclick);
		continuebutton.setOnClickListener(continuebuttonclick);
	}

	public OnClickListener restartbuttonclick = new OnClickListener() {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(GoOutGloomy.this, Text.class);
			Bundle bundle = new Bundle();
			bundle.putInt("groupPosition", 0);
			bundle.putInt("childPosition", 0);
			intent.putExtra("tag", bundle);
			startActivity(intent);
		}

	};
	public OnClickListener continuebuttonclick = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(GoOutGloomy.this, Text.class);
			Bundle bundle = new Bundle();
			bundle.putInt("groupPosition", getgroupPosition);
			bundle.putInt("childPosition", getchildPosition);
			intent.putExtra("tag", bundle);
			startActivity(intent);
		}

	};

	public parameter extparameter() {
		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("tag"); // ��ȡ��Դ
		parameter mparameter = new parameter();
		mparameter.groupPosition = bundle.getInt("groupPosition");
		mparameter.childPosition = bundle.getInt("childPosition");
		return mparameter;
	}

	public class parameter {
		public int groupPosition;
		public int childPosition;

		public parameter() {
		}
	}

	public class TreeViewAdapter extends BaseExpandableListAdapter {

		@Override
		public void onGroupExpanded(int groupPosition) {
			super.onGroupExpanded(groupPosition);
		}

		private LayoutInflater groupInflater;
		private LayoutInflater childrenInflater;

		public TreeViewAdapter(Context c) {
			this.groupInflater = LayoutInflater.from(c);
			this.childrenInflater = LayoutInflater.from(c);
		}

		public Object getChild(int groupPosition, int childPosition) {
			return childPosition;
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			parent.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			View myView = childrenInflater.inflate(R.layout.children, null);

			TextView textview = (TextView) myView.findViewById(R.id.name);
			textview.setText(children[groupPosition][childPosition]);
			textview.setTextColor(Color.GRAY);
			
			myView.setOnClickListener(new OnClickListener() {
				public void onClick(View myView) {
					Intent intent = new Intent();
					intent.setClass(GoOutGloomy.this, Text.class);
					Bundle bundle = new Bundle();
					bundle.putInt("groupPosition", groupPosition);
					bundle.putInt("childPosition", childPosition);
					intent.putExtra("tag", bundle);
					startActivityForResult(intent, 1);
				}
			});
			
			myView.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						Log.i("ACTION_DOWN", children[groupPosition][childPosition]);
						return true;
					}
					if (event.getAction() == MotionEvent.ACTION_UP) {
						Intent intent = new Intent();
						intent.setClass(GoOutGloomy.this, Text.class);
						Bundle bundle = new Bundle();
						bundle.putInt("groupPosition", groupPosition);
						bundle.putInt("childPosition", childPosition);
						intent.putExtra("tag", bundle);
						startActivityForResult(intent, 1);
						return true;
					}
					return false;
				}
			});

			return myView;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public Object getGroup(int groupPosition) {
			return "group";
		}

		public int getGroupCount() {
			return group.length;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View myView = groupInflater.inflate(R.layout.group, null);

			TextView textview = (TextView) myView.findViewById(R.id.groupName);
			textview.setTextColor(Color.GRAY);
			textview.setText(group[groupPosition]);
			return myView;
		}

		public boolean hasStableIds() {
			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	public static int getRid(String src) {
		int id = 0;
		try {
			Field field = R.xml.class.getField(src);
			id = field.getInt(new R.xml());
		} catch (Exception e) {
			Log.e("icon", e.toString());
		}

		return id;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) { // requestCode

			if (resultCode == RESULT_OK) { // resultCode

				Bundle bundle = data.getBundleExtra("ood");
				getgroupPosition = bundle.getInt("groupPosition");
				getchildPosition = bundle.getInt("childPosition");
				System.out.println(getgroupPosition);
				System.out.println(getchildPosition);

			}
		}
	}

}