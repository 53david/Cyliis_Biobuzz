package org.firstinspires.ftc.teamcode.Components.Shooter;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Math.ShooterCalculator;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Config
public class Turret {
    ServoImplEx servo1,servo2;
    double x = 0;
    public double gearRatio = 0.9;
    public static double minAngleLimit = 0.046, maxAngleLimit = 0.96;
    public static double redX = -1330,redY = -840;
    public static double blueX = 1200,blueY = -765;
    public static double offsetUpX = 1000, offsetDownX = 0;
    public static double offsetUpY = 0, offsetDownY = 0;
    public double targetAngle = 0,rAngle = 0;
    public double targetPosition = 0.5;
    public boolean pause = false;
    public double maxAngle = Math.toRadians(394);
    public static double dx = 0, tx =0;
    public static double dy = 0, ty = 0;
    public enum HiveState{
        UP(offsetUpX,offsetUpY),
        DOWN(offsetDownX,offsetDownY);
        double offsetX, offsetY;
        HiveState(double offsetX,double offsetY){
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }
    }
    public enum State{
        RED(redX,redY),
        BLUE(blueX,blueY),
        IDLE;
        double x,y;
        State(){

        }
        State(double x,double y){
            this.x = x;
            this.y = y;
        }
    }
    public static State state = State.BLUE;
    public static HiveState hiveState = HiveState.UP;
    public Turret() {
        servo1 = Hardware.ssh3;
        servo2 = Hardware.ssh4;

        servo1.setDirection(Servo.Direction.FORWARD);
        servo2.setDirection(Servo.Direction.FORWARD);

        servo1.setPwmRange(new PwmControl.PwmRange(500,2500));
        servo2.setPwmRange(new PwmControl.PwmRange(500,2500));

    }
    public void update() {
        updateTargetPos();
        updateAngle();
        updateServosPosition();
        updateState();

    }

    private void updateServosPosition() {
        if (pause){
            servo1.setPosition(0.5);
            servo2.setPosition(0.5);
            return;
        }
        rAngle = targetAngle;
        rAngle -= Odo.heading;
        rAngle = normalizeRadians(rAngle);
        rAngle /= maxAngle;
        rAngle += minAngleLimit;
        targetPosition = rAngle;

        targetPosition = Math.max(minAngleLimit, targetPosition);
        targetPosition = Math.min(maxAngleLimit, targetPosition);

        servo1.setPosition(targetPosition);
        servo2.setPosition(targetPosition);

    }

    private void updateAngle() {

        dx = (state.x + hiveState.offsetX) - Odo.x;
        dy = (state.y + hiveState.offsetY) - Odo.y;
        targetAngle = Math.atan2(dy, dx);

    }
    public void updateState(){
        switch (state){
            case IDLE:
                pause = true;
                break;
            case BLUE:
            case RED:
                pause = false;
                break;
        }
        if (Odo.getX() > 1600) hiveState=HiveState.UP;
        else hiveState = HiveState.DOWN;
    }
    public void updateTargetPos(){
        State.RED.x = redX; State.RED.y = redY;
        State.BLUE.x = blueX; State.BLUE.y = blueY;
        HiveState.UP.offsetX = offsetUpX;
        HiveState.UP.offsetY = offsetUpY;
        HiveState.DOWN.offsetX = offsetDownX;
        HiveState.DOWN.offsetY = offsetDownY;
    }
    private double normalizeRadians(double angle) {
        angle %= (2.0 * Math.PI);
        if (angle < 0) angle += (2.0 * Math.PI);
        return angle;

    }
    public void setState(State state){
        Turret.state = state;
    }
    public State getState(){
        return state;
    }
    public HiveState getHiveState(){
        return hiveState;
    }
    public static double getTargetX(){
        return state.x + hiveState.offsetX;
    }

    public static double getTargetY(){
        return state.y;
    }
}