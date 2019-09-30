package com.orange.sqlexecutor;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/21
 * Time:下午12:08
 **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BeanRegistryTest {

    /*@Autowired
    private RegistryDataSourceListenerImpl registryDataSourceListener;

    @Test
    public void test1() throws SQLException {
        *//*RegistryDataSourceEventInfo info = new RegistryDataSourceEventInfo();
        info.setDbName("H2TEST");
        info.setUserName("h2");
        info.setPassword("licheng7");
        info.setDriverClassName("org.h2.Driver");
        info.setUrl("jdbc:h2:./h2/data:orangeDB");
        registryDataSourceListener.listener(info);

        String beanName = "SQLSESSIONFACTORY-"+info.getDbName().toUpperCase();
        DefaultSqlSessionFactory sqlSessionFactory = (DefaultSqlSessionFactory) ContextUtils.getBean(beanName);
        System.out.println(sqlSessionFactory);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            conn = sqlSession.getConnection();
            //pst = conn.prepareStatement("update DB_INFO set DB_NAME='呼呼3' where DB_ID=18");
            //pst = conn.prepareStatement("DELETE FROM DB_INFO WHERE DB_ID=19;");
            pst = conn.prepareStatement("SELECT * FROM DB_INFO;");
            pst.execute();

            //boolean moreResults = pst.getMoreResults();
            //System.out.println(moreResults);
            //resultSet = pst.getResultSet();
            //int count1 = pst.getUpdateCount();
            //System.out.println(count1);
            boolean moreResults = pst.getMoreResults();
            System.out.println(moreResults);
            //int count2 = pst.getUpdateCount();
            //System.out.println(count2);

            //boolean moreResults = pst.getMoreResults();
            //System.out.println(moreResults);


            *//**//*ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            for(int i=1; i<=resultSetMetaData.getColumnCount(); i++) {
                System.out.print(resultSetMetaData.getColumnName(i)+" | ");
            }
            System.out.print("\n");
            while(resultSet.next()) {

                for(int i=1; i<=resultSetMetaData.getColumnCount(); i++) {
                    System.out.print(resultSet.getObject(i)+" | ");
                }
                System.out.print("\n-------------------------------------------\n");
            }*//**//*
        } finally {
            if(null != resultSet) {
                resultSet.close();
            }
            if(null != pst) {
                pst.close();
            }
            if(null != conn) {
                conn.close();
            }
        }*//*
    }

    @Test
    public void test2() {
        String str = " usedb hhh;";
        boolean b = str.toUpperCase().startsWith("USEDB ");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!"+b);
    }*/
}
