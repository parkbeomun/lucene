package sketchbook.kr.lucene;

import java.io.File;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Search {

	public static void main(String[] args) throws Exception {

		String indexDir = "./index";
		String q = "TEST_DEV";
		
		search(indexDir, q);
	}
	
	public static void search(String indexDir, String q) throws Exception {
		
		Directory dir = FSDirectory.open(new File(indexDir));
		
		IndexReader reader = DirectoryReader.open(dir);
		
		//search class
		//생성자 파라미터로 색인이 들어있는 디렉토리 인스턴스를 지정한다.
		IndexSearcher is = new IndexSearcher(reader);
		
		QueryParser parse = new QueryParser(Version.LUCENE_46, "contents", new StandardAnalyzer(Version.LUCENE_46));
		Query query = parse.parse(q);
		
		long start = System.currentTimeMillis();
		
		//TopDocs : 검색결과 중 최상위 문서에 대한 링크를 담고 있는 결과 클래스이다.
		TopDocs hits = is.search(query, 10);
		
		long end = System.currentTimeMillis();
		
		System.out.println("Found "+hits.totalHits + " document(s) (in "+(end-start) + "miliseconds) that mached query "+q+":");
		
		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = is.doc(scoreDoc.doc);
			System.out.println(doc.get("fullpath"));
		}

		reader.close();
		
	
	}
	
	
	
	

}
