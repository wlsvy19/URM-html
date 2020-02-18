package com.ism.urm.vo;

import java.net.URL;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.ism.urm.config.URMProperties;

public class HiberUtill {

    private static SessionFactory sf;

    public void init() {
        System.out.println("HiberUtil init.");
        String path = "hibernate.cfg.xml";
        URL resource = this.getClass().getResource(path);
        Configuration cfg = new Configuration().configure(resource);
        cfg.setProperty(Environment.DRIVER, URMProperties.get("db.driver"));
        cfg.setProperty(Environment.URL, URMProperties.get("db.url"));
        cfg.setProperty(Environment.USER, URMProperties.get("db.user"));
        cfg.setProperty(Environment.PASS, URMProperties.get("db.password"));
        sf = cfg.buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (sf == null) {
            new HiberUtill().init();
        }
        return sf;
    }
    
    public static void shutdown() {
        if (sf != null) {
            sf.close();
        }
    }
}
