package sketchbook.kr.lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
	
	private IndexWriter writer;
	
	public Indexer(String indexDir) throws IOException {
		
		//create or open directory 
		Directory dir = FSDirectory.open(new File(indexDir));
		
		//Analyzer 는 Text를 잘게 자라서 Term을 만드는 작업을 수행한다.
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_46, analyzer);
		
		//첫번째 디렉토리 lucene index 를 저장할 디렉토리
		//두번째 디렉토리는  대상 파일이 들어있는 디렉토리이다.
		writer = new IndexWriter(dir, config);
	}
	
	public static void main(String[] args) throws Exception {
		
//		if(args.length != 2) {
//			throw new IllegalAccessError("Usage: java" + Indexer.class.getName()+ "<index dir> <data dir>");
//		}
		
		String indexDir = "./index";
		String dataDir = "./data";
		
		long start = System.currentTimeMillis();
		
		//lucene index directory
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
		writer.close();
	}
	
	public int index(String dataDir, FileFilter filter) throws Exception {
		
		File[] files = new File(dataDir).listFiles();
		
		for(File file :files) {
			System.out.println(file.getName());
			System.out.println(filter.accept(file));
			if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && (filter == null || filter.accept(file))) {
				indexFile(file);
			}
		}
		return writer.numDocs(); // 다음 추가될 Document가 할당받을 번호
	}
	
	//Add Document 
	private void indexFile(File file) throws Exception{
		//getCanonicalPath : 현재 프로그램을 실행한 경로를 포함한 파일경로를 반환한다.
		System.out.println("indexing : "+ file.getCanonicalPath());
		Document document = getDocument(file);
		writer.addDocument(document);
		
	}
	
	//Get Document
	protected Document getDocument(File file) throws Exception {
		Document doc = new Document();
		doc.add(new TextField("contents",new FileReader(file)));
		
		doc.add(new TextField("filename", file.getName(),Field.Store.YES));
		
		doc.add(new StringField("fullpath",file.getCanonicalPath(),Field.Store.YES));
		
		return doc;
	}
	
	//색인할 대상 txt 파일 목록을 뽑아낸다.
	private static class TextFilesFilter implements FileFilter {
		
		public boolean accept(File pathname) {
			return pathname.getName().toLowerCase().endsWith(".txt");
		}
	}
}
