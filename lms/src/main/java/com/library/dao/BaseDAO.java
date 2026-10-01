package com.library.dao;

import com.library.util.DataAccessException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** Shared helpers for the DAO classes. */
abstract class BaseDAO {

    protected static DataAccessException wrap(String what, SQLException e) {
        return new DataAccessException(what + " failed: " + e.getMessage(), e);
    }

    /** Timestamps that hold a zero date come back as null (see zeroDateTimeBehavior in db.properties). */
    protected static java.util.Date date(ResultSet rs, String col) throws SQLException {
        Timestamp t = rs.getTimestamp(col);
        return t == null ? null : new java.util.Date(t.getTime());
    }
}
