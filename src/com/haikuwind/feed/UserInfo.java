package com.haikuwind.feed;

import java.util.HashMap;
import java.util.Map;

import com.haikuwind.R;

public class UserInfo {
    private static UserInfo current;
    
    public static UserInfo getCurrent() {
        return current;
    }
    
    public static void setCurrent(UserInfo userInfo) {
        current = userInfo;
    }
    
    public enum Rank {
        APPRENTICE(1, R.string.apprentice, R.drawable.rank_1, R.drawable.rank_1_small),
        JOURNEYMAN(2, R.string.journeyman, R.drawable.rank_2, R.drawable.rank_2_small),
        MASTER(3, R.string.master, R.drawable.rank_3, R.drawable.rank_3_small),
        PRIEST(4, R.string.priest, R.drawable.rank_4, R.drawable.rank_4_small),
        DEVIL(5, R.string.devil, R.drawable.rank_5, R.drawable.rank_5_small),
        DAEMON(6, R.string.daemon, R.drawable.rank_6, R.drawable.rank_6_small),
        GOD(7, R.string.god, R.drawable.rank_7, R.drawable.rank_7_small);

        /**
         * numeric value of the rank, as it will be received from a server.
         */
        private int power;
        private int rankStringId;
        private int imageId;
        private int smallImageId;

        private Rank(int power, int stringId, int imageId, int smallImageId) {
            this.power = power;
            this.rankStringId = stringId;
            this.imageId = imageId;
            this.smallImageId = smallImageId;
        }

        public int getImageId() {
            return imageId;
        }

        public int getSmallImageId() {
            return smallImageId;
        }

        public int getPower() {
            return power;
        }

        public int getRankStringId() {
            return rankStringId;
        }

        private static Map<Integer, Rank> power_to_rank = new HashMap<Integer, Rank>();
        static {
            for (Rank r : Rank.values()) {
                power_to_rank.put(r.power, r);
            }
        }

        static Rank getFromNum(int num) {
            return power_to_rank.get(num);
        }
    }

    private Rank rank;
    private int score;
    private int favoritedTimes;

    public Rank getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = Rank.getFromNum(rank);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getFavoritedTimes() {
        return favoritedTimes;
    }

    public void setFavoritedTimes(int favoritedTimes) {
        this.favoritedTimes = favoritedTimes;
    }

}
