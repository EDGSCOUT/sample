package wsi.psy.recommend;

import wsi.psy.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Recommend extends Activity implements
		MediaPlayer.OnCompletionListener {

	int gloomyScore;
	int anxiousScore;
	int happyScore;
	int lonelyScore;
	int dif1, dif2, dif3, dif4;
	private ImageButton play, pause, stop;
	private MediaPlayer mp;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recommend);
		Intent intent = getIntent();
		gloomyScore = intent.getIntExtra("score1", 0);
		anxiousScore = intent.getIntExtra("score2", 0);
		happyScore = intent.getIntExtra("score3", 0);
		lonelyScore = intent.getIntExtra("score4", 0);
		System.out.println("Recommendscore" + gloomyScore + anxiousScore + happyScore + lonelyScore);
		
		
		tv = (TextView)findViewById(R.id.textView);
		caculate(gloomyScore, anxiousScore, happyScore, lonelyScore);
		init(dif1, dif2, dif3, dif4);
		
        play = (ImageButton)findViewById(R.id.play);
//        play.setVisibility(View.VISIBLE);
        pause = (ImageButton)findViewById(R.id.pause);
//        pause.setVisibility(View.VISIBLE);
        stop = (ImageButton)findViewById(R.id.stop);
//        stop.setVisibility(View.VISIBLE);
        
        play.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				play();
			}
		});
		
		pause.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				pause();
			}
		});
		
		stop.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				stop();
			}
		});
        
		setup();
	}

	private void init(int dif11, int dif22, int dif33, int dif44) {
		// TODO Auto-generated method stub
		System.out.println("diff: " + dif11 + dif22 + dif33 + dif44);
		if (dif11 >= dif22 && dif11 >= dif33 && dif11 >= dif44) {
			tv.setText("您可能会感到情绪低落。下面的一些想法或做法可能是原因，请注意矫正：" +
					"\n1.否认或压抑痛苦；" +
					"\n2.认为压抑痛苦是坚强的表现；" +
					"\n3.逃避痛苦；" +
					"\n4.自责内疚；" +
					"\n5.遇到挫折后，过分悲观失望；" +
					"\n6.面对痛苦时听天由命。");
			System.out.println("recommend111");
		}
		if (dif22 >= dif11 && dif22 >= dif33 && dif22 >= dif44) {
			tv.setText("您可能会感到有些焦虑。腹式呼吸训练是很好的应对方法，它能够调节心率，降低血压，平衡植物神经，利于缓解焦虑情绪。每天坚持腹式呼吸很有益处，特别是当我们感到紧张、压力大、心情烦躁的时候，更能体会到它的魅力。");
			System.out.println("recommend222");
		}
		if (dif33 >= dif11 && dif33 >= dif22 && dif33 >= dif44) {
			tv.setText("您可能会感到自己不太幸福。学会从不同角度来看待身边的事物是幸福的保鲜剂。请试着从不同角度看待使人深感沮丧的情况，它真有那么糟吗？是不是换一个角度来看待这件事？请将所有的不愉快都放飞在遥远的蓝天中，让它随风而去吧。");
			System.out.println("recommend333");
		}
		if (dif44 >= dif11 && dif44 >= dif22 && dif44 >= dif33) {
			tv.setText("您可能会感到有些孤独。长期孤独可能会危害健康，因此请为自己寻找一个倾诉心灵的机会。无论倾诉对象是朋友、配偶、亲人，甚或仅仅是陌生人，我们都能够在这个过程中体会到体谅、支持和慰籍，获得心灵上的满足与释然。");
			System.out.println("recommend444");
		}
		if(dif11 == 0 && dif22 == 0 && dif33 == 0 && dif44 == 0){
			tv.setText("您需要使用中科心解一段时间，我们才能根据您的信息，来向您推荐内容，您可以听下面的放松音乐。");
			System.out.println("recommend555");
		}
	}

	private void caculate(int gloomyScore2, int anxiousScore2, int happyScore2,
			int lonelyScore2) {
		// TODO Auto-generated method stub
		if (gloomyScore2 > 36) {
			dif1 = gloomyScore2 - 36;
		}
		if (anxiousScore2 > 39) {
			dif2 = anxiousScore2 - 39;
		}
		if ((happyScore2!=0) && happyScore2 < 15) {
			dif3 = 15 - happyScore2;
		}
		if (lonelyScore2 > 39) {
			dif4 = lonelyScore2 - 39;
		}
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		stop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (stop.isEnabled()) {
			stop();
		}
	}

	private void play() {
		mp.start();

		play.setEnabled(false);
		pause.setEnabled(true);
		stop.setEnabled(true);
	}

	private void stop() {
		mp.stop();
		pause.setEnabled(false);
		stop.setEnabled(false);

		try {
			mp.prepare();
			mp.seekTo(0);
			play.setEnabled(true);
		} catch (Throwable t) {
			error(t);
		}
	}

	private void pause() {
		mp.pause();
		play.setEnabled(true);
		pause.setEnabled(false);
		stop.setEnabled(true);
	}

	private void loadClip() {
		try {
			mp = MediaPlayer.create(this, R.raw.com);
			mp.setOnCompletionListener(this);
		} catch (Throwable t) {
			error(t);
		}
	}

	private void setup() {
		loadClip();
		play.setEnabled(true);
		pause.setEnabled(false);
		stop.setEnabled(false);
	}

	private void error(Throwable t) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("报错啦!").setMessage(t.toString())
				.setPositiveButton("确定", null).show();
	}

}
