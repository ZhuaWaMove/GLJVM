package cn.itheima.solrJ;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * @author new
 *solrJ管理
 *添加
 *删除
 *修改
 *查询
 */
public class SolrJManager {
    
	public static void main(String[] args) {
		System.out.println("git");
	}
	/**
	 * 添加
	 * @throws Exception 
	 * @throws SolrServerException
	 * 
	 */
	@Test
	public void testAdd() throws SolrServerException, Exception{
		//solr  url路径
		String baseurl ="http://localhost:8080/solr";//默认使用collection1
		//单机版
		HttpSolrServer solrServer = new HttpSolrServer(baseurl);
		//创建文档输入对象
		SolrInputDocument document = new SolrInputDocument();
		document.setField("id", "haha");
		document.setField("name", "范冰冰");
		
		//添加  并提交
		solrServer.add(document);
		solrServer.commit();
	}
	
	/**
	 * 查询
	 * @throws Exception 
	 */
	@Test
	public void testSearch() throws Exception{
		
		String baseurl ="http://localhost:8080/solr/collection1";//默认使用collection1
		//单机版
		HttpSolrServer solrServer = new HttpSolrServer(baseurl);
		//查询 关键字  台灯 过滤条件   "product_catalog_name" :"幽默杂货"
		SolrQuery solrQuery = new SolrQuery();
		//关键字
		solrQuery.set("q", "*:*");
		//执行查询
		QueryResponse response = solrServer.query(solrQuery);
		//文档结果集
		SolrDocumentList docs =  response.getResults();
		//总-条-数 
		long numFound = docs.getNumFound();
		System.out.println("总条数"+numFound);
		for (SolrDocument doc : docs) {
			System.out.println(doc.get("product_picture"));
			System.out.println(doc.get("product_catalog_name"));
			System.out.println(doc.get("product_price"));
			System.out.println(doc.get("product_name"));
			System.out.println(doc.get("id"));
		}
		
	}
	
	/**
	 * 删除文档 根据id删除
	 * @throws Exception 
	 * @throws SolrServerException 
	 */
	@Test
	public void testDeleteDocumentByid() throws SolrServerException, Exception{
		
		String baseurl ="http://localhost:8080/solr/collection1";//默认使用collection1
		//单机版
		HttpSolrServer solrServer = new HttpSolrServer(baseurl);
		
		//根据id删除文档
		solrServer.deleteById("c0001");
		//提交修改
		solrServer.commit();
	}
	
	/**
	 * 根据查询条件删除文档
	 * @throws Exception 
	 * @throws SolrServerException 
	 */
	@Test
	public void testDeleteDocumentByQuery() throws SolrServerException, Exception{
		String baseurl ="http://localhost:8080/solr/collection1";//默认使用collection1
		//单机版
		HttpSolrServer solrServer = new HttpSolrServer(baseurl);
		
		//根据id删除文档
		solrServer.deleteByQuery("*:*");
		//提交修改
		solrServer.commit();
	}
	/**
	 * 根据查询索引
	 * @throws Exception 
	 * @throws SolrServerException 
	 */
	@Test
	public void testQueryIndex() throws SolrServerException, Exception{
		String baseurl ="http://localhost:8080/solr/collection1";//默认使用collection1
		//单机版
		HttpSolrServer solrServer = new HttpSolrServer(baseurl);
		
		//创建一个query对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
		solrQuery.setQuery("钻石");
		//设置过滤条件
		solrQuery.setFilterQueries("product_catalog_name:幽默杂货");
		//设置排序条件
		solrQuery.setSort("product_price", ORDER.asc);
		//设置分页处理
		solrQuery.setStart(0);
		solrQuery.setRows(10);
		//设置结果中域的列表
		solrQuery.setFields("id","product_name","product_price","product_catalog_name","product_picture");
		//设置默认搜索域
		solrQuery.set("df", "product_keywords");
		//设置高亮显示
		solrQuery.setHighlight(true);
		//设置高亮域
		solrQuery.addHighlightField("product_name");
		//设置高亮显示前缀
		solrQuery.setHighlightSimplePre("<em>");
		//设置高亮后缀
		solrQuery.setHighlightSimplePost("</em>");
		//执行查询
		QueryResponse queryResponse = solrServer.query(solrQuery);
		//获取查询结果
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		//获取商品数量 并输出
		System.out.println("共查询到商品数量："+solrDocumentList.getNumFound());
		//遍历查询到的结果
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			//取高亮显示
			String productName = "";
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("product_name");
			//判断是否有高亮内容
			if (null != list) {
				productName = list.get(0);
			} else {
				productName = (String) solrDocument.get("product_name");
			}
			
			System.out.println(productName);
			System.out.println(solrDocument.get("product_price"));
			System.out.println(solrDocument.get("product_catalog_name"));
			System.out.println(solrDocument.get("product_picture"));

			
		}
	}
	
}
