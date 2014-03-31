package kr.scramban.wac.domain.map;

import java.util.List;

import kr.scramban.wac.domain.player.Player;

public interface Region {

    int getId();

    List<Region> getNeighbor();

    Player getOwner();

    long getArmy();

    void addArmy(long reinforcement);

    boolean isHinterland();
}
