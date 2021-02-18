package com.ism.urm.vo;

import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.ism.urm.config.URMProperties;

public class HiberUtill {

    private static SessionFactory sf;
    private static Hashtable<String, SessionFactory> ismsfs;

    public synchronized static SessionFactory getSessionFactory() {
        if (sf == null) {
            new HiberUtill().init();
        }
        return sf;
    }

    public static SessionFactory getSessionFactory(String key) {
        if (ismsfs == null) {
            ismsfs = new Hashtable<>();
        }
        SessionFactory rtn = ismsfs.get(key);
        if (rtn == null) {
            rtn = new HiberUtill().initRemote(key);
            ismsfs.put(key, rtn);
        }
        return rtn;
    }
    
    public static void shutdown() {
        if (sf != null) {
            sf.close();
        }
        if (ismsfs != null) {
            Iterator<String> iterator = ismsfs.keySet().iterator();
            while (iterator.hasNext()) {
                ismsfs.get(iterator.next()).close();
            }
        }
    }

    public void init() {
        String path = "hibernate.cfg.xml";
        URL resource = this.getClass().getResource(path);
        Configuration cfg = new Configuration().configure(resource);
        
        String dbType = URMProperties.get("db.type");
        String dialect = URMProperties.get("db.dialect");
        
        switch (dbType) {
        case "oracle":
            cfg.setProperty(Environment.DIALECT, dialect != null ? dialect : "org.hibernate.dialect.Oracle9iDialect");
            cfg.setProperty(Environment.DRIVER, "oracle.jdbc.driver.OracleDriver");
            break;
        case "mysql":
            cfg.setProperty(Environment.DIALECT, dialect != null ? dialect : "org.hibernate.dialect.MySQL5Dialect");
            cfg.setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");
            break;
        case "sqlserver":
            cfg.setProperty(Environment.DIALECT, dialect != null ? dialect : "org.hibernate.dialect.SQLServerDialect");
            cfg.setProperty(Environment.DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
            break;
        case "derby":
            cfg.setProperty(Environment.DIALECT,
                    dialect != null ? dialect : "org.hibernate.dialect.DerbyTenSevenDialect");
            cfg.setProperty(Environment.DRIVER, "org.apache.derby.jdbc.ClientDriver");
            break;
        default:
            throw new IllegalStateException("not yet immplemented. " + dbType);
        }
        
        cfg.setProperty(Environment.URL, URMProperties.get("db.url"));
        cfg.setProperty(Environment.USER, URMProperties.get("db.user"));
        cfg.setProperty(Environment.PASS, URMProperties.get("db.password"));
        sf = cfg.buildSessionFactory();
        
        System.out.println("HiberUtil init.");
    }

    public SessionFactory initRemote(String key) {
        String path = "hibernate.cfg.xml";
        URL resource = this.getClass().getResource(path);
        Configuration cfg = new Configuration().configure(resource);
        
        String dbType = URMProperties.get(key + ".db.type");
        String dialect = URMProperties.get(key + ".db.dialect");
        
        switch (dbType) {
        case "oracle":
            cfg.setProperty(Environment.DIALECT, dialect != null ? dialect : "org.hibernate.dialect.Oracle9iDialect");
            cfg.setProperty(Environment.DRIVER, "oracle.jdbc.driver.OracleDriver");
            break;
        case "mysql":
            cfg.setProperty(Environment.DIALECT, dialect != null ? dialect : "org.hibernate.dialect.MySQL5Dialect");
            cfg.setProperty(Environment.DRIVER, "com.mysql.jdbc.Driver");
            break;
        case "sqlserver":
            cfg.setProperty(Environment.DIALECT, dialect != null ? dialect : "org.hibernate.dialect.SQLServerDialect");
            cfg.setProperty(Environment.DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
            break;
        case "derby":
            cfg.setProperty(Environment.DIALECT,
                    dialect != null ? dialect : "org.hibernate.dialect.DerbyTenSevenDialect");
            cfg.setProperty(Environment.DRIVER, "org.apache.derby.jdbc.ClientDriver");
            break;
        default:
            throw new IllegalStateException("not yet immplemented. " + dbType);
        }
        
        cfg.setProperty(Environment.URL, URMProperties.get(key + ".db.url"));
        cfg.setProperty(Environment.USER, URMProperties.get(key + ".db.user"));
        cfg.setProperty(Environment.PASS, URMProperties.get(key + ".db.password"));
        return cfg.buildSessionFactory();
    }

}
