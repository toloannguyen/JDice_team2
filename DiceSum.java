

 import com.mycompany.demo.DieRoll;
 import com.mycompany.demo.RollResult;
 import java.util.logging.Logger;
 
 /*
   Class DiceSum mo rong tu DieRoll de ket hop hai lan xuc xac lai thanh mot lan duy nhat.
   Refactor: Da doi ten method va bien thanh ten ro rang de de hieu hon.
   Da them logging de theo doi ket qua xuc xac.
   Da them kiem tra du lieu dau vao de tranh loi null pointer.
 */
 
 public class DiceSum extends DieRoll {
     private DieRoll r1;
     private DieRoll r2;
 
     private static final Logger logger = Logger.getLogger(DiceSum.class.getName());
 
     // Constructor khoi tao hai lan xuc xac. Neu dau vao la null thi se nem ra loi.
     public DiceSum(DieRoll r1, DieRoll r2) {
         super(0, 0, 0);
         if (r1 == null || r2 == null) {
             throw new IllegalArgumentException("Doi tuong xuc xac khong duoc null");
         }
         this.r1 = r1;
         this.r2 = r2;
     }
 
     // Thuc hien xuc xac cho r1 va r2, tra ve ket qua ket hop.
     public RollResult makeRoll() {
         RollResult result1 = r1.makeRoll();
         RollResult result2 = r2.makeRoll();
         RollResult finalResult = result1.andThen(result2);
 
         logger.info("Ket qua xuc xac: " + finalResult.toString());
 
         return finalResult;
     }
 
     // Tra ve xau chuoi dai dien cho hai lan xuc xac.
     public String toString() {
         return r1.toString() + " & " + r2.toString();
     }
 
     // Ham tien ich tra ve tong ket qua xuc xac (khong in ket qua chi tra ve so).
     public int getSumValue() {
         return new RollResult(r1.makeRoll().getResult() + r2.makeRoll().getResult())+1;
     }
 }