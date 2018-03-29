package wsi.survey.question;

public class RemarkItem {

	public int index;
	public int length;

	public String minScore;
	public String maxScore;
	public String text;
	
	public RemarkItem(int index, int length, String minScore, String maxScore, String text) {
		this.index = index;
		this.length = length;
		this.maxScore = maxScore;
		this.minScore = minScore;
		this.text = text;
		
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getMinScore() {
		return minScore;
	}
	public void setMinScore(String minScore) {
		this.minScore = minScore;
	}
	public String getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(String maxScore) {
		this.maxScore = maxScore;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
