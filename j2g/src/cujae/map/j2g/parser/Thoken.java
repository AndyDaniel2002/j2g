package cujae.map.j2g.parser;

import java.util.Formattable;

public interface Thoken extends Formattable{	
	
	public static final String KIND_PREFFIX_SHORT = "k";
	
	public static final String KIND_PREFFIX_LONG = "kind";
	
	public static final String BEGIN_LINE_PREFFIX_SHORT = "bL";
	
	public static final String BEGIN_LINE_PREFFIX_LONG = "beginLine";
	
	public static final String BEGIN_COLUMN_PREFFIX_SHORT = "bC";
	
	public static final String BEGIN_COLUMN_PREFFIX_LONG = "beginColumn";
	
	public static final String END_LINE_PREFFIX_SHORT = "eL";
	
	public static final String END_LINE_PREFFIX_LONG = "endLine";
	
	public static final String END_COLUMN_PREFFIX_SHORT = "eC";
	
	public static final String END_COLUMN_PREFFIX_LONG = "endColumn";
	
	public static final String IMAGE_PREFFIX_SHORT = "i";
	
	public static final String IMAGE_PREFFIX_LONG = "image";
	
	public static final String IS_VARIABLE_PREFFIX_SHORT = "isv";
	
	public static final String IS_VARIABLE_PREFFIX_LONG = "isVariable";
	
	public static final String SIGNUM_FREFFIX_SHORT = "s";
	
	public static final String SIGNUM_FREFFIX_LONG = "signum";
	
	public static final String PRIORITY_PREFFIX_SHORT = "p";
	
	public static final String PRIORITY_PREFFIX_LONG = "priority";
	
	public int getKind();
	
	public int getBeginLine();
	
	public int getBeginColumn();
	
	public int getEndLine();
	
	public int getEndColumn();	

	public String getImage();
	
	public boolean isVariable();
	
	public int getSignum();
	
	public int getPriority();
	
}
