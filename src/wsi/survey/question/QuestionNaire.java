package wsi.survey.question;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class QuestionNaire {
	private String caption;			// 问卷标题
	private String info;				// 问卷描述
	private int questionsNum;			// 问卷题目数
	private String interfaceType; 		// 计算分数的接口类型（接口名字-前缀）
	private QuestionItem questionItems[];	
	public RemarkItem remarkItem[];
	private Map<String, String> tagsMap;	
	private Map<String, String> remarksMap;
	public int length;
	public String remarkText;
	

	public QuestionNaire() {
		this.questionsNum = 0;
		
		this.tagsMap = new HashMap<String, String>();
		
		
		this.remarksMap = new HashMap<String, String>();
	}
	
	public void addTags(String name, String label) {
		tagsMap.put(name, label);
	}
	
	public Map<String, String> getTagsMap() {
		return tagsMap;
	}
	

	public void addRemarks(String name, String score, String remark) {
		String tagScore = name + "@" + score;
		this.remarkText = remark;
		if(remarksMap.containsKey(name)) {
			remarksMap.put(tagScore, remark);
		} else {
			remarksMap.put(tagScore, remark);
		}
		
	}
//	public void addRemarks(int i, int length, String minScore, String maxScore, String text) {
////		remarkItem[i].maxScore = maxScore;
////		remarkItem[i].minScore = minScore;
////		remarkItem[i].text = text;
//		remarkItem[i] = new RemarkItem(i, length, minScore, maxScore, text);
//	}
	
	public String getRemarksMap() {
		return remarkText;
	}
	
	
	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getCaption() {
		return this.caption;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInterfaceType(String interfaceType){
		this.interfaceType = interfaceType;
	}
	
	public String getInterfaceType(){
		return this.interfaceType;
	}
	
	/** 问题的题目数，并初始化问题数组quetions[] */
	public void setQuestionsNum(int qNum) {
		this.questionsNum = qNum;
		this.questionItems = new QuestionItem[this.questionsNum];
	}
	
	public void setRemarkItem(int length) {
		this.length = length;
		this.remarkItem = new RemarkItem[this.length];
	}

	public int getQuestionsNum() {
		return this.questionsNum;
	}

	public void setQuestionItem(int idx, QuestionItem qitem) {
		this.questionItems[idx] = qitem;
	}

	public QuestionItem getQuestionItem(int idx) {
		return this.questionItems[idx];
	}
	
	public String getQuestionItemTitle(int idx) {
		return questionItems[idx].getTitle();
	}

	public int getQuestionItemOptionNum(int idx) {
		return questionItems[idx].getOptionItemNum();
	}
	
	public String getQuestionItemOptionType(int idx){
		return questionItems[idx].getQuestionType();
	}

	/**
	 * @param idx		questions数组的下标
	 * @param oidx	QuestionItem的选项下标
	 * @return		返回选项内容
	 */
	public String getQuestionItemOptionText(int idx, int oidx) {
		return questionItems[idx].getOptionItem(oidx).getText();
	}

	/** 设置第idx道题目，答案选项下标为oidx_selected */
	public void setQuestionItemAnswerOptionIndex(int idx, int oidx_selected) {
		questionItems[idx].setAnswerOptionIndex(oidx_selected);
	}

	public int getQuestionItemAnswerOptionIndex(int idx) {
		return questionItems[idx].getAnswerOptionIndex();
	}
	
	public void setQuestionItemAnswerContent(int idx,String value){
		questionItems[idx].setAnswerContent(value);
	}
	
	public String getQuestionItemAnswerContent(int idx){
		return questionItems[idx].getAnswerContent();
	}

	public void setQuestionItemAnswerOfN(int idx, boolean answer, int oidx) {
		questionItems[idx].setAnswerOfM(answer, oidx);
	}

	public boolean getQuestionItemAnswerOfN(int idx, int oidx) {
		return questionItems[idx].getAnswerofM(oidx);
	}

}
