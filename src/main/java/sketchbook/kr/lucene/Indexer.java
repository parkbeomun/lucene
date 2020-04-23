package sketchbook.kr.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
	
	private IndexWriter writer;
	
	public Indexer(String indexDir) throws IOException {
		
		Directory dir = FSDirectory.open(new File(indexDir));
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
		
		writer = new IndexWriter(dir, config);
	}
	
	public static void main(String[] args) throws Exception {
		
		if(args.length != 2) {
			throw new IllegalAccessError("Usage: java" + Indexer.class.getName()+ "<index dir> <data dir>");
		}
		
		String indexDir = "./index";
		String dataDir = "./data";
		
		long start = System.currentTimeMillis();
		
		Indexer indexer = new Indexer(indexDir);
		
		int numIndexed;
		
		try {
			numIndexed = indexer.index(dataDir, new TextFilesFilter());
				
		} finally {
			indexer.close();
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("indexed number"+numIndexed);
		System.out.println(end-start);
	}
	
	public void close() throws IOException {
		
	}
	
	public int index(String dataDir, FileFilter filter) {
		
		return 0;
	}
	
	private void indexFile(File f) {
		
	}
	
	protected Document getDocument(File f) {
		
		return null;
	}
	
	//색인할 대상 txt 파일 목록을 뽑아낸다.
	private static class TextFilesFilter implements FileFilter {
		
		public boolean accept(File pathname) {
			return pathname.getName().toLowerCase().endsWith(".txt");
		}
	}
}
