package kr.scramban.wac.domain.map;

import java.util.ArrayList;
import java.util.List;

import kr.scramban.wac.domain.player.Player;
import kr.scramban.wac.domain.player.PlayerType;

public class SetupableRegion implements Region {

    private final int id;
    private final SuperRegion superRegion;
    private final List<SetupableRegion> neighbors = new ArrayList<SetupableRegion>();
    private Player owner;
    private int army;

    public SetupableRegion(final int id, final SuperRegion superRegion) {
        this.id = id;
        this.superRegion = superRegion;
    }

    public void addNeighbor(final SetupableRegion neighbor) {
        neighbors.add(neighbor);
    }

    public void setOwner(final Player owner) {
        this.owner = owner;
    }

    public void setArmy(final int army) {
        this.army = army;
    }

    @Override
    public void addArmy(final long reinforcement) {
        army += reinforcement;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public SuperRegion getSuperRegion() {
        return superRegion;
    }

    @Override
    public List<? extends Region> getNeighbors() {
        return neighbors;
    }

    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public int getArmy() {
        return army;
    }

    @Override
    public boolean isMy() {
        return owner != null && owner.getType().isMe();
    }

    @Override
    public boolean isHinterland() {
        if (!isMy()) {
            return false;
        }
        for (Region neighbor : neighbors) {
            if (!neighbor.isMy()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Integer getHinterlandCount() {
        return getHinterlandCount(new ArrayList<SetupableRegion>());
    }

    Integer getHinterlandCount(final List<SetupableRegion> list) {
        if (!isHinterland()) {
            return 0;
        }
        Integer neighborHinterlandCount = null;
        list.add(this);
        for (SetupableRegion neighbor : neighbors) {
            if (!list.contains(neighbor) && list.size() < 6) {
                ArrayList<SetupableRegion> listCopy = new ArrayList<SetupableRegion>();
                listCopy.addAll(list);
                Integer hinterlandCount = neighbor.getHinterlandCount(listCopy);
                if (hinterlandCount != null && (neighborHinterlandCount == null || hinterlandCount < neighborHinterlandCount)) {
                    neighborHinterlandCount = hinterlandCount;
                    if (neighborHinterlandCount == 0) {
                        break;
                    }
                }
            }
        }
        return neighborHinterlandCount != null ? neighborHinterlandCount + 1 : null;
    }

    @Override
    public Region getNeighborWithLowestHinterlandCount() {
        return getNeighborWithLowestHinterlandCount(new ArrayList<SetupableRegion>());
    }

    public Region getNeighborWithLowestHinterlandCount(final List<SetupableRegion> list) {
        if (!isHinterland()) {
            return null;
        }
        Region neighborWithLowestHinterlandCount = null;
        Integer neighborHinterlandCount = null;
        list.add(this);
        for (SetupableRegion neighbor : neighbors) {
            if (!list.contains(neighbor) && list.size() < 6) {
                ArrayList<SetupableRegion> listCopy = new ArrayList<SetupableRegion>();
                listCopy.addAll(list);
                Integer hinterlandCount = neighbor.getHinterlandCount(listCopy);
                if (hinterlandCount != null && (neighborHinterlandCount == null || hinterlandCount < neighborHinterlandCount)) {
                    neighborHinterlandCount = hinterlandCount;
                    neighborWithLowestHinterlandCount = neighbor;
                    if (neighborHinterlandCount == 0) {
                        break;
                    }
                }
            }
        }
        return neighborWithLowestHinterlandCount;
    }

    @Override
    public int getNeighborEnemyArmy() {
        int neighborEnemyArmy = 0;
        for (Region neighbor : neighbors) {
            if (PlayerType.ENEMY == neighbor.getOwner().getType()) {
                neighborEnemyArmy += neighbor.getArmy() - 1;
            }
        }
        return neighborEnemyArmy;
    }

    @Override
    public int getMyNeighborArmy() {
        int neighborMyArmy = 0;
        for (Region neighbor : neighbors) {
            if (neighbor.isMy()) {
                neighborMyArmy += neighbor.getArmy() - 1;
            }
        }
        return neighborMyArmy;
    }

    @Override
    public List<Region> getElseNeighbors() {
        List<Region> enemyNeighbor = new ArrayList<Region>();
        for (Region neighbor : neighbors) {
            if (!neighbor.isMy()) {
                enemyNeighbor.add(neighbor);
            }
        }
        return enemyNeighbor;
    }

    @Override
    public List<Region> getMyNeighbors() {
        List<Region> myNeighbor = new ArrayList<Region>();
        for (Region neighbor : neighbors) {
            if (neighbor.isMy()) {
                myNeighbor.add(neighbor);
            }
        }
        return myNeighbor;
    }

    @Override
    public boolean isSuperBorder() {
        for (Region neighbor : neighbors) {
            if (getSuperRegion() != neighbor.getSuperRegion()) {
                return true;
            }
        }
        return false;
    }
}
