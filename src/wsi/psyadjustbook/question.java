package wsi.psyadjustbook;

import java.util.ArrayList;
import java.util.List;

public class question {
	public String subject = "";
	public List<String> obtions = new ArrayList<String>();

	public question() {
	}

	public question(String subject1, List<String> obtions1) {
		this.subject = subject1;
		this.obtions = obtions1;
	}

	public void removeobtion() {
		for (int index = 0; index < obtions.size(); index++)
			obtions.remove(index);
	}

}
