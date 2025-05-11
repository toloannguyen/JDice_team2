
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/*
JDice: Java Dice Rolling Program
Copyright (C) 2006 Andrew D. Hilton  (adhilton@cis.upenn.edu)


This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

 */

public class DieRoll {

    private int ndice;
    private int nsides;
    private int bonus;
    private static Random rnd;

    static {
        rnd = new Random();
    }

    /**
     * Constructor kiểm tra dữ liệu đầu vào hợp lệ. ndice Số xúc xắc (phải lớn
     * hơn 0). nsides Số mặt của xúc xắc (phải lớn hơn 0). bonus Giá trị cộng
     * thêm hoặc trừ đi kết quả tổng.
     *
     * @throws IllegalArgumentException Nếu số xúc xắc hoặc số mặt không hợp lệ.
     */

    public DieRoll(int ndice, int nsides, int bonus) {
        if (ndice <= 0 || nsides <= 0) {
            throw new IllegalArgumentException("So xuc xac va so mat phai lon hon 0");
        }
        this.ndice = ndice;
        this.nsides = nsides;
        this.bonus = bonus;
    }

    /**
     * Thực hiện tung xúc xắc và trả về kết quả. return Đối tượng RollResult
     * chứa các giá trị xúc xắc.
     */

    public RollResult makeRoll() {
        RollResult r = new RollResult(bonus);
        ThreadLocalRandom.current().ints(ndice, 1, nsides + 1).forEach(r::addResult);
        return r;
    }

    /**
     * Tạo chuỗi mô tả cú pháp tung xúc xắc.
     *
     * @return Chuỗi biểu diễn số xúc xắc, số mặt và bonus.
     */

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder(ndice + "d" + nsides);
        if (bonus != 0) {
            ans.append(bonus > 0 ? "+" : "").append(bonus);
        }
        return ans.toString();
    }
}
