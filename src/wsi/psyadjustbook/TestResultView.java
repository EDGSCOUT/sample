package wsi.psyadjustbook;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class TestResultView extends View {
	private Context context;
	private List<record> recordlist = new ArrayList<record>(); // 传入的答题记录

	private int startnum = 0;
	private int sumnum = 0;

	public Point startPoint = new Point(0, 0);
	public Point movePoint = new Point(0, 0);
	public Point upPoint = new Point(0, 0);

	public boolean touchdown = false;
	public boolean touchup = false;
	public boolean touchmove = false;

	public float[] currentdisplaypts;

	public TestResultView(Context context) {
		super(context);
		this.context = context;
		readresultfromdb(context);
		sumnum = recordlist.size();

	}

	public TestResultView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TestResultView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setstartPoint(Point startpoint) {
		this.startPoint = startpoint;
	}

	public void setmovePoint(Point movepoint) {
		this.movePoint = movepoint;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measuredHeight = measureHeight(heightMeasureSpec);
		int measuredWidth = measureWidth(widthMeasureSpec);
		setMeasuredDimension(measuredWidth, measuredHeight);
	}

	private int measureWidth(int widthMeasureSpec) {
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		int result = 200;
		if (specMode == MeasureSpec.AT_MOST) {
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		return result;
	}

	private int measureHeight(int heightMeasureSpec) {
		int specMode = MeasureSpec.getMode(heightMeasureSpec);
		int specSize = MeasureSpec.getSize(heightMeasureSpec);
		int result = 300;
		if (specMode == MeasureSpec.AT_MOST) {
			result = specSize;
		} else if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int height = getMeasuredHeight();
		int width = getMeasuredWidth();

		Paint edgepaint = new Paint();  // 绘制边缘
		edgepaint.setColor(Color.GRAY);
		canvas.drawLine(2, 2, 2, height - 3, edgepaint);
		canvas.drawLine(2, 2, width - 3, 2, edgepaint);
		canvas.drawLine(2, height - 3, width - 3, height - 3, edgepaint);
		canvas.drawLine(width - 3, 2, width - 3, height - 3, edgepaint);

		Paint titlepaint = new Paint();
		String title = "抑郁测试分数变化趋势";
		titlepaint.setTextSize(15); 
		titlepaint.setColor(Color.GRAY);
		float titlewidth = titlepaint.measureText(title);
		int titlestartX = (width - (int) titlewidth) / 2 + 30; // 向左移动 30
		int titlestartY = 31;
		canvas.drawText(title, titlestartX, titlestartY, titlepaint);

		Paint inedgepaint = new Paint(); //绘制边缘
		inedgepaint.setColor(Color.GRAY);
		canvas.drawLine(40, 40, 40, height - 40, inedgepaint); // | left
		canvas.drawLine(40, 40, width - 30, 40, inedgepaint); // - top
		canvas.drawLine(40, height - 40, width - 30, height - 40, inedgepaint); // -
																				// bottom
		canvas.drawLine(width - 30, 40, width - 30, height - 40, inedgepaint); // |
																				// right

		int Yspace = (height - 80) / 10;
		int endYx = 43;
		int startYy = 40; //实际坐标 为100
		int startYx = 37;

		for (int i = 0; i < 11; i++) {
			Paint Ypaint = new Paint(); //绘制 Y 轴 
			Ypaint.setColor(Color.GRAY);
			canvas.drawLine(startYx, startYy + i * Yspace, endYx, startYy + i
					* Yspace, Ypaint);
			switch (i) {
			case 0:
				drawlabelnum(13, startYy + i * Yspace + 5, "1.0", canvas, 10);
				break;// paint0
			case 5:
				drawlabelnum(13, startYy + i * Yspace + 5, "0.5", canvas, 10);
				break;// paint0.5
			case 10:
				drawlabelnum(13, startYy + i * Yspace + 5, "0.0", canvas, 10);
				break;// paint1.0/
			default:
				break;//

			}

		}

		int linenum = (width - 80) / (Yspace * 2);
		int Xspace = Yspace * 2;
		int startXx = 40;
		int startXy = height - 40 - 3;
		int endXy = height - 40 + 3;

		dealpoint(
				linenum, //显示点 ，因为起始点的位置，只能这里显示，显得有点乱。
				startPoint, movePoint, (float) (height - 80) / (float) 100,
				Xspace, 40 + Xspace, height, canvas);

		for (int i = 0; i < linenum + 1; i++) {
			if (startXx + i * Yspace * 2 >= width - 30) {
				break;
			}
			Paint Xpaint = new Paint(); // 绘制 X 轴
			Xpaint.setColor(Color.GRAY);
			canvas.drawLine(startXx + i * Yspace * 2, startXy, startXx + i
					* Yspace * 2, endXy, Xpaint);
			drawlabelnum(startXx + i * Yspace * 2 - 6, endXy + 15,
					Integer.toString(i + startnum), canvas, 12);
		}

		int xlabeltextsize = 12;
		int xlabelx = width - xlabeltextsize - 30 + 6;
		int xlabely = height - 20;

		Paint xlablePaint = new Paint(Paint.ANTI_ALIAS_FLAG); // x label
		xlablePaint.setColor(Color.GRAY);
		String displayText = "次数";
		xlablePaint.setTextSize((float) xlabeltextsize);
		canvas.drawText(displayText, xlabelx, xlabely, xlablePaint);

		int ylabeltextsize = 12;
		int ylabelx = 40 - 12;
		int ylabely = 40 - 12;
		String ylable = "分数";

		Paint ylablePaint = new Paint(Paint.ANTI_ALIAS_FLAG); // ylabel
		ylablePaint.setColor(Color.GRAY);
		ylablePaint.setTextSize((float) ylabeltextsize);
		canvas.drawText(ylable, ylabelx, ylabely, ylablePaint);

		super.onDraw(canvas);
	}

	private void drawlabelnum(int x, int y, String num, Canvas canvas, int arg0) {
		Paint labelnum = new Paint();
		labelnum.setColor(Color.GRAY);
		labelnum.setTextSize(arg0);
		canvas.drawText(num, x, y, labelnum);

	}

	public void dealpoint(int linenum, // ÿһ����ʾ�ĵ���
			Point startpoint, // ��ʼ�ƶ��ĵ�
			Point movepoint, // ��ֹ�ƶ��ĵ�
			float radio, // ���� width-80/100
			int xspace, int x, // first x label
			int y, Canvas canvas) // first y label
	{
		if (((upPoint.x - startPoint.x) > 15

		) // �����ƶ� ��ʾ ��벿��
				&& (linenum < sumnum) && startnum > 0) {

			startnum -= 3;
			if (startnum >= 0) // ʣ����㹻��ʾ
			{
				float[] tempcirclepts = new float[linenum * 2];
				for (int i = startnum, j = 0; i < startnum + linenum; i++, j++) {
					tempcirclepts[2 * j] = 40 + xspace * j;
					tempcirclepts[2 * j + 1] = y
							- (recordlist.get(i).recodnum * radio + 40);

				}
				drawpoint(tempcirclepts, canvas);
				currentdisplaypts = tempcirclepts;
				// startnum=startnum-3;
			} else // ʣ��Ĳ�����ʾ
			{
				startnum = 0;
				float[] tempcirclepts = new float[linenum * 2];
				for (int i = 0; i < linenum; i++) {
					tempcirclepts[2 * i] = x + xspace * i;
					tempcirclepts[2 * i + 1] = y
							- (recordlist.get(i).recodnum * radio + 40);
				}
				drawpoint(tempcirclepts, canvas);
				currentdisplaypts = tempcirclepts; // ��¼��ǰ��ʾ������
			}

			// touchmove=false;
			// touchdown=false;
			// touchup=false;
		} else if (((startPoint.x - upPoint.x) > 15) // ���� �ƶ� ��ʾ �Ұ벿��
				&& (linenum < sumnum) && (startnum < sumnum) // ʣ�����������
																// Ҫ��ʾ������
		) {
			startnum += 2;

			if (sumnum - (startnum + linenum) > 0) // ʣ����㹻��ʾ
			{
				float[] tempcirclepts = new float[linenum * 2];
				for (int i = startnum, j = 0; i < startnum + linenum; i++, j++) {
					tempcirclepts[2 * j] = 40 + xspace * j;
					tempcirclepts[2 * j + 1] = y
							- (recordlist.get(i).recodnum * radio + 40);
				}
				drawpoint(tempcirclepts, canvas);
				currentdisplaypts = tempcirclepts; // ��¼��ǰ��ʾ������
				// startnum=startnum+1;
			} else // ʣ��Ĳ�����ʾ
			{
				startnum = sumnum - linenum;
				float[] tempcirclepts = new float[linenum * 2];
				for (int i = startnum, j = 0; i < linenum + startnum; i++, j++) {
					tempcirclepts[2 * j] = 40 + xspace * j;
					tempcirclepts[2 * j + 1] = y
							- (recordlist.get(i).recodnum * radio + 40);
				}
				drawpoint(tempcirclepts, canvas);
				currentdisplaypts = tempcirclepts; // ��¼��ǰ��ʾ������
			}

		} else if (sumnum > linenum) {
			startnum = 0;
			float[] tempcirclepts = new float[linenum * 2];
			for (int i = 0, j = 0; i < linenum; i++, j++) {
				tempcirclepts[2 * j] = x + xspace * j;
				tempcirclepts[2 * j + 1] = y
						- (recordlist.get(i).recodnum * radio + 40);
			}
			drawpoint(tempcirclepts, canvas);
			currentdisplaypts = tempcirclepts; // ��¼��ǰ��ʾ������
		} else {
			startnum = 0;
			float[] tempcirclepts = new float[sumnum * 2];
			for (int i = 0; i < sumnum; i++) {
				tempcirclepts[2 * i] = x + xspace * i;
				tempcirclepts[2 * i + 1] = y
						- (recordlist.get(i).recodnum * radio + 40);
			}
			drawpoint(tempcirclepts, canvas);
			currentdisplaypts = tempcirclepts; // ��¼��ǰ��ʾ������
		}

	}

	private void drawpoint(float[] circlepts, Canvas canvas) { // ��ԲȦ

		Paint circle = new Paint();
		circle.setColor(Color.GRAY);

		float[] temppts = new float[circlepts.length * 2];
		for (int i = 0; i < circlepts.length; i = i + 2) {
			canvas.drawCircle(circlepts[i], circlepts[i + 1], (float) 10,
					circle);

		}
		for (int i = 0; i < circlepts.length; i = i + 2) {
			temppts[2 * i] = circlepts[i];
			temppts[2 * i + 1] = circlepts[i + 1];
			temppts[2 * i + 2] = circlepts[i];
			temppts[2 * i + 3] = circlepts[i + 1];
		}
		if (circlepts.length * 2 - 4 > 0) {
			float[] linepts = new float[circlepts.length * 2 - 4];
			for (int i = 0; i < linepts.length; i++)
				linepts[i] = temppts[i + 2];
			canvas.drawLines(linepts, circle);
		}
	}

	private void readresultfromdb(Context context) {
		Cursor qandarecord = null;
		QuestionDataBaseAdapter mQuestionDataBaseAdapter = new QuestionDataBaseAdapter(
				context);

		try {
			mQuestionDataBaseAdapter.open();
			qandarecord = mQuestionDataBaseAdapter.fetchAllEntries();
			int timeIndex = qandarecord.getColumnIndex(QuestionDataBaseAdapter.KEY_TIME);
			int recordIndex = qandarecord.getColumnIndex(QuestionDataBaseAdapter.KEY_RECORD);
			int dbsize = qandarecord.getCount();
			qandarecord.moveToFirst();
			for (int i = 0; i < dbsize; i++) {
				record temprecord = new record();
				temprecord.recodnum = qandarecord.getInt(recordIndex);
				temprecord.date = qandarecord.getString(timeIndex);
				recordlist.add(temprecord);
				try {
					qandarecord.moveToNext();
				} catch (Exception e) {
					Log.e("qandarecord read is", "over");
				}

			}
			qandarecord.close();
			mQuestionDataBaseAdapter.close();
		} catch (Exception e) {
			Log.e("data base exception", "Exception", e);
			if (qandarecord != null)
				qandarecord.close();
			mQuestionDataBaseAdapter.close();

		}
		mQuestionDataBaseAdapter.close();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startPoint.x = (int) event.getX();
			startPoint.y = (int) event.getY();
			touchdown = true;
			break;
		case MotionEvent.ACTION_MOVE:
			movePoint.x = (int) event.getX();
			movePoint.y = (int) event.getY();
			touchmove = true;
			break;
		case MotionEvent.ACTION_UP:
			upPoint.x = (int) event.getX();
			upPoint.y = (int) event.getY();
			Log.i("touch event", "up");
			displaytoast();
			touchup = true;
			break;
		default:
			break;
		}
		return true;
	}

	void displaytoast() {
		if (Math.abs(startPoint.x - upPoint.x) < 10) {
			Log.i("point x", Integer.toString(startPoint.y));
			for (int i = 0; i < currentdisplaypts.length - 1; i += 2) {
				if ((Math.abs(startPoint.x - currentdisplaypts[i]) < 15)
						&& (Math.abs(startPoint.y - currentdisplaypts[i + 1])) < 15) {
					String time;
					int recordnum;
					int num;
					if (startnum > 0)
						num = startnum + i / 2 - 1;
					else
						num = i / 2;
					record record = new record();
					record = recordlist.get(num);
					time = record.date;
					recordnum = record.recodnum;

					Toast.makeText(this.context, 
							"第 "+Integer.toString(num+1)+" 次的测试结果：\n时间: "+time+"\n分数: "+recordnum,
							Toast.LENGTH_SHORT).show();

				}
			}

		}
	}

}
