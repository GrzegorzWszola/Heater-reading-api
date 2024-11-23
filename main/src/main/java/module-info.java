module main {
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;
    exports logger;
    exports database.objects;
    exports dao;
    exports dao.intefaces;
}