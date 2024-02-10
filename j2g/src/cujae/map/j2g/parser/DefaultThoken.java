package cujae.map.j2g.parser;

import java.util.FormattableFlags;
import java.util.Formatter;

public class DefaultThoken implements Thoken, Cloneable {
	
	public static String format(Thoken t, boolean alternative){		
		StringBuffer buffer = new StringBuffer();
		
		String kindPreffix = alternative ? KIND_PREFFIX_LONG 
				: KIND_PREFFIX_SHORT;
		
		buffer.append(String.format("%s:%d;", kindPreffix, t.getKind()));
		
		
		String beginLinePreffix = alternative ? BEGIN_LINE_PREFFIX_LONG 
				: BEGIN_LINE_PREFFIX_SHORT;
		
		buffer.append(String.format("%s:%d;", beginLinePreffix, t.getBeginLine()));
		
		
		String beginColumnPreffix = alternative ? BEGIN_COLUMN_PREFFIX_LONG 
				: BEGIN_COLUMN_PREFFIX_SHORT;
		
		buffer.append(String.format("%s:%d;", beginColumnPreffix, t.getBeginColumn()));
		
		
		String endLinePreffix = alternative ? END_LINE_PREFFIX_LONG 
				: END_LINE_PREFFIX_SHORT;
		
		buffer.append(String.format("%s:%s;", endLinePreffix, t.getEndLine()));
		
		String endColumnPreffix = alternative ? END_COLUMN_PREFFIX_LONG
				: END_COLUMN_PREFFIX_SHORT;
		
		buffer.append(String.format("%s:%d;", endColumnPreffix, t.getEndColumn()));
		
		
		String imagePreffix = alternative ? IMAGE_PREFFIX_LONG 
				: IMAGE_PREFFIX_SHORT;
		
		buffer.append(String.format("%s:%s;", imagePreffix, t.getImage()));
		
		
		String isVariablePreffix = alternative ? IS_VARIABLE_PREFFIX_LONG
				: IS_VARIABLE_PREFFIX_SHORT;
		
		buffer.append(String.format("%s:%b;", isVariablePreffix, t.isVariable()));
		
		String signumPreffix = alternative ? SIGNUM_FREFFIX_LONG 
				: SIGNUM_FREFFIX_SHORT;
		
		buffer.append(String.format("%s:%d;", signumPreffix, t.getSignum()));
		
		
		String priorityPreffix = alternative ? PRIORITY_PREFFIX_LONG 
				: PRIORITY_PREFFIX_SHORT;
		
		buffer.append(String.format("%s:%d;", priorityPreffix, t.getPriority()));		
		
		return buffer.toString();
	}
	
	private final int kind;
	private final int beginLine;
	private final int beginColumn;
	private final int endLine;
	private final int endColumn;
	private final String image;
	private final boolean isVariable;
	private final int signum;
	private final int priority;

	public DefaultThoken(int kind, int beginLine, int beginColumn, int endLine,
			int endColumn, String image, boolean isVariable, int signum,
			int priority) {
		super();
		this.kind = kind;
		this.beginLine = beginLine;
		this.beginColumn = beginColumn;
		this.endLine = endLine;
		this.endColumn = endColumn;
		this.image = image;
		this.isVariable = isVariable;
		this.signum = signum;
		this.priority = priority;
	}	

	@Override
	public int getKind() {
		return kind;
	}

	@Override
	public int getBeginLine() {
		return beginLine;
	}

	@Override
	public int getBeginColumn() {
		return beginColumn;
	}

	@Override
	public int getEndLine() {
		return endLine;
	}

	@Override
	public int getEndColumn() {
		return endColumn;
	}	

	@Override
	public String getImage() {
		return image;
	}

	@Override
	public boolean isVariable() {
		return isVariable;
	}

	@Override
	public int getSignum() {
		return signum;
	}	

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public void formatTo(Formatter formatter, int flags, int width,
			int precision) {			
		formatter.format("%s", format(this, (flags & FormattableFlags.ALTERNATE) 
				== FormattableFlags.ALTERNATE));		
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.getMessage());
		}
	}

	@Override
	public String toString() {
		return format(this, false);
	}

}
