package ledger;

import java.sql.*;

public class JdbcTest2 {
   static String fullname;
   static String username;
   static String password;
   static String email;
   static String question;
   static String answer;
   
   static Connection con;
   static Statement stmt;
   static ResultSet rs;
   public static void main(String[] args) {
      // TODO Auto-generated method stub
      try{
         Class.forName("com.mysql.jdbc.Driver");
         System.out.println("mysql 로딩완료");
         con = DriverManager.getConnection("jdbc:mysql://localhost:3306/test_db","root","0zero6six");
         System.out.println("데이터베이스 연결 성공");
         
         stmt = con.createStatement();
         
         String sql = "create table account(";
         sql += "fullname varchar(30) not null,";
         sql += "username varchar(20) not null,";
         sql += "password varchar(20) not null,";
         sql += "email varchar(320) not null,";
         sql += "question text not null,";
         sql += "answer varchar(30) not null,";
         sql += "primary key (username))";
         
         stmt.executeUpdate(sql);
         
         
         String sql2 = "insert into account values ('Younsun Hwang', 'ysHwang','password','hys618@naver.com','좋아하는 색깔은 무엇입니까?', '초록')";
         stmt.executeUpdate(sql2);
         String sql3 = "insert into account values ('Younsun Hwang', 'hys618','password','hys012784@gmail.com','중학교 1학년 담임선생님 이름은?', '이수현')";
         stmt.executeUpdate(sql3);
         
         
         String sql4 = "select * from account where username='hys618'";
         rs = stmt.executeQuery(sql4);
         while(rs.next()){
            fullname=rs.getString("fullname");
            username=rs.getString("username");
            password=rs.getString("password");
            email=rs.getString("email");
            question=rs.getString("question");
            answer=rs.getString("answer");
         }
         AccountInfo new_account = new AccountInfo(fullname, username, password, email, question, answer);
         
         System.out.println(new_account.getFullname());
         System.out.println(new_account.getUsername());
         System.out.println(new_account.getPassword());
         System.out.println(new_account.getEmail());
         System.out.println(new_account.getQuestion());
         System.out.println(new_account.getAnswer());
         
         rs.close();
         stmt.close();
         con.close();

      
      }
      
      catch(ClassNotFoundException e){
         System.out.println("mysql드라이버 찾을 수 없음");
      }
      catch(SQLException e){
         System.out.println("연결 실패");
      }
      catch(Exception e){
         System.out.println(e.toString());
      }
      finally{
         System.out.println("성공");
         
      }
      
      
      

   }

}