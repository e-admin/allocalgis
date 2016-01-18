import java.sql.*;   // All we need for JDBC
import java.text.*;
import java.io.*;

public class TestPostgresql
{
  Connection       db;        // A connection to the database
  Statement        sql;       // Our statement to run queries with
  DatabaseMetaData dbmd;      // This is basically info the driver delivers
                              // about the DB it just connected to. I use
                              // it to get the DB version to confirm the
                              // connection in this example.

  //CONSTRUCTOR
  public TestPostgresql(String argv[]) throws ClassNotFoundException, 
SQLException
  {
    String database = argv[0];
    String username = argv[1];
    String password = argv[2];

    Class.forName("org.postgresql.Driver"); //load the driver

    db = DriverManager.getConnection("jdbc:postgresql:"+database,
                                     username,
                                     password); //connect to the db

    dbmd = db.getMetaData(); //get MetaData to confirm connection

    System.out.println("Connection to "+dbmd.getDatabaseProductName()+" "+
                       dbmd.getDatabaseProductVersion()+" successful.\n");


    db.close();
  }

  public static void correctUsage()
  {
    System.out.println("\nIncorrect number of arguments.\nUsage:\n "+
                       "java   \n");
    System.exit(1);
  }

  public static void main (String args[])
  {
    if (args.length != 3) correctUsage();
    try
    {
      TestPostgresql demo = new TestPostgresql(args);
    }
    catch (Exception ex)
    {
      System.out.println("***Exception:\n"+ex);
      ex.printStackTrace();
    }
  }
}

