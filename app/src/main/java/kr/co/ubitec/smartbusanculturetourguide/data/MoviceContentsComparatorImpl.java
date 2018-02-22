package kr.co.ubitec.smartbusanculturetourguide.data;

import java.util.Comparator;

/**
 * Created by Administrator on 2017-09-21.
 */

public class MoviceContentsComparatorImpl implements Comparator<MovieContents> {
    @Override
    public int compare(MovieContents t1, MovieContents t2) {//거리를 비교하여서 가까운 컨텐츠를 나타낸다.
        if (t1.getDistance() < t2.getDistance()) return -1;
        if (t1.getDistance() > t2.getDistance()) return 1;
        return 0;
    }
}
