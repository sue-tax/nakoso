/**
 * 設定ファイルの処理
 * 設定ファイルは、utf-8で記載されたCSVファイル
 */

/*
0, Version, 0
0, Option, Rename or Copy
0, FilenameFormat, %2$s_%1$2
【マッチ番号】,【マッチ名称】,【変換文字列フォーマット】,【マッチパターン】,【マッチオプション】
 */
/* プロトタイプと順番が入れ替わっている点に注意 */

package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author sue-t
 *
 */
public class ConfigProc {

	static final String COMMENT = "Comment";

	static final String VERSION = "Version";

	static final String OPTION = "Option";
	static final String OPTION_RENAME = "Rename";
	static final String OPTION_COPY = "Copy";

	static final String FILENAME_FORMAT = "FilenameFormat";

	static final String ERROR_TOOFEW_COLUMN
			= "TooFewColumn_Line-%d_not_less_than_%d_"
			+ "その行の項目数が少ない";
	static final String ERROR_MIX_MATCH_KIND
			= "MixMatchKind_マッチ種別が混じっています";

	private float version = 0.0f;

	/**
	 * 設定ファイルのファイル名
	 */
	private String strFileConfig;

	/**
	 * ファイル変更名のフォーマット文字列
	 * formatメソッドで利用する文字列で指定します。
	 * 例：%2$s_%1$s
	 */
	private String strFileFormat;

	/**
	 * ファイル名変更をリネームで行なうか、コピーして行なうか
	 */
	private boolean flagRename = true;


	private Analysis analysis;

	private ExAnal exAnal;

	private int startNumbering;


	public ConfigProc( String strFileConfig,
			Analysis analysis, ExAnal exAnal ) {
		this.strFileConfig = strFileConfig;
		this.analysis = analysis;
		this.exAnal = exAnal;
	}

	public String readConfig() {
		D.dprint_method_start();

//		aExMatch = new ExMatch[10];

		String strRet = null;
		Path path;
		try {
			path = Paths.get(strFileConfig);
		} catch (Exception e1) {
			strRet = String.format(
					"InvalidFileName_Paths.get_%s_"
					+ "ファイル名が正しくない",
					strFileConfig);
			D.dprint(strRet);
			D.dprint_method_end();
			return strRet;
		}
    	D.dprint(path);

    	List<String> lines;
		try {
//			lines = Files.readAllLines(
//					path, Charset.forName("UTF-8"));
			lines = Files.readAllLines(path);
		} catch (IOException e1) {
			strRet = String.format(
					"CannotReadFile_Files.readAllLines_%s"
					+ "ファイルが読めませんでした",
					strFileConfig);
			D.dprint(strRet);
			D.dprint_method_end();
			return strRet;
		}
//        D.dprint(lines);

		String firstLine = lines.get(0);
//        D.dprint(String.format("%02x", (int)(firstLine.charAt(0))));
//        D.dprint(String.format("%02x", (int)(firstLine.charAt(1))));
//        D.dprint(String.format("%02x", (int)(firstLine.charAt(2))));
		// BOM対策
        if ((int)(firstLine.charAt(0)) == 0xfeff) {
			lines.set(0, firstLine.substring(1));
		}
//        D.dprint(lines);

		for (int iLine = 0; iLine < lines.size(); iLine++) {
        	D.dprint(lines.get(iLine));
            String[] aData = lines.get(iLine).split(",");
            D.dprint(aData[0]);
            if (aData[0].matches("[1-9]")) {
            	strRet = readMatch(iLine, aData);
        		if (strRet != null) {
        			D.dprint(strRet);
        			D.dprint_method_end();
        			return strRet;
        		}
            } else if (aData[0].equals("0")) {
            	if (aData.length < 2) {
        			strRet = String.format(ERROR_TOOFEW_COLUMN,
        					iLine+1, 2);
        			D.dprint(strRet);
        			D.dprint_method_end();
        			return strRet;
            	}
            	if (aData[1].equals(VERSION)) {
            		strRet = readVersion(iLine, aData);
            		if (strRet != null) {
            			D.dprint(strRet);
            			D.dprint_method_end();
            			return strRet;
            		}
            	} else if (aData[1].equals(FILENAME_FORMAT)) {
            		strRet = readFormat(iLine, aData);
            		if (strRet != null) {
            			D.dprint(strRet);
            			D.dprint_method_end();
            			return strRet;
            		}
            	} else if (aData[1].equals(OPTION)) {
            		strRet = readOption(iLine, aData);
            		if (strRet != null) {
            			D.dprint(strRet);
            			D.dprint_method_end();
            			return strRet;
            		}
            	} else {
            		strRet = String.format(
            				"Invalid_Line-%d_%s_"
            				+ "２列目が正しくない",
            				iLine+1, aData[1]);
        			D.dprint(strRet);
        			D.dprint_method_end();
        			return strRet;
            	}
            } else if (aData[0].equals(COMMENT)) {
            	// コメント行
            } else {
        		strRet = String.format(
        				"InvalidMatchNumber_Line-%d_%s_"
        				+ "１列目のマッチ番号が正しくない",
        				iLine+1, aData[0]);
//        		continue;
    			D.dprint(strRet);
    			D.dprint_method_end();
    			return strRet;
            }
        }
        if (strFileFormat == null) {
    		strRet = String.format(
    				"NoFileFormat_"
    				+ "変更ファイル名フォーマットの指定がない");
			D.dprint(strRet);
			D.dprint_method_end();
			return strRet;
        }
        D.dprint(strRet);
        D.dprint_method_end();
        return strRet;
	}

	private String readMatch( int iLine, String[] aData ) {
		D.dprint_method_start();
		String strRet = null;
		if (aData.length < 4) {
			strRet = String.format(ERROR_TOOFEW_COLUMN,
					iLine, 4);
			D.dprint_method_end();
			return strRet;
		}
		Integer index = Integer.valueOf(aData[0]);
    	D.dprint(index);
    	// aData[1]は、処理に関係ない
    	D.dprint("*"+aData[3]+"*");
		if (! aData[3].equals("")) {	// PROTOTYPEと違う
			if (exAnal.contains(index)) {
				strRet = ERROR_MIX_MATCH_KIND;
				D.dprint_method_end();
				return strRet;
			}
			if (aData[3].equals("+")) {
				// マッチパターンは
				// 変換文字列フォーマットと一致
        		analysis.addMapElement(index,
        				aData[1], aData[2], aData[2]);
			} else if (aData[3].equals("*")) {
				// マッチパターンは
				// 変換文字列フォーマットの
				// 各文字の間にスペース等を挿入
				StringBuilder stringBuilder = new StringBuilder();
				for (int j=0; j<aData[2].length(); j++) {
					stringBuilder.append(aData[2].charAt(j));
					stringBuilder.append("(\\s|　)*");
				}
				String strMatch = stringBuilder.toString();
				D.dprint(strMatch);
        		analysis.addMapElement(index,
        				aData[1], strMatch, aData[2]);
			} else {
        		analysis.addMapElement(index,
        				aData[1], aData[3], aData[2]);
			}
		} else {
			if (analysis.contains(index)) {
				strRet = ERROR_MIX_MATCH_KIND;
				D.dprint_method_end();
				return strRet;
			}
			// 日時、連番、元ファイルの処理
			if (aData[2].matches("(m|c|a|p)")) {
				// ファイル更新日時等
				exAnal.addMapElement(index,
						aData[1], aData[2], "", aData[4]);
			} else if (aData[2].equals("f")) {
				exAnal.addMapElement(index,
						aData[1], aData[2],
						aData[4], aData[5]);
			} else if (aData[2].equals("d")) {
				exAnal.addMapElement(index,
						aData[1], aData[2],
						aData[4], aData[5]);
			} else if (aData[2].equals("#")) {
//				indexNumbering = index;
				if (aData.length >= 5) {
					try {
						startNumbering = Integer.valueOf(
								aData[4]);
					} catch (NumberFormatException e) {
			    		strRet = String.format(
			    				"InvalidStartNumber_Line-%d_%s_"
			    				+ "開始番号が正しくない",
			    				iLine+1, aData[4]);
			    		D.dprint(strRet);
			    		D.dprint_method_end();
						return strRet;
					}
				} else {
					startNumbering = 1;
				}
				exAnal.addMapElement(index,
						aData[1], aData[2], "", "");
			} else {
	    		strRet = String.format(
	    				"InvalidMatch_Line-%d_%s_"
	    				+ "マッチ指定が正しくない",
	    				iLine+1, aData[2]);
	    		D.dprint(strRet);
	    		D.dprint_method_end();
				return strRet;
			}
		}
		D.dprint(strRet);
		D.dprint_method_end();
		return strRet;
	}


	private String readVersion( int iLine, String[] aData ) {
		String strRet = null;
		if (aData.length < 3) {
			strRet = String.format(ERROR_TOOFEW_COLUMN,
					iLine+1, 3);
			return strRet;
		}
		try {
			this.version = Float.valueOf(aData[2]);
		} catch (NumberFormatException e) {
			strRet = String.format(
					"InvalidVersion_FloatNumber_Line-%d_"
					+ "バージョンが小数でない",
					iLine+1);
			return strRet;
		}
		return strRet;
	}


	private String readFormat( int iLine, String[] aData ) {
		String strRet = null;
		if (aData.length < 3) {
			strRet = String.format(ERROR_TOOFEW_COLUMN,
					iLine, 3);
			return strRet;
		}
		this.strFileFormat = aData[2];
		return strRet;
	}

	private String readOption( int iLine, String[] aData ) {
		String strRet = null;
		if (aData.length < 3) {
			strRet = String.format(ERROR_TOOFEW_COLUMN,
					iLine, 3);
			return strRet;
		}
		if (aData[2].equals(OPTION_RENAME)) {
			this.flagRename = true;
    	} else if (aData[2].equals(OPTION_COPY)) {
    		this.flagRename = false;
    	} else {
    		strRet = String.format(
    				"InvalidOption_Line-%d_%s_"
    				+ "オプションが正しくない",
    				iLine+1, aData[2]);
			return strRet;
    	}
		this.strFileFormat = aData[2];
		return strRet;
	}


	public String getFileFormat() {
		return strFileFormat;
	}

	public boolean getFlagRename() {
		return flagRename;
	}

	public float getVersion() {
		return version;
	}

	public void clear() {
		analysis.clear();
		exAnal.clear();
	}

	public String[] getMatchString( int iFile, String strText,
			FileProc fileProc ) {
		D.dprint_method_start();
		String strRet = null;
		String strFileOrginal = fileProc.getFileName();
		File file = new File(strFileOrginal);
		Map<Integer, String> map = analysis.getStringList(
				strText);
		Map<Integer, String> exMap = exAnal.getStringList(
				iFile, fileProc);
		String aStr[] = new String[10];
		for (int i=1; i<10; i++) {
			aStr[i] = map.get(i);
			if (aStr[i] == null) {
				aStr[i] = exMap.get(i);
			}
		}

		D.dprint(aStr);
		D.dprint_method_end();
		return aStr;
	}


	public String getColoredString( String strText ) {
		D.dprint_method_start();
//		D.dprint(strText);
		String strRet = null;
		String strColoredText = strText;
		for (int i=1; i<10; i++) {
			strColoredText = analysis.getColoredString(
					i, strColoredText);
		}
		strColoredText = strColoredText.replaceAll(
				"(?<!font)\\h", "(\\\\h)");
//		"(?<!font)\\s", "(\\\\s)");
//		D.dprint(strColoredText);
		D.dprint_method_end();
		return strColoredText;
	}


	public List<MatchTable> getMatchTableList() {
		D.dprint_method_start();
		MatchTable aMatchTable[] = new MatchTable[10];
		for (int i=1; i<10; i++) {
			if (analysis.contains(i)) {
				aMatchTable[i] = analysis.getMatchTable(i);
			} else if (exAnal.contains(i)) {
				aMatchTable[i] = exAnal.getMatchTable(i);
			}
			if (aMatchTable[i] == null) {
				aMatchTable[i] = new MatchTable(
						i, "", "",
						"", "");
			}
			D.dprint(aMatchTable[i]);
		}
		List<MatchTable> listMatchTable
				= Arrays.asList(aMatchTable);
		listMatchTable = listMatchTable.subList(1, 10);
		D.dprint(listMatchTable);
		D.dprint_method_end();
		return listMatchTable;
	}

	public List<ItemTable> getItemTableList( Integer intMatch ) {
		D.dprint_method_start();
//		List<ItemTable> listItemTable
//				= new ArrayList<ItemTable>();
		if (! analysis.contains(intMatch)) {
			D.dprint_method_end();
			return null;
		}
		List<ItemTable> listItemTable
				= analysis.getItemTableList(intMatch);
		D.dprint(listItemTable);
		D.dprint_method_end();
		return listItemTable;
	}
}
