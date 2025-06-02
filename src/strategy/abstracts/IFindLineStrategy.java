package strategy.abstracts;

/**
 * IFindLineStrategy is an interface that defines the methods for a line-finding strategy.
 */
public interface IFindLineStrategy {
    /**
     * The RoboController instance used to control the robot.
     * This method initializes the strategy, setting up any necessary parameters or state.
     *
     * @throws NullPointerException if the controller or its components are null.
     */
    void initialize();

    /**
     * The RoboController instance used to control the robot.
     * This method deinitializes the strategy, cleaning up any resources or state.
     */
    void deinitialize();

    /**
     * The RoboController instance used to control the robot.
     * This method runs the strategy, executing the logic defined in the strategy.
     */
    void run();
}
