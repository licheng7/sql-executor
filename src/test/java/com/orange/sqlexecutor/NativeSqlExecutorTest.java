package com.orange.sqlexecutor;

/**
 * Created with IDEA
 * author:licheng
 * Date:2019/9/22
 * Time:上午11:33
 **/
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class NativeSqlExecutorTest {

    /*@Test
    public void test1() {
        //ContextUtils.registryBean("h2DB", this.getSqlSessionFactory());
        //SqlSessionFactory sqlSessionFactory = ContextUtils.getBean(SqlSessionFactory.class);
        //System.out.print(sqlSessionFactory.getConfiguration());
    }

    private SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(this.getDataSource());

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            bean.setMapperLocations(resolver.getResources("classpath*:mybatis_mapper/*.xml"));
            bean.setConfigLocation(resolver.getResources("classpath*:mybatis_config/*.xml")[0]);
        }
        catch(Throwable t) {
            throw new RuntimeException("数据源加载失败,读取配置文件发生异常", t);
        }

        try {
            return bean.getObject();
        }
        catch (Exception e) {
            throw new RuntimeException("实例化SqlSessionFactory异常", e);
        }
    }

    private DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("hahaha");
        dataSource.setUsername("aaa");
        return dataSource;
    }*/
}
