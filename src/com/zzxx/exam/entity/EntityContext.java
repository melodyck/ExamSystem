package com.zzxx.exam.entity;

import com.zzxx.exam.util.Config;
import org.junit.Test;

import javax.imageio.IIOException;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * 实体数据管理, 用来读取数据文件放到内存集合当中
 */
public class EntityContext {
    //创建集合存储用户信息
    private Map<Integer, User> users = new TreeMap<>();
    //创建集合存储题目信息
    private Map<Integer, List<Question>> question = new TreeMap<>();
    //创建集合存储考场规则
    private ArrayList<String> rules = new ArrayList<>();
    //创建集合存储考生的考试信息
    private Map<Integer, ExamInfo> examInfos = new TreeMap<>();
    //创建Config对象读取文件名
    private Config config = new Config("config.properties");
    //生成试卷题目
    private ArrayList<QuestionInfo> questionInfos;
    public EntityContext() {
        loadQuestion();
        loadRules();
        loadUsers();
        loadExamInfo();
        generateQuestion();
    }

    //读取user.txt文件,将读取出的信息解析存入集合
//    @Test
    private void loadUsers(){
        String filename = config.getString("UserFile");
        File userFile = new File("./src/com/zzxx/exam/util/" + filename);
//        System.out.println(userFile.getAbsolutePath());
        //读取问题文件config.properties
        try ( BufferedReader br = new BufferedReader(new FileReader(userFile))){
            String info;
            while((info = br.readLine()) != null){
                String[] infos = info.split(":");
                //如果字符串数组首个元素为整数,则进行后续分析,否则就跳过
                if(infos[0].matches("\\d+")){
                    User user = new User(infos[1], Integer.parseInt(infos[0]), infos[2]);
                    users.put(Integer.parseInt(infos[0]), user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(users);
    }
    //读取config.properties文件将题目以难度等级为键值存储进Map集合中

//    @Test
    private void loadQuestion(){
        String filename = config.getString("QuestionFile");
        File questionFile = new File("./src/com/zzxx/exam/util/" + filename);
//        System.out.println(userFile.getAbsolutePath());
        try ( BufferedReader br = new BufferedReader(new FileReader(questionFile))){
            String info;
            for (int i = 0; (info = br.readLine()) != null; i++) {
                //新建一个问题对象
                Question questionTemp = new Question();
                //设置问题id
                questionTemp.setId(i);
                //处理读取的第一行 @answer=2/3,score=5,level=5
                String[] infos = info.split(",");
                for (int j = 0; j < infos.length; j++) {
                    infos[j] = infos[j].split("=")[1];
                }
//                for (String s: infos){
//                    System.out.println(s);
//                }
                //获得答案选项序号
                String[] num = infos[0].split("/");
                //将答案序号存入问题答案集合
                Arrays.stream(num).forEach(s -> questionTemp.answerAdd(Integer.parseInt(s)));
                //根据答案数量设置问题类型
                if(num.length == 1){
                    questionTemp.setType(Question.SINGLE_SELECTION);
                }else {
                    questionTemp.setType(Question.MULTI_SELECTION);
                }
                //设置问题分数
                questionTemp.setScore(Integer.parseInt(infos[1]));
                //设置问题难度等级
                questionTemp.setLevel(Integer.parseInt(infos[2]));
                //遍历处理下5行
                for (int j = 0; j < 5; j++) {
                    //读取一行
                    String s = br.readLine();
                    if(j == 0){
                        //读取的第一行为问题题目
                        questionTemp.setTitle(s);
                    }else {
                        //后四行均为选项添加进问题的选项集合中
                        questionTemp.optionAdd(s);
                    }
                }
                //将生成的完整问题对象根据难度等级加入到问题集合中
                //获取难度等级对应集合
                List<Question> list = question.get(questionTemp.getLevel());
                //如果集合为null说明此难度等级第一次出现,创建一个新集合
                if(list == null){
                    list = new ArrayList<Question>();
                }
                //将设置好的集合
                list.add(questionTemp);
                question.put(questionTemp.getLevel(), list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //键值遍历
/*        Set<Integer> keySet = question.keySet();
        for (Integer i: keySet){
            System.out.println("难度等级:" + i);
            for (Question q: question.get(i)){
                System.out.println(q);
                System.out.println(q.getLevel() + q.getScore() + q.getAnswers().toString());
            }
        }*/
    }

    //读取rule.txt文件信息存储到集合中
//    @Test
    private void loadRules(){
        String filename = config.getString("ExamRule");
        File file = new File("./src/com/zzxx/exam/util/" + filename);
        System.out.println(file.getAbsolutePath());
        try ( BufferedReader br = new BufferedReader(new FileReader(file))){
            String str;
            //读取每一行直到结束
            while ((str = br.readLine()) != null){
                rules.add(str);
            }
//            for (String s: rules){
//                System.out.println(s);
//            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

//    @Test
    //加载考生考试信息
    private void loadExamInfo(){
        //键值遍历考生信息
        Set<Integer> keySet = users.keySet();
        for (Integer num : keySet){
            User user = users.get(num);
            //使用user的信息初始化个人考试信息
            ExamInfo personExamInfo = new ExamInfo(config.getString("PaperTitle"), user, config.getInt("TimeLimit"), config.getInt("QuestionNumber"));
            //加入列表
            examInfos.put(num, personExamInfo);
        }
//        System.out.println(examInfos);
    }

    //随机生成试题集
    @Test
    private void generateQuestion(){
        //每个难度等级随机生成2题
        questionInfos = new ArrayList<>();
        int index = 0;
//        entityContext = new EntityContext();
        for (int i = Question.LEVEL1; i < Question.LEVEL10 + 1; i++) {
            List<Question> list = question.get(i);
            //随机选择2题加入题目列表中
            int num;
            x: for (int j = 0; j < 2; j++) {
                //产生随机数
                num = (int)(Math.random() * list.size());
                //获取随机数对应题目
                Question question =list.get(num);
                if(j > 0){
                    //遍历题目列表
                    for (QuestionInfo qsi: questionInfos){
                        //如果随机产生的题目已经在题目列表中存在
                        if(qsi.getQuestion().equals(question)){
                            //循环次数减一
                            j--;
                            //跳过之后的代码,再次生成随机题目
                            continue x;
                        }
                    }
                }
                //添加题目到列表
                QuestionInfo questionInfo = new QuestionInfo(index++, question);
                questionInfos.add(questionInfo);
            }
        }
//        for (QuestionInfo q: questionInfos
//             ) {
//            System.out.println(q.getQuestion().getLevel() + q.getQuestion().getTitle());
//        }
    }
    //根据id查询user
    public User getUserById(Integer id) {
        return users.get(id);
    }
    //查询对应难度题目集合
    public List<Question> getQuestionList(Integer level) {
        return question.get(level);
    }
    //获取考试规则
    public ArrayList<String> getRules() {
        return rules;
    }
    //查询考生考试信息
    public ExamInfo getExamInfos(Integer num) {
        return examInfos.get(num);
    }
    //获取试卷题目信息
    public QuestionInfo getQuestionInfo(int index){
        return questionInfos.get(index);
    }
    //获取试卷题目数量
    public int getQuestionSum(){
        return questionInfos.size();
    }
    //获取考试规则文件位置
    public String getPath(String name) {
        return config.getString(name);
    }


//    public ArrayList<QuestionInfo> getQuestionInfos() {
//        return questionInfos;
//    }
//
//    public void setQuestionInfos(ArrayList<QuestionInfo> questionInfos) {
//        this.questionInfos = questionInfos;
//    }
}
