package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MatchTable {
//	private SimpleIntegerProperty no;		// マッチ番号
	private SimpleStringProperty strNo;		// マッチ番号
	private SimpleStringProperty strName;	// マッチ名称
	private SimpleStringProperty strKind;	// マッチ種別(-,f,d,m,c,a,p,#)
	private SimpleStringProperty strFormat;	// 変換文字列フォーマット
	private SimpleStringProperty strPattern;	// マッチパターン
	private SimpleStringProperty strExchg;	// 変換文字列
	private SimpleStringProperty strMatch;	// マッチ文字列

	public MatchTable( int no, String strName, String strKind,
			String strFormat, String strPattern ) {
//		this.no = new SimpleIntegerProperty(no);
		this.strNo = new SimpleStringProperty(Integer.toString(no));
		this.strName = new SimpleStringProperty(strName);
		this.strKind = new SimpleStringProperty(strKind);
		this.strFormat = new SimpleStringProperty(strFormat);
		this.strPattern = new SimpleStringProperty(strPattern);
		this.strExchg = new SimpleStringProperty("");
		this.strMatch = new SimpleStringProperty("");
	}

	public void set( String strExchg, String strMatch ) {
		this.strExchg = new SimpleStringProperty(strExchg);
		this.strMatch = new SimpleStringProperty(strMatch);
	}

//	public IntegerProperty noProperty() {
//		return no;
//	}
	public StringProperty strNoProperty() {
		return strNo;
	}
	public StringProperty strNameProperty() {
		return strName;
	}
	public StringProperty strKindProperty() {
		return strKind;
	}
	public StringProperty strFormatProperty() {
		return strFormat;
	}
	public StringProperty strPatternProperty() {
		return strPattern;
	}
	public StringProperty strExchgProperty() {
		return strExchg;
	}
	public StringProperty strMatchProperty() {
		return strMatch;
	}

}
