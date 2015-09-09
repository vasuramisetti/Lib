package edu.mum.lms.commonUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import edu.mum.lms.commonUtil.DbClient.FilterCondition;

public class JDBCUtil {
    
    private DbClient dbClient;
    
    public static void main(String[] args) {
        JDBCUtil db = new JDBCUtil();
        //db.insertRow("Person", "4","Bidhut","Karki","655-242-233","1000 N", "fairfield", "IA", "54224");
        
        /*LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("firstName", "Pritivi");
        map.put("lastName", "Khatri");
        map.put("phoneNo", "655-767-321");
        map.put("street", "Sinamangal");
        map.put("city", "Kathmandu");
        map.put("state", "BG");
        map.put("zip", "44600");
        db.insertRow("Person", map, true);
        System.out.println("Inserted");
       */
       
        FilterCondition conditon = new DbClient.FilterCondition();
        conditon.addCondition("firstName", DbClient.EQUALS, "Bidhut");
        
        List<Map<String, Object>> elm = db.get("Person", new String[]{"firstName", "lastName"}, conditon);
        System.out.println(elm);
       
    }
    
    public void insertRow(String table, Object... colValues) {
        execute(() ->  dbClient.insertRow(table, colValues));
    }

    
    public int insertRow(String table, String[] colNames, Object[] colValues, boolean autoKeyGenerated) {
        return execute(() -> dbClient.insertRow(table, colNames, colValues, autoKeyGenerated));
    }

    
    public int insertRow(String table, LinkedHashMap<String, Object> data, boolean autoKeyGenerated) {
        return execute(() -> dbClient.insertRow(table, data, autoKeyGenerated));
    }

    
    public boolean insertRows(String tableName, List<LinkedHashMap<String, Object>> rowsToInsert) {
        return execute(() ->  dbClient.insertRows(tableName, rowsToInsert));
    }

    
    public int getCount(String tableName) {
        return execute(() ->  dbClient.getCount(tableName));
    }

    
    public int getDistinctCount(String tableName, String columnName) {
        return execute(() -> dbClient.getDistinctCount(tableName, columnName));
    }

    
    public int executeUpdate(String sql) {
        return execute(() ->  dbClient.executeUpdate(sql));
    }

    
    public boolean executeUpdate(List<String> queries) {
        return execute(() -> dbClient.executeUpdate(queries));
    }
    
    public int update(String tableName, LinkedHashMap<String, Object> data, FilterCondition conditions) {
        return execute(() -> dbClient.update(tableName, data, conditions));
    }

    
    public int[] updateData(String tableName, List<LinkedHashMap<String, Object>> rowsToUpdate,
            List<FilterCondition> conditions) {
        return execute(() -> dbClient.updateData(tableName, rowsToUpdate, conditions));
    }
    
    public boolean delete(String tableName, FilterCondition conditions) {
        return execute(() -> dbClient.delete(tableName, conditions));
    }

    
    public boolean delete(String tableName, List<FilterCondition> conditions) {
        return execute(() -> dbClient.delete(tableName, conditions));
    }

    
    public Map<String, Object> getOne(String tableName, String[] columns, FilterCondition conditions) {
        return execute(() -> dbClient.getOne(tableName, columns, conditions));
    }

    
    public Map<String, Object> getFirstOne(String tableName, String[] columns, FilterCondition conditions,
            String orderByCol) {
        return execute(() -> dbClient.getFirstOne(tableName, columns, conditions, orderByCol));
    }

    
    public List<Map<String, Object>> get(String tableName, String[] columns, FilterCondition conditionsobj) {
        return execute(() -> dbClient.get(tableName, columns, conditionsobj));
    }

    
    public List<Map<String, Object>> get(String tableName, String[] columns, FilterCondition conditionsobj,
            String orderByCol) {
        return execute(() -> dbClient.get(tableName, columns, conditionsobj, orderByCol));
    }

    
    public List<Map<String, Object>> get(String tableName, String[] columns, FilterCondition conditionsobj,
            String orderByCol, Integer limit, Integer offset) {        
        return execute(() -> dbClient.get(tableName, columns, conditionsobj, orderByCol, limit, offset));
    }

    
    public List<Map<String, Object>> join(String tableName1, String tableName2, String[] columns,
            FilterCondition conditionsobj, String orderByCol, String joinType, Map<String, String> joinOn) {
        return execute(() -> dbClient.join(tableName1, tableName2, columns, conditionsobj, orderByCol, joinType, joinOn));
    }

    
    public List<Map<String, Object>> join(String tableName1, String tableName2, String[] columns,
            FilterCondition conditionsobj, String orderByCol, String joinType, Map<String, String> joinOn,
            Integer limit, Integer offset) {
        
        return execute(() -> dbClient.join(tableName1, tableName2, columns, conditionsobj, orderByCol, joinType, joinOn, limit, offset));
    }

    
    public List<Map<String, Object>> runQuery(String sql) {
        return execute(() -> dbClient.runQuery(sql));       
    }

    
    public List<Map<String, Object>> executePSQuery(String psQuery, List<Object> values) {
        return execute(() -> dbClient.executePSQuery(psQuery, values));
    }
    
    private <T> T execute(Callable<T> runnable){
        try{
            dbClient = new DbClient();
            dbClient.connectDb();
            return runnable.call();
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            dbClient.close();
        }
        
    }
    
    private void execute(Runnable runnable){
        try{
            dbClient = new DbClient();
            dbClient.connectDb();
            runnable.run();
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            dbClient.close();
        }
        
    }

}
