package cujae.map.j2g.dgraphm.util;


public abstract class Flags {
	
	public static final int INPUT_FLAG = 1;
	
	public static final int OUTPUT_FLAG = 2;
	
	public static final int CONTROL_UNKNOWN_FLAG = 4;
	
	public static final int PARAMETRIC_VARIABLE_FLAG = 8;	
	
	public static final int DESIGN_PARAMETER_FLAG = 12;	
	
	public static final int FREEDOM_DEGREE_FLAG = 16;
	
	public static final int DEFICIENCE_FLAG = 32;
	
	public static final int MARKED_EDGE_FLAG = 64;
	
	public static final int CANDIDATE_FREEDOM_DEGREE_FLAG = 128;
	
	public static final int CANDIDATE_DEFICIENCE_FLAG = 256;
	
	public static final int ERROR_FLAG0 = 512;
	
	public static final int ERROR_FLAG1 = 1024;
	
	public static final int ERROR_FLAG2 = 2048;
	
	public static boolean isOutput(int flags){
		return (flags & OUTPUT_FLAG) == OUTPUT_FLAG;
	}
	
	public static boolean isInput(int flags){
		return (flags & INPUT_FLAG) == INPUT_FLAG;
	}	
	
	public static int clearInputOutput(int flags){
		return flags & ~(INPUT_FLAG | OUTPUT_FLAG);
	}
	
	public static int setInput(int flags){
		return clearInputOutput(flags) | INPUT_FLAG;
	}
	
	public static int setOutput(int flags){
		return clearInputOutput(flags) | OUTPUT_FLAG;
	}
	
	public static boolean isStateVariable(int flags){
		return (flags & PARAMETRIC_VARIABLE_FLAG) == 0;
	}	
	
	public static boolean isControlUnknown(int flags){
		return (flags & DESIGN_PARAMETER_FLAG) == CONTROL_UNKNOWN_FLAG;
	}
	
	public static boolean isParametricVariable(int flags){
		return (flags & PARAMETRIC_VARIABLE_FLAG) == PARAMETRIC_VARIABLE_FLAG;
	}
	
	public static boolean isDesignParameter(int flags){
		return (flags & DESIGN_PARAMETER_FLAG) == DESIGN_PARAMETER_FLAG ;
	}
	
	public static int setStateVariable(int flags){
		return (flags & ~DESIGN_PARAMETER_FLAG);
	}
	
	public static int setControlUnknown(int flags){
		return ((flags & ~DESIGN_PARAMETER_FLAG) | CONTROL_UNKNOWN_FLAG);
	}
	
	public static int setParametricVariable(int flags){
		return ((flags & ~DESIGN_PARAMETER_FLAG) | PARAMETRIC_VARIABLE_FLAG);
	}
	
	public static int setDesignParameter(int flags){
		return ((flags & ~DESIGN_PARAMETER_FLAG) | DESIGN_PARAMETER_FLAG);
	}
	
	public static boolean isFreedomDegree(int flags){
		return (flags & FREEDOM_DEGREE_FLAG) == FREEDOM_DEGREE_FLAG;
	}
	
	public static int setFreedomDegree(int flags){
		return (flags | FREEDOM_DEGREE_FLAG);
	}
	
	public static int unsetFreedomDegree(int flags){
		return (flags & ~FREEDOM_DEGREE_FLAG);
	}
	
	public static boolean isDeficience(int flags){
		return (flags & DEFICIENCE_FLAG) == DEFICIENCE_FLAG;
	}
	
	public static int setDeficience(int flags){
		return (flags | DEFICIENCE_FLAG);
	}
	
	public static int unsetDeficience(int flags){
		return (flags & ~DEFICIENCE_FLAG);
	}
	
	public static boolean isMarkedEdge(int flags){
		return (flags & MARKED_EDGE_FLAG) == MARKED_EDGE_FLAG;
	}
	
	public static int setMarkedEdge(int flags){
		return (flags | MARKED_EDGE_FLAG);
	}
	
	public static int unsetMarkedEdge(int flags){
		return (flags & ~MARKED_EDGE_FLAG);
	}
	
	public static boolean isCandidateFreedomDegree(int flags){
		return (flags & CANDIDATE_FREEDOM_DEGREE_FLAG) == CANDIDATE_FREEDOM_DEGREE_FLAG;
	}
	
	public static boolean isCandidateDeficience(int flags){
		return (flags & CANDIDATE_DEFICIENCE_FLAG) == CANDIDATE_DEFICIENCE_FLAG;
	}
	
	public static boolean isCandidate(int flags){
		return isCandidateFreedomDegree(flags) || isCandidateDeficience(flags);
	}
	
	public static int clearCandidate(int flags){
		return (flags & ~(CANDIDATE_FREEDOM_DEGREE_FLAG | CANDIDATE_DEFICIENCE_FLAG));
	}
	
	public static int setCandidateFreedomDegree(int flags){
		return clearCandidate(flags) | CANDIDATE_FREEDOM_DEGREE_FLAG;
	}
	
	public static int setCandidateDeficience(int flags){
		return clearCandidate(flags) | CANDIDATE_DEFICIENCE_FLAG;
	}
	
	public static int setError0(int flags){
		return flags | ERROR_FLAG0;
	}
	
	public static int unsetError0(int flags){
		return flags & ~ERROR_FLAG0;
	}	
	
	public static boolean isError0(int flags){
		return (flags & ERROR_FLAG0) != 0;
	}
	
	public static int setError1(int flags){
		return flags | ERROR_FLAG1;
	}
	
	public static int unsetError1(int flags){
		return flags & ~ERROR_FLAG1;
	}
	
	public static boolean isError1(int flags){
		return (flags & ERROR_FLAG1) != 0;
	}
	
	public static int setError2(int flags){
		return flags | ERROR_FLAG2;
	}
	
	public static int unsetError2(int flags){
		return flags & ~ERROR_FLAG2;
	}
	
	public static boolean isError2(int flags){
		return (flags & ERROR_FLAG2) != 0;
	}
	
	public static boolean isError(int flags){
		return isError0(flags) || isError1(flags) || isError2(flags);
	}

}
