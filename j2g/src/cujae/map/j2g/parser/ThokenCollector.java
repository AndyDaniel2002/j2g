package cujae.map.j2g.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThokenCollector {
	
	public static final int RULE_TYPE = 1;

	public static final int PROCEDURE_TYPE = 2;
	
	public static final int CONDITIONAL_TYPE = 4;
	
	public static final int REGEX0 = 0;
	
	public static final int REGEX1 = 1;
	
	public static final int REGEX2 = 2;
	
	public static final String EC_NAME_REGEX0 = "[^\\(]*\\(([^\\s\\(\\)]+)\\)[^\\)]*";
	
	public static final String EC_NAME_REGEX1 = "[^\\[]*\\[([^\\s\\(\\)]+)\\][^\\]]*";
	
	public static final String EC_NAME_REGEX2 = "[^\\{]*\\{([^\\s{}]+)\\}[^\\}]*";
	
	public static final Pattern EC_NAME_PATTERN0 = Pattern.compile(EC_NAME_REGEX0);
	
	public static final Pattern EC_NAME_PATTERN1 = Pattern.compile(EC_NAME_REGEX1);
	
	public static final Pattern EC_NAME_PATTERN2 = Pattern.compile(EC_NAME_REGEX2);
	
	public static String getEndCommentName(String input, int regex){
		String name = null;
		
		Matcher m = null;

		switch(regex){
		case 0:
			m = EC_NAME_PATTERN0.matcher(input);
			if(m.matches()){
				name = m.group(1);
			}
			break;
		case 1: 
			m = EC_NAME_PATTERN1.matcher(input);
			if(m.matches()){
				name = m.group(1);
			}
			break;
		case 2: 
			m = EC_NAME_PATTERN2.matcher(input);
			if(m.matches()){
				name = m.group(1);
			}
			break;
		}
		
		return name;
	}
	
	public static String getEndCommentName(String input){
		return getEndCommentName(input, REGEX0);
	}
	
	public static Integer getVariableSignum(String image, Collection<Thoken> c, boolean lowestPriority){		
		int priority = lowestPriority ? Integer.MAX_VALUE : Integer.MIN_VALUE;
		int signum = 0;	
		int frecuency = 0;
		for(Iterator<Thoken> it = c.iterator(); it.hasNext();){
			Thoken next = it.next();
			if(next.isVariable()){
				String nextImage = next.getImage();
				boolean commit = false;			
				if((image == null && nextImage == null) || 
						(image != null && image.equals(nextImage))){
					
					frecuency ++;
					
					int nextPriority = next.getPriority();
					
					if(lowestPriority){
						commit = nextPriority < priority;
					} else{
						commit = nextPriority > priority;
					}
					
					if(commit){
						priority = nextPriority;
						signum = next.getSignum();
					}
				}
			}

		}		
		return (frecuency != 0) ? signum : null;
	}
	
	public static Map<String, Integer> getImageSignum(Collection<Thoken> c, boolean lowestPriority){
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		for(Iterator<Thoken> it = c.iterator(); it.hasNext();){
			Thoken next = it.next();
			
			if(next.isVariable()){				
				String image = next.getImage();
				Integer signum = getVariableSignum(image, c, lowestPriority);
				map.put(image, signum);
			}
		}		
		
		return map;
	}
	
	public static Map<Integer, Set<String>> getSignumImage(Map<String, Integer> map){
		Map<Integer, Set<String>> map2 = new HashMap<Integer, Set<String>>();
		
		for(Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator(); it.hasNext();){
			Map.Entry<String, Integer> next = it.next();
			
			/**
			 * ensure sign as an atomic value
			 */
			int signum = 0;
			if(next.getValue() != null){
				signum = Integer.signum(next.getValue());
			}
			
			if(map2.get(signum) == null){			
				map2.put(signum, new HashSet<String>());
			}
			map2.get(signum).add(next.getKey());
		}		
		
		return map2;
	}
	
	public static Map<String, Integer> postAnalysis(Collection<Thoken> c, boolean lowestPriority){		
		
		Map<String, Integer> imageSignum = getImageSignum(c, lowestPriority);
		Map<Integer, Set<String>> signumImage = getSignumImage(imageSignum);
		
		int lt0 = 0;
		int eq0 = 0;
		int gt0 = 0;
		
		if(signumImage.get(-1) != null){
			lt0 = signumImage.get(-1).size();
		}
		
		if(signumImage.get(0) != null){
			eq0 = signumImage.get(0).size();
		}
		
		if(signumImage.get(1) != null){
			gt0 = signumImage.get(1).size();
		}
		
		if(gt0 != 0){
			Set<String> s = signumImage.get(0);
			if(s != null){
				for(Iterator<String> it = s.iterator(); it.hasNext();){
					imageSignum.put(it.next(), -1);
				}
			}
		} else{
			if(lt0 != 0 && eq0 == 1){
				Set<String> s = signumImage.get(0);
				if(s != null){
					for(Iterator<String> it = s.iterator(); it.hasNext();){
						imageSignum.put(it.next(), 1);
					}
				}
			}
		}
		
		return imageSignum;
	}
	
	private final List<Thoken> thokens = new ArrayList<Thoken>();
	
	private int type;
	
	private int regex;
	
	private String name;
	
	public int getType(){
		return this.type;
	}
	
	public void setType(int type){
		this.type = type;
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRegex() {
		return regex;
	}

	public void setRegex(int regex) {
		this.regex = regex;
	}

	public boolean add(int kind, int beginLine, int beginColumn, int endLine,
			int endColumn, String image, boolean isVariable, int signum, int priority){
		return thokens.add(new DefaultThoken(kind, beginLine, beginColumn, endLine, 
				endColumn, image, isVariable, signum, priority));
	}

	public boolean isEmpty() {
		return thokens.isEmpty();
	}

	public Iterator<Thoken> iterator() {
		final Iterator<Thoken> it = thokens.iterator();
		return new Iterator<Thoken>() {

			@Override
			public boolean hasNext() {
				return it.hasNext();
			}

			@Override
			public Thoken next() {
				return it.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public void clear() {
		thokens.clear();
	}

	public Thoken get(int index) {
		return thokens.get(index);
	}

	public int indexOf(Object o) {
		return thokens.indexOf(o);
	}
	
	public Integer getVariableSignum(String image, boolean lowestPriority){
		return getVariableSignum(image, thokens, lowestPriority);
	}
	
	public Integer getVariableSignum(String image){
		return getVariableSignum(image, false);
	}
	
	public Map<String, Integer> getPostAnalysis(boolean lowestPriority){
		return postAnalysis(thokens, lowestPriority);
	}
	
	public Map<String, Integer> getPostAnalysis(){
		return postAnalysis(thokens, false);
	}

}
