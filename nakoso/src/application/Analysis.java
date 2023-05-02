package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析用データ
 * @author sue-t
 *
 */
public class Analysis {

	class Item {
		private String strName;
		private String strPattern;
		private String strFormat;
		private Pattern pattern;
		private String strExch;
		private String strMatch;
		private int start;
		private int end;

		public Item( String strName,
				String strPattern, String strFormat ) {
			this.strName = strName;
			this.strPattern = strPattern;
			this.strFormat = strFormat;
			this.pattern = null;
			strExch = "";
			strMatch = "";
			start = Integer.MAX_VALUE;
		}

		public boolean compile() {
			try {
				this.pattern = Pattern.compile(strPattern);
			} catch (Exception e) {
				return false;
			}
			return true;
		}

		public boolean rewrite(
				String strPattern, String strFormat ) {
			D.dprint_method_start();
			D.dprint(strPattern);
			D.dprint(strFormat);
			this.strPattern = strPattern;
			this.strFormat = strFormat;
			this.pattern = null;
			try {
				this.pattern = Pattern.compile(strPattern);
			} catch (Exception e) {
				D.dprint(e);
				D.dprint_method_end();
				return false;
			}
			D.dprint_method_end();
			return true;
		}

		public void clear() {
			D.dprint_method_start();
			strExch = "";
			strMatch = "";
			start = Integer.MAX_VALUE;
			D.dprint_method_end();
		}

		public int match( String text ) {
			D.dprint_method_start();
			D.dprint(this.strPattern);
			D.dprint(this.strFormat);
			start = Integer.MAX_VALUE;
			Matcher m = this.pattern.matcher(text);
			if (m.find()) {
				D.dprint(m.group(0));
				int i = m.groupCount();
				D.dprint(i);
				if (i == 0) {
					strExch = this.strFormat;
				} else if (i == 1) {
					D.dprint(m.group(1));
					strExch = String.format(this.strFormat,
							m.group(1));
				} else if (i == 2) {
					strExch = String.format(this.strFormat,
							m.group(1), m.group(2));
				} else if (i == 3) {
					strExch = String.format(this.strFormat,
							m.group(1), m.group(2),
							m.group(3));
				} else {
					strExch = this.strFormat;
				}
				strMatch = m.group(0);
				start = m.start();
				end = m.end();
			} else {
				strExch = "";
				strMatch = "";
			}
			D.dprint(strExch);
			D.dprint(start);
			D.dprint_method_end();
			return start;
		}

		public String getExch() {
			return strExch;
		}

		public MatchTable getMatchTable( int no ) {
			D.dprint_method_start();
			MatchTable matchTable = new MatchTable(
					no, strName, "-",
					strFormat, strPattern);
			matchTable.set(strExch, strMatch);
			D.dprint(matchTable);
			D.dprint_method_end();
			return matchTable;
		}

		public ItemTable getItemTable() {
			D.dprint_method_start();
			ItemTable itemTable = new ItemTable(
					strName, strFormat, strPattern);
			D.dprint(strMatch);
			itemTable.set(strExch, strMatch);
			D.dprint_method_end();
			return itemTable;
		}
	}

	private Map<Integer, List<Item>> mapAnal;

	// マッチしたItem
	private Map<Integer, Item> mapMatch;

	private Set<Integer> setAnal;

	public Analysis() {
		mapAnal = new HashMap<Integer, List<Item>>();
		mapMatch = new HashMap<Integer, Item>();
		setAnal = new HashSet<Integer>();
	}

	public boolean addMapElement( Integer intMap,
			String strName,
			String strPattern, String strFormat ) {
		D.dprint_method_start();
		D.dprint(intMap);
		D.dprint(strPattern);
		D.dprint(strFormat);
		Item item = new Item(strName, strPattern, strFormat);
		boolean bSuccess = item.compile();
		if( ! bSuccess ) {
			D.dprint(false);
			D.dprint_method_end();
			return false;
		}
		if (! setAnal.contains(intMap)) {
			List<Item>itemList = new ArrayList<Item>();
			itemList.add(item);
			mapAnal.put(intMap, itemList);
			mapMatch.put(intMap, null);
			setAnal.add(intMap);
		} else {
			List<Item>itemList = mapAnal.get(intMap);
			itemList.add(item);
			mapAnal.put(intMap, itemList);
		}
		D.dprint(true);
		D.dprint_method_end();
		return true;
	}

	public void clear() {
		mapAnal.forEach((k, itemList) -> {
			D.dprint(k);
			D.dprint(itemList);
			Iterator<Item> iter = itemList.iterator();
			while(iter.hasNext()) {
			    Item item = (Item)iter.next();
			    item.clear();
			}
		});
	}

	public MatchTable getMatchTable( Integer intMap ) {
		D.dprint_method_start();
		if (! setAnal.contains(intMap)) {
			D.dprint_method_end();
			return null;
		}
		Item item = mapMatch.get(intMap);
		if (item == null) {
			D.dprint(intMap);
			D.dprint_method_end();
			return null;
		}
		MatchTable matchTable = item.getMatchTable(intMap);
		D.dprint_method_end();
		return matchTable;
	}

	public List<ItemTable> getItemTableList( Integer intMap ) {
		D.dprint_method_start();
		List<ItemTable> itemTableList
				= new ArrayList<ItemTable>();
		List<Item> itemList = mapAnal.get(intMap);
		Iterator<Item> iter = itemList.iterator();
		while (iter.hasNext()) {
			Item item = iter.next();
			ItemTable itemTable = item.getItemTable();
			itemTableList.add(itemTable);
		}
		D.dprint_method_end();
		return itemTableList;
	}

	public void rewriteItem( Integer intMap,
			List<String> listPattern, List<String> listFormat ) {
		D.dprint_method_start();
		List<Item> itemList = mapAnal.get(intMap);
		Iterator<Item> iter = itemList.iterator();
		Iterator<String> iterPattern = listPattern.iterator();
		Iterator<String> iterFormat = listFormat.iterator();
		while (iter.hasNext()) {
			Item item = iter.next();
			String strPattern = iterPattern.next();
			String strFormat = iterFormat.next();
			item.rewrite(strPattern, strFormat);
		}
		D.dprint_method_end();
		return;
	}

	public Map<Integer, String> getStringList( String text ) {
		D.dprint_method_start();
//		D.dprint(text);
		Map<Integer, String> map = new HashMap<>();
		mapAnal.forEach((k, itemList) -> {
			D.dprint(k);
			D.dprint(itemList);
			int minIndex = Integer.MAX_VALUE;
			Item minItem = null;
			int index;
			Iterator<Item> iter = itemList.iterator();
			while(iter.hasNext()) {
			    Item item = (Item)iter.next();
			    // 正規表現で合致するのを探す
			    index = item.match(text);
			    if (index < minIndex) {
			    	minIndex = index;
			    	minItem = item;
			    }
			}
			if (minItem != null) {
				// 合致した中で、前にあるものを採用
				String strMatch = minItem.getExch();
		    	map.put(k, strMatch);
		    	mapMatch.put(k, minItem);
		    }
		});
		D.dprint_method_end();
		return map;
	}


	public String getColoredString( int indexMap, String strText ) {
		D.dprint_method_start();
//		D.dprint(strText);
		List<Item> itemList = mapAnal.get(indexMap);
		if (itemList == null) {
			D.dprint_method_end();
			return strText;
		}
		D.dprint(itemList);
		String strColored;
		int minIndex = Integer.MAX_VALUE;
		Item minItem = null;
		int index;
		Iterator<Item> iter = itemList.iterator();
		while(iter.hasNext()) {
		    Item item = (Item)iter.next();
		    // 正規表現で合致するのを探す
		    index = item.match(strText);
		    if (index < minIndex) {
		    	minIndex = index;
		    	minItem = item;
		    }
		}
		if (minItem != null) {
			// 合致した中で、前にあるものを採用
			String strC = String.format(
					"<font color=\"red\">[%d]", indexMap);
			String str =
					strText.substring(0, minItem.start)
					+ strC
					+ strText.substring(minItem.start,
							minItem.end)
					+ "</font>"
					+ strText.substring(minItem.end);
			strColored = str;
	    } else {
	    	strColored = strText;
	    }
//	    D.dprint(strColored);
		D.dprint_method_end();
		return strColored;
	}

	public boolean contains( int indexMap ) {
		return setAnal.contains(indexMap);
	}
}
