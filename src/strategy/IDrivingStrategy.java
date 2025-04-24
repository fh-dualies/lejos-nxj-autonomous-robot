package strategy;

import core.RoboController;

public interface IDrivingStrategy {
  void activate(RoboController controller);

  void execute(RoboController controller);

  void deactivate(RoboController controller);
}
