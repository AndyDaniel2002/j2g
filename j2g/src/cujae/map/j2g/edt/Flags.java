package cujae.map.j2g.edt;


/**
 * A bit set<p>
 * 
 * <table> 
 * 
 * <tr><th>Bit</th><th>Value (If is set)</th><th>Meaning</th></tr> 
 * <tr><td>2^0</td><th>1</th><td>INPUT_FLAG</td></tr> 
 * <tr><td>2^1</td><th>2</th><td>OUTPUT_FLAG</td></tr> 
 * <tr><td>2^2</td><th>4</th><td>STATE_VARIABLE_FLAG</td></tr>
 * <tr><td>2^3</td><th>8</th><td>PARAMETRIC_VARIABLE_FLAG</td></tr>
 * <tr><td>2^4</td><th>16</th><td>CONTROL_UNKNOWN_FLAG</td></tr>
 * <tr><td>2^5</td><th>32</th><td>DESIGN_PARAMETER_FLAG</td></tr>
 * <tr><td>2^6</td><th>64</th><td>FREEDOM_DEGREE_FLAG</td></tr>
 * <tr><td>2^7</td><th>128</th><td>DEFICIENCE_FLAG</td></tr>
 * <tr><td>2^15</td><th>32768</th><td>ERROR15_FLAG</td></tr>
 * <tr><td>2^23</td><th>8388608</th><td>ERROR23_FLAG</td></tr>
 * <tr><td>2^31</td><th>-2147483648</th><td>ERROR31_FLAG</td></tr>
 * 
 * </table>
 * 
 * 
 * @author aromero
 *
 */
public abstract class Flags {

	public static final int INPUT_FLAG = 1;
	
	public static final String INPUT_SYMBOL = "E";
	
	public static final int OUPUT_FLAG = 2;
	
	public static final String OUTPUT_SYMBOL = "S";
	
	public static final int STATE_VARIABLE_FLAG = 4;
	
	public static final String STATE_VARIABLE_SYMBOL = "T";
	
	public static final int PARAMETRIC_VARIABLE_FLAG = 8;
	
	public static final String PARAMETRIC_VARIABLE_SYMBOL = "R";
	
	public static final int CONTROL_UNKNOWN_FLAG = 16;
	
	public static final String CONTROL_UNKNOWN_SYMBOL = "Y";

	public static final int DESIGN_PARAMETER_FLAG = 32;
	
	public static final String DESIGN_PARAMETER_SYMBOL = "Z";

	public static final int FREEDOM_DEGREE_FLAG = 64;
	
	public static final String FREEDOM_DEGREE_SYMBOL = "L";

	public static final int DEFICIENCE_FLAG = 128;
	
	public static final String DEFICIENCE_SYMBOL = "D";
	
	public static final int MASK = 
			INPUT_FLAG | OUPUT_FLAG |
			STATE_VARIABLE_FLAG | PARAMETRIC_VARIABLE_FLAG |
			CONTROL_UNKNOWN_FLAG | DESIGN_PARAMETER_FLAG |
			FREEDOM_DEGREE_FLAG | DEFICIENCE_FLAG;
	
	public static final int ERROR15_FLAG = 32768;
	
	public static final int ERROR23_FLAG = 8388608;
	
	public static final int ERROR31_FLAG = -2147483648;

	public static boolean isSet(int flags, int mod){
		return (flags & mod) != 0;
	}
	
	public static int set(int flags, int mod){
		return (flags | mod);
	}
	
	public static int unset(int flags, int mod){
		return (flags & ~ mod);
	}
	
	public static boolean isInput(int flags){
		return isSet(flags, INPUT_FLAG);
	}
	
	public static boolean isOutput(int flags){
		return isSet(flags, OUPUT_FLAG);
	}
	
	public static boolean isStateVariable(int flags){
		return isSet(flags, STATE_VARIABLE_FLAG);
	}
	
	public static boolean isParametricVariable(int flags){
		return isSet(flags, PARAMETRIC_VARIABLE_FLAG);
	}
	
	public static boolean isControlUnknown(int flags){
		return isSet(flags, CONTROL_UNKNOWN_FLAG);
	}

	public static boolean isDesignParameterFlag(int flags){
		return isSet(flags, DESIGN_PARAMETER_FLAG);
	}
	
	public static boolean isFreedomDegree(int flags){
		return isSet(flags, FREEDOM_DEGREE_FLAG);
	}
	
	public static boolean isDeficience(int flags){
		return isSet(flags, DEFICIENCE_FLAG);
	}
	
	public static boolean isError15(int flags){
		return isSet(flags, ERROR15_FLAG);
	}
	
	public static boolean isError23(int flags){
		return isSet(flags, ERROR23_FLAG);
	}
	
	public static boolean isError31(int flags){
		return isSet(flags, ERROR31_FLAG);
	}
	
	public static String flags2Str(int flags){
		StringBuffer sb = new StringBuffer();
		
		if(isSet(flags, INPUT_FLAG)){
			sb.append(INPUT_SYMBOL);
		}
		
		if(isSet(flags, OUPUT_FLAG)){
			sb.append(OUTPUT_SYMBOL);
		}
		
		if(isSet(flags, STATE_VARIABLE_FLAG)){
			sb.append(STATE_VARIABLE_SYMBOL);
		}
		
		if(isSet(flags, PARAMETRIC_VARIABLE_FLAG)){
			sb.append(STATE_VARIABLE_SYMBOL);
		}
		
		if(isSet(flags, CONTROL_UNKNOWN_FLAG)){
			sb.append(CONTROL_UNKNOWN_SYMBOL);
		}
		
		if(isSet(flags, DESIGN_PARAMETER_FLAG)){
			sb.append(CONTROL_UNKNOWN_SYMBOL);
		}
		
		if(isSet(flags, FREEDOM_DEGREE_FLAG)){
			sb.append(DESIGN_PARAMETER_SYMBOL);
		}
		
		if(isSet(flags, DEFICIENCE_FLAG)){
			sb.append(DEFICIENCE_SYMBOL);
		}		
		
		return sb.toString();
	}
	
	public static int str2Flags(String s){
		return 0;
	}

}
