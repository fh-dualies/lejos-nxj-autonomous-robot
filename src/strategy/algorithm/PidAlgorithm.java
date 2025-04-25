package strategy.algorithm;

public class PidAlgorithm implements IFollowingAlgorithm {
    NXTRegulatedMotor left;
    NXTRegulatedMotor right;

    public PidAlgorithm(NXTRegulatedMotor motorLeft, NXTRegulatedMotor motorRight) {
        this.left = motorLeft;
        this.right = motorRight;
    }

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
    }

    @Override
    public void go() {
        // TODO Auto-generated method stub
    }
}
