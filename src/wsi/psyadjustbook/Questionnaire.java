package wsi.psyadjustbook;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import wsi.psy.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Gallery.LayoutParams;

public class Questionnaire extends Activity {

	List<question> mQuestionlist = new ArrayList<question>();
	List<QandA> answeredList = new ArrayList<QandA>();
	public int questionsize;
	public int currentposition;
	public int state;

	public int groupPosition;
	public int childPosition;
	public String[] group = Listitem.parent;
	public String[][] children = Listitem.chiled;
	int groupsize = group.length;
	int childsize;

	public int answering = 1;
	public int uncomplish = 2;
	public int complish = 3;

	private int childrennum = 0;
	private int currentprogress = 0;
	private QuestionDataBaseAdapter mQuestionDataBaseAdapter;
	public List<intclass> uncomplishlist = new ArrayList<intclass>();
	int startposition = 0;
	int endposition;
	ImageButton home;
	ImageButton arrowleft;
	ImageButton arrowright;
	public ProgressBar progressbar;
	public Intent Qintent;
	public Context mcontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.questionnaire);
		Qintent = getIntent();
		initview();
		startexam();

	}

	public void initview() {
		this.home = (ImageButton) findViewById(R.id.homequestion);
		this.arrowleft = (ImageButton) findViewById(R.id.arrowleftquestion);
		this.arrowright = (ImageButton) findViewById(R.id.arrowrightquestion);
		this.progressbar = (ProgressBar) findViewById(R.id.progressBarquestion1);

		this.home.setOnTouchListener(hemotouch);
		this.arrowleft.setOnTouchListener(arrowlefttouch);
		this.arrowright.setOnTouchListener(arrowrighttouch);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		TableRow.LayoutParams params = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.width = screenWidth - 150;
		params.gravity = Gravity.CENTER_VERTICAL;
		this.progressbar.setLayoutParams(params);

	}

	public void startexam() {
		int srcid = extparameter();
		childsize = children[groupPosition].length;
		Log.i("child size", Integer.toString(childsize));
		mQuestionlist = DecodeXMLquestion.decodexmlquestion(srcid);
		questionsize = mQuestionlist.size();
		Log.i("question size", Integer.toString(questionsize));
		getchildrennum();  			//设置进度条的最大值
		this.progressbar.setMax(childrennum);

		getcurrentprogress(); 		//设置进度条当前的值
		this.progressbar.setProgress(currentprogress);

		if (questionsize > 0) {
			state = answering; 		// 设置状态   
			endposition = questionsize - 1;
			currentposition = 0;
			createQuestionnaire(mQuestionlist.get(0)); 		// 开始做第一题
		}
	}

	public void getchildrennum() {
		for (int i = 0; i < Listitem.chiled.length; i++)
			childrennum = childrennum + Listitem.chiled[i].length;
	}

	public OnTouchListener hemotouch = new OnTouchListener() {

		public boolean onTouch(View arg0, MotionEvent arg1) {
			returnbaseactivity("home");		// 返回 做题目处
			return false;
		}
	};
	
	public OnTouchListener arrowlefttouch = new OnTouchListener() {

		public boolean onTouch(View arg0, MotionEvent arg1) {
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				if ((currentposition > startposition) && (state == answering)
						&& currentposition < endposition) {
					currentposition = currentposition - 1;
					createQuestionnaire(mQuestionlist.get(currentposition));
				} else if ((currentposition > startposition + 1)
						&& (state == uncomplish)
						&& currentposition < endposition) { 				//调回 原来的题目
					currentposition = currentposition - 1;
					createQuestionnaire(mQuestionlist.get(uncomplishlist
							.get(currentposition).num));					//取下一道题
				} else {
					uncomplishlist = checkunselected(answeredList); 		//检查问卷完成情况
					if (state == complish) {
						saveresulttodb(); 		//题目 已全部 做完  ，跳转  回原处 。保存做题
						returnbaseactivity("left");

					} else if (state == uncomplish) {
						startposition = 0;
						endposition = uncomplishlist.size(); 					//重新设置开始的位置值
						currentposition = endposition - 1; 						//重新定位当前的值
						for (int i = 0; i < uncomplishlist.size(); i++) { 		//显示尚未完成的题目信息。
						}
						createQuestionnaire(mQuestionlist.get(uncomplishlist.get(currentposition).num));
					}
				}
			}

			return false;
		}
	};
	
	public OnTouchListener arrowrighttouch = new OnTouchListener() {

		public boolean onTouch(View arg0, MotionEvent arg1) {
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				if ((currentposition < endposition - 1) && (state == answering)) {
					currentposition = currentposition + 1;
					createQuestionnaire(mQuestionlist.get(currentposition));
				} else if ((currentposition < endposition - 1)
						&& (state == uncomplish)) {
					currentposition = currentposition + 1;
					createQuestionnaire(mQuestionlist.get(uncomplishlist
							.get(currentposition).num));
				} else {
					uncomplishlist = checkunselected(answeredList);
					if (state == complish) {
						saveresulttodb(); 					//将结果 保存到数据库中
						returnbaseactivity("right");		//返回 做题目处
						
					} else if (state == uncomplish) {
						currentposition = 0;
						startposition = 0;
						endposition = uncomplishlist.size() - 1;
						for (int i = 0; i < uncomplishlist.size(); i++) {
						}
						createQuestionnaire(mQuestionlist.get(uncomplishlist.get(0).num));
					}
				}
			}

			return false;
		}
	};

	public void returnbaseactivity(String tag) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);
		intent.putExtras(bundle);
		Questionnaire.this.setResult(RESULT_OK, intent); 	// 返回给A的发送参数
		Questionnaire.this.finish();				 		// 必须在setResult后关闭
	}

	public int extparameter() {
		Bundle bundle = Qintent.getBundleExtra("tag"); 		// 获取资源
		int srcid = bundle.getInt("srcid");
		groupPosition = bundle.getInt("groupPosition");
		Log.i("input groupposition", Integer.toString(groupPosition));
		childPosition = bundle.getInt("childPosition");
		Log.i("input childposition", Integer.toString(childPosition));
		return srcid;
	}

	public class parameter {
		public int groupPosition;
		public int childPosition;

		public parameter() {
		}
	}

	public void createQuestionnaire(final question newquestion) {

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout linearlayoutquestion = (LinearLayout) findViewById(R.id.LinearLayoutquestion1);
		linearlayoutquestion.setGravity(17);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;

		final RadioGroup mradiogroup = (RadioGroup) findViewById(R.id.radioGroupquestion1);
		if (screenWidth > screenHeight) {
			mradiogroup.setOrientation(0); // 1 is horizontal 0 is vertical， default is vertical
		}
		
		mradiogroup.setGravity(17);
		mradiogroup.removeViews(0, mradiogroup.getChildCount());
		mradiogroup.setClickable(true);
		TextView objectview = (TextView) findViewById(R.id.objectquestion);
		objectview.setTextColor(Color.GRAY);
		objectview.setTextSize((float) 20.0);
		objectview.setText(newquestion.subject);
		TextView questionprogress = (TextView) findViewById(R.id.questionprogress);
		questionprogress.setText(Integer.toString(currentposition) + " / " + Integer.toString(endposition));	// 1 - 19 调查问卷第一页（题）
		final QandA tempqanda = new QandA();
		for (int index = 0; index < newquestion.obtions.size(); index++) {
			RadioButton radio = new RadioButton(Text.mText);
			radio.setText(newquestion.obtions.get(index));
			radio.setTextColor(Color.GRAY);
			radio.setTextSize((float) 18.0);
			radio.setId(index);
			radio.setClickable(true);
			mradiogroup.addView(radio, params);
			mradiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
						public void onCheckedChanged(RadioGroup arg0, int arg1) {
							int id = arg0.getCheckedRadioButtonId();
							String selected = newquestion.obtions.get(id);
							if (state == uncomplish) {
								tempqanda.subjectID = uncomplishlist.get(currentposition).num;
							} else {
								tempqanda.subjectID = currentposition;
							}
							
							tempqanda.subject = newquestion.subject;
							tempqanda.selected = selected;
							tempqanda.selectedNum = id + 1;
							recordselectresult(tempqanda, answeredList); 		// 将做题结果添加做题队列中
//							Toast.makeText(Questionnaire.this, "你选择了:" + tempqanda.selected, Toast.LENGTH_SHORT).show();
						}
					});
		}
	}

	public class QandA {
		public int subjectID;
		public String subject = null;
		public String selected = null;
		public int selectedNum;

		public QandA() {
		}

		public QandA(int id, String subject, String selected, int selectednum) {
			this.subjectID = id;
			this.subject = subject;
			this.selected = selected;
			this.selectedNum = selectednum;
		}
	}

	public void recordselectresult(QandA newA, List<QandA> answerlist) {
		boolean isinanswerlist = false;
		for (int i = 0; i < answerlist.size(); i++) {

			if (newA.subjectID == answerlist.get(i).subjectID) {
				answerlist.get(i).selected = newA.selected;
				isinanswerlist = true;
			}
		}
		if (!isinanswerlist) {
			answerlist.add(newA);

		}
	}

	public List<intclass> checkunselected(List<QandA> selected) {
		List<intclass> tempunchecked = new ArrayList<intclass>();

		if (selected.size() < questionsize) {
			intclass temint = new intclass();
			for (int i = 0; i < questionsize; i++) {
				boolean isin = false;
				for (int j = 0; j < selected.size(); j++) {
					if (selected.get(j).subjectID == i) {
						isin = true;
					}
				}
				if (isin == false) {
					state = uncomplish; 
					temint = new intclass(i);
					tempunchecked.add(temint);
				}
			}
		} else {	// 需要重新做题
			state = complish; 	// 全部题目已做完
		}
		return tempunchecked;
	}

	public class intclass {
		int num;

		public intclass() {
		};

		public intclass(int input) {
			this.num = input;
		};
	}

	public void getNextparameter(String buttonname) {
		if (buttonname == "arrowleft") {
			if (childPosition == 0 && groupPosition > 0) {
				groupPosition = groupPosition - 1;
				childPosition = children[groupPosition].length - 1;
			} else if (childPosition == 0 && groupPosition == 0) {
				//回到起始页面
			} else {
				childPosition = childPosition - 2;
			}
		} else if (buttonname == "arrowright") {
			if (childPosition == (childsize - 1)
					&& groupPosition == (groupsize - 1)) {

			} else if (childPosition == (childsize - 1)
					&& groupPosition < groupsize) {
				childPosition = 0;
				groupPosition = groupPosition + 1;

			} else {
				childPosition = childPosition + 1;
			}
		}
	}

	public void getcurrentprogress() {
		currentprogress = 0;
		for (int i = 0; i < groupPosition; i++)
			currentprogress = currentprogress + Listitem.chiled[i].length;
		currentprogress = currentprogress + childPosition + 1;
	}

	private void saveresulttodb() {
		mQuestionDataBaseAdapter = new QuestionDataBaseAdapter(this);
		JSONObject qandajson = new JSONObject();
		QandA tempQandA = new QandA();
		int record = 0;
		for (int i = 0; i < answeredList.size(); i++) {
			tempQandA = answeredList.get(i);
			record += tempQandA.selectedNum;
			try {
				qandajson.put(Integer.toString(tempQandA.subjectID), tempQandA.selected);
			} catch (JSONException e) {
			}
		}

		System.out.println("the record is" + Integer.toString(record));
		mQuestionDataBaseAdapter.createEntry(qandajson, record);
		mQuestionDataBaseAdapter.flushDb();
		mQuestionDataBaseAdapter.close();
	}

}
