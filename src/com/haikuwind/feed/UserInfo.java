package com.haikuwind.feed;

import java.util.HashMap;
import java.util.Map;

import com.haikuwind.R;

public class UserInfo {
    public enum Rank {
        APPRENTICE(1, R.string.apprentice), JOURNEYMAN(2, R.string.journeyman), MASTER(
                3, R.string.master), PRIEST(4, R.string.priest), DEVIL(5,
                R.string.devil), DAEMON(6, R.string.daemon), GOD(7,
                R.string.god);

        /**
         * numeric value of the rank, as it will be received from a server.
         */
        private int power;
        private int rankStringId;

        private Rank(int power, int stringId) {
            this.power = power;
            this.rankStringId = stringId;
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
