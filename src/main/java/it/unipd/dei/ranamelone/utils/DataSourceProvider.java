package it.unipd.dei.ranamelone.utils;

import it.unipd.dei.ranamelone.dao.AbstractDAO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DataSourceProvider {

    //static datasource
    private static DataSource ds = null;

    //constructor method
    public static synchronized DataSource getDataSource() throws NamingException {

        // we don't want to initialize a new datasoruce everytime, so, we check first that ds is null
        if (ds == null) {

            //we get the context
            InitialContext ctx = new InitialContext();

            //and use the proper resource to initialize the datasource
            ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/ranamelone"); // NEED TO CHANGE MORE LIKELY
        }
        return ds;
    }
}
