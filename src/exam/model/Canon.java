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
public class Canon {

    public static final String[] CANON_NAME_LIST = {
        "創世記",
        "出埃及記",
        "利未記",
        "民數記",
        "申命記",
        "約書亞記",
        "士師記",
        "路得記",
        "撒母耳記上",
        "撒母耳記下",
        "列王紀上",
        "列王紀下",
        "歷代志上",
        "歷代志下",
        "以斯拉記",
        "尼希米記",
        "以斯帖記",
        "約伯記",
        "詩篇",
        "箴言",
        "傳道書",
        "雅歌",
        "以賽亞書",
        "耶利米書",
        "耶利米哀歌",
        "以西結書",
        "但以理書",
        "何西阿書",
        "約珥書",
        "阿摩司書",
        "俄巴底亞書",
        "約拿書",
        "彌迦書",
        "那鴻書",
        "哈巴谷書",
        "西番雅書",
        "哈該書",
        "撒迦利亞書",
        "瑪拉基書",
        "馬太福音",
        "馬可福音",
        "路加福音",
        "約翰福音",
        "使徒行傳",
        "羅馬書",
        "哥林多前書",
        "哥林多後書",
        "加拉太書",
        "以弗所書",
        "腓立比書",
        "歌羅西書",
        "帖撒羅尼迦前書",
        "帖撒羅尼迦後書",
        "提摩太前書",
        "提摩太後書",
        "提多書",
        "腓利門書",
        "希伯來書",
        "雅各書",
        "彼得前書",
        "彼得後書",
        "約翰一書",
        "約翰二書",
        "約翰三書",
        "猶大書",
        "啟示錄"};

    private final String name;

    private final String shortName;

    private final List<Chapter> chapters = new ArrayList<>();

    public Canon(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

}
