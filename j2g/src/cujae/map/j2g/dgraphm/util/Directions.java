package cujae.map.j2g.dgraphm.util;

public abstract class Directions {
	
	public static boolean shareSignum(int direction1, int direction2){
		return Integer.signum(direction1) == Integer.signum(direction2);
	}
	
	public static boolean shareParity(int direction1, int direction2){
		return direction1 % 2 == direction2 % 2;
	}
	
	public static boolean isChangeable(Integer oldDirection, int newDirection){
		boolean b = false;
		
		if(oldDirection != null){
			if(shareParity(0, oldDirection)){
				b = shareParity(0, newDirection);
			} else{
				b = shareParity(1, oldDirection) && shareSignum(oldDirection, newDirection);
			}
		}
		
		return b;
	}
	
	public static boolean isEven(int direction){
		return shareParity(0, direction);
	}
	
	public static boolean isOdd(int direction){
		return shareParity(1, direction);
	}

}
