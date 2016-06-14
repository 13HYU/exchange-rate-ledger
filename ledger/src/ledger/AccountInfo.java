package ledger;

public class AccountInfo {
   private String fullname;
   private String username;
   private String password;
   private String email;
   private String question;
   private String answer;


   public String toString(){
      return fullname+" "+username+" "+password+" "+email+" "+question+" "+answer;
   }
   
   public AccountInfo(String fullname, String username, String password, String email, String question, String answer){
      this.fullname = fullname;
      this.username = username;
      this.password = password;
      this.email = email;
      this.question = question;
      this.answer = answer;
   }
   
   public String getFullname(){
      return fullname;
   }
   public void setFullname(String fullname){
      this.fullname = fullname;
   }
   
   public String getUsername(){
      return username;
   }
   public void setUsername(String username){
      this.username = username;
   }
   
   public String getPassword(){
      return password;
   }
   public void setPassword(String password){
      this.password = password;
   }
   
   public String getEmail(){
      return email;
   }
   public void setEmail(String email){
      this.email = email;
   } 
   
   public String getQuestion(){
      return question;
   }
   public void setQuestion(String question){
      this.question = question;
   } 
   
   public String getAnswer(){
      return answer;
   }
   public void setAnswer(String answer){
      this.answer = answer;
   }
   
}