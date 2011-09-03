/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficownage.simulation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Gerrit
 */
public class TrafficLight extends Node implements TrafficLightInterface
{
    public static final double IGNORE_TRAFFIC_TIME = 3.0;
    public static final double GREEN_TIME = 40.0; //20,40,60,80,100,120
    public static final double MIN_GREEN_TIME = 5.0;

    public static final double MAX_RECEIVE_DISTANCE = 100.0;
    
    private double greenTime;
    private double timePassed;
    private List<Lane> greenLanes;
    private boolean needsLights;
    
    // Determines if the GreenwaveScheduler can override the lights
    private boolean greenWaveActive;

    private LinkedList<List<Lane>> laneSets;

    public TrafficLight(Point2D.Double location)
    {
        super(location);
        greenWaveActive = false;
    }

    @Override
    public void init(NodeListener listener)
    {
        super.init(listener);

        greenLanes = new ArrayList<Lane>();

        timePassed = 0.0;

        if (getNeighbourNodes().size() <= 2)
        {
            needsLights = false;
            return;
        }
        else
        {
            needsLights = true;
        }

        setNodeType(Node.TRAFFICLIGHT_NODE);

        laneSets = (LinkedList<List<Lane>>)getLaneSets().clone();

        List<Lane> lanes = laneSets.pop();
        setGreen(lanes, GREEN_TIME);
    }

    public List<Lane> getGreenLanes()
    {
        return greenLanes;
    }

    @Override
    boolean drivethrough(Car incoming)
    {
        Lane l = incoming.getNextLane();

        if (l == null)
        {
            return false;
        }

        return (!needsLights || greenLanes.contains(incoming.getLane())) && l.acceptsCarAdd(incoming);

    }

    @Override
    void acceptCar(Car incoming)
    {

        if (incoming.getNextLane() == null || !incoming.getNextLane().acceptsCarAdd(incoming))
        {
            System.err.println("Car did not check correctly if it could join a lane.");
        }

        incoming.getNextLane().addCar(incoming);
    }

    /**
     * Special setGreen for greenwaves! Doesn't do anyting if not part of greenwave!
     * Sets lights greeeeen
     * @param greenLanes Lanes which need to be given green
     * @param greenTime How long the lanes get da green
     */
    public void setGreen(List<Lane> greenLanes, double greenTime)
    {
        this.greenLanes = greenLanes;
        this.greenTime = greenTime;

        timePassed = 0.0;
    }


    public int getHighestLaneCount(List<Lane> lanes)
    {

        int laneCount = 0;

        for (Lane l : lanes)
        {
            if (!l.hasCars())
                continue;

            laneCount = Math.max(laneCount, l.getQueueCount());
        }

        return laneCount;
    }

    private boolean isCarOnTime(Car car)
    {
//        if (car.getDistanceToLaneEnd() >= MAX_RECEIVE_DISTANCE)
//            return false;
//        else
//            return (car.getDistanceToLaneEnd() / car.getVelocity()) < (greenTime - timePassed);

        if (car.getDistanceToLaneEnd() >= MAX_RECEIVE_DISTANCE) {
            return false;
        } else {
            double s = car.getDistanceToLaneEnd();
            double a = car.getAcceleration();
            double v0 = car.getVelocity();
            double arrivalTime =
                        s
                    /
                            (
                                .5
                        *
                                (
                                    v0
                                +
                                    Math.sqrt(
                                        (2 * s * a) + (v0 * v0)
                                    )
                                )
                            );

            return arrivalTime < (greenTime - timePassed);
        }
    }


    private Comparator<List<Lane>> laneSetComparator = new Comparator<List<Lane>>() {

        public int compare(List<Lane> o1, List<Lane> o2) {
            return (int)Math.signum(getHighestLaneCount(o2) - getHighestLaneCount(o1));
        }

    };

    private void checkForNewTraffic(boolean mustChange)
    {
        int higestLaneCount = 0;

        Collections.sort(laneSets, laneSetComparator);

        List<Lane> greenLanes = null;

        while (!laneSets.isEmpty() && higestLaneCount == 0) {
            greenLanes = laneSets.poll();
            higestLaneCount = getHighestLaneCount(greenLanes);
        }

        if (greenLanes != null && (higestLaneCount > 0 || mustChange))
            setGreen(greenLanes, GREEN_TIME);
        

        if (laneSets.isEmpty())
            laneSets.addAll(getLaneSets());
    }

    //private boolean checkForNextRoad;

    @Override
    public void update(double timestep)
    {
        super.update(timestep);

        //no lights, no update!
        if (!needsLights)
            return;
       
        
        //update our local time var
        timePassed += timestep;
        
        //disable the green wave if necessary
        if (timePassed >= greenTime && greenWaveActive)
            greenWaveActive = false;

        //if there is a green wave, we never check for a next road.
        //if the green wave is off, we might check.
        
        
        boolean greenRoadsEmpty = true;
        //if there is no green wave active and the green time has not passed yet
        //check if all active roads are empty. If so, switch to the next light.
        if (!greenWaveActive && timePassed < greenTime)
        {
            for (Lane l : greenLanes)
            {
                if (l.hasCars() && isCarOnTime(l.getFirstCar()))
                {
                    //current green road is still in use, therefore do not check
                    greenRoadsEmpty = false;
                    break;
                }
            }
        }

        if (timePassed >= greenTime || (timePassed >= MIN_GREEN_TIME && !greenWaveActive && greenRoadsEmpty))
        {
            //if we are past due, we have to force the change of traffic light
            checkForNewTraffic(timePassed >= greenTime);
        }

    }

    /**
     * Actives a green-wave meaning that you must now use the special setGreen
     * method to start/use the green wave and then after the set time it is done
     * (you need to set the time & lanes directly after!)
     */
    public void activateGreenWave()
    {
        greenWaveActive = true;
    }

    /**
     * Returns wether this trafficlight is currently busy with a greenwave
     * thus he is not actively selecting new roads which can be green
     */
    public boolean isBusyWithGreenWave()
    {
        return greenWaveActive;
    }

    /**
     * Check how long the current green lanes have green light
     */
    public double getGreenTime()
    {

        return greenTime;
    }
}
