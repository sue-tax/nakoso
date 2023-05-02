package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemTable {
	private SimpleStringProperty strNameItem;	// マッチ名称
	private SimpleStringProperty strFormatItem;	// 変換文字列フォーマット
	private SimpleStringProperty strPatternItem;	// マッチパターン
	private SimpleStringProperty strExchgItem;	// 変換文字列
	private SimpleStringProperty strMatchItem;	// マッチ文字列

	public ItemTable( String strName,
			String strFormat, String strPattern ) {
		this.strNameItem = new SimpleStringProperty(strName);
		this.strFormatItem = new SimpleStringProperty(strFormat);
		this.strPatternItem= new SimpleStringProperty(strPattern);
		this.strExchgItem = new SimpleStringProperty("");
		this.strMatchItem = new SimpleStringProperty("");
	}

	public void set( String strExchg, String strMatch ) {
		this.strExchgItem = new SimpleStringProperty(strExchg);
		this.strMatchItem = new SimpleStringProperty(strMatch);
	}

	public void rewrite( String strFormat, String strPattern ) {
		this.strFormatItem = new SimpleStringProperty(strFormat);
		this.strPatternItem= new SimpleStringProperty(strPattern);
	}

	public StringProperty strNameProperty() {
		return strNameItem;
	}
	public StringProperty strFormatProperty() {
		return strFormatItem;
	}
	public StringProperty strPatternProperty() {
		return strPatternItem;
	}
	public StringProperty strExchgProperty() {
		return strExchgItem;
	}
	public StringProperty strMatchProperty() {
		return strMatchItem;
	}

}
