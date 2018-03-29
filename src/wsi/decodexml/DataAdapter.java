package wsi.decodexml;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter {
	public List<String> text = new ArrayList<String>();
	public List<String> imgLink = new ArrayList<String>();
	public List<Item> pageItem = new ArrayList<Item>();
	public List<String> videoLink = new ArrayList<String>();
	public List<String> questionLink = new ArrayList<String>();
	public List<String> ebookLink = new ArrayList<String>();

	public DataAdapter() {
	}

	public class Item {
		public int id;
		public String name;

		public Item(String name, int id) {
			this.id = id;
			this.name = name;
		}
	}

	public void pageItemadd(String name, int id) {
		Item nItem = new Item(name, id);
		pageItem.add(nItem);
	}

}
