package com.ism.urm.vo;

import java.net.URL;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.ism.urm.config.URMProperties;

public class HiberUtill {

    private static SessionFactory sf;

    public static SessionFactory getSessionFactory() {
        if (sf == null) {
            synchronized(HiberUtill.class) {
                if (sf == null) {
                    new HiberUtill().init();
                }
            }
        }
        return sf;
    }
    
    public static void shutdown() {
        if (sf != null) {
            sf.close();
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

}
