package cujae.map.j2g.graphml;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;

import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

public class AbbGraphmlCodec {
	
	public static final String SUFFIX = ".graphml";
	
	public static final String UTF_8 = "UTF-8";
	
	public static final String DEFAULT_ENCODING = UTF_8;
	
	public static Map<Class<?>, ValueHandler> createDefaultProvider(){
		Map<Class<?>, ValueHandler> map = new HashMap<Class<?>, ValueHandler>();
	
		map.put(Map.class, new MapValueHandler());
		map.put(String.class, new StringValueHandler());
		
		return map;
	}
	
	public static void serialize(mxGraph graph, OutputStream os, Map<Class<?>, ValueHandler> vhp, String encoding) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException{
		
		DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
        DOMImplementationLS domImplLS = (DOMImplementationLS)registry.getDOMImplementation(AbbGraphmlConstants.LS); 
        
        Document document = encode(graph, vhp);
        
        LSSerializer ser = domImplLS.createLSSerializer();
        ser.getDomConfig().setParameter(AbbGraphmlConstants.FORMAT_PRETTY_PRINT, Boolean.TRUE);
        LSOutput out = domImplLS.createLSOutput();
        out.setByteStream(os); 
        out.setEncoding(encoding);
        ser.write(document, out); 
	}
	
	public static void serialize(mxGraph graph, OutputStream os, String encoding) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException{
		serialize(graph, os, createDefaultProvider(), DEFAULT_ENCODING);
	}
	
	public static void serialize(mxGraph graph, OutputStream os, Map<Class<?>, ValueHandler> vhp) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException{
		serialize(graph, os, vhp, DEFAULT_ENCODING);
	}
	
	public static void serialize(mxGraph graph, OutputStream os) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException{
		serialize(graph, os, createDefaultProvider(), DEFAULT_ENCODING);
	}

	public static Document encode(mxGraph graph, Map<Class<?>, ValueHandler> vhp) throws ParserConfigurationException {
		
		DocumentBuilderFactory instance = DocumentBuilderFactory.newInstance();		
		instance.setNamespaceAware(true);
		
		DocumentBuilder builder = instance.newDocumentBuilder();
		
		Document document = builder.newDocument();
		
		Element graphmlElement = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.GRAPHML);
		
		Element key0 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.KEY);
		key0.setAttribute(AbbGraphmlConstants.ID, AbbGraphmlConstants.D0);
		key0.setAttribute(AbbGraphmlConstants.FOR, AbbGraphmlConstants.NODE);
		key0.setAttribute(AbbGraphmlConstants.ATTR_NAME, AbbGraphmlConstants.URL);
		key0.setAttribute(AbbGraphmlConstants.ATTR_TYPE, AbbGraphmlConstants.STRING);
		
		Element key1 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.KEY);
		key1.setAttribute(AbbGraphmlConstants.ID, AbbGraphmlConstants.D1);
		key1.setAttribute(AbbGraphmlConstants.FOR, AbbGraphmlConstants.NODE);
		key1.setAttribute(AbbGraphmlConstants.ATTR_NAME, AbbGraphmlConstants.DESCRIPTION);
		key1.setAttribute(AbbGraphmlConstants.ATTR_TYPE, AbbGraphmlConstants.STRING);
		
		Element key2 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.KEY);
		key2.setAttribute(AbbGraphmlConstants.ID, AbbGraphmlConstants.D2);
		key2.setAttribute(AbbGraphmlConstants.FOR, AbbGraphmlConstants.NODE);
		key2.setAttribute(AbbGraphmlConstants.YFILES_TYPE, AbbGraphmlConstants.NODEGRAPHICS);
		
		Element key3 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.KEY);
		key3.setAttribute(AbbGraphmlConstants.ID, AbbGraphmlConstants.D3);
		key3.setAttribute(AbbGraphmlConstants.FOR, AbbGraphmlConstants.EDGE);
		key3.setAttribute(AbbGraphmlConstants.ATTR_NAME, AbbGraphmlConstants.URL);
		key3.setAttribute(AbbGraphmlConstants.ATTR_TYPE, AbbGraphmlConstants.STRING);
		
		Element key4 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.KEY);
		key4.setAttribute(AbbGraphmlConstants.ID, AbbGraphmlConstants.D4);
		key4.setAttribute(AbbGraphmlConstants.FOR, AbbGraphmlConstants.EDGE);
		key4.setAttribute(AbbGraphmlConstants.ATTR_NAME, AbbGraphmlConstants.DESCRIPTION);
		key4.setAttribute(AbbGraphmlConstants.ATTR_TYPE, AbbGraphmlConstants.STRING);
		
		Element key5 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.KEY);
		key5.setAttribute(AbbGraphmlConstants.ID, AbbGraphmlConstants.D5);
		key5.setAttribute(AbbGraphmlConstants.FOR, AbbGraphmlConstants.EDGE);
		key5.setAttribute(AbbGraphmlConstants.YFILES_TYPE, AbbGraphmlConstants.EDGEGRAPHICS);
		
		graphmlElement.appendChild(key0);
		graphmlElement.appendChild(key1);
		graphmlElement.appendChild(key2);
		graphmlElement.appendChild(key3);
		graphmlElement.appendChild(key4);
		graphmlElement.appendChild(key5);
		graphmlElement.appendChild(encodeGraph(graph, vhp, document));		
		
		document.appendChild(document.createComment(AbbGraphmlConstants.COMMENT));
		document.appendChild(graphmlElement);
		
		return document;
	}

	private static Node encodeGraph(mxGraph graph, Map<Class<?>, ValueHandler> vhp, Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.GRAPH);
		e.setAttribute(AbbGraphmlConstants.ID, AbbGraphmlConstants.G);
		e.setAttribute(AbbGraphmlConstants.EDGEDEFAULT, AbbGraphmlConstants.DIRECTED);
		
		Object [] vertexes = graph.getChildVertices(graph.getDefaultParent());		
		for(int i = 0; i < vertexes.length; i ++){
			e.appendChild(encodeCell(graph, vhp, (mxICell)vertexes[i], document));
		}
		
		Object [] edges = graph.getChildEdges(graph.getDefaultParent());		
		for(int i = 0; i < edges.length; i ++){
			e.appendChild(encodeCell(graph, vhp, (mxICell)edges[i], document));
		}	
		
		return e;
	}

	private static Node encodeCell(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell, Document document) {
		Element e = null;
		
		if(cell.isVertex()){			
			e = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.NODE);
			
			Element data0 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.DATA);
			data0.setAttribute(AbbGraphmlConstants.KEY, AbbGraphmlConstants.D0);
			
			Element data1 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.DATA);
			data1.setAttribute(AbbGraphmlConstants.KEY, AbbGraphmlConstants.D1);
			
			Object value = cell.getValue();
			if(value != null){
				ValueHandler vh = vhp.get(value.getClass());
				if(vh != null){
					data0.appendChild(document.createCDATASection(vh.getURL(value)));
					data1.appendChild(document.createCDATASection(vh.getDescription(value)));
				}
			}
			
			Element data2 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.DATA);
			data2.setAttribute(AbbGraphmlConstants.KEY, AbbGraphmlConstants.D2);
			data2.appendChild(encodeShapeNode(graph, vhp, cell, document));			
			
			e.appendChild(data0);
			e.appendChild(data1);
			e.appendChild(data2);
			
		}
		else{
			e = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.EDGE);
			e.setAttribute(AbbGraphmlConstants.SOURCE, cell.getTerminal(true).getId());
			e.setAttribute(AbbGraphmlConstants.TARGET, cell.getTerminal(false).getId());
			
			Element data3 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.DATA);
			data3.setAttribute(AbbGraphmlConstants.KEY, AbbGraphmlConstants.D3);
			
			Element data4 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.DATA);
			data4.setAttribute(AbbGraphmlConstants.KEY, AbbGraphmlConstants.D4);
			
			Object value = cell.getValue();
			if(value != null){
				ValueHandler vh = vhp.get(value.getClass());
				if(vh != null){
					data3.appendChild(document.createCDATASection(vh.getURL(value)));
					data4.appendChild(document.createCDATASection(vh.getDescription(value)));
				}
			}
			
			Element data5 = document.createElementNS(AbbGraphmlConstants.XML_NS, AbbGraphmlConstants.DATA);
			data5.setAttribute(AbbGraphmlConstants.KEY, AbbGraphmlConstants.D5);
			data5.appendChild(encodePolyLineEdge(graph, vhp, cell, document));

			e.appendChild(data3);
			e.appendChild(data4);
			e.appendChild(data5);
		}
		e.setAttribute(AbbGraphmlConstants.ID, cell.getId());
		
		return e;
	}

	private static Node encodePolyLineEdge(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell, Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.POLY_LINE_EDGE);
		e.appendChild(encodePath(graph, vhp, cell, document));
		e.appendChild(encodeLineStyle(graph, vhp, cell, document));
		e.appendChild(encodeArrows(graph, vhp, cell, document));
		e.appendChild(encodeEdgeLabel(graph, vhp, cell, document));
		e.appendChild(encodeBendStyle(graph, vhp, cell, document));
		return e;
	}

	private static Node encodeBendStyle(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell, Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.BEND_STYLE);
		e.setAttribute(AbbGraphmlConstants.SMOOTHED, String.valueOf(false));
		return e;
	}

	private static Node encodeEdgeLabel(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell, Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.EDGE_LABEL);
		
		Object value = cell.getValue();
		if(value != null){
			ValueHandler vh = vhp.get(value.getClass());
			if(vh != null){
				e.appendChild(document.createTextNode(vh.getName(value)));
			}
		}

		return e;
	}

	private static Node encodeArrows(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell, Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.ARROWS);		
		
		Object startArrow = getStyleValueFor(graph, cell, mxConstants.STYLE_STARTARROW);		
		Object endArrow = getStyleValueFor(graph, cell, mxConstants.STYLE_ENDARROW);
		
		if(startArrow instanceof String && !((String)startArrow).equals(mxConstants.NONE)){
			e.setAttribute(AbbGraphmlConstants.SOURCE, AbbGraphmlConstants.STANDARD);			
		}
		else{
			e.setAttribute(AbbGraphmlConstants.TARGET, mxConstants.NONE);
		}
		
		if(endArrow instanceof String && !((String)endArrow).equals(mxConstants.NONE)){
			e.setAttribute(AbbGraphmlConstants.TARGET, AbbGraphmlConstants.STANDARD);			
		}
		else{
			e.setAttribute(AbbGraphmlConstants.TARGET, mxConstants.NONE);
		}		
		
		return e;
	}

	private static Node encodeLineStyle(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell, Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.LINE_STYLE);
		
		Object strokeColor = getStyleValueFor(graph, cell, mxConstants.STYLE_STROKECOLOR);
		Object strokeWidth = getStyleValueFor(graph, cell, mxConstants.STYLE_STROKEWIDTH);
		Object styleDashed = getStyleValueFor(graph, cell, mxConstants.STYLE_DASHED);
		
		if(strokeColor instanceof String){
			e.setAttribute(AbbGraphmlConstants.COLOR, (String)strokeColor);
		}
		else{
			e.setAttribute(AbbGraphmlConstants.COLOR, AbbGraphmlConstants.DEFAULT_BORDERCOLOR);
		}

		if(strokeWidth instanceof Double){
			e.setAttribute(AbbGraphmlConstants.WIDTH, String.valueOf((Double)strokeWidth));
		}
		else{
			e.setAttribute(AbbGraphmlConstants.WIDTH, AbbGraphmlConstants.DEFAULT_BORDERWIDTH);
		}
		
		if(styleDashed instanceof Boolean && ((Boolean)styleDashed)){
			e.setAttribute(AbbGraphmlConstants.TYPE, mxConstants.STYLE_DASHED);
		}
		else{
			e.setAttribute(AbbGraphmlConstants.TYPE, AbbGraphmlConstants.LINE);
		}		

		return e;
	}

	private static Node encodePath(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell,	Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.PATH);
		e.setAttribute(AbbGraphmlConstants.SX, "0.0");
		e.setAttribute(AbbGraphmlConstants.SY, "0.0");
		e.setAttribute(AbbGraphmlConstants.TX, "0.0");
		e.setAttribute(AbbGraphmlConstants.TY, "0.0");
		
		List<mxPoint> points = cell.getGeometry().getPoints();
		
		if(points != null){
			for(Iterator<mxPoint> it = points.iterator(); it.hasNext();){
				e.appendChild(encodePoint(it.next(), vhp, document));
			}
		}

		return e;
	}

	private static Node encodePoint(mxPoint point, Map<Class<?>, ValueHandler> vhp, Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.POINT);
		e.setAttribute(AbbGraphmlConstants.X, String.valueOf(point.getX()));
		e.setAttribute(AbbGraphmlConstants.Y, String.valueOf(point.getY()));
		return e;
	}

	private static Node encodeShapeNode(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell, Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.SHAPE_NODE);
		e.appendChild(encodeGeometry(graph, vhp, cell, document));
		e.appendChild(encodeFill(graph, vhp, cell, document));
		e.appendChild(encodeBorderStyle(graph, vhp, cell, document));
		e.appendChild(encodeNodeLabel(graph, vhp, cell, document));
		e.appendChild(encodeShape(graph, vhp, cell, document));
		return e;
	}

	private static Node encodeShape(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell, Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.SHAPE);
		
		Object rounded = getStyleValueFor(graph, cell, mxConstants.STYLE_ROUNDED);
		
		if(rounded instanceof Boolean && ((Boolean)rounded)){
			e.setAttribute(AbbGraphmlConstants.TYPE, AbbGraphmlConstants.ROUNDRECTANGLE);
		}
		else{
			e.setAttribute(AbbGraphmlConstants.TYPE, AbbGraphmlConstants.RECTANGLE);
		}	
		
		return e;
	}

	private static Node encodeNodeLabel(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell, Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.NODE_LABEL);
		
		Object value = cell.getValue();
		if(value != null){
			ValueHandler vh = vhp.get(value.getClass());
			if(vh != null){
				e.appendChild(document.createTextNode(vh.getName(value)));
			}
		}

		return e;
	}

	private static Node encodeBorderStyle(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell,	Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.BORDER_STYLE);
		
		Object strokeColor = getStyleValueFor(graph, cell, mxConstants.STYLE_STROKECOLOR);
		Object strokeWidth = getStyleValueFor(graph, cell, mxConstants.STYLE_STROKEWIDTH);
		Object styleDashed = getStyleValueFor(graph, cell, mxConstants.STYLE_DASHED);
		
		if(strokeColor instanceof String){
			e.setAttribute(AbbGraphmlConstants.COLOR, (String)strokeColor);
		}
		else{
			e.setAttribute(AbbGraphmlConstants.COLOR, AbbGraphmlConstants.DEFAULT_BORDERCOLOR);
		}				

		if(strokeWidth instanceof Double){
			e.setAttribute(AbbGraphmlConstants.WIDTH, String.valueOf((Double)strokeWidth));
		}
		else{
			e.setAttribute(AbbGraphmlConstants.WIDTH, AbbGraphmlConstants.DEFAULT_BORDERWIDTH);
		}
		
		if(styleDashed instanceof Boolean && ((Boolean)styleDashed)){
			e.setAttribute(AbbGraphmlConstants.TYPE, mxConstants.STYLE_DASHED);
		}
		else{
			e.setAttribute(AbbGraphmlConstants.TYPE, AbbGraphmlConstants.LINE);
		}

		return e;
	}

	private static Node encodeFill(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell,	Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.FILL);
		
		Object fillColor = getStyleValueFor(graph, cell, mxConstants.STYLE_FILLCOLOR);
		
		if(fillColor instanceof String){
			e.setAttribute(AbbGraphmlConstants.COLOR, (String)fillColor);
		}
		else{
			e.setAttribute(AbbGraphmlConstants.COLOR, AbbGraphmlConstants.DEFAULT_FILLCOLOR);
		}

		e.setAttribute(AbbGraphmlConstants.TRANSPARENT, String.valueOf(false));
		return e;
	}

	private static Object getStyleValueFor(mxGraph graph, mxICell cell,	String key) {
		Object value = null;
		
		Map<String, Object> style = graph.getCellStyle(cell);
	    
	    if(style != null){
	    	value = style.get(key);
	    	System.out.println(value);
	    }
	    
		return value;
	}

	private static Node encodeGeometry(mxGraph graph, Map<Class<?>, ValueHandler> vhp, mxICell cell,	Document document) {
		Element e = document.createElementNS(AbbGraphmlConstants.XML_NS_YWORKS, AbbGraphmlConstants.GEOMETRY);
		e.setAttribute(AbbGraphmlConstants.X, String.valueOf(cell.getGeometry().getX()));
		e.setAttribute(AbbGraphmlConstants.Y, String.valueOf(cell.getGeometry().getY()));
		e.setAttribute(AbbGraphmlConstants.WIDTH, String.valueOf(cell.getGeometry().getWidth()));
		e.setAttribute(AbbGraphmlConstants.HEIGHT, String.valueOf(cell.getGeometry().getHeight()));
		return e;
	}

}
