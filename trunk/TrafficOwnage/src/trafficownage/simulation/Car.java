/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package trafficownage.simulation;

/**
 *
 * @author Gerrit
 */
public class Car {
    private CarType carType;
    private DriverType driverType;

    private DriverModel driverModel;

    private Lane lane;

    private double max_velocity;

    private double velocity;
    private double acceleration;

    private double position;

    private class IDM implements DriverModel {
        private double a,b,v0,s0,T;

        public void init(DriverType driver, CarType car, double initial_max_velocity) {
            a = Math.min(driver.getMaxAcceleration(), car.getMaxAcceleration());
            b = driver.getMaxComfortableDeceleration();
            v0 = initial_max_velocity;
            s0 = driver.getMinimumDistanceToLeader();
            T = driver.getDesiredTimeHeadway();
        }

        private double desired_distance;
        public double update(double velocity_leader, double distance_to_leader) {
            desired_distance = s0 + (velocity * T) + ((velocity * Math.abs(velocity_leader - velocity)) / (2 * Math.sqrt(a*b)));
            
            return a * (1 - Math.pow((velocity/v0),4.0) - (desired_distance / distance_to_leader));

        }

        public void setMaxVelocity(double max_velocity) {
            v0 = max_velocity;
        }

    }

    /**
     * Initializes the car. Since we will use object pools, this just initializes the object
     * @param carType
     * @param driverType
     */
    public Car() {
        driverModel = new IDM();
    }


    /**
     * Real initialization. Sets the driver and car type, and the lane the car
     * will start driving on.
     * @param max_velocity
     */
    public void init(CarType carType, DriverType driverType) {
        this.carType = carType;
        this.driverType = driverType;
        driverModel.init(driverType, carType, max_velocity);
    }

    public void switchLane(Lane lane) {
        this.lane = lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
        this.max_velocity = lane.getMaximumVelocity();
        this.position = 0.0;
        driverModel.setMaxVelocity(max_velocity);
    }


    /**
     * Sets the maximum allowed velocity.
     * @param max_velocity
     */
    public void setMaxVelocity(double max_velocity) {
        this.max_velocity = max_velocity;

        driverModel.setMaxVelocity(max_velocity);
    }

    /**
     * @return the velocity
     */
    public double getVelocity() {
        return velocity;
    }

    /**
     * @return the acceleration
     */
    public double getAcceleration() {
        return acceleration;
    }

    /**
     * @return the position
     */
    public double getPosition() {
        return position;
    }

    public double getLength() {
        return carType.getLength();
    }

    /**
     * Updates the car position and velocity
     * @param dT
     * @param velocity_leader
     * @param distance_to_leader
     */
    public void update(double timestep, double velocity_leader, double distance_to_leader) {
        acceleration = driverModel.update(velocity_leader, distance_to_leader);
        velocity += acceleration * timestep;
        position += velocity * timestep;
    }
}
