package com.mezzofy.mzcoupon.apputills;


import com.mezzofy.mzcoupon.mapper.ResultSetMapper;

import org.sqldroid.SQLDroidConnection;
import org.sqldroid.SQLDroidResultSet;
import org.sqldroid.SQLDroidStatement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aruna on 01/06/17.
 */

public class DatabaseUtils {
    public static boolean update(Connection con, String sql, Object[] objlist) throws SQLException {
        boolean res = false;

        PreparedStatement ps = null;
        try {

            ps = con.prepareStatement(sql);
            for(int i = 0; (objlist != null) && (i < objlist.length); i++){
                ps.setObject(i + 1, objlist[i]);
            }
            Integer cnt = ps.executeUpdate();
            res = (cnt != null && cnt > 0);

        } finally {
            if(ps != null){
                ps.close();
            }

        }
        return res;
    }

    public static SQLDroidResultSet GetResultSet(SQLDroidConnection connection, String query) {
        SQLDroidResultSet sqlresult = null;
        try {
            SQLDroidStatement stmt = (SQLDroidStatement) connection.createStatement();
            sqlresult = (SQLDroidResultSet) stmt.executeQuery(query);
        } catch (SQLException e) {

            e.printStackTrace();

        }
        return sqlresult;
    }


    public static <T> T queryForObj(Connection con, String sql, ResultSetMapper<T> resultSetMapper, Class<T> outputclass, Object[] objlist) throws SQLException {

        T retobj = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {

            ps = con.prepareStatement(sql);
            for(int i = 0; (objlist != null) && (i < objlist.length); i++){
                ps.setObject(i + 1, objlist[i]);
            }
            rs = ps.executeQuery();

            if(rs.isBeforeFirst()){
                List<T> reslist =  resultSetMapper.mapRersultSetToObject(rs, outputclass);
                if((reslist != null) && (reslist.size() >0))
                    retobj = reslist.get(0);
            }


        } finally {
            if(rs != null){
                rs.close();
            }
            if(ps != null){
                ps.close();
            }

        }
        return retobj;
    }

    public static <T> List<T> queryForList(Connection con, String sql, ResultSetMapper<T> resultSetMapper, Class<T> outputclass,  Object[] objlist) throws SQLException {

        List<T> retobj = null;
        ResultSet rs = null;

        PreparedStatement ps = null;
        try {

            ps = con.prepareStatement(sql);
            for(int i = 0; (objlist != null) && (i < objlist.length); i++){
                ps.setObject(i + 1, objlist[i]);
            }
            rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                retobj = resultSetMapper.mapRersultSetToObject(rs, outputclass);
            }

        } finally {
            if(rs != null){
                rs.close();
            }
            if(ps != null){
                ps.close();
            }

        }
        return retobj;
    }


    @SuppressWarnings("unchecked")
    public static <T> T queryForGeneric(Connection conn, String sql, Class<T> outputclass, Object[] objlist) throws SQLException {
        T retobj = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i = 0; (objlist != null) && (i < objlist.length); i++){
                ps.setObject(i + 1, objlist[i]);
            }
            rs = ps.executeQuery();

            if(rs.next())
                retobj = (T) rs.getObject(1);
        } finally {
            if(rs != null){
                rs.close();
            }
            if(ps != null){
                ps.close();
            }
        }
        return retobj;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> queryForGenericList(Connection con, String sql, Class<T> outputclass,  Object[] objlist) throws SQLException {

        List<T> listLookup = new ArrayList<T>();
        ResultSet rs = null;

        PreparedStatement ps = null;
        try {

            ps = con.prepareStatement(sql);
            for(int i = 0; (objlist != null) && (i < objlist.length); i++){
                ps.setObject(i + 1, objlist[i]);
            }
            rs = ps.executeQuery();
            while(rs.next()){
                listLookup.add((T) rs.getObject(1));
            }

        } finally {
            if(rs != null){
                rs.close();
            }
            if(ps != null){
                ps.close();
            }

        }
        return listLookup;
    }

    public static Integer insertQueryGetId(Connection conn, String sql, Object[] objlist) throws SQLException {
        Integer retval = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < objlist.length; i++){
                ps.setObject(i + 1, objlist[i]);
            }
            Integer cnt = ps.executeUpdate();
            if(cnt != null && cnt > 0)
                rs = ps.getGeneratedKeys();
            if(rs.next())
                retval = rs.getInt(1);
        } finally {
            if(ps != null){
                ps.close();
            }
        }
        return retval;
    }
}
