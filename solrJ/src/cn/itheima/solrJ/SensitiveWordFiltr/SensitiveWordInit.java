package cn.itheima.solrJ.SensitiveWordFiltr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sun.net.www.protocol.gopher.Handler;
 
/**
 * @Description: 初始化敏感词库，将敏感词加入到HashMap中，构建DFA算法模型
 */
public class SensitiveWordInit {
    private String ENCODING = "UTF-8";
    @SuppressWarnings("rawtypes")
    public HashMap sensitiveWordMap = new HashMap<>();
 
    public SensitiveWordInit() {
        super();
    }
 
    @SuppressWarnings("rawtypes")
    public Map initKeyWord() {
        try {
            // 读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();
            // 将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }
 
    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型
     * 
     * @param keyWordSet
     *            敏感词库
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size()); // 初始化敏感词容器，减少扩容操作
        Map nowMap = null;
        Map<String, String> newWorMap = null;
 
        for (String key : keyWordSet) {
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i); // 转换成char型
                Object wordMap = nowMap.get(keyChar); // 获取
 
                if (wordMap != null) { // 如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else { // 不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0"); // 不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
 
                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1"); // 最后一个
                }
            }
        }
    }
 
    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("resource")
    private Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = null;
 
        File file = new File("F:/test/filterword/SensitiveWord.txt");
        FileInputStream inputStream = new FileInputStream(file);
        
        InputStreamReader read = new InputStreamReader( inputStream, ENCODING);
        try {
            if (file.isFile() && file.exists()) { // 文件流是否存在
                set = new HashSet<String>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                while ((txt = bufferedReader.readLine()) != null) { // 读取文件，将文件内容放入到set中
                    set.add(txt);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            read.close();
        }
        return set;
    }
}
