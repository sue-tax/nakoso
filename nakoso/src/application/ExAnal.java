package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ExAnal {

	class ExItem {
		private String strName;	// マッチ名称
		private String strKind;	// 種別　f,d,m,c,a,p,#
		private String strFormat;	// 変換フォーマット
		private String strPattern;	// マッチパターン
		private String strExch;	// 変換文字列
		private String strMatch;	// マッチ文字列
		private Pattern pattern;	// f, d 用

		public ExItem( String strName, String strKind,
				String strPattern, String strFormat ) {
			this.strName = strName;
			this.strKind = strKind;
			this.strPattern = strPattern;
			this.strFormat = strFormat;
			this.pattern = null;
			this.strExch = "";
			this.strMatch = "";
		}

		public boolean compile() {
			D.dprint_method_start();
			if (! strKind.matches("(f|d)")) {
				return false;
			}
			try {
				D.dprint(strPattern);
				this.pattern = Pattern.compile(strPattern);
			} catch (Exception e) {
				return false;
			}
			D.dprint_method_end();
			return true;
		}

		public void clear() {
			this.strExch = "";
			this.strMatch = "";
		}

		public MatchTable getMatchTable( int no ) {
			D.dprint_method_start();
			MatchTable matchTable = new MatchTable(
					no, strName, strKind,
					strFormat, strPattern);
			matchTable.set(strExch, strMatch);
			D.dprint(strExch);
			D.dprint(strMatch);
			D.dprint(matchTable);
			D.dprint_method_end();
			return matchTable;
		}

		private String strError;

		public String getError() {
			return strError;
		}

		public String matchString( Integer iFile, FileProc fileProc ) {
			D.dprint_method_start();
			String strFileOrginal = fileProc.getFileName();
			File file = new File(strFileOrginal);
			D.dprint(strKind);
			if (strKind.equals("m")) {
				BasicFileAttributes attrs;
				try {
					attrs = Files.readAttributes(file.toPath(),
							BasicFileAttributes.class);
				} catch (IOException e) {
					strError = String.format(
							"CannotReadFile_Files.readAttributes_%s"
							+ "ファイルが読めませんでした",
							strFileOrginal);
					D.dprint(strError);
					D.dprint_method_end();
					return null;
				}
			    FileTime time = attrs.lastModifiedTime();
			    D.dprint(time);
			    D.dprint(strFormat);
			    SimpleDateFormat simpleDateFormat;
				try {
					simpleDateFormat = new SimpleDateFormat(
							strFormat);
				} catch (Exception e) {
					strError = String.format(
							"InvalidDatetimeFormat_%s"
							+ "日時のフォーマットが間違っています",
							strFormat);
					D.dprint(strError);
					D.dprint_method_end();
					return null;
				}
			    strExch = simpleDateFormat.format(
			    				new Date(time.toMillis()));
			    D.dprint(strExch);
			} else if (strKind.equals("c")) {
				BasicFileAttributes attrs;
				try {
					attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
				} catch (IOException e) {
					strError = String.format(
							"CannotReadFile_Files.readAttributes_%s"
							+ "ファイルが読めませんでした",
							strFileOrginal);
					D.dprint(strError);
					D.dprint_method_end();
					return null;
				}
			    FileTime time = attrs.creationTime();
			    D.dprint(time);
			    SimpleDateFormat simpleDateFormat;
				try {
					simpleDateFormat = new SimpleDateFormat(
							strFormat);
				} catch (Exception e) {
					strError = String.format(
							"InvalidDatetimeFormat_%s"
							+ "日時のフォーマットが間違っています",
							strFormat);
					D.dprint(strError);
					D.dprint_method_end();
					return null;
				}
			    strExch = simpleDateFormat.format(
			    				new Date(time.toMillis()));
			}  else if (strKind.equals("a")) {
				BasicFileAttributes attrs;
				try {
					attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
				} catch (IOException e) {
					strError = String.format(
							"CannotReadFile_Files.readAttributes_%s"
							+ "ファイルが読めませんでした",
							strFileOrginal);
					D.dprint(strError);
					D.dprint_method_end();
					return null;
				}
			    FileTime time = attrs.lastAccessTime();
			    D.dprint(time);
			    SimpleDateFormat simpleDateFormat;
				try {
					simpleDateFormat = new SimpleDateFormat(
							strFormat);
				} catch (Exception e) {
					strError = String.format(
							"InvalidDatetimeFormat_%s"
							+ "日時のフォーマットが間違っています",
							strFormat);
					D.dprint(strError);
					D.dprint_method_end();
					return null;
				}
			    strExch = simpleDateFormat.format(
			    				new Date(time.toMillis()));
			} else if (strKind.equals("p")){
		        LocalDateTime nowDate
		        		= LocalDateTime.now();
		        D.dprint(nowDate);
		        D.dprint(strFormat);
		        DateTimeFormatter dtf1;
				try {
					dtf1 = DateTimeFormatter.ofPattern(
							strFormat);
				} catch (Exception e) {
					strError = String.format(
							"InvalidDatetimeFormat_%s"
							+ "日時のフォーマットが間違っています",
							strFormat);
					D.dprint(strError);
					D.dprint_method_end();
					return null;
				}
		        strExch = dtf1.format(nowDate);
			} else if (strKind.equals("f")) {
				List<String> list = fileProc
						.getExchFileName(pattern,
						strFormat);
				strExch = list.get(0);
				strMatch = list.get(1);
			} else if (strKind.equals("d")) {
				List<String> list = fileProc
						.getExchDirName(pattern,
						strFormat);
				strExch = list.get(0);
				strMatch = list.get(1);
			} else if (strKind.equals("#")) {
				D.dprint(iFile);
				strExch = Integer.toString(iFile);
			}
			D.dprint(strExch);
			D.dprint_method_end();
			return strExch;
		}
	}

	private Map<Integer, ExItem> mapExAnal;

	private Set<Integer> setExAnal;

	public ExAnal() {
		mapExAnal = new HashMap<Integer, ExItem>();
		setExAnal = new HashSet<Integer>();
	}

	public boolean addMapElement( Integer intMap,
			String strName, String strKind,
			String strPattern, String strFormat ) {
		D.dprint_method_start();
		D.dprint(intMap);
		D.dprint(strPattern);
		D.dprint(strFormat);
		if (setExAnal.contains(intMap)) {
			D.dprint_method_end();
			// TODO エラーメッセージ
			return false;
		}
		ExItem exItem = new ExItem(strName, strKind,
				strPattern, strFormat);
		if (strKind.matches("(f|d)")) {
			boolean bSuccess = exItem.compile();
			if( ! bSuccess ) {
				D.dprint(false);
				D.dprint_method_end();
				return false;
			}
		}
		mapExAnal.put(intMap, exItem);
		setExAnal.add(intMap);
		D.dprint(true);
		D.dprint_method_end();
		return true;
	}

	public void clear() {
		mapExAnal.forEach((k, exItem) -> {
			D.dprint(k);
			D.dprint(exItem);
			exItem.clear();
		});
	}

	public MatchTable getMatchTable( Integer intMap ) {
		D.dprint_method_start();
		if (! setExAnal.contains(intMap)) {
			D.dprint_method_end();
			return null;
		}
		ExItem exItem = mapExAnal.get(intMap);
		if (exItem == null) {
			D.dprint_method_end();
			return null;
		}
		MatchTable matchTable = exItem.getMatchTable(intMap);
		D.dprint_method_end();
		return matchTable;
	}

	public Map<Integer, String> getStringList(
			int iFile, FileProc fileProc ) {
		D.dprint_method_start();
		Map<Integer, String> map = new HashMap<>();
		mapExAnal.forEach((k, exItem) -> {
			D.dprint(k);
			D.dprint(exItem);
			String strMatch = exItem.matchString(
					iFile, fileProc);
			map.put(k, strMatch);
		});
		D.dprint_method_end();
		return map;
	}

	public boolean contains( int indexMap ) {
		return setExAnal.contains(indexMap);
	}


}
