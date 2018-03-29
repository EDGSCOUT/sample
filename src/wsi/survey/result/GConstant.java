package wsi.survey.result;

import wsi.psy.R;
import android.content.Context;
import android.telephony.TelephonyManager;


public class GConstant {
	
	public static String IMEI = "000000000000000";
	public static int deviceScreenWidth = 480;
	public static int deviceScreenHeight = 800;
	public static int titleFontSize = 24;
	
	
	public final static String surveyFileFolder = "surveyfiles"; // 问卷文件夹，注意：文件名称必须与下面二维数组完全一致（特别是字母大小写）
	public final static int[] imgs = { R.drawable.img03, R.drawable.img04, R.drawable.img05, R.drawable.img06, R.drawable.img06};
//	public final static int[] imgs_completed = { R.drawable.img03_completed, R.drawable.img04_completed, R.drawable.img05_completed,  R.drawable.img06_completed,};
	public final static int[] imgs_completed = { R.drawable.img03, R.drawable.img04, R.drawable.img05, R.drawable.img06, R.drawable.img06};
	public final static String[][] surveyFiles = {
			{
					"CES-D_Phone.xml",
					"流调中心抑郁量表",
					"	焦虑是一种比较普遍的精神体验，长期存在焦虑反应的人易发展为焦虑症。本量表的题目主要描述了您可能存在的或最近有过的感受，请您认真阅读题目并在每一道题目后点击选择合适的数字来表示在【最近一周来】您产生这种感受的频繁度。" },
			{
					"IAS_Phone.xml",
					"交往焦虑量表（IAS）",
					"	本量表是Leary于1983年编制，主要用于测量个体关于社交情境中主观焦虑体验的倾向。本量表对于区分主观上的焦虑和外在的行为表现是有帮助的，对于评估那些害怕人际交往的个体的焦虑程度是一种实用的工具。" },		
			{
					"PWB_Phone.xml",
					"心理幸福感量表（PWB）",
					"	心理幸福感量表是对主观心理幸福感的一种评估，其测评结果将协助您从 不同维度了解自己的心理状态，从而评测您的心理幸福感。" },
			{
					"UCLAAl_Phone.xml",
					"孤独感测试量表", 
					"	欢迎您填写本问卷，下面的题目列出了人们在日常社会交往中可能会出现的一些感受，对每项描述，请指出您在具有此种感受的频度，选择您认同的选项。请您在不受外界打扰的情况下，认真、真实地填写完成本问卷。\n	这是本套问卷最后一次测评，请您确保将每套问卷的测评结果提交。如果测评过程中提交失败，还请您返回主界面查看菜单键并重新提交，非常感谢您的配合！" }
					
		
	};

	/**
	 * author：qinning
	 * 全局变量，记录某一问卷是否已经填写完，如果已经填写完，则对应的问卷position将会被设置为true，否则为false，默认进入应用时第一个为true，其他为false
	 */
	public static boolean[] flags=new boolean[surveyFiles.length];
	public static String[] strings = {
		"\t 0~40分:此范围的分数处于低水平范围，得分者在最近并没有或只有很少的抑郁症状，并没有深陷于抑郁情绪的困扰。\t\n 40~80分: 此范围的分数处于高水平范围，这说明在得分者身上具有比其他人更多的抑郁症状。处于此范围分数的得分者可能比较容易遭到抑郁情绪的侵袭，对生活中遇到的问题常感到力不从心或身心俱疲并因此变得情绪低沉。如果你正巧有类似的感受，请尽快寻求他人帮助，或者进入抑郁调节自助系统。预测结果仅供参考，如果想了解准确分数，可以填写",
		"\t IAS与在真实交往中的自陈焦虑相关良好。与低得分者相比，高得分者陈述在人际交往之前及之中都更加焦虑及缺乏信心，并关注在交往中别人怎样看待他们。在交谈中也更多的感到抑制。别人也认为他们表现的显得较为紧张及缺乏信心。高得分者还担心别人如何评价其外表。得分与在面对面的交往时的心率增加有关。预测结果仅供参考，如果想了解准确分数，可以填写",
		"\t 幸福感是一种心理体验，它既是对生活的客观条件和所处状态的一种事实判断，又是对于生活的主观意义和满足程度的一种价值判断。它表现为在生活满意度基础上产生的一种积极心理体验。如果您的得分高于20分，则您的幸福感处于高分水平；如果得分低于10分，您的幸福感体验比较低，请注意及时调节自己的状态或者进行专业的咨询。预测结果仅供参考，如果想了解准确分数，可以填写",
		"\t 孤独感是一种封闭心理的反映，是感到自身和外界隔绝或受到外界排斥所产生出来的孤伶苦闷的情感。一般而言，短暂的或偶然的孤独不会造成心理行为紊乱，但长期或严重的孤独可引发某些情绪障碍，降低人的心理健康水平。孤独感还会增加与他人和社会的隔膜与疏离，而隔膜与疏离又会强化人的孤独感，久之势必导致疏离的个人体格失常。如果您的得分高于60分，则您有可能有比较高程度的孤独感，请注意及时调节或治疗。预测结果仅供参考，如果想了解准确分数，可以填写"
	};
	
	
	public static void adjustFontSize(int screenWidth, int screenHeight) {
		deviceScreenWidth = screenWidth;
		deviceScreenHeight = screenHeight;

		if (deviceScreenWidth <= 240) { // 240X320 屏幕
			titleFontSize = 10;

		} else if (deviceScreenWidth <= 320) { // 320X480 屏幕
			titleFontSize = 14;

		} else if (deviceScreenWidth <= 480) { // 480X800 或 480X854 屏幕
			titleFontSize = 24;

		} else if (deviceScreenWidth <= 540) { // 540X960 屏幕
			titleFontSize = 26;

		} else if (deviceScreenWidth <= 800) { // 800X1280 屏幕
			titleFontSize = 30;

		} else { // 大于 800X1280
			titleFontSize = 32;

		}
	}
}
