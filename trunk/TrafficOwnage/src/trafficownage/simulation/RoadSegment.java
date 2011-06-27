/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package trafficownage.simulation;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Gerrit Drost <gerritdrost@gmail.com>
 */
public class RoadSegment {
    private Node startNode;
    private Node endNode;

    private LinkedList<Lane> startLanes; //lanes originating from the startNode
    private LinkedList<Lane> endLanes; //lanes originating from the endNode

    private RoadSegment nextSegment, previousSegment;

    private Road parent;

    private double maxSpeed;
    private double length;

    public RoadSegment(Road parent, double maxSpeed, Node startNode, Node endNode) {
        this.startNode = startNode;
        this.endNode = endNode;

        this.maxSpeed = maxSpeed;

        this.startLanes = new LinkedList<Lane>();
        this.endLanes = new LinkedList<Lane>();

        this.length = startNode.distanceTo(endNode);

        this.parent = parent;
    }

    public double getMaxVelocity() {
        return maxSpeed;
    }

    public Road getRoad() {
        return parent;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public List<Lane> getDestinationLanes(Node destination) {
        return destination == startNode ? endLanes : startLanes;
    }

    public List<Lane> getSourceLanes(Node source) {
        return source == startNode ? startLanes : endLanes;
    }

    public void addLeftStartLane(int laneId, boolean ending) {
        addLeftStartLane(laneId, null, ending);
    }

    public void addLeftStartLane(int laneId, List<Node> allowedDirections, boolean ending) {
        addLeftLane(startLanes, laneId, startNode, endNode, allowedDirections, ending);

        if (!startNode.getDestinationNodes().contains(endNode))
            startNode.addDestination(endNode, this);

        if (!endNode.getSourceNodes().contains(startNode))
            endNode.addSource(startNode, this);
    }

    public void addLeftEndLane(int laneId, boolean ending) {
        addLeftEndLane(laneId, null, ending);
    }
    
    public void addLeftEndLane(int laneId, List<Node> allowedDirections, boolean ending) {
        addLeftLane(endLanes, laneId, endNode, startNode, allowedDirections, ending);

        if (!endNode.getDestinationNodes().contains(startNode))
            endNode.addDestination(startNode, this);

        if (!startNode.getSourceNodes().contains(endNode))
            startNode.addSource(endNode, this);
    }

    private void addLeftLane(LinkedList<Lane> laneList, int laneId, Node startNode, Node endNode, List<Node> allowedDirections, boolean ending) {
        Lane newLane = new Lane(laneId, this, startNode, endNode, allowedDirections, maxSpeed);

        if (laneList.size() > 0) {
            newLane.setRightNeighbour(laneList.getLast());
            laneList.getLast().setLeftNeighbour(newLane);
        }

        laneList.addLast(newLane);
    }

    public List<Lane> getStartLanes() {
        return startLanes;
    }

    public List<Lane> getEndLanes() {
        return endLanes;
    }

    public void addRightStartLane(int laneId, boolean ending) {
        addRightStartLane(laneId, null, ending);
    }

    public void addRightStartLane(int laneId, List<Node> allowedDirections, boolean ending) {
        addRightLane(startLanes, laneId, startNode, endNode, allowedDirections);

        if (!startNode.getDestinationNodes().contains(endNode))
            startNode.addDestination(endNode, this);

        if (!endNode.getSourceNodes().contains(startNode))
            endNode.addSource(startNode, this);
    }

    public void addRightEndLane(int laneId, boolean ending) {
        addRightEndLane(laneId, null, ending);
    }

    public void addRightEndLane(int laneId, List<Node> allowedDirections, boolean ending) {
        addRightLane(endLanes, laneId, endNode, startNode, allowedDirections);

        if (!endNode.getDestinationNodes().contains(startNode))
            endNode.addDestination(startNode, this);

        if (!startNode.getSourceNodes().contains(endNode))
            startNode.addSource(endNode, this);
    }

    private void addRightLane(LinkedList<Lane> laneList, int laneId, Node startNode, Node endNode, List<Node> allowedDirections) {
        Lane newLane = new Lane(laneId, this, startNode, endNode, allowedDirections, maxSpeed);

        if (laneList.size() > 0) {
            newLane.setLeftNeighbour(laneList.getFirst());
            laneList.getFirst().setRightNeighbour(newLane);
        }

        laneList.addFirst(newLane);
    }

    public double getLength() {
        return length;
    }

    /**
     * @return the nextSegment
     */
    public RoadSegment getNextSegment() {
        return nextSegment;
    }

    /**
     * @param nextSegment the nextSegment to set
     */
    public void setNextSegment(RoadSegment nextSegment) {
        this.nextSegment = nextSegment;
    }

    /**
     * @return the previousSegment
     */
    public RoadSegment getPreviousSegment() {
        return previousSegment;
    }

    /**
     * @param previousSegment the previousSegment to set
     */
    public void setPreviousSegment(RoadSegment previousSegment) {
        this.previousSegment = previousSegment;
    }

    public void update(double timestep) {
        for (Lane l : startLanes)
            l.update(timestep);

        for (Lane l : endLanes)
            l.update(timestep);
    }


}