package cujae.map.j2g.content.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cujae.map.j2g.content.MDocument;

public class TreeNode {
	
	private int id;
	private Pattern pattern;
	private Matcher matcher;
	private List<TreeNode> childs;
	
	private List<TreeNode> getChilds(){
		if(childs == null){
			childs = new ArrayList<TreeNode>();
		}
		return childs;
	}
	
	public TreeNode(int id, String regex, int flags){
		this.id = id;
		this.pattern = Pattern.compile(regex);
	}
	
	public int getId(){
		return id;
	}
	
	public Pattern getPattern(){
		return pattern;
	}
	
	public Matcher getMatcher(){
		return matcher;
	}
	
	public boolean match(CharSequence input){
		matcher = getPattern().matcher(input);
		return matcher.matches();
	}
	
	public TreeNode next(CharSequence input){
		for(Iterator<TreeNode> it = getChilds().iterator(); it.hasNext();){
			TreeNode next = it.next();
			if(next.match(input)){
				return next;
			}
		}
		return null;
	}
	
	public void action(TreeEvent event, MDocument document, Logger logger){
		if(logger != null){
			logger.log(Level.INFO, String.format("[%d;%d;'%s']", id, event.getLineNumber(), event.getInput()));
		}
	}
	
	public boolean addChild(TreeNode child){
		return getChilds().add(child);
	}
	
	public boolean removeChild(TreeNode child){
		return getChilds().remove(child);
	}

}
