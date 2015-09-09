package edu.mum.lms.commonUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DbClient {

    private static Logger log = EnvironmentUtil.loggerForThisClass();
    protected Connection _conn;
    private boolean autocommit;

    public static final int EQUALS = 0;
    public static final int LESS = 1;
    public static final int GREATER = 2;
    public static final int NOT_EQUALS = 3;
    public static final int LIKE = 4;

    public static void main(String[] argv) throws Exception {
        DbClient dbClient = new DbClient();
        dbClient.connectDb();
        dbClient.insertRow("Person", "","Bidhut","Karki","655-242-233","1000 N", "fairfield", "IA", "54224");
        dbClient.close();
        
    }

    public void connectDb() {
        Properties prop = EnvironmentUtil.readPropertiesFromFile(EnvironmentUtil.getPropertiesFilePath());
        String host = prop.getProperty("jdbc.host");
        String user = prop.getProperty("jdbc.user");
        String pass = prop.getProperty("jdbc.password");
        try {
            _conn = DriverManager.getConnection(host, user, pass);
        } catch (Exception e) {
            log.error("Error occurred while getting db connection", e);
            throw new RuntimeException(e);
        }
    }

    // INSERTS

    // lets start using this pattern

    public void insertRow(String table, Object... colValues) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(table);
        StringBuilder values = new StringBuilder();

        values.append("(");
        for (Object value : colValues) {
            values.append(getDbStringForValue(value) + ",");
        }
        values.deleteCharAt(values.length() - 1);
        values.append(")");
        sql.append(" VALUES " + values);

        executeUpdate(sql.toString());
    }

    public int insertRow(String table, String[] colNames, Object[] colValues, boolean autoKeyGenerated) {
        if (colNames.length != colValues.length) {
            throw new RuntimeException("Column names must be equal to column values");
        }
        LinkedHashMap<String, Object> row = new LinkedHashMap<String, Object>();
        for (int i = 0; i < colNames.length; i++) {
            row.put(colNames[i], colValues[i]);
        }
        return insertRow(table, row, autoKeyGenerated);
    }

    public int insertRow(String table, LinkedHashMap<String, Object> data, boolean autoKeyGenerated) {
        removeNullKeys(data);

        // Prepared Statement is more expensive then Normal Statement
        // but Normal Statement does not skip string with single quote, so
        // values like "'ticker" will be troublesome for Normal Statement
        String query = createInsertPSQuery(table, data);
        PreparedStatement ps = null;

        try {
            ps = _conn.prepareStatement(query, autoKeyGenerated ? PreparedStatement.RETURN_GENERATED_KEYS
                    : PreparedStatement.NO_GENERATED_KEYS);

            setValues(ps, data.values());

            ps.executeUpdate();

            if (autoKeyGenerated) {
                ResultSet resultSet = ps.getGeneratedKeys();

                if (resultSet.next()) {
                    int key = resultSet.getInt(1);
                    return key;
                }
            }

            return -1;

        } catch (Exception e) {
            log.error("Error in sql: last query - " + ps.toString(), e);
            throw new RuntimeException(e);
        } finally {
            close(ps);
        }
    }

    // get the number of rows in a table
    public boolean insertRows(String tableName, List<LinkedHashMap<String, Object>> rowsToInsert) {
        log.info("Inserting " + rowsToInsert.size() + " rows into table " + tableName);
        boolean success = false;

        if (rowsToInsert.isEmpty())
            return false;

        String query = createInsertPSQuery(tableName, rowsToInsert.get(0));
        PreparedStatement ps = null;
        try {
            ps = _conn.prepareStatement(query);

            for (LinkedHashMap<String, Object> row : rowsToInsert) {
                setValues(ps, row.values());
                ps.addBatch();
            }
            ps.executeBatch();
            success = true;
        } catch (Exception e) {
            log.error("Error in sql: last query - " + ps.toString(), e);
            throw new RuntimeException(e);
        } finally {
            close(ps);
        }
        return success;
    }

    // return list of columns to insert in a prepared statement query
    private String createInsertPSQuery(String tableName, LinkedHashMap<String, Object> row) {
        // prepare sql
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("INSERT INTO " + tableName);
        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();

        keys.append("(");
        values.append("(");

        // generate keys
        for (String key : row.keySet()) {
            keys.append(key + ",");
            values.append("?,");

        }

        keys.deleteCharAt(keys.length() - 1);
        values.deleteCharAt(values.length() - 1);

        keys.append(")");
        values.append(")");
        queryBuilder.append(" " + keys + " VALUES " + values);

        String query = queryBuilder.toString();
        return query;

    }

    public int getCount(String tableName) {
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");
            sql.append(tableName);
            Statement stmt = _conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql.toString());
            // move the pointer to the first position i.e. (1,1) cell
            rs.first();
            // return the first value we get, that contains our count
            int count = rs.getInt(1);
            stmt.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getDistinctCount(String tableName, String columnName) {
        try {
            StringBuilder sql = new StringBuilder("SELECT COUNT(DISTINCT(" + columnName + ")) FROM ");
            sql.append(tableName);
            Statement stmt = _conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql.toString());
            // move the pointer to the first position i.e. (1,1) cell
            rs.first();
            // return the first value we get, that contains our count
            int count = rs.getInt(1);
            stmt.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // UDATES

    public int executeUpdate(String sql) {
        log.debug("Executing update sql:");
        log.debug(sql);
        Statement stmt = null;
        try {
            System.out.println(sql);
            stmt = _conn.createStatement();
            int result = stmt.executeUpdate(sql);
            stmt.close();
            return result;
        } catch (Exception e) {
            log.error("SQL Query Error - " + sql, e);
            throw new RuntimeException(e);
        }
    }

    public boolean executeUpdate(List<String> queries) {
        Statement stmt = null;
        try {
            stmt = _conn.createStatement();

            for (String query : queries) {
                stmt.addBatch(query);
            }
            int no_of_affected_rows[] = stmt.executeBatch();
            stmt.close();
            if (no_of_affected_rows.length == queries.size()) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    
    public int update(String tableName, LinkedHashMap<String, Object> data, FilterCondition conditions) {

        removeNullKeys(data);

        String query = createUpdatePSQuery(tableName, data, conditions);

        PreparedStatement ps = null;

        int result = -1;
        try {

            ps = _conn.prepareStatement(query);
            setValues(ps, data.values());
            setConditions(ps, conditions, data.values().size() + 1);

            result = ps.executeUpdate();
        } catch (Exception e) {
            log.error("Error in sql: last query - " + ps.toString(), e);
            throw new RuntimeException(e);
        } finally {
            close(ps);
        }

        return result;
    }

    public int[] updateData(String tableName, List<LinkedHashMap<String, Object>> rowsToUpdate,
            List<FilterCondition> conditions) {
        if (rowsToUpdate.isEmpty() || conditions.isEmpty())
            return new int[] {};

        // note that items in conditions list must correspond with items in rows
        // list

        // This makes implicit assumption is that all condition in
        // List<FilterConditions> are conditions in the same column
        // because the same prepared statement based on first condition is
        // applied to all rows. The above public method guarentees this
        String query = createUpdatePSQuery(tableName, rowsToUpdate.get(0), conditions.get(0));

        PreparedStatement ps = null;

        try {

            ps = _conn.prepareStatement(query);
            for (int i = 0; i < rowsToUpdate.size(); i++) {

                LinkedHashMap<String, Object> row = rowsToUpdate.get(i);

                FilterCondition condition = conditions.get(i);

                setValues(ps, row.values());
                setConditions(ps, condition, row.values().size() + 1);

                ps.addBatch();
            }

            return ps.executeBatch();
        } catch (Exception e) {
            log.error("Error in sql: last query - " + ps.toString(), e);
            throw new RuntimeException(e);
        } finally {
            close(ps);
        }
    }

    private void removeNullKeys(LinkedHashMap<String, Object> data) {
        // remove map entries with null value - this also means update can't set
        // column to null

        for (Iterator<String> iter = data.keySet().iterator(); iter.hasNext();) {
            Object value = data.get(iter.next());
            if (value == null) {
                iter.remove();
            }
        }

    }

    private String createUpdatePSQuery(String tableName, LinkedHashMap<String, Object> data, FilterCondition conditions) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("UPDATE " + tableName + " SET ");

        for (String key : data.keySet()) {
            queryBuilder.append(key + "=?, ");

        }

        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());

        if (conditions != null && conditions.getConditions().size() > 0) {
            queryBuilder.append(" WHERE ");
            LogicalOperator logicalOperator = conditions.getOp();

            if (logicalOperator == null)
                logicalOperator = LogicalOperator.AND;

            for (ColumnFilter condition : conditions.getConditions()) {
                queryBuilder.append(condition.getPSFilterString());
                queryBuilder.append(" " + logicalOperator.toString() + " ");

            }
            queryBuilder.delete(queryBuilder.length() - (logicalOperator.toString().length() + 2),
                    queryBuilder.length());
        }
        String query = queryBuilder.toString();
        return query;
    }

    public boolean delete(String tableName, FilterCondition conditions) {
        boolean success = false;

        String query = createDeletePSQuery(tableName, conditions);
        PreparedStatement ps = null;

        try {
            ps = _conn.prepareStatement(query);
            setConditions(ps, conditions, 1);
            ps.executeUpdate();
            success = true;
        } catch (Exception e) {
            log.error("Error in sql: last query - " + ps.toString(), e);
            throw new RuntimeException(e);
        } finally {
            close(ps);
        }

        return success;
    }

    public boolean delete(String tableName, List<FilterCondition> conditions) {
        boolean success = false;

        if (conditions.isEmpty())
            return false;

        String query = createDeletePSQuery(tableName, conditions.get(0));

        PreparedStatement ps = null;

        try {
            ps = _conn.prepareStatement(query);

            for (FilterCondition condition : conditions) {
                setConditions(ps, condition, 1);
                ps.addBatch();
            }

            ps.executeBatch();
            success = true;
        } catch (Exception e) {
            log.error("Error in sql: last query - " + ps.toString(), e);
            throw new RuntimeException(e);
        } finally {
            close(ps);
        }

        return success;
    }

    private String createDeletePSQuery(String tableName, FilterCondition conditionsobj) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DELETE from " + tableName);

        if (conditionsobj != null && conditionsobj.getConditions().size() > 0) {

            queryBuilder.append(" WHERE ");
            LogicalOperator logicalOperator = conditionsobj.getOp();
            List<ColumnFilter> conditions = conditionsobj.getConditions();

            if (logicalOperator == null)
                logicalOperator = LogicalOperator.AND;

            for (ColumnFilter condition : conditions) {
                queryBuilder.append(condition.getPSFilterString());
                queryBuilder.append(" " + logicalOperator.toString() + " ");

            }

            queryBuilder.delete(queryBuilder.length() - (logicalOperator.toString().length() + 2),
                    queryBuilder.length());
        }

        String query = queryBuilder.toString();
        return query;
    }


    public Map<String, Object> getOne(String tableName, String[] columns, FilterCondition conditions) {
        // the conditions should return only one record
        List<Map<String, Object>> results = get(tableName, columns, conditions, null, null, null);
        if (results.size() == 0) {
            return null;
        }
        if (results.size() > 1) {
            throw new RuntimeException("More than one record found for getOne query");
        }
        // results.size() == 1
        return results.get(0);
    }

    public Map<String, Object> getFirstOne(String tableName, String[] columns, FilterCondition conditions,
            String orderByCol) {
        List<Map<String, Object>> results = get(tableName, columns, conditions, orderByCol, 1, 0);
        if (results.size() == 0)
            return null;
        return results.get(0);
    }

    public List<Map<String, Object>> get(String tableName, String[] columns, FilterCondition conditionsobj) {
        return get(tableName, columns, conditionsobj, null, null, null);
    }

    public List<Map<String, Object>> get(String tableName, String[] columns, FilterCondition conditionsobj,
            String orderByCol) {
        return get(tableName, columns, conditionsobj, orderByCol, null, null);
    }

    public List<Map<String, Object>> get(String tableName, String[] columns, FilterCondition conditionsobj,
            String orderByCol, Integer limit, Integer offset) {

        if (limit == null && offset != null) {
            throw new RuntimeException("You cannot specify offset without specifying limit");
        }

        StringBuilder sqlbuffer = new StringBuilder();
        sqlbuffer.append("SELECT ");
        if (columns == null || columns.length == 0) {
            sqlbuffer.append("*");
        } else {
            for (String col : columns) {
                sqlbuffer.append(col + ", ");
            }
            sqlbuffer.delete(sqlbuffer.length() - 2, sqlbuffer.length());
        }

        sqlbuffer.append(" FROM ");
        sqlbuffer.append(tableName);

        if (conditionsobj != null && conditionsobj.getConditions().size() > 0) {
            LogicalOperator logicalOpeartor = conditionsobj.getOp();
            if (logicalOpeartor == null)
                logicalOpeartor = LogicalOperator.AND; // can be for single
                                                       // condition
            List<ColumnFilter> conditions = conditionsobj.getConditions();

            sqlbuffer.append(" WHERE ");
            for (ColumnFilter condition : conditions) {
                sqlbuffer.append(condition.getPSFilterString());
                sqlbuffer.append(" " + logicalOpeartor.toString() + " ");

            }

            int trimLength = logicalOpeartor.toString().length() + 2;
            sqlbuffer.delete(sqlbuffer.length() - trimLength, sqlbuffer.length());
        }

        if (orderByCol != null)
            sqlbuffer.append(" ORDER BY " + orderByCol);

        if (limit != null) {
            sqlbuffer.append(" LIMIT " + limit.intValue());
        }

        if (offset != null) {
            sqlbuffer.append(" OFFSET " + offset.intValue());
        }

        String sql = sqlbuffer.toString();

        PreparedStatement ps = null;

        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

        try {
            ps = _conn.prepareStatement(sql);
            setConditions(ps, conditionsobj, 1);

            ResultSet resultSet = ps.executeQuery();
            results = parseResultSet(resultSet);
        } catch (Exception e) {
            if (ps != null)
                log.debug("Error occurred while executing sql: " + ps.toString());

            throw new RuntimeException(e);
        } finally {
            close(ps);
        }

        return results;
    }

    public List<Map<String, Object>> join(String tableName1, String tableName2, String[] columns,
            FilterCondition conditionsobj, String orderByCol, String joinType, Map<String, String> joinOn) {
        return join(tableName1, tableName2, columns, conditionsobj, orderByCol, joinType, joinOn, null, null);
    }

    public List<Map<String, Object>> join(String tableName1, String tableName2, String[] columns,
            FilterCondition conditionsobj, String orderByCol, String joinType, Map<String, String> joinOn,
            Integer limit, Integer offset) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        if (columns == null || columns.length == 0) {
            sql.append("*");
        } else {
            for (String col : columns) {
                sql.append(col + ", ");
            }
            sql.delete(sql.length() - 2, sql.length());
        }

        sql.append(" FROM " + tableName1 + " ");

        // Join statement
        sql.append(joinType + " JOIN " + tableName2 + " ON ");
        if (joinOn != null && joinOn.size() > 0) {
            // here key contains table1 join column and value contains table2
            // join column
            for (String table1Col : joinOn.keySet()) {
                String table2Col = (String) joinOn.get(table1Col);
                sql.append(tableName1 + "." + table1Col + "=" + tableName2 + "." + table2Col + " AND ");
            }
            sql.delete(sql.length() - 5, sql.length());
        } else {
            // TODO - Can join clause work without JOIN join condition, what to
            // do if joinOn is null?
        }

        if (conditionsobj != null && conditionsobj.getConditions().size() > 0) {
            List<ColumnFilter> conditions = conditionsobj.getConditions();
            sql.append(" WHERE ");
            for (ColumnFilter condition : conditions) {
                sql.append(condition.getPSFilterString());
                sql.append(" AND ");

            }
            sql.delete(sql.length() - 5, sql.length());
        }

        if (orderByCol != null)
            sql.append(" ORDER BY " + orderByCol);

        if (limit != null) {
            sql.append(" LIMIT " + limit.intValue());
        }

        if (offset != null) {
            sql.append(" OFFSET " + offset.intValue());
        }

        PreparedStatement ps = null;

        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        try {
            ps = _conn.prepareStatement(sql.toString());

            setConditions(ps, conditionsobj, 1);

            ResultSet resultSet = ps.executeQuery();
            results = parseResultSet(resultSet);
        } catch (Exception e) {
            log.error("Exception in join sql: " + ps.toString(), e);
            throw new RuntimeException(e);
        } finally {
            close(ps);
        }

        return results;
    }

    public List<Map<String, Object>> runQuery(String sql) {
        try {
            Statement stmt = _conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            List<Map<String, Object>> results = parseResultSet(resultSet);
            stmt.close();
            return results;
        } catch (SQLException e) {
            log.error("Error in sql: " + sql, e);
            throw new RuntimeException(e);
        }
    }

    public List<Map<String, Object>> executePSQuery(String psQuery, List<Object> values) {
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

        PreparedStatement ps = null;

        try {
            ps = _conn.prepareStatement(psQuery);
            setValues(ps, values);

            log.debug("Running sql : " + ps.toString());
            ResultSet rs = ps.executeQuery();

            rows = parseResultSet(rs);
        } catch (SQLException e) {
            log.error("Error in sql: " + ps.toString(), e);
            throw new RuntimeException(e);
        } finally {
            close(ps);
        }

        return rows;
    }

    private List<Map<String, Object>> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

        ResultSetMetaData metadata = resultSet.getMetaData();
        int colCount = metadata.getColumnCount();

        String[] cols = new String[colCount];
        for (int i = 1; i <= colCount; i++) {
            cols[i - 1] = metadata.getColumnName(i);
        }

        while (resultSet.next()) {
            Map<String, Object> row = new HashMap<String, Object>();
            for (int i = 1; i <= colCount; i++) {
                String key = cols[i - 1];
                Object value = resultSet.getObject(i);

                if (value == null)
                    continue;

                row.put(key, value);
            }
            results.add(row);
        }

        return results;
    }

    public static String getDbStringForValue(Object value) {

        if (value == null)
            return "null";

        if (value instanceof Number)
            return value.toString();
        else if (value instanceof String)
            return "'" + value.toString() + "'";
        else if (value instanceof Date) {
            Date date = (Date) value;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String s = "'" + formatter.format(date) + "'";
            return s;
        } else if (value instanceof Boolean)
            return value.toString();

        return null; // this should not happen
    }

    public void close() {
        try {
            if (_conn != null) {
                _conn.close();
            }
        } catch (Exception e) {
            log.error("Error occurred while closing JDBC Connection", e);
        }
    }

    public void close(boolean success) {
        try {
            if (_conn != null) {

                if (!autocommit) {
                    if (success)
                        _conn.commit();
                    else
                        _conn.rollback();
                }

                _conn.close();
            }
        } catch (Exception e) {
            log.error("Error occurred while closing JDBC Connection", e);
        }
    }

    private void close(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setConditions(PreparedStatement ps, FilterCondition conditions, int startIndex) throws SQLException {
        // startIndex parameter is required because for update prepared
        // statement, there could be update placeholders before
        // condition placeholders

        if (conditions == null)
            return;

        for (ColumnFilter condition : conditions.getConditions()) {
            Object value = condition.getValue();

            if (value instanceof String)
                ps.setString(startIndex, value.toString());
            else if (value instanceof Boolean)
                ps.setBoolean(startIndex, (Boolean) value);
            else if (value instanceof Integer)
                ps.setInt(startIndex, (Integer) value);
            else if (value instanceof Long)
                ps.setLong(startIndex, (Long) value);
            else if (value instanceof Double)
                ps.setDouble(startIndex, (Double) value);
            else if (value instanceof Date)
                ps.setDate(startIndex, new java.sql.Date(((Date) value).getTime()));
            else
                ps.setObject(startIndex, value);

            startIndex++;
        }
    }

    private void setValues(PreparedStatement ps, Collection<Object> columnValues) throws SQLException {

        int count = 1;

        StringBuilder valueLogs = new StringBuilder();
        for (Object value : columnValues) {
            if (value instanceof Double) {
                valueLogs.append(GeneralUtil.roundTwoPlaces((Double) value) + ", ");
            } else {
                valueLogs.append(value + ", ");
            }

            if (value instanceof String)
                ps.setString(count, value.toString());
            else if (value instanceof Boolean)
                ps.setBoolean(count, (Boolean) value);
            else if (value instanceof Integer)
                ps.setInt(count, (Integer) value);
            else if (value instanceof Long)
                ps.setLong(count, (Long) value);
            else if (value instanceof Double)
                ps.setDouble(count, (Double) value);
            else if (value instanceof Date)
                ps.setDate(count, new java.sql.Date(((Date) value).getTime()));
            else
                ps.setObject(count, value);

            count++;
        }
    }

    public static FilterCondition newFilterCondition(LogicalOperator operator) {
        return new FilterCondition(operator);
    }

    public static FilterCondition singleFilterCondition(String column, int op, Object value) {
        return new FilterCondition().addCondition(column, op, value);
    }

    public static FilterCondition singleEqualFilterCondition(String column, Object value) {
        return new FilterCondition().addEqualsCondition(column, value);
    }

    public static class FilterCondition {

        private List<ColumnFilter> conditions;
        private LogicalOperator op;

        public LogicalOperator getOp() {
            return op;
        }

        public FilterCondition() {
            this(null);
        }

        public FilterCondition(LogicalOperator op) {
            conditions = new ArrayList<ColumnFilter>();
            this.op = op;
        }

        public FilterCondition addCondition(String column, int op, Object value) {
            if (this.op == null && conditions.size() > 0) {
                throw new RuntimeException("Adding multiple conditions not allowed without logical operator");
            }
            ColumnFilter filter = new ColumnFilter(column, op, value);
            conditions.add(filter);
            return this;
        }

        public FilterCondition addEqualsCondition(String column, Object value) {
            return addCondition(column, DbClient.EQUALS, value);
        }

        public List<ColumnFilter> getConditions() {
            return conditions;
        }

        public List<String> getConditionColumns() {
            List<String> columns = new ArrayList<String>();
            for (ColumnFilter colFilter : conditions) {
                columns.add(colFilter.getColumn());
            }
            return columns;
        }

    }

    public static class ColumnFilter {
        public String column;
        public int op;
        public Object value;

        public ColumnFilter(String col, int op, Object val) {
            this.column = col;
            this.op = op;
            this.value = val;
        }

        public String getFilterString() {
            String opStr = getOpString();
            String valStr = getDbStringForValue(value);
            return column + opStr + valStr;
        }

        public String getPSFilterString() {
            String opStr = getOpString();
            return column + opStr + "?";
        }

        public String getColumn() {
            return column;
        }

        public Object getValue() {
            return value;
        }

        private String getOpString() {
            String opStr = null;
            switch (op) {
            case EQUALS:
                opStr = (value == null) ? " is " : " = ";
                break;
            case NOT_EQUALS:
                opStr = (value == null) ? " is not " : " != ";
                break;
            case LESS:
                opStr = " < ";
                break;
            case GREATER:
                opStr = " > ";
                break;
            case LIKE:
                opStr = " LIKE ";
            }
            return opStr;
        }
    }

    public static enum LogicalOperator {
        OR, AND;
    }
}
