/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exam.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author steven
 */
public class Chapter {

    private final int num;

    private final Canon canon;

    private final List<Verse> verses = new ArrayList<>();

    public Chapter(final Canon canon, final int num) {
        this.canon = canon;
        this.num = num;
    }

    public Canon getCanon() {
        return canon;
    }

    public int getNum() {
        return num;
    }

    public List<Verse> getVerses() {
        return verses;
    }

}
