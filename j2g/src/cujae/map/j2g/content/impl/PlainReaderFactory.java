package cujae.map.j2g.content.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import cujae.map.j2g.content.MDocument;
import cujae.map.j2g.content.MDocumentReader;
import cujae.map.j2g.content.MDocumentReaderFactory;
import cujae.map.j2g.content.MVertex;
import cujae.map.j2g.content.util.MDocumentConstants;
import cujae.map.j2g.content.util.TreeEvent;
import cujae.map.j2g.content.util.TreeNode;

public class PlainReaderFactory extends MDocumentReaderFactory {

	private static final TreeNode root;
	
	static{
		
		root = new TreeNode(0, "", 0);
		
		root.addChild(new TreeNode(1, ".*", 0){

			@Override
			public void action(TreeEvent event, MDocument document,
					Logger logger) {
				super.action(event, document, logger);
				int size = document.getRelations().size();
				document.getRelations().add(new MVertex(size, String.format("r%d", size), event.getInput(), null));
			}
			
		});
	}
	
	
	@Override
	public boolean accept(File pathname) {
		return pathname != null && pathname.isFile();
	}

	@Override
	public MDocumentReader newDocumentReader() {
		return new MDocumentReader() {
			
			@Override
			public MDocument read(InputStream in, String charsetName) throws IOException {
				return read(new LineNumberReader(new InputStreamReader(in, charsetName)));
			}

			@Override
			public MDocument read(InputStream in, Charset cs) throws IOException {
				return read(new LineNumberReader(new InputStreamReader(in, cs)));
			}

			@Override
			public MDocument read(InputStream in) throws IOException {
				return read(in, MDocumentConstants.DEFAULT_CHARSET_NAME);
			}
			
			@Override
			public MDocument read(Reader in) throws IOException {
				
				Logger logger = Logger.getLogger(cujae.map.j2g.content.MDocumentReader.class.getName());
				
				LineNumberReader lnr = new LineNumberReader(in);
				MDocument document = new MDocument();
				String line = null;
				while((line = lnr.readLine()) != null){
					TreeNode next = root.next(line);
					if(next != null){
						next.action(new TreeEvent(line, lnr.getLineNumber()), document, logger);
					}
				}
				
				return document;
			}
		};
	}

}
