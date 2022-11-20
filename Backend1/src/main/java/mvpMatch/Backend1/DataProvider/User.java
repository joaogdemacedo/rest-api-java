package mvpMatch.Backend1.DataProvider;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
   @Id
   @Column(unique = true)
   private String username;

   private String password;
   private String role;

   private long credit;

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getRole() {
      return role;
   }

   public void setRole(String role) {
      this.role = role;
   }

   public long getCredit() {
      return credit;
   }

   public void setCredit(long credit) {
      this.credit = credit;
   }
}
